package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    public final EntityManager em;

    // Repository 는 가급적 순수한 엔티티를 조회하는데 사용하기 때문에 따로 Repository 를 만드는 것이 유지보수성에 좋다.
    // JPA 에서 DTO 로 바로 조회 메소드(재사용 불가)
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m "+
                        " join o.delivery d ", OrderSimpleQueryDto.class
        ).getResultList();
    }
}
