package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private long id;

    @JsonIgnore // 양방향 연관관계에서는 무한루프가 돌지 않도록 한쪽을 @JsonIgnore 를 해야한다.
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded // 내장 타입
    private  Address address;

    // default 설정이 EnumType.ORDINAL 이다.
    // (EnumType.ORDINAL 은 데이터 추가 시 순서 값이 꼬일 수 있기 때문에 쓰지 않는게 좋다)
    // EnumType.ORDINAL : enum 순서 값을 DB에 저장
    // ex) DeliveryStatus.READY 는 1로 저장, DeliveryStatus.COMP 는 2로 숫자로 저장
    // EnumType.STRING : enum 이름을 DB에 저장
    // ex) "READY", "COMP" 문자열 자체가 저장
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY(배송준비), COMP(배송)
}
