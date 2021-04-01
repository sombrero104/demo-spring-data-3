package me.demospringdata3.post;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 애플리케이션의 모든 빈이 다 등록이 되는 통합 테스트(integration test).
 * application.properties에 있는 속성도 모두 적용이 된다.
 * 혹은 테스트용으로 프로퍼티즈 파일을 별개로 만들어서 사용하고 싶은 경우에는
 * 테스트 코드에 @ActiveProfiles("test")를 붙이고,
 * test 디렉토리 하위에 resources 디렉토리를 만들어서
 * application-test.properties 파일을 만들어준다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostControllerTest {



}