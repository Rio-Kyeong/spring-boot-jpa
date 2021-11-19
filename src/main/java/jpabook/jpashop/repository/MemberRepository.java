package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository //Spring Bean 으로 등록
public class MemberRepository {

    @PersistenceContext // EntityManager 의존성 주입
    private EntityManager em;

    // 회원 등록
    public void save(Member member){
        // 엔티티 매니저를 사용해서 회원 엔티티를 영속성 컨텍스트에 저장
        em.persist(member);
    }

    // 아이디로 개별 회원 검색
    public Member findOne(Long id){
        // find() 메서드는 식별자를 통해서만 데이터 조회
        // find(반환타입, PK)
        return em.find(Member.class, id);
    }

    // 전체 회원 조회
    public  List<Member> findAll(){
        // em.createQuery("JPQL", 반환타입);
        // JPQL 은 Entity(객체)를 대상으로 쿼리를 한다.
        return em.createQuery("select m from Member m", Member.class)
                .getResultList(); // 결과를 컬렉션으로 반환
    }

    // 이름으로 회원 검색
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name) // name 파라미터 바인딩
                .getResultList(); // 결과를 컬렉션으로 반환
    }
}

