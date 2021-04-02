package me.demospringdata3.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleStartsWith(String title);

    @Query("SELECT p FROM Post AS p WHERE p.title = ?1")
    // @Query(value = "", nativeQuery = true) // native query를 쓴 경우에는 nativeQuery = true 옵션을 줘서 알려준다.
    List<Post> findByTitle(String title);

}
