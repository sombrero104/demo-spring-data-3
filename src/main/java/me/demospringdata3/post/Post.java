package me.demospringdata3.post;

import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.util.Date;

/**
 * @NamedQuery를 사용하게 되면 엔티티에 쿼리를 작성해야 하고, 코드가 지저분해진다.
 * 그래서 Repository에 있는 메소드쿼리에서 @Query()를 사용해서 쿼리를 정의하는 편이 더 낫다.
 *      => @Query("SELECT p FROM Post AS p WHERE p.title = ?1")
 * PostRepository의 findByTitle() 메소드 쿼리 참조.
 */
@Entity
// @NamedQuery(name = "Post.findByTitle", query = "SELECT p FROM Post AS p WHERE p.title = ?1") // title이 첫번째 인자값과 같은 것. (JPQL)
// @NamedNativeQuery(name  = "",  query = "") // (Native Query로 사용하는 방법.)
public class Post {

    @Id @GeneratedValue
    private Long id;

    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

}
