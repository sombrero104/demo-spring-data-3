package me.demospringdata3.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

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

    private Post savePost() {
        Post post = new Post();
        post.setTitle("Spring Data Jpa");
        Post savedPost = postRepository.save(post);
        return savedPost;
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

    @Test
    void updateTitle() {
        Post spring = savePost(); // Persistent 상태의 객체.
        String hibernate = "hibernate";
        int update = postRepository.updateTitle(hibernate, spring.getId()); // id를 가지고 title을 변경한다.
        assertThat(update).isEqualTo(1);

        // spring이 Persistent 상태이기 때문에 DB에서 조회하지 않고 JPA가 캐싱하고 있던 것을 그대로 가져온다.
        // 때문에 값이 변경되지 않은 상태 그대로이다.
        Optional<Post> byId = postRepository.findById(spring.getId()); // select를 하지 않는다.
        assertThat(byId.get().getTitle()).isEqualTo(hibernate); // title은 여전히 'spring'이기 때문에 테스트가 깨진다.

        /**
         * update 쿼리 메소드에서
         * '@Modifying(clearAutomatically = true, flushAutomatically = true)'를 붙이면 테스트가 깨지지 않는다.
         *
         * [ clearAutomatically ]
         *  => update 쿼리를 실행한 이후에 Persistent context를 clear 해준다. (캐시를 비워준다. 때문에 이후에 조회할 때 DB에서 새로 읽어온다.)
         *
         * [ flushAutomatically ]
         *  => update 쿼리 실행 이전에 Persistent context를 flush 해준다.
         *
         * 하지만 이 방법을 추천하지는 않는다.
         * (이 방법으로 delete도 구현할 수 있기는 하다. 하지만 또 다른 문제가..)
         */
    }

    @Test
    void updateTitle2() {
        Post spring = savePost();
        spring.setTitle("hibernate"); // update
        // findAll()하기 전에 DB에 싱크를 맞춰야 하므로 update 쿼리가 날아간다.

        List<Post> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo("hibernate");
    }

}