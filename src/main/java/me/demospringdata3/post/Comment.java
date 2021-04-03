package me.demospringdata3.post;

import javax.persistence.*;

/**
 * @NamedEntityGraph
 *  => @Entity에서 재사용할 여러 엔티티 그룹을 정의할 때 사용.
 */
/*@NamedEntityGraph(name = "Comment.post",
        attributeNodes = @NamedAttributeNode("post"))*/
@Entity
public class Comment {

    @Id @GeneratedValue
    private Long id;

    private String comment;

    // @ManyToOne(fetch = FetchType.EAGER) // @ManyToOne은 기본 Fetch 모드는 EAGER이다.
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private int up;

    private int down;

    private boolean best;

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

}
