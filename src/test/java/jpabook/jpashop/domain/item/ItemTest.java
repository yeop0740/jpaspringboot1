package jpabook.jpashop.domain.item;

import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.service.ItemService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ItemTest {

    @Test
    public void addStockTest() throws Exception {
        // given
        Item album = new Album();
        Item book = new Book();
        Item movie = new Movie();

        // when
        album.addStock(17);
        book.addStock(1);
        movie.addStock(119);

        // then
        Assertions.assertThat(album.getStockQuantity()).isEqualTo(17);
        Assertions.assertThat(book.getStockQuantity()).isEqualTo(1);
        Assertions.assertThat(movie.getStockQuantity()).isEqualTo(119);
    }

    @Test
    public void removeStockTest() throws Exception {
        // given
        Item album = new Album();
        Item book = new Book();
        Item movie = new Movie();

        // when
        album.addStock(18);
        album.removeStock(11);
        book.addStock(22991);
        book.removeStock(2994);
        movie.addStock(2778);
        movie.removeStock(33);

        // then
        Assertions.assertThat(album.getStockQuantity()).isEqualTo(7);
        Assertions.assertThat(book.getStockQuantity()).isEqualTo(22991 - 2994);
        Assertions.assertThat(movie.getStockQuantity()).isEqualTo(2778 - 33);
    }

    @Test
    public void outOfStockExceptionTest() throws Exception {
        // given
        Item album = new Album();
        Item book = new Book();
        Item movie = new Movie();

        // when
        book.addStock(14);
        movie.addStock(77043);

        // then
        Assertions.assertThatThrownBy(() -> movie.removeStock(1000000))
                .isInstanceOf(NotEnoughStockException.class);
        Assertions.assertThatThrownBy(() -> book.removeStock(117))
                .isInstanceOf(NotEnoughStockException.class);
        Assertions.assertThatThrownBy(() -> album.removeStock(77))
                .isInstanceOf(NotEnoughStockException.class);

    }

}