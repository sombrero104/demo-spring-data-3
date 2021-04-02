package me.demospringdata3.post;

import javax.persistence.*;

@Entity
public class Comment {

    @Id @GeneratedValue
    private Long id;

    private String comment;

    // @ManyToOne(fetch = FetchType.EAGER) // @ManyToOne은 기본 Fetch 모드가 EAGER이다.
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

}
