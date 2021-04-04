package me.demospringdata3.post;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

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
    // <T> List<T> findByPost_Id(Long id, Class<T> type);

    /**
     * [ @Transactional ]
     * JPA 구현체로 Hibernate를 사용할 때 트랜잭션을 readOnly를 사용하면 좋은 점.
     *  => Flush 모드를 'NEVER'로 설정하여, Dirty checking을 하지 않도록 한다.
     *      - Flush 모드: 데이터베이스에 싱크를 하는 모드. 언제 데이터베이스에 싱크를 할 것인가.
     *          적절한 타이밍에 하게 되는데 보통은 커밋할 때, 또는 데이터를 read하기 전에 Flush를 한다.
     *          readOnly의 경우 데이터 변경이 일어나지 않기 때문에 Flush할 필요가 없다.
     *          그렇기 때문에 Persistent는 Dirty checking을 할 필요도 없다.
     *          (변경이 없으므로 변경을 감지해야 할 필요가 없는 것.)
     *          특히 데이터가 많은 경우에는 Dirty checking을 끄면 성능에 많은 도움이 된다.
     */
    @Transactional(readOnly = true)
    <T> List<T> findByPost_Id(Long id, Class<T> type);

}
