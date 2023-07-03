<<<<<<< HEAD
# Classifier
=======
# flask와 통신 2

개요
방식 : flask의 응답과 함께 결과출력 URL을 js에 넘기는 방식
flask의 요청의 응답으로 받은 스프링의 처리 결과를 redirect URL과 함께 JSON 형식으로 받아서 프론트로 넘긴 후 프론트에선 그 JSON을 이용해서 결과를 출력하는 방식

가능한 방법이지만 결과 출력에 제약 사항이 있음
- 제약사항
  1. fetch()는 응답으로 뷰 렌더링을 하지 못함 직접 해줘야해서 .then()에서 직접 만들어줘야 함
  2. locatoin.href를 이용해선 get 요청밖에 보내지 못하고 body에 데이터를 싣지 못하고 요청 파라미터를 통해 데이터를 보내야 함
  3. locstion.href를 이용할 때 ajax를 이용하면 좀 더 편해진다는데..

위 제약사항 중 2번 방식을 이용해 구현했음

변경사항
- 불필요한 TomatoRepository : 기존엔 static 객체에 결과를 저장해서 결과를 출력할 때 받아왔음

- V1 보다 정갈해진 흐름
  1. JS에서 flask로 이미지 전송 
  2. flask에서 스프링을 통해 DB에 접근해서 결과 질병 전체 정보(diseaseInfo) 조회
  3. 스프링에선 조회 후 만들어진 질병 정보를 flask로 반환
  4. flask는 diseaseInfo와 결과를 출력할 URL과 함께 JSON으로 만들어 JS로 응답
  5. JS에선 받은 diseaseInfo와 URL로 요청을 만들어서 스프링으로 페이지 요청
  6. 스프링에선 요청 파라미터 받아서 결과물 출력

해결하면서 걸림돌
  - flask의 requests.post()는 Response()객체를 응답으로 받음

  - test 에서 할 땐 flask에서 받은 json을 이용해서 요청 URL을 만들 때 result를 JSON.parse()를 이용해야 했는데 실제 main에서 할 땐 이용하면 아래 오류가 발생
오류 : Uncaught (in promise) SyntaxError: "[object Object]" is not valid JSON at JSON.parse (<anonymous>)

: 위 오류의 의미는 JSON.parse()의 파라미터로 object 자료형이 들어갈 수 없다는 것을 의미함 JSON.parse()는 string을 Object로 바꿔주는 메소드로 string만 받을 수 있음

  - 그렇다면 flask로부터 응답을 받을 때 string으로 받는 경우와 json으로 받는 경우가 나뉘는 이유는? 
: flask에서 requests.post() 의 응답 Response() 객체를 이용해서 json을 만들 때 사용하는 속성에 따라 달라짐
response.text : 받은 응답의 body 내용을 string으로 반환 - js가 받을 때 string으로 받음
response.json() : 받은 응답의 body 내용을 json으로 반환 - js가 받을 때 object로 받음

flask에서 json으로 반환하면 JSON.parse() 없이 바로 Object.entries()를 적용하고
text로 반환하면 JSON.parse()를 통해 Object로 바꾼 후 Object.entries()를 적용해야 함


  - flask로부터 받은 json에 result에 해당하는 key가 없음에도 아래 방식으로 정상적으로 작동됨..
const result = JSON.parse(json_data.result)
: 그냥 착각이었다 실험해보니까 모든 경우에서 undefined 로 나옴

  - @RequestParam 으로 여러개 요청 파라미터 한꺼번에 받는 방법
@RequestParam Map<String, Object> params
이후 ObjectMapper의 .converValue()를 이용해서 DTO로 변환할 수 있음


   - then(resp => {}) 에서 resp가 뭐임
fetch()의 응답은 응답에 대한 정보를 가지고 있는 Promise 객체임 then()에 들어가는 파라미터는 그 객체를 가리킴

  - then()에 return이 상황마다 있거나 없는 이유
중괄호({..}) 유무에 따라 달라짐
then(resp => resp.json()) - 자동으로 return 함
then(resp => {return resp.json()) - return 이 안되기 때문에 return을 직접 해줘야 함


  - Promise 객체를 다루는 방법이 어떻게 되나
첫번째 then()에선 response.json() 을 return 
두번째 then()에서 return 된 json을 이용해서 이후 처리

  - 그리하는 이유
fetch 에서 첫번째 .then()의 Promise 객체는 pending 상태임 그래서 직접적인 처리는 두번째 then()을 이용해야 함 fullfilled 상태의 응답을 이용해야 함
사실 첫번째 then이 return 하는 값도 Promise 객체임 접근할 수 있는 방법이 생기는 것임

  - response.json() 이후 json에 접근하는 방법
then(data => { result = data.result_key }
: 위처럼 json 에 접근해서 값을 변수에 저장할 수 있음
result_key 는 응답 body에 있는 json의 key임


  - fetch() 방식과 html 태그(button, form) 방식의 요청 전달 차이
fetch() 방식 : fetch(url, {header=...}).then(resp => {location.href = "/auth/register"})
html 태그(button, form) 방식 : 두 가지 방법이 존재함
1. button type = button th:onclick="|location.href='@{/auth/register}'|"
2. button type = submit / form method='post' th:action="@{/auth/register}" button이 form 태그 안에 있어야 함
타임리프 안쓰면 @{} 이런거 빠지고 url 만 문자열로 넘김


todo
- 실제 redirect()를 이용하는게 아닌 출력할 결과와 redirect URL을 json으로 전달하는 방식임
- DB 조회를 스프링을 통해서 할게 아니라 flask에서 직접하게끔
- 지금 데이터 흐름도 부자연스러움 (js - flask - spring - js - spring)
- 출력할 데이터가 요청 파라미터를 통해 전달됨
>>>>>>> fbf704387ef36c13eaade9e742c00edc1bc55146
