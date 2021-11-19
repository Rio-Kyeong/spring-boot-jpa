package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {
/*
    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    // @Transactional annotation 이 Test case 에 있으면 test 가 끝나면 rollback 을 시킨다.
    @Rollback(false) //롤백을 시키지 않는다.
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
        // 같은 영속성 컨텍스트 안에서는 식별자가 같으면 같은 엔티티로 식별되므로 -> findMember == member : true
        // JPA 엔티티 동일성 보장
        System.out.println("findMember == member : "+(findMember == member));
    }
        */
}
