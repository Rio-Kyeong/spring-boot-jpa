package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // 하나의 회원이 여러 상품을 주문
    // mappedBy = "member" :
    // orders 에 값을 변경 한다고해서 Order Table 에 member field(PK)의 값이 변경되지 않는다.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
