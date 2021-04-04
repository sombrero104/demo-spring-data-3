package me.demospringdata3.post;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @NamedEntityGraph
 *  => @Entity에서 재사용할 여러 엔티티 그룹을 정의할 때 사용.
 */
/*@NamedEntityGraph(name = "Comment.post",
        attributeNodes = @NamedAttributeNode("post"))*/
@Entity
@EntityListeners(AuditingEntityListener.class) // Auditing 기능을 사용하려면 이 애노테이션을 붙여줘야 한다.
public class Comment {

    @Id @GeneratedValue
    private Long id;

    private String comment;

    /**
     * Enum 맵핑.
     * 기본값은 ORDINAL인데 Enum 값을 순서대로 0, 1, 2 값을 사용한다.
     * 추후 만약 Enum 값의 순서가 바뀌거나 수정될 수도 있기 때문에
     * 문자열값으로 사용하는 것이 안전하다.
     * 때문에 Enum을 사용할 때에는 'EnumType.STRING'으로 설정해서 사용하는 것을 권장한다.
     */
    @Enumerated(value = EnumType.STRING)
    private CommentStatus commentStatus;

    // @ManyToOne(fetch = FetchType.EAGER) // @ManyToOne은 기본 Fetch 모드는 EAGER이다.
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private int up;

    private int down;

    private boolean best;

    /**
     * [ Auditing ]
     * 엔티티에 변화가 생길 때마다 아래 정보들을 기록.
     * created, createdBy, updated, updatedBy
     */
    @CreatedDate
    private Date created;

    @CreatedBy
    @ManyToOne
    private Account createdBy;

    @LastModifiedDate
    private Date updated;

    @LastModifiedBy
    @ManyToOne
    private Account updatedBy;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public boolean isBest() {
        return best;
    }

    public void setBest(boolean best) {
        this.best = best;
    }

    /**
     * 엔티티가 생성될 때 (Persistent 상태가 될 때)
     * 바로 이전에 실행된다.
     */
    @PrePersist
    public void prePersist() {
        System.out.println("=============================");
        System.out.println("Pre Persist is called!!");
        System.out.println("=============================");
        this.created = new Date();
        // this.createdBy = SecurityContextHolder.getContext().getAuthentication();
        // 스프링 시큐리티에서 현재 사용자 꺼내오기.
    }

}
