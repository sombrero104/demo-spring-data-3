package me.demospringdata3.post;

import org.springframework.beans.factory.annotation.Value;

public interface CommentSummary {

    String getComment();

    int getUp();

    int getDown();

    /**
     * [ Open 프로젝션 ]
     * 여기서의 target은 Comment인데, target을 일단 가져와야 하므로
     * Open 프로젝션을 추가하게 되면 Comment 컬럼을 전부 select하게 된다.
     * 먼저 한정지어서 가져오는게 아니기 때문에 Closed가 아닌 Open 프로젝션이다.
     */
    @Value("#{target.up + ' ' + target.down}")
    String getVotes();

}
