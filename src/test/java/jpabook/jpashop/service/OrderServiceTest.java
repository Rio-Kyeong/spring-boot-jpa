package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
// 좋은 Test 는 DB 나 Spring 통합 없이 순수하게 메서드를 단위테스트 하는 것이 좋다.
// 해당 Test 는 JPA 가 잘 동작하는가를 보기위해 통합하여 테스트를 작성하였다.
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void order() throws Exception {
        //given
        Member member = createMember();

        Item book = createBook("시골 JPA",10000,10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("총 주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.",8, book.getStockQuantity());
    }



    // 상품주문 재고수량초과
    // NotEnoughStockException 이 발생하면 예외를 잡아준다(Test 성공)
    @Test(expected = NotEnoughStockException.class)
    public void stockExcess() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);

        // 재고수량은 10개인데 주문 수량을 11개로 지정
        // 재고수량보다 주문 수량이 많을 경우 NotEnoughStockException 이 발생하도록 해놓음
        int orderCount = 11;

        //when
        orderService.order(member.getId(), item.getId(), orderCount);

        //then
        fail("재고 수량 부족 예외가 발생");
    }
        
    // 주문취소
    @Test
    public void cancelOrder() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        // 주문 취소를 위한 주문
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        // 주문취소
        orderService.cancelOrder(orderId);

        //then
        // 재고가 복귀 되었는지 검증
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 CANCEL 이다.",OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());
    }

    private Item createBook(String Name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(Name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","한강","123-123"));
        em.persist(member);
        return member;
    }

}