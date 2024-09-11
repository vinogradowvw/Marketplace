package com.marketplace.demo.controller;

import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.CartDTO;
import com.marketplace.demo.controller.dto.OrderDTO;
import com.marketplace.demo.controller.dto.ProductDTO;
import com.marketplace.demo.domain.Cart;
import com.marketplace.demo.domain.Order;
import com.marketplace.demo.domain.Product;
import com.marketplace.demo.service.CartService.CartService;
import com.marketplace.demo.service.ProductService.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CartController {

    private final CartService cartService;
    private DTOConverter<CartDTO, Cart> cartDTOConverter;
    private DTOConverter<ProductDTO, Product> productDTOConverter;
    private ProductService productService;
    private DTOConverter<OrderDTO, Order> orderDTOConverter;

    @GetMapping
    public List<CartDTO> getAllCarts() {
        Iterable<Cart> carts = cartService.readAll();
        List<CartDTO> cartDTOList = new ArrayList<>();

        carts.forEach(cart -> cartDTOList.add(cartDTOConverter.toDTO(cart)));

        return cartDTOList;
    }

    @GetMapping(path = "/{id}")
    public CartDTO getCartById(@PathVariable("id") Long id) {
        return cartDTOConverter.toDTO(cartService.readById(id).get());
    }

    @GetMapping(path = "/{id}/products")
    public Map<ProductDTO, Long> getProductsInCart(@PathVariable("id") Long id) {
        Map<ProductDTO, Long> productDTOLongMap = new HashMap<>();

        Cart cart = cartService.readById(id).get();

        cart.getProducts().forEach(p -> productDTOLongMap.put(productDTOConverter.toDTO(p.getProduct()), p.getQuantity()));

        return productDTOLongMap;
    }

    @PostMapping(path = "/{idCart}/product")
    public CartDTO addProduct(@PathVariable("idCart") Long idCart, @RequestParam("idProduct") Long idProduct,
                              @RequestParam("quantity") Long quantity) {

        Cart cart = cartService.readById(idCart).get();
        Product product = productService.readById(idProduct).get();

        return cartDTOConverter.toDTO(cartService.addProduct(cart, product, quantity));
    }

    @DeleteMapping(path = "/{idCart}/product/{idProduct}")
    public CartDTO deleteProduct(@PathVariable("idCart") Long idCart, @PathVariable("idProduct") Long idProduct,
                                 @RequestParam("quantity") Long quantity) {

        Cart cart = cartService.readById(idCart).get();
        Product product = productService.readById(idProduct).get();

        return cartDTOConverter.toDTO(cartService.deleteProduct(cart, product, quantity));
    }

    @PostMapping(path = "/{id}/order")
    public OrderDTO createOrder(@PathVariable("id") Long id){
        Cart cart = cartService.readById(id).get();

        return orderDTOConverter.toDTO(cartService.createOrder(cart));
    }

    @DeleteMapping(path = "/{id}/products")
    public CartDTO clearCart(@PathVariable("id") Long id){
        Cart cart = cartService.readById(id).get();

        return cartDTOConverter.toDTO(cartService.clearCart(cart));
    }
}
