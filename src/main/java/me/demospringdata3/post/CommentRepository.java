package me.demospringdata3.post;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    // @EntityGraph(value = "Comment.post") // Comment의 Post를 EAGER 모드로 가져오게 된다.
    @EntityGraph(attributePaths = "post") // @NamedEntityGraph로 이름 설정없이 이렇게 한줄로 사용해도 된다.
    Optional<Comment> getById(Long id);

    /**
     * [ Projection ]
     */
    // 프로젝션을 사용하지 않은 경우, 모든 Comment의 컬럼 데이터를 다 select해서 가져오게 된다.
    // List<Comment> findByPost_Id(Long id);

    // Closed 프로젝션을 사용할 경우, CommentSummary에 있는 컬럼만 select해서 가져온다.
    // Closed 프로젝션 => 딱 이거이거만 가져오겠다고 정의해두는 방식.
    // List<CommentSummary> findByPost_Id(Long id);
    // List<CommentOnly> findByPost_Id(Long id);

    /**
     * 프로젝션이 여러개일 경우 제네릭을 사용한다.
     * 파라미터로 프로젝션 타입을 받아서 해당 프로젝션 타입으로 반환할 수 있다.
     */
    <T> List<T> findByPost_Id(Long id, Class<T> type);
}
