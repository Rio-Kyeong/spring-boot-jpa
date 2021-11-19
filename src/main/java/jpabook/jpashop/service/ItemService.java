package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    // 변경 감지 사용
    // 파라미터 값이 많을 경우 DTO 를 따로 만들어서 받는 방법을 사용하면 된다.
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        // findItem 의 상태는 영속상태이다.
        // 영속 상태의 엔티티는 변경 감지(dirty checking)기능이 동작해서 값이 셋팅(set)된 후
        // 트랜잭션을 커밋할 때 자동으로 수정되므로 별도의 수정 메서드를 호출할 필요가 없고 그런 메서드도 없다.
        // (영속성 컨텍스트에서 변경된 엔티티를 찾고 바뀐값을 update 한다)
        Item findItem = itemRepository.findOne(itemId);

        //setter method 보다는 엔티티에서 의미있는 변경 메서드를 하나 만들어서 값을 주입 해주는게 좋다.
        //EX) findItem.change(price, name, stockQuantity);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems(){
        return  itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
