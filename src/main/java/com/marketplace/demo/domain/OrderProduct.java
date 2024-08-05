package com.marketplace.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "order_product")
public class OrderProduct {

    @EmbeddedId
    private CartProductId id = new CartProductId();

    @MapsId("orderId")
    @ManyToOne
    @JoinColumn(name = "id_order")
    private Order order;

    @MapsId("productId")
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    @Column(name = "quantity")
    private Long quantity;

    @Embeddable
    @Getter
    @Setter
    public static class CartProductId implements Serializable {

        private Long orderId;
        private Long productId;

        public CartProductId() {}

        public CartProductId(Long orderId, Long productId) {
            this.orderId = orderId;
            this.productId = productId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CartProductId that = (CartProductId) o;
            return Objects.equals(orderId, that.orderId) &&
                    Objects.equals(productId, that.productId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(orderId, productId);
        }
    }

}
