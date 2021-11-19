package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Entity Class
@Entity
//Entity Class 와 DB Table 의 이름이 다르면 @Table 을 통해서 이름을 지정해준다.
@Table(name = "orders")
@Setter @Getter
public class Order {

    @Id // PK
    @GeneratedValue // PK의 값을 위한 자동 생성
    @Column(name = "order_id") // "order_id" Column 을 mapping
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Order(상품)입장에서는 Member(회원)과 N:1 관계이다.
    @JoinColumn(name = "member_id") //member_id를 FK로 설정
    private Member member; // 연관관계의 주인

    // order 를 가지고 있는 OrderItem 이 order(FK)의 주인이 된다.
    // 오직 주인만 FK를 관리한다.
    // 주인이 아닌 곳에서는 mappedBy로 주인을 명시해야한다.
    // orderItems 의 값을 변경한다고 해서 OrderItem 의 order field(FK)의 값이 변경되지 않는다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    // 1:1 (1:1관계에서는 PK를 어디에 넣어도 상관없다)
    // 고로 연관관계를 어디에 설정하여도 상관없다
    // 모든 Entity는 기본적으로 parsist()를 하려면 각각 해야한다.
    // CascadeType.ALL 속성값으로 Order 를 parsist 하면 자동으로 Delivery 도 parsist 된다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배송

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING) //enum type
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    //==연관관계 편의 메서드==//
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //createOrder 를 통해서만 값을 설정(set)할 수 있도록 생성자를 막아둔다.
    protected Order() {
    }

    //==생성 메서드==//
    // 하나하나 찾아서 설정(set)할 필요 없이 해당 메서드의 매개변수 변경으로만 Entity 를 제어를 할 수 있다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        // 주문 상태의 기본 값을 ORDER 로 설정
        order.setStatus(OrderStatus.ORDER);
        // 주문 시간을 현재로 설정
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    // 주문 취소
    public void cancel(){
        //배송완료 시 취소 불가능
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        // 주문 상태를 CANCEL(취소)로 변경
        this.setStatus(OrderStatus.CANCEL);
        // loop 를 돌리면서 주문상품의 재고수량을 원복
        for(OrderItem orderItem : this.orderItems){
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    // 전체 주문 가격 조회
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : this.orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return  totalPrice;
    }
}


