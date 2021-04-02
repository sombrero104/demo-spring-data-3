package me.demospringdata3.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

        assertThat(entityManager.contains(post)).isTrue(); // post도 영속화가 되고,
        assertThat(entityManager.contains(savedPost)).isTrue(); // savedPost도 영속화가 된다.
        assertThat(savedPost == post); // 여기에서는 post와 savedPost가 같다.

        Post postUpdate = new Post();
        postUpdate.setId(post.getId());
        postUpdate.setTitle("hibernate");
        Post updatedPost = postRepository.save(postUpdate);// merge() 호출, update 쿼리 발생.

        assertThat(entityManager.contains(updatedPost)).isTrue(); // updatedPost는 영속화가 되고,
        assertThat(entityManager.contains(postUpdate)).isFalse(); // postUpdate는 영속화가 되지 않는다.
        assertThat(updatedPost == postUpdate); // updatedPost와 postUpdate는 같지 않다.

        List<Post> all = postRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

}