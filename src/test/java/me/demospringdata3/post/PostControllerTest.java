package me.demospringdata3.post;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 애플리케이션의 모든 빈이 다 등록이 되는 통합 테스트(integration test).
 * application.properties에 있는 속성도 모두 적용이 된다.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {



}