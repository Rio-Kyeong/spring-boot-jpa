package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
// JPA 의 모든 데이터 변경이나 로직들은 트랜잭션 안에서 실행 되어야 한다.
// readOnly = true -> 트렌잭션 읽기 전용 모드로 설정(plush 가 되지 않는다 - 등록 수정 삭제가 안된다)
// 변경 감지를 위한 스냅샷 비교와 같은 무거운 로직들을 수행하지 않으므로 성능향상에 도움을 준다.
// Class 에 @Transactional(readOnly = true) 를 선언함으로써 모든 메서드에 적용된다.
// 그러므로 조회(읽기)가 아닌 메서드에는 따로 @Transactional 명시해주어야한다.
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired // 생성자 의존성 주입(권장)
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    // 회원 가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);

        return member.getId();
    }

    // 중복 회원 검증
    private void validateDuplicateMember(Member member) {
        // Exception
        // 동시 회원가입을 방지하기 위해 DB 에서 member_name 을 unique 설정해주는 것이 좋다.
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
            // IllegalStateException : 메소드가 요구된 처리를 하기에 적합한 상태에 있지 않을 때
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    // 개별 회원 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    //Dirty Checking
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
