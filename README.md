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
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/format/Formatter.html


<br/><br/><br/><br/>
