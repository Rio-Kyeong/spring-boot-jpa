package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") //item_id를 FK로 지정
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // 여러개 주문한 아이템 : 하나의 주문(N:1)
    @JoinColumn(name = "order_id") //order_id를 FK로 지정
    private  Order order;

    private int orderPrice; //주문가격
    private int count; //주문 수량

    //createOrderItem 를 통해서만 값을 설정(set)할 수 있도록 생성자를 막아둔다.
    protected OrderItem() {
    }

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        //OrderItem 을 생성할 때는 기본적으로 재고수량을 감소시켜야한다.
        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel(){
        getItem().addStock(this.count); //재고수량을 원복
    }

    //==조회 로직==//
    //전체 주문 가격 계산
    public int getTotalPrice() {
        return getOrderPrice() *  getCount();
    }
}
