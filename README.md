# [스프링 데이터 JPA 3 - 스프링 데이터 Common]
<br/>

# Web 2부: DomainClassConverter
<br/>

### * Converter
컨버터는 어떤 하나의 타입을 다른 타입으로 변환하는 인터페이스이다.<br/>
그래서 아무 타입이나 다 변환할 수 있다. 
<pre>
Interface Converter❮S, T❯ 
    // (S(the source type)타입을 T(the target type)타입으로 변환)
</pre>
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/convert/converter/Converter.html
<br/><br/>

## DomainClassConverter
엔티티 타입을 아이디로도 변환하고, 아이디를 엔티티 타입으로 변환해주기도 한다. <br/>
때문에 아래처럼 아이디를 파라미터로 받는 메소드를
<pre>
@GetMapping("/posts/{id}") // 원래 id는 문자열로 받지만
public String getPost(@PathVariable Long id) { // Long형으로 데이터바인더가 기본적으로 바인딩해준다.
    Optional❮Post❯ byId = postRepository.findById(id);
    Post post = byId.get();
    return post.getTitle();
}
</pre>
아래처럼 엔티티로 받아와도 DomainClassConverter가 변환해 준다. 
<pre>
@GetMapping("/posts/{id}")
public String getPost(@PathVariable("id") Post post) {
    return post.getTitle();
}
</pre>
<br/><br/>

### * Formatter
'어떠한 문자열을 어떠한 타입으로 바꿀 것인가.' <br/>
Converter와 매우 비슷한데 다른 점은 Formatter는 문자열 기반이라는 것이다. 
<pre>
Interface Formatter❮T❯
    // (문자열을 T(the target type)타입으로 변환)
</pre>
아래와 같이 아이디를 파라미터로 받는 경우에는 아이디가 문자열이 아닐수도 있기 때문에 (현재 여기에서는 Long이다.)<br/>
Formatter가 사용되지 않고, DomainClassConverter가 사용된다. 
<pre>
@GetMapping("/posts/{id}")
public String getPost(@PathVariable("id") Post post) {
    return post.getTitle();
}
</pre>
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/format/Formatter.html
<br/><br/><br/><br/>

# Web 3부: Pageable과 Sort
<br/>

### * 스프링 MVC HandlerMethodArgumentResolver
스프링 MVC 핸들러 메소드의 매개변수로 받을 수 있는 객체를 확장하고 싶을 때 사용하는 인터페이스.<br/>
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/method/support/HandlerMethodArgumentResolver.html <br/>
여기에 나와있는 구현체들을 모두 사용할 수 있다. (abstract 빼고) <br/>
여기에 추가로 스프링 데이터 JPA의 웹 기능을 사용하면 Pageable과 Sort를 사용할 수도 있다. <br/>
(주로 Pageable을 사용하고 Sort는 거의 사용하지 않는다. Sort만 사용하게 되면 모든 내용을 가져와서 소팅만 하기 때문..)
<br/><br/><br/><br/>

# Web 4부: HATEOAS
HATEOAS 의존성을 추가해 준다. 
<pre>
❮dependency❯
    ❮groupId❯org.springframework.boot❮/groupId❯
    ❮artifactId❯spring-boot-starter-hateoas❮/artifactId❯
❮/dependency❯
</pre>
그리고 아래와 같이 PagedResourcesAssembler를 받도록 해준다. 
<pre>
/**
 * HATEOAS를 사용할 경우. (HATEOAS 의존성을 추가한 경우.)
 */
@GetMapping("/posts")
public PagedModel❮EntityModel❮Post❯❯ getPosts(Pageable pageable, PagedResourcesAssembler❮Post❯ assembler) {
    Page❮Post❯ all = postRepository.findAll(pageable);
    return assembler.toModel(all);
}
</pre><br/> 
테스트를 하면 응답이 아래와 같이 나온다. 
<pre>
{
  "_embedded": {
    "postList": [
      {
        "id": 90,
        "title": "jpa",
        "created": null
      },
      {
        "id": 92,
        "title": "jpa",
        "created": null
      },
      {
        "id": 96,
        "title": "jpa",
        "created": null
      },
      {
        "id": 97,
        "title": "jpa",
        "created": null
      },
      {
        "id": 98,
        "title": "jpa",
        "created": null
      },
      {
        "id": 99,
        "title": "jpa",
        "created": null
      },
      {
        "id": 100,
        "title": "jpa",
        "created": null
      },
      {
        "id": 89,
        "title": "jpa",
        "created": null
      },
      {
        "id": 78,
        "title": "jpa",
        "created": null
      },
      {
        "id": 81,
        "title": "jpa",
        "created": null
      }
    ]
  },
  "_links": {
    "first": {
      "href": "http://localhost/posts/?page=0&size=10&sort=created,desc&sort=title,asc"
    },
    "prev": {
      "href": "http://localhost/posts/?page=2&size=10&sort=created,desc&sort=title,asc"
    },
    "self": {
      "href": "http://localhost/posts/?page=3&size=10&sort=created,desc&sort=title,asc"
    },
    "next": {
      "href": "http://localhost/posts/?page=4&size=10&sort=created,desc&sort=title,asc"
    },
    "last": {
      "href": "http://localhost/posts/?page=9&size=10&sort=created,desc&sort=title,asc"
    }
  },
  "page": {
    "size": 10,
    "totalElements": 100,
    "totalPages": 10,
    "number": 3
  }
}
</pre><br/>
아래와 같이 HATEOAS를 사용하지 않을 경우에는
<pre>
/**
 * HATEOAS를 사용하지 않을 경우. 
 */
@GetMapping("/posts")
public Page❮Post❯ getPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
}
</pre>
아래와 같이 JSON 결과가 다르게 나오는 것을 확인할 수 있다. 
<pre>
{
  "content": [
    {
      "id": 91,
      "title": "jpa",
      "created": null
    },
    {
      "id": 98,
      "title": "jpa",
      "created": null
    },
    {
      "id": 89,
      "title": "jpa",
      "created": null
    },
    {
      "id": 96,
      "title": "jpa",
      "created": null
    },
    {
      "id": 99,
      "title": "jpa",
      "created": null
    },
    {
      "id": 100,
      "title": "jpa",
      "created": null
    },
    {
      "id": 101,
      "title": "jpa",
      "created": null
    },
    {
      "id": 97,
      "title": "jpa",
      "created": null
    },
    {
      "id": 80,
      "title": "jpa",
      "created": null
    },
    {
      "id": 83,
      "title": "jpa",
      "created": null
    }
  ],
  "pageable": {
    "sort": {
      "unsorted": false,
      "sorted": true,
      "empty": false
    },
    "offset": 30,
    "pageNumber": 3,
    "pageSize": 10,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 101,
  "totalPages": 11,
  "last": false,
  "numberOfElements": 10,
  "number": 3,
  "size": 10,
  "sort": {
    "unsorted": false,
    "sorted": true,
    "empty": false
  },
  "first": false,
  "empty": false
}
</pre>
<br/><br/><br/><br/>

# [스프링 데이터 JPA 3 - 스프링 데이터 JPA]
<br/>

# JpaRepository 
<br/>

### @EnableJpaRepositories
- 스프링 부트 사용할 때는 사용하지 않아도 자동 설정 됨.
- 스프링 부트 사용하지 않을 때는 @Configuration과 같이 사용.
<br/>

### @Repository 애노테이션
@Repository이 이미 붙어있기 때문에 직접 붙이지 않아도 된다. <br/>
구현체인 SimpleJpaRepository에 이미 붙어있기 때문.. <br/>
@Repository는 SQLException 또는 JPA 관련 예외를 스프링의 DataAccessException으로 변환 해준다.<br/>
여러가지 모든 예외를 SQLException 하나로 발생시켜서 SQLException 안의 코드값을 확인하고 <br/>
실제로 어떤 에러인지 확인해야 하는 불편함이 있어서<br/>
구체적으로 어떤 에러인지 알 수 있는 DataAccessException의 하위 클래스들 중 하나로 매핑해서<br/>
클래스 이름만 봐도 알 수 있도록 만들었다. (그런데 JPA보다 하이버네이트가 발생시키는 예외가 좀 더 직관적..)
<br/><br/><br/><br/>

# JpaRepository.save() 메소드
새로운 객체일 경우에는 insert 쿼리, 새로운 객체가 아닐 경우에는 update 쿼리가 발생한다.
<pre>
@Test
void save() {
    Post post = new Post();
    post.setId(1l);
    post.setTitle("jpa");
    postRepository.save(post); // insert 쿼리 발생.

    Post postUpdate = new Post();
    postUpdate.setId(1l);
    postUpdate.setTitle("hibernate");
    postRepository.save(postUpdate); // update 쿼리 발생.

    List❮Post❯ all = postRepository.findAll();
    assertThat(all.size()).isEqualTo(1);
}
</pre>
SimpleJpaRepository 소스를 보면 새로운 객체(Transient 상태의 객체)일 경우에는 EntityManager.persist(), <br/>
새로운 객체가 아닐 경우(Detached 상태의 객체)에는 EntityManager.merge()를 실행하는 것을 확인할 수 있다. 
- persist(): Transient 상태의 객체를 Persistent 상태로 변경한다.
- merge(): Detached 상태의 객체를 Persistent 상태로 변경한다. 
<pre>
@Transactional
public ❮S extends T❯ S save(S entity) {
    Assert.notNull(entity, "Entity must not be null.");
    if (this.entityInformation.isNew(entity)) {
        this.em.persist(entity);
        return entity;
    } else {
        return this.em.merge(entity);
    }
}
</pre>

<br/><br/><br/><br/>