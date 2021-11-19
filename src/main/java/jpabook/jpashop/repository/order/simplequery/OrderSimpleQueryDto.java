package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Repository 에서 Controller 안의 static class SimpleOrderDto 를 조회하여
 * Controller 에 의존관계가 생기면 안되므로 새로 DTO 를 만들어 주었다.
 * 의존관계는 Repository -> Service -> Controller 로 한 방향으로 흘러가야 한다.
 */
@Data
public class OrderSimpleQueryDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
