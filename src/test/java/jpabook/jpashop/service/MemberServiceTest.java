package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

//JUnit4 을 실행할 때 Spring 과 같이 엮어서 실행 하기위해 사용
@RunWith(SpringRunner.class)
//SpringBoot 를 띄운 상태에서 Test 를 진행하기 위해 사용
@SpringBootTest
// @Transactional : begin, commit 을 자동 수행하고 예외를 발생시키면, rollback 처리를 자동 수행해준다.
// @Transactional annotation 이 Test case 에 있게되면 test 가 끝날 때 rollback 을 시킨다.
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    //회원가입(엔티티 매니저를 사용해 회원 엔티티를 영속성 컨텍스트에 저장)이 잘 되는지 확인하는 TestCase
    @Test
    @Rollback(false) // Rollback 이 되지않으므로 메서드 실행 후 데이터가 남는다.
    public void join() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        // 회원가입한 회원과 아이디로 찾은 회원과 같은지?
        Assert.assertEquals(member, memberRepository.findOne(savedId));
    }

    // 아이디가 중복일 경우 IllegalStateException Exception 이 발생하는지 확인하는 TestCase
    // expected : try~catch 를 작성하지 않아도 IllegalStateException Exception 이 발생히면 테스트 성공
    @Test(expected = IllegalStateException.class)
    public void dup() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        //try {
        memberService.join(member2); //중복회원 예외가 발생한다!!
        //}catch (IllegalStateException e){
        //    return;
        //}

        //then
        Assert.fail("예외가 발생해야 한다.");
        // 주어진 메시지로 테스트에 실패합니다.
        // fail() 까지 오면 테스트 실패!! 예외를 잡아주자.
    }
}