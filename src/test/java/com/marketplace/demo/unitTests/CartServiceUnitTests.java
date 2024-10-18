package com.marketplace.demo.unitTests;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import com.marketplace.demo.domain.*;
import com.marketplace.demo.persistance.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.marketplace.demo.service.CartService.CartService;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartServiceUnitTests {

	@Autowired
	private CartService cartService;

	@MockBean
	private CartRepository cartRepository;
	@MockBean
	private ProductRepository productRepository;
	@MockBean
	private CartProductRepository cartProductRepository;
	@MockBean
	private OrderRepository orderRepository;
	@MockBean
	private OrderProductRepository orderProductRepository;
	@MockBean
	private PaymentRepository paymentRepository;
	
	private Cart cart;
	private CartProduct cartProduct;
	private Product product;
	private Product productNew;
	private Post postNew;
	private CartProduct.CartProductId cartProductId;
	private User buyer;
	private User seller;
	private Post post;

	@BeforeEach
	void setRules() {
		Mockito.when(cartRepository.existsById(cart.getID())).thenReturn(true);
		Mockito.when(cartRepository.save(cart)).thenReturn(cart);
		Mockito.when(productRepository.existsById(product.getID())).thenReturn(true);
		Mockito.when(productRepository.save(product)).thenReturn(product);
		Mockito.when(productRepository.existsById(productNew.getID())).thenReturn(true);
		Mockito.when(productRepository.save(productNew)).thenReturn(productNew);
		Mockito.when(cartProductRepository.existsByCartAndProduct(cart, product)).thenReturn(true);
		Mockito.when(cartProductRepository.existsByCartAndProduct(cart, productNew)).thenReturn(false);
		Mockito.when(cartProductRepository.findById(cartProductId)).thenReturn(Optional.of(cartProduct));
		Mockito.when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
		Mockito.when(orderProductRepository.save(any(OrderProduct.class))).thenAnswer(invocation -> invocation.getArgument(0));
	}

	@BeforeEach
	void setUp() {
		cart = new Cart();
		cart.setId(1L);

		buyer = new User();
		buyer.setId(1L);
		seller = new User();
		seller.setId(2L);

		cart.setUser(buyer);
		buyer.setCart(cart);

		product = new Product();
		product.setId(1L);
		post = new Post();
		post.setId(1L);
		product.setPost(post);
		post.setProduct(product);
		post.setUser(seller);
		productNew = new Product();
		productNew.setId(2L);
		product.setPrice(1257);
		productNew.setPrice(2460);
		postNew = new Post();
		postNew.setId(2L);
		postNew.setProduct(productNew);
		productNew.setPost(postNew);
		postNew.setUser(seller);

		cartProductId = new CartProduct.CartProductId(cart.getID(), product.getID());
		cartProduct = new CartProduct();
		cartProduct.setId(cartProductId);
		cartProduct.setCart(cart);
		cartProduct.setProduct(product);
		cartProduct.setQuantity(1L);

		cart.getProducts().add(cartProduct);
		product.getCarts().add(cartProduct);
	}

	private boolean checkProductInCart(Cart cart, Product product, Long quantity) {

		int counter = 0;
		for (CartProduct cartProduct : cart.getProducts()) {
			if (cartProduct.getProduct().equals(product) && cartProduct.getQuantity().equals(quantity)) {
				counter++;
			}
		}

        return counter == 1;
    }

	private boolean checkCartsByProducts(Cart cart, Product product) {

		int counter = 0;
		for (CartProduct cartProduct : product.getCarts()) {
			if (cart.equals(cartProduct.getCart())) {
				counter++;
			}
		}

        return counter == 1;
    }

	@Test
	public void addProduct() {
		cart = cartService.addProduct(cart, product, 2L);

		cart = cartService.addProduct(cart, productNew, 2L);

		assertTrue(checkProductInCart(cart, product, 3L));
		assertTrue(checkProductInCart(cart, productNew, 2L));
		
		Mockito.verify(cartRepository, Mockito.times(2)).save(cart);
		Mockito.verify(cartProductRepository, Mockito.times(2)).save(any(CartProduct.class));
		Mockito.verify(productRepository, Mockito.times(1)).save(productNew);
	}

	@Test
	public void deleteProduct() {

		cart = cartService.addProduct(cart, product, 2L);

		cart = cartService.deleteProduct(cart, product, 1L);

		Mockito.verify(cartRepository, Mockito.times(2)).save(cart);
		Mockito.verify(cartProductRepository, Mockito.times(2)).save(any(CartProduct.class));
		Mockito.verify(productRepository, Mockito.times(0)).save(product);

		assertTrue(checkProductInCart(cart, product, 2L));
		assertTrue(checkCartsByProducts(cart, product));

		cart = cartService.deleteProduct(cart, product, 2L);

		Mockito.verify(cartRepository, Mockito.times(3)).save(cart);
		Mockito.verify(cartProductRepository, Mockito.times(2)).save(any(CartProduct.class));
		Mockito.verify(cartProductRepository, Mockito.times(1)).delete(any(CartProduct.class));
		Mockito.verify(productRepository, Mockito.times(1)).save(product);

		assertFalse(checkCartsByProducts(cart, product));
		assertFalse(checkProductInCart(cart, product, 1L));
	}

	@Test
	public void clearCart() {

		cart = cartService.addProduct(cart, product, 2L);

		cart = cartService.addProduct(cart, productNew, 2L);

		cartService.clearCart(cart);

		assertFalse(checkCartsByProducts(cart, product));
		assertFalse(checkProductInCart(cart, product, 1L));
		assertFalse(checkCartsByProducts(cart, productNew));
		assertFalse(checkProductInCart(cart, productNew, 1L));

		Mockito.verify(cartProductRepository, Mockito.times(2)).delete(any(CartProduct.class));
		Mockito.verify(productRepository, Mockito.times(3)).save(any(Product.class));
		Mockito.verify(cartRepository, Mockito.times(3)).save(cart);
	}

	@Test
	public void createOrder() {

		cart = cartService.addProduct(cart, product, 2L);

		cart = cartService.addProduct(cart, productNew, 2L);

		cartService.createOrder(cart);

		Mockito.verify(orderRepository, Mockito.times(2)).save(any(Order.class));
		Mockito.verify(orderProductRepository, Mockito.times(2)).save(any(OrderProduct.class));
		/* "product" has already been added to the cart(before the test), this is the reason
		 * why the "product" is saved 1 time less than "productNew".
		 */
		Mockito.verify(productRepository, Mockito.times(2)).save(product);
		Mockito.verify(productRepository, Mockito.times(3)).save(productNew);
		Mockito.verify(cartRepository, Mockito.times(3)).save(cart);

	}
}
