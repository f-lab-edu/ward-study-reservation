# 👯‍♀️ ward-study
스터디모임 룸 예약 서비스 - **Ward Study Reservation**
![ward-study_image](https://user-images.githubusercontent.com/62453668/169026026-6278161e-781f-4f9c-86fd-1d7a0e8c821f.jpg)

언제 어디서든 소규모로 사용자들이 스터디모임을 활성화하기 위한 서비스로 도안하였습니다.
스터디를 형성하는 데 많은 갈증을 느끼는 사용자가 하나의 연결 토픽을 중심으로 룸을 예약하여  “몰입의 경험”을 선사하는 것이 이 앱의 취지입니다.

<br>

## 🔍 서비스 기능

[Wiki UseCase](https://github.com/f-lab-edu/ward-study-reservation/wiki/3.-UseCase-:-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84-%EC%82%AC%EC%A0%84-%EC%84%A4%EA%B3%84)에서 확인할 수 있습니다!

<br>

## 💡 프로젝트 목표
**1. 객체 지향 원리를 적용하여 `CleanCode`를 목표로 유지보수가 용이한 코드 구현**
- 중복되는 코드들, 불필요하게 수정이 일어날 코드들은 최소화해 확장이 용이하게 노력합니다.
- SOLID 원칙과 디자인패턴의 이해를 바탕으로 하여 최대한 객체지향의 장점을 활용합니다.

**2. 단순 기능 구현만이 아닌 대용량 트래픽을 고려한 `scale-out`에 고려한 서버 구조 설계**
- 서비스가 성장하여 1000QPS 수치의 접속자수가 있다고 가정, 그 정도의 트래픽에도 견딜 수 있는 시스템 아키텍쳐를 설계하기 노력합니다.  

**3. `문서화`를 통한 협업**
- 프론트엔드-백엔드 팀들이 협업하는 환경에서 API 문서를 통한 커퓨니케이션 호율성을 높이기 위해 노력합니다.
- API 문서를 함께 작성하는 것은 비효율적이기 때문에 Spring RestDocs와 같은 툴을 활용하여 문서 작업을 자동화합니다.

**4. `테스트코드`를 통해 코드품질 향상, `CI/CD`를 통한 자동화 구현**
- 다수의 개발자가 하나의 서비스를 개발해나가는 환경에서는 각자의 코드를 머지하고 충돌을 해결하고 테스트하고 빌드, 배포하는 과정에도 많은 리소스가 소요됩니다. 이러한 문제를 해결하기 위한 방법으로 CI/CD를 직접 구축하여 애자일한 개발 프로세스를 실현하기 위해 노력합니다.

**5. `성능 테스트`로 프로젝트의 신뢰성을 높임**
- 성능 테스트를 통해 병목 지점을 개선하고 컴퓨팅 자원을 더 효율적을 활용할 수 있는 방안들을 고민하여 성능을 향상시키기 위해 노력합니다.
- APM과 nGrinder 같은 툴을 이용해 높은 트래픽을 발생시키고 성능을 모니터링하여 개선점을 찾아냅니다.

<br>

## 🛒 사용 기술 스택
- Java11
- SpringBoot
- Gradle
- Mysql8.0 / Redis
- JPA
- JUnit5
- Spring Batch
- Docker 
- Jenkins
- nGrinder 

<br>

## 🔗 CI/CD 구조도
![image](https://user-images.githubusercontent.com/62453668/164407464-9df1d184-da84-4e4f-b533-2aad2a5b3757.png)


## 🎡 서버 구조도
- 업데이트예정
![image](https://user-images.githubusercontent.com/62453668/164407568-b361e935-1e87-4a0f-8023-a9a5508cd118.png)

<br>

## ❗ 이슈 정리
[Wiki Issues & Trouble shooting](https://github.com/f-lab-edu/ward-study-reservation/wiki/4.-Issues-&-Trouble-shooting)에서 확인할 수 있습니다!

<br>

## 🎞 ER 다이어그램
- 업데이트예정
![image](https://user-images.githubusercontent.com/62453668/169023418-d4bafc12-1cf2-4b77-a975-8fe9367f333c.png)

<br>

## 🎨 클라이언트 화면

![image](https://user-images.githubusercontent.com/62453668/164407764-93ce620d-0823-4e64-bd41-2acd6ee01458.png)



