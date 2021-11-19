package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //회원 가입 페이지(MemberForm 으로 관리)
    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    //회원 가입(데이터 검증 수행)
    //@Valid : 객체에 대한 검증을 수행한다.
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){

        // error 가 있으면 다시 회원가입 폼을 반환
        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        // 회원가입이 완료된 후 페이지가 재로딩되거나 하면 안좋기 떄문에 redirect 로 첫 페이지로 이동한다.
        return "redirect:/";
    }

    // 회원 목록 페이지(List)
    @GetMapping("/members")
    public String list(Model model){
        // 해당 메서드에서는 Member Entity 그대로 사용했지만
        // DTO 를 따로 만들어서 사용하는 것을 권장한다.
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
