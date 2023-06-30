# flask와 spring 통신1

관련 파일 
flask/app.py
src/main/java/tomato/classifier/service/MemberService.java
src/main/java/tomato/classifier/repository/main/*
src/main/java/tomato/classifier/controller/MemberController.java
src/main/resources/templates/main/*

데이터 흐름
1. 프론트에서 js의 fetch()를 이용해서 이미지를 flask로 직접 전송
2. flask에서 이미지로 결과 낸 후 name과 prob 를 json으로 만듦
3. 만든 결과물 body에 실어서 스프링에 요청(requests.post())
4. 스프링에선 요청 추가 필요한 처리 후 flask로 반환
5. flask 에선 프론트로 name과 prob으로 만든 json 반환
6. 프론트에선 fetch의 .then()으로 응답이 오면 /main/result 페이지로 이동
7. 결과물 출력

4번의 스프링에서 처리 상세
1. flask에서 json형태의 결과물을 body에 실어서 스프링의 /main/predict로 요청을 보냄 
2. 스프링에선 그 json을 파싱해서 ResultData(dto)에 저장하고 그 dto를 선언돼있는 static 객체에 저장
3. /main/result로 redirect 
4. 그곳에선 static 객체의 name 값을 이용해서 DB에서 질병에 대한 정보 조회하고 질병에 대한 전체 정보를 diseaseDto에 저장
5. diseaseDto를 model에 실어서 view에 전달하고 결과창 출력


위 상황에서 문제점
1. flask 에서 보낸 requests.post()의 응답을 사용하지 않음
requests.post()는 redirect가 아님 그냥 post 요청을 보내는 메소드임 그래서 현재 flask 코드는 요청A를 처리하는 와중에 A 안에서 다른 요청 B를 보내는 방식임 요청 B도 그에 대한 응답이 있고 그 응답이 오기 전까지 요청 A의 다음 코드가 실행되지 않음 
즉 다른 갈래로 뻗어져 나가는 것도 아니고 비동기 처리와도 연관이 없음 
때문에 스프링의 처리가 프론트보다 늦어지는 것을 걱정하지 않아도 되고 스프링으로 뻗어져 간 다른 갈래의 행방을 걱정할 필요가 없음 스프링이 보내는 응답이 requests.post()의 반환값임 스프링에서 redirect 했음에도 페이지가 출력되지 않았던 이유도 그 응답을 flask가 받았기 때문

2. 1번과 연계되서 불필요한 데이터 흐름이 있음
requests.post()에서는 결과값만 저장하던가 아니면 모든 처리를 해주고 응답을 프론트에 응답을 보낼 때 같이 보내던가 해야 함 현재 상황은 requests.post()로 전부 다 처리하는데 이용하지 않고 프론트에서 또 처리하는 방식으로 결과를 내고 있음

3. 스프링의 처리가 불필요함
static으로 저장하지 말고 tomatoRepository 이것도 불필요하고 훨씬 더 나은 방법이 있을 듯

4. flask 에 오류가 생겨도 무조건 result page로 가게끔 되어있음 오류 여부에 따라 출력되는 페이지를 달리해야 함
