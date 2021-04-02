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
- persist(): Transient 상태의 객체를 Persistent 상태로 변경한다. <br/>
    (Transient 상태의 객체는 JPA와 하이버네이트 둘 다 이 객체에 대해서 모르는 상태이다. (이 객체의 아이디를 가지고 있지 않은 상태.)) 
- merge(): Detached 상태의 객체를 Persistent 상태로 변경한다. <br/>
    (Detached 상태는 한번이라도 Persistent 상태가 되었었던 객체. (이 객체의 아이디를 가지고 있는 상태.)) <br/>
    merge()를 실행하더라도 데이터베이스에 해당 데이터가 없으면 insert()가 발생하기도 한다. <br/>
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

#### * Transient인지 Detached인지 어떻게 판단하는가?
- 엔티티의 @Id 프로퍼티를 찾는다. <br/>
해당 프로퍼티가 null이면 Transient 상태로 판단하고, id가 null이 아니면 Detached 상태로 판단한다.
- 엔티티가 Persistable 인터페이스를 구현하고 있다면 isNew() 메소드에 위임한다.
- JpaRepositoryFactory를 상속받는 클래스를 만들고 getEntityInformation()을 오버라이딩해서 <br/>
자신이 원하는 판단 로직을 구현할 수도 있다. 
<br/>

#### * EntityManager.persist()
persist() 메소드에 넘긴 그 엔티티 객체를 Persistent 상태로 변경한다.
<br/>

#### * EntityManager.merge()
merge() 메소드에 넘긴 그 엔티티의 복사본을 만들고,<br/>
그 복사본을 다시 Persistent 상태로 변경하고 그 복사본을 반환한다.
<br/>

<pre>
@Test
void save() {
    Post post = new Post();
    post.setTitle("jpa");
    Post savedPost = postRepository.save(post);// persist() 호출, insert 쿼리 발생.
    // 위와 같이 save()는 영속화되어 있는 객체를 리턴해 준다.
    // persist()는 persist() 메소드에 넘긴 그 엔티티 객체를 Persistent 상태로 변경한다.

    assertThat(entityManager.contains(post)).isTrue(); // post도 영속화가 되고,
    assertThat(entityManager.contains(savedPost)).isTrue(); // savedPost도 영속화가 된다.
    assertThat(savedPost == post).isTrue(); // 여기에서는 post와 savedPost가 같다. 하지만 다른 경우도 있다. 

    Post postUpdate = new Post();
    postUpdate.setId(post.getId());
    postUpdate.setTitle("hibernate");
    Post updatedPost = postRepository.save(postUpdate);// merge() 호출, update 쿼리 발생.
    // merge()는 merge() 메소드에 넘긴 그 엔티티의 복사본을 만들고,
    // 그 복사본을 다시 Persistent 상태로 변경하고 그 복사본을 반환한다.

    assertThat(entityManager.contains(updatedPost)).isTrue(); // updatedPost는 영속화가 되고,
    assertThat(entityManager.contains(postUpdate)).isFalse(); // postUpdate는 영속화가 되지 않는다.
    assertThat(updatedPost == postUpdate).isFalse(); // updatedPost와 postUpdate는 같지 않다.
    
    // postUpdate.setTitle("good luck"); // 파라미터로 넘긴 postUpdate는 영속화 상태가 아니기 때문에 변경되지 않는다.
    updatedPost.setTitle("good luck"); // 반환 받은 updatedPost는 영속화 상태이기 때문에 변경된다.
    // 내가 명시적으로 update하라고 알려주지 않아도 jpa가 알아서 객체 상태를 감지하다가
    // 필요하다고 느낀 시점(여기서는 아래의 findAll()이다. '데이터를 가져오려고 하네? 빨리 싱크해야겠다.'
    // 이 시점에 위의 모든 변경 내용들이 반영되고 데이터를 가져온다.)에 디비에 반영이 된다. 

    List❮Post❯ all = postRepository.findAll();
    assertThat(all.size()).isEqualTo(1);
}
</pre>
위와 같이 파라미터로 넘긴 객체와 저장 후 반환 받은 객체는 다를 수 있다.<br/>
때문에 실수를 줄이기 위해서는 무조건 반환 받은 객체를 사용하고 파라미터로 넘긴 객체는 더이상 사용하지 않는 것이 좋다. 
<br/><br/><br/><br/>

# 쿼리 메소드 
https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
<br/>

# 쿼리 메소드 Sort
<pre>
/**
 * Sort의 정렬옵션에 들어갈 수 있는 문자열은 반드시 엔티티의 프로퍼티이거나 alias이어야 한다.
 * 예를 들어, title은 Post 엔티티의 프로퍼티이므로 title로 정렬하는 것이 가능하다.
 * 그리고 아래처럼 p.title의 alias인 pTitle로도 정렬이 가능하다.
 * 프로퍼티 또는 alias가 아닌 경우에는 Sort로 사용할 수 없다. (예를 들어, 함수와 같은..)
 * 하지만 JpaSort.unsafe()를 사용하면 함수도 사용할 수 있다.
 */
@Query("SELECT p, p.title AS pTitle FROM Post AS p WHERE p.title = ?1")
List❮Post❯ findByTitle(String title, Sort sort);
</pre>
<pre>
@Test
void findByTitle() {
    savePost();
    
    // List❮Post❯ all = postRepository.findByTitle("Spring Data Jpa", Sort.by("title"));
    // List❮Post❯ all = postRepository.findByTitle("Spring Data Jpa", Sort.by("LENGTH(title)")); // (X) 함수는 사용할 수 없다.
    List❮Post❯ all = postRepository.findByTitle("Spring Data Jpa", JpaSort.unsafe("LENGTH(title)")); // (O) JpaSort.unsafe()를 사용하면 함수도 가능하다.

    assertThat(all.size()).isEqualTo(1);
}
</pre>
<br/><br/><br/><br/>

# Named Parameter와 SpEL
<br/>

### Named Parameter
<pre>
/**
 * [ Named Parameter ]
 * @Query에서 참조하는 매개변수를 '?1', '?2' 이렇게 채번으로 참조하는게 아니라
 * 이름으로 ':title' 이렇게 참조할 수 있다.
 */
@Query("SELECT p, p.title AS pTitle FROM Post AS p WHERE p.title = :title") // Named Parameter
List❮Post❯ findByTitle(@Param("title") String title, Sort sort);
</pre><br/>

### SpEL (Spring Expression Language) 
스프링 표현 언어. 
<pre>
/**
 * [ SpEL ]
 * 엔티티 이름 대신에 '#{#entityName}'를 써도 된다.
 * 이곳은 Post에 대한 Repository이기 때문에 자동적으로 엔티티 이름을 Post로 인식한다.
 * 장점은 엔티티 이름이 변경되었을 때 Repository에 있는 모든 쿼리의 엔티티 이름을 변경하지 않아도 된다는 장점이 있다.
 */
@Query("SELECT p, p.title AS pTitle FROM #{#entityName} AS p WHERE p.title = :title") // Named Parameter
List❮Post❯ findByTitle(@Param("title") String title, Sort sort);
</pre>
SpEL에 대한 자세한 내용은 아래 URL 참조. <br/>
https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions <br/>
<br/><br/><br/><br/>

# Update 쿼리 메소드
주로 Persistent 상태의 객체를 관리하다가 이 객체 상태에 변화가 일어났고, <br/>
이 변화를 DB에 싱크해야겠다라고 하는 시점에 flush()를 한다. <br/>
flush()를 해서 객체 상태를 데이터베이스에 동기화 시킨다. 이때 보통 update 쿼리가 자동적으로 실행이 된다. <br/>
때문에 우리가 직접 update 쿼리 메소드를 만들어서 사용해야 하는 경우는 거의 없다. <br/>
꼭 정의해서 사용해야 할 때에는 직접 정의해서 사용할 수도 있다. <br/>
**_하지만 추천하지 않는 방법이다._** <br/>
굳이 만들려면 아래와 같이 update 쿼리 메소드를 만들 수 있다. 
<pre>
/**
 * [ update 쿼리 메소드 ]
 */
// @Modifying // 조회가 아닌 수정하는 쿼리임을 표시. update 쿼리를 만들 때 붙여줘야 한다.
@Modifying(clearAutomatically = true, flushAutomatically = true)
@Query("UPDATE Post p Set p.title = ?1 WHERE p.id = ?2")
int updateTitle(String title, Long id);
</pre>
<pre>
@Test
void updateTitle() {
    Post spring = savePost(); // Persistent 상태의 객체.
    String hibernate = "hibernate";
    int update = postRepository.updateTitle(hibernate, spring.getId()); // id를 가지고 title을 변경한다.
    assertThat(update).isEqualTo(1);

    // spring이 Persistent 상태이기 때문에 DB에서 조회하지 않고 JPA가 캐싱하고 있던 것을 그대로 가져온다.
    // 때문에 값이 변경되지 않은 상태 그대로이다.
    Optional❮Post❯ byId = postRepository.findById(spring.getId()); // select를 하지 않는다.
    assertThat(byId.get().getTitle()).isEqualTo(hibernate); // title은 여전히 'spring'이기 때문에 테스트가 깨진다.

    /**
     * update 쿼리 메소드에서
     * '@Modifying(clearAutomatically = true, flushAutomatically = true)'를 붙이면 테스트가 깨지지 않는다.
     *
     * [ clearAutomatically ]
     *  => update 쿼리를 실행한 이후에 Persistent context를 clear 해준다. (캐시를 비워준다. 때문에 이후에 조회할 때 DB에서 새로 읽어온다.)
     *
     * [ flushAutomatically ]
     *  => update 쿼리 실행 이전에 Persistent context를 flush 해준다.
     *
     * 하지만 이 방법을 추천하지는 않는다.
     * (이 방법으로 delete도 구현할 수 있기는 하다. 하지만 또 다른 문제가..)
     */
}
</pre>
<br/>
그냥.. 위와 같이 update 쿼리 메소드를 만들지 않고, 아래처럼 사용하는게 간단하기도 하고 권장하는 방법이다.
<pre>
@Test
void updateTitle2() {
    Post spring = savePost();
    spring.setTitle("hibernate"); // update
    // findAll()하기 전에 DB에 싱크를 맞춰야 하므로 update 쿼리가 날아간다.

    List❮Post❯ all = postRepository.findAll();
    assertThat(all.get(0).getTitle()).isEqualTo("hibernate");
}
</pre>


<br/><br/><br/><br/>