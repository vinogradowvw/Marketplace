package com.marketplace.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "cart_product")
@NoArgsConstructor
@Getter
@Setter
public class CartProduct {

    @EmbeddedId
    private CartProductId id = new CartProductId();

    @MapsId("cartId")
    @ManyToOne
    @JoinColumn(name = "id_cart")
    private Cart cart;

    @MapsId("productId")
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    @Column(name = "quantity")
    private Long quantity;

    public CartProduct(Cart cart) {
        this.cart = cart;
    }

    @Getter
    @Setter
    @Embeddable
    public static class CartProductId implements Serializable {

        private Long cartId;
        private Long productId;

        public CartProductId() {}

        public CartProductId(Long cartId, Long productId) {
            this.cartId = cartId;
            this.productId = productId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CartProductId that = (CartProductId) o;
            return Objects.equals(cartId, that.cartId) &&
                    Objects.equals(productId, that.productId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cartId, productId);
        }
    }
}

