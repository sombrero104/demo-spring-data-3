package me.demospringdata3.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void save() {
        Post post = new Post();
        post.setTitle("jpa");
        Post savedPost = postRepository.save(post);// persist() 호출, insert 쿼리 발생.
        // 위와 같이 save()는 영속화되어 있는 객체를 리턴해 준다.
        // persist()는 persist() 메소드에 넘긴 그 엔티티 객체를 Persistent 상태로 변경한다.

        assertThat(entityManager.contains(post)).isTrue(); // post도 영속화가 되고,
        assertThat(entityManager.contains(savedPost)).isTrue(); // savedPost도 영속화가 된다.
        assertThat(savedPost == post).isTrue(); // 여기에서는 post와 savedPost가 같다.

        Post postUpdate = new Post();
        postUpdate.setId(post.getId());
        postUpdate.setTitle("hibernate");
        Post updatedPost = postRepository.save(postUpdate);// merge() 호출, update 쿼리 발생.
        // merge()는 merge() 메소드에 넘긴 그 엔티티의 복사본을 만들고,
        // 그 복사본을 다시 Persistent 상태로 변경하고 그 복사본을 반환한다.

        assertThat(entityManager.contains(updatedPost)).isTrue(); // updatedPost는 영속화가 되고,
        assertThat(entityManager.contains(postUpdate)).isFalse(); // postUpdate는 영속화가 되지 않는다.
        assertThat(updatedPost == postUpdate).isFalse(); // updatedPost와 postUpdate는 같지 않다.

        // postUpdate.setTitle("good luck"); // 파라미터로 넘긴 postUpdate는 영속화 상태가 아니기 때문에 변경되지 않는다.
        updatedPost.setTitle("good luck"); // 반환 받은 updatedPost는 영속화 상태이기 때문에 변경된다.
        // 내가 명시적으로 update하라고 알려주지 않아도 jpa가 알아서 객체 상태를 감지하다가
        // 필요하다고 느낀 시점(여기서는 아래의 findAll()이다. '데이터를 가져오려고 하네? 빨리 싱크해야겠다.'
        // 이 시점에 위의 모든 변경 내용들이 반영되고 데이터를 가져온다.)에 디비에 반영이 된다.

        List<Post> all = postRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    void findByTitleStartsWith() {
        savePost();

        List<Post> all = postRepository.findByTitleStartsWith("Spring");
        assertThat(all.size()).isEqualTo(1);
    }

    private void savePost() {
        Post post = new Post();
        post.setTitle("Spring Data Jpa");
        Post savedPost = postRepository.save(post);
    }

    @Test
    void findByTitle() {
        savePost();

        // List<Post> all = postRepository.findByTitle("Spring Data Jpa");

        /**
         * Sort의 정렬옵션에 들어갈 수 있는 문자열은 반드시 엔티티의 프로퍼티이거나 alias이어야 한다.
         * title은 Post 엔티티의 프로퍼티이므로 title로 정렬하는 것이 가능하다.
         * 프로퍼티 또는 alias가 아닌 경우에는 Sort로 사용할 수 없다. (예를 들어, 함수와 같은..)
         * 하지만 JpaSort.unsafe()를 사용하면 함수도 사용할 수 있다.
         */
        // List<Post> all = postRepository.findByTitle("Spring Data Jpa", Sort.by("title"));
        // List<Post> all = postRepository.findByTitle("Spring Data Jpa", Sort.by("LENGTH(title)")); // (X) 함수는 사용할 수 없다.
        List<Post> all = postRepository.findByTitle("Spring Data Jpa", JpaSort.unsafe("LENGTH(title)")); // (O) JpaSort.unsafe()를 사용하면 함수도 가능하다.

        assertThat(all.size()).isEqualTo(1);
    }

}