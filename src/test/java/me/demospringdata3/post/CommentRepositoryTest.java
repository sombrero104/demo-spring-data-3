package me.demospringdata3.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Test
    void getComment() {
        // commentRepository.findById(1l);
        /**
         * @ManyToOne에서는 Fetch 모드를 설정하지 않으면 기본값이 EAGER이기 때문에
         * comment 하나를 가져올 때 연관된 post 데이터도 같이 가져온다.
         * 아래와 같은 쿼리가 실행된다.
         * select
         *         comment0_.id as id1_0_0_,
         *         comment0_.comment as comment2_0_0_,
         *         comment0_.post_id as post_id3_0_0_,
         *         post1_.id as id1_1_1_,
         *         post1_.created as created2_1_1_,
         *         post1_.title as title3_1_1_
         *     from
         *         comment comment0_
         *     left outer join
         *         post post1_
         *             on comment0_.post_id=post1_.id
         *     where
         *         comment0_.id=?
         */

        // commentRepository.findById(1l);
        /**
         * Comment에서 Post의 Fetch 모드를 Lazy로 바꾸면 comment만 가져온다.
         * 만약 Comment의 Post에 접근하게 되면 그때서야 Post도 같이 조회하는 쿼리가 날아간다.
         * (Comment 인스턴스가 Persistent 상태(Persistent context가 관리하는 상태)이어야지만 가능하다.)
         * select
         *         comment0_.id as id1_0_0_,
         *         comment0_.comment as comment2_0_0_,
         *         comment0_.post_id as post_id3_0_0_
         *     from
         *         comment comment0_
         *     where
         *         comment0_.id=?
         */

        /*commentRepository.getById(1l); // @EntityGraph를 사용했기 때문에 EAGER 모드로 가져온다.
        System.out.println("================================");
        commentRepository.findById(1l); // Fetch 모드를 LAZY로 설정했기 때문에 LAZY 모드로 가져온다.
        */

        /**
         * [ Projection ]
         */
        // commentRepository.findByPost_Id(1l);
        /**
         * 1. 프로젝션을 사용하지 않았을 때에는 모든 컬럼의 데이터를 다 select 한다.
         * select
         *         comment0_.id as id1_0_,
         *         comment0_.best as best2_0_,
         *         comment0_.comment as comment3_0_,
         *         comment0_.down as down4_0_,
         *         comment0_.post_id as post_id6_0_,
         *         comment0_.up as up5_0_
         *     from
         *         comment comment0_
         *     left outer join
         *         post post1_
         *             on comment0_.post_id=post1_.id
         *     where
         *         post1_.id=?
         */
        /**
         * 2. Closed 프로젝션을 사용하면 특정 컬럼만 select 한다.
         * select
         *         comment0_.comment as col_0_0_,
         *         comment0_.up as col_1_0_,
         *         comment0_.down as col_2_0_
         *     from
         *         comment comment0_
         *     left outer join
         *         post post1_
         *             on comment0_.post_id=post1_.id
         *     where
         *         post1_.id=?
         */
        /**
         * 3. Open 프로젝션을 사용하면 일단 다 select해서 가져온다.
         * select
         *         comment0_.id as id1_0_,
         *         comment0_.best as best2_0_,
         *         comment0_.comment as comment3_0_,
         *         comment0_.down as down4_0_,
         *         comment0_.post_id as post_id6_0_,
         *         comment0_.up as up5_0_
         *     from
         *         comment comment0_
         *     left outer join
         *         post post1_
         *             on comment0_.post_id=post1_.id
         *     where
         *         post1_.id=?
         */
        /**
         * 4. default 메소드를 사용하는 방법 (Closed 프로젝션 + Open 프로젝션)
         * Closed 프로젝션의 장점(특정 컬럼만 select)과 Open 프로젝션의 장점(커스텀한 구현체를 만들어서 메소드를 추가)을 다 사용할 수 있는 방법.
         */

        Post post = new Post();
        post.setTitle("jpa");
        Post savedPost = postRepository.save(post);

        Comment comment = new Comment();
        comment.setPost(savedPost);
        comment.setUp(10);
        comment.setDown(1);
        commentRepository.save(comment);

        commentRepository.findByPost_Id(savedPost.getId()).forEach(c -> {
            System.out.println("================================");
            System.out.println(c.getVotes());
        });
    }

}