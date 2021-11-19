package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문 하기
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        //회원의 주소 값으로 배송을 한다(간단한 예제)
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        //static 이기 때문에 객체생성없이 클래스 선언으로 바로 메서드를 부를 수 있다.
        //하나하나 orderItem.setXXX() 을 할 필요없이 createOrderItem 라는 생성 메서드를 만들어 한번에 값을 관리한다.
        //orderItem.setXXX() 을 못하도록 OrderItem 의 생성자의 접근 지정자를 protected 로 설정한다.
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        //order 만 저장(save)해주어도 order entity 의 cascade = CascadeType.ALL 때문에
        //orderItem 과 delivery 가 연쇄적으로 저장된다.
        //cascade = CascadeType.ALL 설정이 없으면 각각 저장해 주어야 한다.
        orderRepository.save(order);

        return order.getId();
    }

    //주문 취소
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        order.cancel();
    }

    //주문 내역 검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAll(orderSearch);
    }

}
