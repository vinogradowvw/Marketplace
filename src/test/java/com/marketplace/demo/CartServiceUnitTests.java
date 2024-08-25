package com.marketplace.demo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.marketplace.demo.domain.Cart;
import com.marketplace.demo.domain.CartProduct;
import com.marketplace.demo.domain.Order;
import com.marketplace.demo.domain.OrderProduct;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.persistance.CartProductRepository;
import com.marketplace.demo.persistance.CartRepository;
import com.marketplace.demo.persistance.OrderProductRepository;
import com.marketplace.demo.persistance.OrderRepository;
import com.marketplace.demo.persistance.ProductRepository;
import com.marketplace.demo.service.CartService.CartService;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartServiceunitTests {

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
	
	private Cart cart;
	private CartProduct cartProduct;
	private Product product;
	private Product productNew;
	private CartProduct.CartProductId cartProductId;

	@BeforeEach
	void setRules() {
		Mockito.when(cartRepository.existsById(cart.getID())).thenReturn(true);
		Mockito.when(cartRepository.save(cart)).thenReturn(cart);
		Mockito.when(productRepository.existsById(product.getID())).thenReturn(true);
		Mockito.when(productRepository.save(product)).thenReturn(product);
		Mockito.when(cartProductRepository.existsByCartAndProduct(cart, product)).thenReturn(true);
		Mockito.when(cartProductRepository.existsByCartAndProduct(cart, productNew)).thenReturn(false);
		Mockito.when(cartProductRepository.findById(cartProductId)).thenReturn(Optional.of(cartProduct));
	}

	@BeforeEach
	void setUp() {
		cart = new Cart();
		cart.setId(1L);
		product = new Product();
		product.setId(1L);
		cartProductId = new CartProduct.CartProductId(cart.getID(), product.getID());
		cartProduct = new CartProduct();
		cartProduct.setId(cartProductId);
		cartProduct.setCart(cart);
		cartProduct.setProduct(product);
		cartProduct.setQuantity(1L);
	}

	private boolean checkProductInCart(Cart cart, Product product, Long quantity) {

		int counter = 0;
		for (CartProduct cartProduct : cart.getProducts()) {
			if (cartProduct.getProduct() == product && cartProduct.getQuantity() == quantity) {
				counter++;
			}
		}

		if (counter == 1) return true;

		return false;
	}

	private boolean checkCartsByProduts(Cart cart, Product product) {

		int counter = 0;
		for (CartProduct cartProduct : product.getCarts()) {
			if (cart == cartProduct.getCart()) {
				counter++;
			}
		}

		if (counter == 1) return true;

		return false;
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

		Mockito.verify(cartRepository, Mockito.times(1)).save(cart);
		Mockito.verify(cartProductRepository, Mockito.times(1)).save(any(CartProduct.class));
		Mockito.verify(productRepository, Mockito.times(0)).save(product);

		assertTrue(checkProductInCart(cart, product, 1L));
		assertTrue(checkCartsByProduts(cart, product));

		cart = cartService.deleteProduct(cart, product, 1L);

		Mockito.verify(cartRepository, Mockito.times(1)).save(cart);
		Mockito.verify(cartProductRepository, Mockito.times(1)).save(any(CartProduct.class));
		Mockito.verify(productRepository, Mockito.times(1)).save(product);

		assertFalse(checkCartsByProduts(cart, product));
		assertFalse(checkProductInCart(cart, product, 1L));
	}

	@Test
	public void clearCart() {

		cart = cartService.addProduct(cart, product, 2L);

		cart = cartService.addProduct(cart, productNew, 2L);

		cartService.clearCart(cart);

		assertFalse(checkCartsByProduts(cart, product));
		assertFalse(checkProductInCart(cart, product, 1L));
		assertFalse(checkCartsByProduts(cart, productNew));
		assertFalse(checkProductInCart(cart, productNew, 1L));

		Mockito.verify(cartProductRepository, Mockito.times(2)).delete(any(CartProduct.class));
		Mockito.verify(productRepository, Mockito.times(2)).save(any(Product.class));
		Mockito.verify(cartRepository, Mockito.times(1)).save(cart);
	}

	@Test
	public void createOrder() {

		cart = cartService.addProduct(cart, product, 2L);

		cart = cartService.addProduct(cart, productNew, 2L);

		cartService.createOrder(cart);

		Mockito.verify(orderRepository, Mockito.times(2)).save(any(Order.class));
		Mockito.verify(orderProductRepository, Mockito.times(2)).save(any(OrderProduct.class));
		Mockito.verify(productRepository, Mockito.times(2)).save(product);
		Mockito.verify(productRepository, Mockito.times(2)).save(productNew);
		Mockito.verify(orderRepository, Mockito.times(1)).save(any(Order.class));
		Mockito.verify(cartRepository, Mockito.times(1)).save(cart);

	}
}
