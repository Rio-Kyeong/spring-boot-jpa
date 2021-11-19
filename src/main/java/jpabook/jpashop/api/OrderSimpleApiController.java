package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * XToOne(ManyToOne, OneToOne) 관계 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     * - 엔티티를 API 응답으로 외부로 노출하는것은 좋지 않다.
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/sample-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        //원하는 정보만 출력하기
        for(Order order : all){
            // order.getMember() : 프록시 객체
            // order member 와 order address 는 지연 로딩이다.
            // 따라서 실제 엔티티 대신에 프록시 존재
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }
        return all;
    }

    /**
     * V2. 엔티티를 조회해서 DTO 로 변환(fetch join 사용X)
     * - 단점: 지연로딩으로 쿼리 N번 호출(많은 쿼리 호출의 발생)
     */
    @GetMapping("/api/v2/sample-orders")
    public List<SimpleOrderDto> ordersV2(){
        // 이 method 는 service 에서 구현 한 내용이 없기 때문에 바로 Repository 에서 받았다.
        // Order(o) 를 SimpleOrderDto 로 변환
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * DTO
     */
    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        // DTO 에 Entity 를 파라미터로 받는 건 크게 문제가 되지 않는다.
        // 별로 중요하지 않은 곳에서 중요한 엔티티를 의존하기 때문에
        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            // LAZY 초기화?
            // Member 의 ID 를 가지고 영속성 컨텍스트에서 정보를 찾아본다.
            // 정보가 없으면 DB Query 를 날린다.
            this.name = order.getMember().getName(); // LAZY 초기화
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }

    /**
     * V3. 엔티티를 조회해서 DTO 로 변환(fetch join 사용)
     * - fetch join 으로 쿼리 1번 호출
     * 참고: fetch join 에 대한 자세한 내용은 JPA 기본편 참고(정말 중요함)
     */
    @GetMapping("/api/v3/sample-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * V4. JPA 에서 DTO 로 바로 조회 * - 쿼리 1번 호출
     * - select 절에서 원하는 데이터만 선택해서 조회
     */
    @GetMapping("/api/v4/sample-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
       return orderSimpleQueryRepository.findOrderDtos();
    }
}