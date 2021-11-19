package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    //상품 주문 페이지
    @GetMapping("/order")
    public String createForm(Model model){

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    //상품 주문
    //@RequestParam("가져올 데이터의 name") [데이터타입] [변수]
    //HttpServletRequest 의 getParameter() 메서드와 같은 역할을 한다.
    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count){

        orderService.order(memberId, itemId, count);
        // 핵심 비지니스 로직이 있으면 가급적이면 Controller 에서 Entity 를 찾아서
        // 넘기는 것보다는 식별자(id)를 넘겨주고 Service 에서 비지니스 로직을 작성하게 되면
        // 엔티티를 영속 컨텍스트에 존재하는 상태에서 조회할 수 있다(변경 감지(Dirty Checking)을 할 수 있다)
        // 영속 상태의 엔티티는 변경 감지(dirty checking)기능이 동작해서 값이 셋팅(set)된 후
        // 트랜잭션을 커밋할 때 자동으로 수정되므로 별도의 수정 메서드를 호출할 필요가 없고 그런 메서드도 없다.
        return "redirect:/orders";
    }

    //주문 내역 페이지
    //@ModelAttribute("Object") : 사용자가 요청시 전달하는 값을 오브젝트 형태로 매핑해준다.
    //view 단에서 memberName, orderStatus 값이 요청이되면 orderSearch 에 바인딩이 된다.
    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model){

        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    //주문 취소
    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId){

        orderService.cancelOrder(orderId);

        return "redirect:/orders";
    }
}