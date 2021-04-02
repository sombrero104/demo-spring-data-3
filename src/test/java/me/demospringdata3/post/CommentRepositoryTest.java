package me.demospringdata3.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Test
    void getComment() {
        commentRepository.findById(1l);
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
    }

}