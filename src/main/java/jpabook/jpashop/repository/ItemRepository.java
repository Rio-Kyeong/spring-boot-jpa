package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
// final field 에 대한 생성자를 생성해준다.
// 의존성 주입(Dependency Injection) 편의성을 위해 사용
@RequiredArgsConstructor
public class ItemRepository {

    // @PersistenceContext 또는 @Autowired 를 정의하지 않아도
    // 자동으로 스프링이 생성자에 의존성 주입을 해준다.
    private final EntityManager em;

    //상품 추가
    public void save(Item item){
        if(item.getId() == null){
            // 상품 신규 등록
            em.persist(item);
        } else {
            // 상품 업데이트
            // merge 는 한번 persist(영속) 상태였다가 detached(준영속) 된 상태에서
            // 그 다음 persist(영속) 상태가 될 때, merge(병합) 한다고 한다.
            em.merge(item); // 수정(병합)은 준영속 상태의 엔티티를 수정할 때 사용
        }
    }

    // 개별 상품 조회
    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    // 전체 상품 조회
    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }


}
