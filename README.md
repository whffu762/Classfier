## 토마토 질병 분류 웹 서버
### 개요
토마토 주요병해충의 이미지를 학습시킨 AI를 이용한 웹 서버로 이미지를 업로드하면 해당 이미지에
해당하는 병해충과 그 확률, 해결책을 결과로 출력함 병해충에 대한 전문 지식이 없어도 인터넷만 가능하다면
간편하게 병해를 판단하고 후속 조치를 취할 수 있도록 함 그 외 추가적으로 게시판 기능도 제공


### 구성
- 웹 서버 : Spring Boot, Mysql
- AI 서버 : Flask
- AI : DenseNet, pytorch
- 프론트 : HTML, JS, Thymeleaf


### 요구 사항
- Python 3.9.11
- Java 11 
- 그 외 라이브러리
  - 파이썬 : flask/needLib.txt 참고
  - Spring boot : classifier/build.gradle 참고




### UML
#### 순차 다이어그램
<img src="https://github.com/whffu762/Tomato-disease-classifier/assets/117614180/5c3c0b8a-5cdf-4d2a-80a8-2bc82d87443e" width="600" height="450">

#### ERD
<img src="https://github.com/whffu762/Tomato-disease-classifier/assets/117614180/ea33265d-3d34-42fd-afad-57b47ee3ae1a" width="600">

#### API 명세
<img src="https://github.com/whffu762/Tomato-disease-classifier/assets/117614180/92cbfff5-83f3-4201-8a9a-4c248e91fae8" width="600">
<br>
<img src="https://github.com/whffu762/Tomato-disease-classifier/assets/117614180/afad7481-d817-445d-94f1-b446c41d6bf6" width="600">
<br>
<img src="https://github.com/whffu762/Tomato-disease-classifier/assets/117614180/d67f5137-119a-4138-8e96-cf8882b5c5cc" width="600">
"
