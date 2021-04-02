package me.demospringdata3.post;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 애플리케이션의 모든 빈이 다 등록이 되는 통합 테스트(integration test).
 * application.properties에 있는 속성도 모두 적용이 된다.
 * 혹은 테스트용으로 프로퍼티즈 파일을 별개로 만들어서 사용하고 싶은 경우에는
 * 테스트 코드에 @ActiveProfiles("test")를 붙이고,
 * test 디렉토리 하위에 resources 디렉토리를 만들어서
 * application-test.properties 파일을 만들어서
 * application.properties에 있는 속성 중에 오버라이딩할 부분만 작성해 준다.
 * 아무것도 작성하지 않으면 application.properties에 있는 속성을 그대로 사용하게 된다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Test
    void getPost() throws Exception {
        Post post = new Post();
        post.setTitle("jpa");
        postRepository.save(post);

        mockMvc.perform(get("/posts/" + post.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("jpa"));
    }

    @Test
    void getPosts() throws Exception {
        createPosts();

        mockMvc.perform(get("/posts/")
                    .param("page", "3")
                    .param("size", "10")
                    .param("sort", "created,desc")
                    .param("sort", "title"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title", is("jpa")));
                    // $.content[0].title => json 배열의 내용 중 0번째의 title 값.
    }

    private void createPosts() {
        int postsCount = 100;
        while (postsCount > 0) {
            Post post = new Post();
            post.setTitle("jpa");
            postRepository.save(post);
            postsCount--;
        }
    }

    /**
     * Post 엔티티에서 id에 @GeneratedValue를 세팅하지 않고 테스트.
     */
    @Test
    void crud() {
        Post post = new Post();
        post.setTitle("jpa");
        postRepository.save(post); // 아이디가 없이 저장하려고 하기 때문에 에러 발생.
        // 위에서 아예 저장도 안되기 때문에
        // 아래처럼 조회하는 코드도 작성해준다.
        List<Post> all = postRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

}