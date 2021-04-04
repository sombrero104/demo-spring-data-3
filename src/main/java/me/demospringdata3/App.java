package me.demospringdata3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Auditing 기능을 사용하려면 이 애노테이션을 붙여줘야 한다.
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
