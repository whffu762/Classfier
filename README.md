# Classifier

게시글 좋아요 싫어요 기능 추가

설계 과정 중 변경 사항

프론트에서 요청
최초 : 어떤 체크박스가 체크됐는지 확인하고 요청 보내자 아무것도 안 돼있으면 null 뭐 돼있으면 해당 체크박스와 체크값을 요청에 실어서 보냄
변경1 : 어떤 버튼을 눌렀냐와 이전에 눌린 버튼이 있는지 여부만 분기하자
최종 : 4가지로 분기되는 현재 방식 
이유 : 변경 이전 방법은 서버에서 처리해야 될 게 너무 많음 비즈니스 로직이 너무 복잡함 프론트에서 어느정도 분기된 후 서버에서 처리하는게 직관적이고 편한 듯?


추천/비추천 갯수
최초 : 갯수는 그냥 좋아요 누른 사람의 개수 뽑자 쿼리 dsl로 쿼리 만들어서 보내자
변경 : service에서 같이 처리하자 ++ -- 로 DB에 직접 접근해서 바꾸자
이유 : 복잡한 쿼리를 최소화하는게 성능에 더 좋은 것 같음 또한 좋아요 갯수가 많아질 수록 더 부하가 심함


deleteYn 필드
최초 : ArticleLikeHate에 deleteYn 필드를 이용해서 추천/비추천 둘 다 안한 계정을 유지
변경 : deleteYn 필드를 삭제하고 likeHate 의 값이 null 이면 둘 다 안한 계정으로 유지
이유 : null로 충분히 유지할 수 있어서 불필요함 또한 불필요한 분기 조건까지 생김


DB 매핑 관련
Article - ArticleLikeHate 가 연관 관계를 맺지 않는 이유
추천/비추천 누르는 유저가 많이 없음 즉 추천/비추천이 많이 눌리는 게시글은 많은 게시글 중 소수임 때문에 특정 게시글에 추천을 누른 사람을 DB에서 조회할 때 게시글에 좋아요를 누른 사람 전체를 조회하는 것 보다 해당 계정이 좋아요를 누른 게시글을 조회하는게 더 빠를 듯
추가적으로 게시물이 지워지면 좋아요 한 로그도 같이 지워져야 하지 않나? 싶어서 관계를 맺어야 할 것 같았는데 굳이 그럴 필요가 없음 예를 들어 유튜브에서도 좋아요한 동영상이 지워져도 삭제된 동영상입니다 라고 되지 내 목록에서 지워지진 않음
또한 굳이 이걸 유지해서 쓸 데가 없는 느낌...
지금 접근한 계정이 뭘 좋아요 눌렀는지만 알면 되지 누가 이 게시물에 좋아요를 눌렀는지 알게뭐람.. 중요한건 좋아요 갯수나 중요한 거 같음

계정의 추천 여부는 어떻게 유지함?
때문에 Member 테이블에서 해당 member가 어떤 Article을 좋아요 싫어요 했는지 저장됨


================================================

해야될 거
1. 체크박스인데 둘 중 하나만 체크되게끔
2. 추천/비추천을 서버로 어떤식으로 뭘 보낼건지
3. 계정 당 하나의 게시물에 한해 한 번만 추천/비추천 가능(여러 번 보내도 하나로 처리)
4. 추천/비추천 개수 받기
5. 로그인 계정만 사용할 수 있음
6. 게시글 추천/비추천 한 계정 유지해야 함
7. 비즈니스 로직 구현

처리 흐름
1. 추천/비추천 요청 보냄
2. 각 요청별로 다른 컨트롤러로 분기
3. 요청으로 받은 데이터를 이용해서 처리에 필요한 데이터 준비
- DB에 이미 존재하는지 확인 후 추출(없으면 NULL)
- 추천/비추천 개수 추출
4. 로직 수행
- 존재 여부에 따라 insert 혹은 update
- 추천/비추천 개수 연산
5. 결과물 DTO 반환


검사 조건
- 로그인 여부 확인
- 현재 접근한 계정의 해당 게시물에 추천/비추천 여부 확인
- 추천/비추천 요청의 종류(4 가지, 추천/비추천 체크 여부에 따라 분기)
- DB에 존재 여부(계정이 전에 추천/비추천 했는지 여부)
- likeHate 값

==================================================

DB 설계 및 구현
ArticleLikeHate : Member(계정)의 게시물에 대한 추천/비추천을 저장
필드
id : PK , 대리키를 PK로 이용 이유는 "해야될 거 3번"에서 설명
member : Member 테이블과 연관 관계를 맺음 DB에선 memberId만 저장됨
articleId : articleId
likeHate : 추천/비추천여부를 저장
true - 좋아요
false - 싫어요
null - 아무것도 안 누름
이 부분이 프론트에서 보내는 값과 다름 이에 대한 처리를 articleService에서 수행

Member : List<ArticleLikeHate>가 필드에 추가됨 
- 이를 통해 해당 계정이 어떤 게시물에 추천/비추천을 눌렀는지 유지
- ArticleLikeHate 와 1 : N 관계를 맺음(Member 가 1)

Article : likeNum, hateNum이 추가됨 
- 단순히 게시물의 추천/비추천 개수만 가짐
- 게시물에서 어떤 계정이 추천을 눌렀는지 유지할 필요가 없음

==================================================

해야될 거 구현

1. 체크박스인데 둘 중 하나만 체크 
필요 조건
- 다시 누르면 체크 해제되야 함
- 둘 중 하나만 체크되야 함
- 아무것도 체크되지 않을 수 있음

구현
- ArticleDetail 추천/비추천 주석 참고
- 기본 로직은 체크될 때 다른 박스가 체크돼있는지 확인하고 해제하는 방식
- 라디오 박스는 다시 눌렀을 때 체크 해제가 안되기 때문에 사용할 수 없음


2. 추천/비추천을 서버로 어떤식으로 무엇을 보낼건지
분기 조건
- 아무것도 안 눌린 상태에서 추천 누름
- 아무것도 안 눌린 상태에서 비추천 누름
- 추천 눌린 상태에서 비추천 누름
- 비추천 눌린 상태에서 추천 누름
위 네 가지 경우를 나눠서 요청을 보냄

구현
- ArticleDetail 추천/비추천의 Script 함수 참고(LikeHate 관련)
- 각각 onlyLike, onlyHate, unLikeAndHate, unHateAndLike 컨트롤러로 요청을 보냄
- unLikeAndHate와 unHateAndLike 부분은 1번 체크 박스와 혼합해서 구현
- 때문에 프론트에서 보내는 true 와 false 의 의미가 각 요청마다 달라짐 자세한건 아래서

2-2.프론트에서 무엇을 보낼건지
필요한 값
- memberId : 어떤 계정이 놀렀는지 구분
- articleId : 어떤 게시물에 눌렀는지 구분
- likeHate : 추천/비추천 여부

구현
- memberId : 서버에서 Authentication을 통해 접근
- articleId : 프론트에서 추출
- likeHate : 체크 박스 값 추출
문제는 버튼이 두 종류인데 어떻게 true/false로 각 버튼의 값을 보내냐임
1. likeHate 는 어떤 버튼인지 구분하지 않고 그저 체크되면 true 해제되면 false를 보냄
2. 어떤 버튼이 체크된 것 인지는 위 분기조건을 통해 구분함
onlyLike - 체크되면 true 해제되면 false
onlyHate - 체크되면 true 해제되면 false
unLikeAndHate - 항상 true
unHateAndLike - 항상 true
이 값에 대한 처리는 서버에서 이루어짐


3. 계정 당 하나의 게시물에 한해 한 번만 추천/비추천 가능
필요 조건
- 추천/비추천을 여러 번 보내도 하나의 계정에서 보낸 값이면 하나로 처리

구현
ArticleService 의 checkSavedNull() 참고
- JPA의 save() 메소드의 update 및 insert 분기를 이용
간단히 설명하면 DB에 PK가 이미 있으면 Update를 하고 없으면 Insert를 함 이를 이용해서 이전에 추천/비추천 요청을 보냈는지 구분해서 동작 자세한건 비즈니스 로직 구현에서 설명

- ArticleLikeHate의 convertEntity() 의 Id
이 때문에 converEntity에서 id 값도 받음 없으면 null 받을 수 있도록 Wrapper 클래스 이용


4. 추천/비추천 개수 받기
프론트 구현
ArticleDetail의 추천/비추천 주석 부분
- 사칙 연산을 위해 문자열과 분리해서 <label> 내부 text를 구성(문자열이 있으면 사칙연산이 안됨)
ArticleDetail 의 Script에서 innerText를 이용한 부분
- 단순하게 좋아요 싫어요 여부에 따라 값 증감
- 실제 서버의 값을 불러오는게 아닌 사용자의 추천/비추천이 눌린 것을 보여주기 위한 증감임
실제 서버에서 값을 불러오는 건 새로 고침했을 때임


서버 구현
ArticleService likeHate() 메소드 부분
- 요청에서 전달받은 articleId를 이용해서 likeNum과 hateNum을 받음
- 이 또한 단순하게 분기된 네 가지 요청에 맞게 좋아요 싫어요 수를 연산하는 것임

출력 구현
View 로 전달하는 부분도 이미 다 구현돼있어서 딱히 크게 변경할 것 없음
- 서버에선 Dto에 필드만 추가해서 값 넣어주면 됨
- 프론트에서도 전달받은 Model의 key를 이용해서 값 출력하면 됨



5. 로그인 계정만 사용할 수 있음
필요 조건
- 로그인이 돼 있지 않으면 로그인 창으로 가야 하고 로그인 이후엔 기존 게시글로 돌아와야 함
- 로그인 돼 있지 않으면 어떤 체크박스도 체크 돼 있으면 안 됨

구현(개선 필요 반드시)
ArticleDetail의 needLoginLikeHate() 함수와 NeedLoginController 참고
- 로그인 계정과 미 로그인 계정의 체크 박스를 아예 분리해서 다른 함수와 연결했음
- 미 로그인 계정의 댓글 작성과 같은 구조


문제점
ArticleService의 tmpFunc() 참고
- likeHate 값이 null인 경우 미 로그인 계정으로 판단하는데 로그인 계정이라도 추천/비추천 아무것도 체크되지 않은 계정도 null 값이 전달 됨 이를 방지하기 위해 서버에서 ArticleLikeHate 객체를 하나 생성하는데.. 이렇게 해도 되는지 모르겠음
- 또한 왜 위 같은 방식으로 구현하지 않으면 안 되지?
미인증 계정의 요청인데 SpringSecurity 가 왜 안 잡음?



6. 게시글 추천/비추천 한 계정 유지해야 함
참고
- 로그인 여부는 이미 처리된 상황
- ArticleController 
- ArticleService 의 tmpFunc()  
- ArticleDetail 의 th:checked

구현
ArticleController
- Authentication 객체가 있는지 확인
- 있으면 ArticleService의 tmpFunc()로 요청 계정의 List<>와 articleId를 보냄
- 없으면 null 이 View로 전달 됨

ArticleService의 tmpFunc()
- 전달받은 List<>에서 articleId가 동일한 값이 있는지 확인
- 있으면 그 ArticleLikeHate 객체 반환
- 없으면 초기화 된 빈 ArticleLikeHateDto 객체 반환
- 로그인 여부 확인과 마찬가지로 개선 필요, stream()으로 변환

ArticleDetail의 th:checked
- 이전에 설명했지만 서버에서 전달되는 likeHate 값은 프론트가 인지하는 값과 다름
프론트 
true - checked
false - unchecked

서버
true - 좋아요 checked
false - 싫어요 checkded
null - 둘 다 unchecked

- 때문에 이를 염두에 두고 구현해야 함
좋아요가 체크 - not null and true
싫어요가 체크 - not null and false


7. 비즈니스 로직 구현
이전까지 상황
- 요청이 4 가지로 분기 됨
- 필요한 값
memberId : 컨트롤러에서 Authentication 객체로 받아옴
articleId, likeHate : 프론트에서 요청으로 받음
likeNum, hateNum : articleId로 받아옴

- 미리 필요한 처리
Member 객체 : ArticleLikeHate.convertEntity() 에 필요함
saved : checkExist()로 DB에 있는 값인지 확인해서 있으면 반환하고 없으면 null

- 위에서 계속 설명했다시피 프론트에 true/false와 서버에 저장되는 true/false의 의미가 다름

구현
ArticleService 참고
onlyLike : 비추천이 안 눌린 상태에서 추천만 눌린 경우
- likeHate 가 true
1. 좋아요 갯수 증가 
2. likeHate true 그대로 저장

- likeHate 가 false
1. 좋아요 갯수 감소
2. likeHate 에 null 저장

onlyHate : 추천이 안 눌린 상태에서 비추천만 눌린 경우
- likeHate 가 true
1. 싫어요 갯수 증가 
2. likeHate false로 저장

- likeHate 가 false
1. 싫어요 갯수 감소
2. likeHate 에 null 저장

unLikeAndHate : 추천이 눌린 상태에서 비추천 눌린 경우
- likeHate가 항상 true만 옴
1. 좋아요 갯수 감소
2. 싫어요 갯수 증가
3. likeHate가 false로 저장

unHateAndLike : 비추천이 눌린 상태에서 추천 눌린 경우
- likeHate가 항상 true만 옴
1. 좋아요 갯수 증가
2. 싫어요 갯수 감소
3. likeHate가 true로 저장

(공통 처리) saved가 null 인지 확인 - checkSavedNull() 참고
- DB에 값이 있는지 확인임 이를 통해 한 계정에서의 요청은 하나로 처리
- saved null인 경우 : 들어온 input 그대로 저장
- saved not null 인 경우 : saved를 input 에 저장
- 이후 서버에 맞게 변환된 likeHate 저장

이후 DB에 저장 - ArticleLikeHate.convertEntity() 참고
- saved null 인 경우 : id 값이 null 이라 insert 쿼리 발생
- saved not null 인 경우 : id 값이 saved 의 id 가 들어오기 때문에 update 쿼리 발생
즉 하나의 계정 당 하나의 추천/비추천을 보장


=================================
새로 알게된 사실 스프링 관련

컨트롤러에서 Authentication 객체를 인자로 받으면 현재 요청을 보낸 주체에 접근할 수 있음.. 원리가 뭘까..?


왜 댓글 작성과 추천 비추천은 따로 로그인 유도되도록 컨트롤러를 따로 만들어야 하나..

DTO에서 다른 Entity에 직접 접근해도 되나?
값 SET만 안하며 되는 듯? Getter만 쓰면 상관없는거 같은데
