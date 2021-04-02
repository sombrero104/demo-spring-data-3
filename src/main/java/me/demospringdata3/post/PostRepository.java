package me.demospringdata3.post;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleStartsWith(String title);

    // @Query("SELECT p FROM Post AS p WHERE p.title = ?1")
    // @Query(value = "", nativeQuery = true) // native query를 쓴 경우에는 nativeQuery = true 옵션을 줘서 알려준다.
    // List<Post> findByTitle(String title);

    /**
     * Sort의 정렬옵션에 들어갈 수 있는 문자열은 반드시 엔티티의 프로퍼티이거나 alias이어야 한다.
     * 예를 들어, title은 Post 엔티티의 프로퍼티이므로 title로 정렬하는 것이 가능하다.
     * 그리고 아래처럼 p.title의 alias인 pTitle로도 정렬이 가능하다.
     * 프로퍼티 또는 alias가 아닌 경우에는 Sort로 사용할 수 없다. (예를 들어, 함수와 같은..)
     */
    @Query("SELECT p, p.title AS pTitle FROM Post AS p WHERE p.title = ?1")
    List<Post> findByTitle(String title, Sort sort);
}
