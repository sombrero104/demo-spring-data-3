package me.demospringdata3.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PostController {

    private final PostRepository postRepository;

    /**
     * 생성자가 하나만 있고, PostRepository가 빈으로 등록되어 있는 경우에는 자동으로 빈을 주입해 준다.
     */
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /*@GetMapping("/posts/{id}") // 원래 id는 문자열로 받지만
    public String getPost(@PathVariable Long id) { // Long형으로 데이터바인더가 기본적으로 바인딩해준다.
        Optional<Post> byId = postRepository.findById(id);
        Post post = byId.get();
        return post.getTitle();
    }*/

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable("id") Post post) {
        return post.getTitle();
    }

    /**
     * HATEOAS를 사용하지 않을 경우.
     */
    @GetMapping("/posts")
    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    /**
     * HATEOAS를 사용할 경우. (HATEOAS 의존성을 추가한 경우.)
     */
    /*@GetMapping("/posts")
    public PagedModel<EntityModel<Post>> getPosts(Pageable pageable, PagedResourcesAssembler<Post> assembler) {
        Page<Post> all = postRepository.findAll(pageable);
        return assembler.toModel(all);
    }*/

}
