package me.demospringdata3.post;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // @EntityGraph(value = "Comment.post") // Comment의 Post를 EAGER 모드로 가져오게 된다.
    @EntityGraph(attributePaths = "post") // @NamedEntityGraph로 이름 설정없이 이렇게 한줄로 사용해도 된다.
    Optional<Comment> getById(Long id);

    List<Comment> findByPost_Id(Long id);

}
