package me.demospringdata3.post;

import org.springframework.beans.factory.annotation.Value;

/**
 * [ Projection ]
 * 인터페이스 버전. 클래스로 만들어도 되지만 인터페이스로 만들 때보다 코드가 더 많아진다.
 */
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
    /*@Value("#{target.up + ' ' + target.down}")
    String getVotes();*/

    /**
     * [ default 메소드를 사용하는 방법 (Closed 프로젝션 + Open 프로젝션) (추천) ]
     * Closed 프로젝션의 장점(특정 컬럼만 select)과 Open 프로젝션의 장점(커스텀한 구현체를 만들어서 메소드를 추가)을 다 사용할 수 있는 방법.
     */
    default String getVotes() {
        return getUp() + " " + getDown();
    }

}
