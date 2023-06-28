package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Movie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {
    
    @Autowired
    ItemService itemService;
    
    @Test
    public void saveAndFindTest() throws Exception {
        // given
        Item movie = new Movie();
        movie.setName("movie1");

        Item book = new Book();
        book.setName("book1");

        Item album = new Album();
        album.setName("album1");

        // when
        itemService.saveItem(movie);
        Long savedId1 = movie.getId();
        itemService.saveItem(book);
        Long savedId2 = book.getId();
        itemService.saveItem(album);
        Long savedId3 = album.getId();

        Item findItem1 = itemService.findOne(savedId1);
        Item findItem2 = itemService.findOne(savedId2);
        Item findItem3 = itemService.findOne(savedId3);

        // then
        Assertions.assertThat(findItem1.getName()).isEqualTo(movie.getName());
        Assertions.assertThat(findItem2.getName()).isEqualTo(book.getName());
        Assertions.assertThat(findItem3.getName()).isEqualTo(album.getName());
    }

    @Test
    public void findAllTest() throws Exception {
        // given
        Item item1 = new Album();
        Item item2 = new Movie();
        Item item3 = new Book();

        // when
        item1.setName("item1");
        item2.setName("item2");
        item3.setName("item3");
        List<Item> items = List.of(item1, item2, item3);

        itemService.saveItem(item1);
        itemService.saveItem(item2);
        itemService.saveItem(item3);

        List<Item> findItems = itemService.findItems();

        // then
        Assertions.assertThat(findItems).containsAll(List.of(item1, item2, item3));
        Assertions.assertThat(findItems)
                .hasSameSizeAs(items)
                .extracting(Item::getName)
//                .containsAll(List.of("item1", "item2"));
                .containsAll(List.of("item1", "item2", "item3"));
    }
}