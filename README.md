# 👯‍♀️ ward-study-reservation
스터디모임 룸 예약 서비스 
![ward-study_image](https://user-images.githubusercontent.com/62453668/169026026-6278161e-781f-4f9c-86fd-1d7a0e8c821f.jpg)

언제 어디서든 소규모로 사용자들이 스터디모임을 활성화하기 위한 서비스로 도안하였습니다.
스터디를 형성하는 데 많은 갈증을 느끼는 사용자가 하나의 토픽을 중심으로 룸을 예약하여  “몰입의 경험”을 선사하는 것이 이 서비스의 취지입니다.

<br>

## 🔍 서비스 `MVP(Minimum Valuable Product)`

**1. 사용자(리더)가 StudyGroup 등록/참여**

**2. 사용자(리더)가 Reservation 등록**

**3. 사용자가 참가한 StudyGroup의 Reservation 조회**

**4. 해당 Room의 Reservation 조회**

**5. Reservation의 해당 사용자들에게 Notification 이메일 정기알람**

<br>

## 💡 프로젝트 목표
**1. 객체 지향 원리를 적용하여 `CleanCode`를 목표로 유지보수가 용이한 코드 구현**
- 중복되는 코드들, 불필요하게 수정이 일어날 코드들을 최소화해 확장이 용이하게 노력합니다.
- SOLID 원칙과 디자인패턴의 이해를 바탕을 하여 최대한 `도메인 주도 설계`를 하기 위해 노력합니다.

**2. 단순 기능 구현만이 아닌 대용량 트래픽을 고려한 `scale-out`에 고려한 서버 구조 설계**
- 서비스가 성장하여 대량의 TPS 수치의 접속자수가 있다고 가정, 그 정도의 트래픽에도 견딜 수 있는 `분산 데이터베이스 아키텍쳐`를 설계하기 위해 노력했습니다.  

**3. `문서화`를 통한 협업**
- 프론트엔드-백엔드 팀들이 협업하는 환경에서 API 문서를 통한 커퓨니케이션 호율성을 높이기 위해 노력합니다.
- API 문서를 함께 작성하는 것은 비효율적이기 때문에 `Swagger`와 같은 툴을 활용하여 문서화와 테스트도 쉽게 처리할 수 있게 합니다.

**4. `테스트코드`를 통해 코드품질 향상, `CI/CD`를 통한 자동화 구현**
- 다수의 개발자가 하나의 서비스를 개발해나가는 환경에서는 각자의 코드가 쉽게 배포할 수 있도록 Jenkins를 활용해 배포 자동화하는 설계에도 많은 리소스를 소요하였습니다.  
- CI/CD를 직접 구축하여 애자일한 개발 프로세스를 실현하기 위해 노력합니다.

**5. `성능 모니터링`으로 프로젝트의 신뢰성을 높임**
- `nGrinder`나 `JMeter` 같은 툴을 사용해 TPS 같은 성능지수를 체크, 트래픽이 많이 발생하는 부분을 모니터링하여 개선점을 찾아냅니다.
- 그외 로그수집을 하여 개발 오류 파트를 빠르게 찾기 위해 `ELK` 시스템을 구축하여, 애플리케이션 오류 진단, 인프라 모니터링에 최적화시키기 위해 노력합니다.  

<br>

## 🛒 사용 기술 스택
- Java11
- SpringBoot2.6
- Gradle7.4
- MySQL8.0 / Redis
- JPA / QueryDsl
- JUnit5
- Spring Batch
- Docker 
- Jenkins
- JMeter 

<br>

## 🔗 CI/CD 구조도
![image](https://user-images.githubusercontent.com/62453668/164407464-9df1d184-da84-4e4f-b533-2aad2a5b3757.png)

<br>

## 🎡 서버 구조도
![image](https://user-images.githubusercontent.com/62453668/204127443-2711dec2-7fd4-495d-9649-1d48fad6bc2b.png)

<br>

## 👁‍🗨 이슈 정리
[Wiki Issues & Trouble shooting](https://github.com/f-lab-edu/ward-study-reservation/wiki/4.-Issues-&-Trouble-shooting)에서 확인할 수 있습니다!

- [예약 시스템에서 동시성 제어와 더블부킹(중복요청)은 어떻게 막을까?](https://velog.io/@mooh2jj/예약-시스템에서-동시성-제어와-더블부킹중복요청은-어떻게-막을까)
- [배치 시스템 Cursor 기반 vs Paging 기반 ItemReader 정하기](https://velog.io/@mooh2jj/Cursor-기반-vs-Paging-기반-ItemReader-정하기)
- [Redis 캐싱적용해서 read 작업 성능 개선하기](https://velog.io/@mooh2jj/Redis-캐싱적용해서-read-작업-성능-개선하기)
- [서버 분산 처리 환경에서 데이터의 불일치 문제- Redis에 session 저장하기](https://velog.io/@mooh2jj/서버-분산-처리-환경에서-데이터의-불일치-Redis에-session-저장하기)
- [Spring ArgumentResolver 로 인증처리하기](https://velog.io/@mooh2jj/Spring-ArgumentResolver-로-인증처리하기)

등등 ...

<br>

## 🔖 Git-Flow 브랜치 및 PR 전략 

![image](https://user-images.githubusercontent.com/62453668/169458249-74e55a36-a631-4440-a52d-332fa78eb9f4.png)




✅ `master` : 제품으로 출시될 수 있는 브랜치를 의미합니다. 

✅ `develop` : 다음 출시 버전을 개발하는 브랜치입니다. feature에서 리뷰완료한 브랜치를 Merge하고 있습니다.

✅ `feature` : 기능을 개발하는 브랜치 

✅ `release` : 이번 출시 버전을 준비하는 브랜치

✅ `hotfix` : 출시 버전에서 발생한 버그를 수정하는 브랜치

<br>

- `master`를 항상 최신 상태로 만들며, stable 상태로 Product에 배포되는 브랜치 (master = main) 로 삼습니다.
- 신규개발 건은 `develop` 을 base로 `feature/#이슈번호` or `feature/작업명` 의 브랜치명으로 생성 후 작업한 다음 `PR`을 날립니다.
- 아직 개발 진행 중이라면 `In Progress` 라벨을 달고, 코드리뷰가 필요한 경우 `Asking for Review` 라벨을 답니다. 리뷰 후 리팩토링이 필요하다면 추가로 `refactoring` 라벨을 달아 진행합니다.
- 모든 `PR`은 반드시 지정한 `리뷰어`에게 코드리뷰를 받아야만 합니다.
- 코드리뷰어의` Approve` 를 받아야 `Merge pull request` 를 할 수 있습니다.

<br>

✳ 참고문헌 : 
우아한 형제들 기술블로그 [우린 Git-flow를 사용하고 있어요](https://woowabros.github.io/experience/2017/10/30/baemin-mobile-git-branch-strategy.html)



<br>

## 🎞 ER 다이어그램
![image](https://user-images.githubusercontent.com/62453668/204138573-da57c4a1-c66f-4826-bc01-a39610c6c2da.png)

<br>

## 🎨 클라이언트 화면
![image](https://user-images.githubusercontent.com/62453668/204130495-55216181-3106-4f9f-9af1-65c4a7ba8403.png)



