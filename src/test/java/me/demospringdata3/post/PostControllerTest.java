package me.demospringdata3.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void getPost() throws Exception {
        Post post = new Post();
        post.setTitle("jpa");
        postRepository.save(post);

        mockMvc.perform(get("/posts/" + post.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

}