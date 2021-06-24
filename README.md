# 대선 온라인 투표 시스템

### Table of contents

- [서비스 시나리오](#서비스-시나리오)
  - [기능적 요구사항](#기능적-요구사항)
  - [비기능적 요구사항](#비기능적-요구사항)
- [분석/설계](#분석설계)
  - [AS-IS 조직 (Horizontally-Aligned)](#AS-IS-조직-(Horizontally-Aligned))
  - [TO-BE 조직 (Vertically-Aligned)](#TO-BE-조직-(Vertically-Aligned))
  - [Event 도출](#Event-도출)
  - [부적격 이벤트 제거](#부적격-이벤트-제거)
  - [액터, 커맨드 부착](#액터,-커맨드-부착)
  - [어그리게잇으로 묶기](#어그리게잇으로-묶기)
  - [바운디드 컨텍스트로 묶기](#바운디드-컨텍스트로-묶기)
  - [폴리시 부착/이동 및 컨텍스트 매핑](#폴리시-부착/이동-및-컨텍스트-매핑)
  - [Event Storming 최종 결과](#Event-Storming-최종-결과)
  - [기능 요구사항 Coverage](#기능-요구사항-Coverage)
  - [헥사고날 아키텍처 다이어그램 도출](#헥사고날-아키텍처-다이어그램-도출)
  - [System Architecture](#System-Architecture)
- [구현](#구현)
  - [DDD(Domain Driven Design)의 적용](#DDD(Domain-Driven-Design)의-적용)
  - [Gateway 적용](#Gateway-적용)
  - [CQRS](#CQRS)
  - [폴리글랏 퍼시스턴스](#폴리글랏-퍼시스턴스)
  - [동기식 호출과 Fallback 처리](#동기식-호출과-Fallback-처리)
- [운영](#운영)
  - [Deploy/ Pipeline](#Deploy/Pipeline)
  - [Config Map](#Config-Map)
  - [Persistence Volume](#Persistence-Volume)
  - [Autoscale (HPA)](#Autoscale-(HPA))
  - [Circuit Breaker](#Circuit-Breaker)
  - [Zero-Downtime deploy (Readiness Probe)](#Zero-Downtime-deploy-(Readiness-Probe))
  - [Self-healing (Liveness Probe)](#Self-healing-(Liveness-Probe))

# 서비스 시나리오

## 기능적 요구사항

* 선관위는 선거를 등록한다.
* 선관위는 후보를 등록할 선거의 위원을 선택한다.
* 선관위는 선택한 선거에 한 명의 대선 후보를 등록한다.
* 대선 후보는 공약을 추가한다.
* 대선 후보는 공약을 삭제한다.
* 투표자는 단 한 명의 후보에게 투표를 할 수 있다.
* 관리자는 후보 별 투표 현황을 확인 할 수 있다.

## 비기능적 요구사항

* 트랜잭션   
    * 투표자 투표 기간내에만 투표가 가능하다.(Sync)
* 장애격리
    * 투표는 24시간 받을 수 있어야 한다. Async (event-driven), Eventual Consistency
    * 투표시스템이 과중 되면, 잠시동안 투표 되지 않고 잠시 후에 하도록 유도한다. Circuit breaker, fallback
* 성능
    * 관리자는 후보 별 투표 현황을 확인 할 수 있다.(CQRS)
    * 투표자는 자신이 투표한 후보를 확인 할 수 있다. (CQRS)

# 분석/설계

## AS-IS 조직 (Horizontally-Aligned)
![Horizontally-Aligned](https://user-images.githubusercontent.com/2360083/123191340-17ac6600-d4dc-11eb-8927-c935cb6d6389.png)

## TO-BE 조직 (Vertically-Aligned)
![Vertically-Aligned](https://user-images.githubusercontent.com/2360083/123191346-19762980-d4dc-11eb-9654-ebf03896a6ea.png)

## Event 도출
![이벤트_도출](https://user-images.githubusercontent.com/2360083/123191357-1aa75680-d4dc-11eb-95a2-bcfbc07f6849.png)

## 부적격 이벤트 제거
![부적격_이벤트_제거](https://user-images.githubusercontent.com/2360083/123191352-1a0ec000-d4dc-11eb-96e6-784a45d6e6e5.png)

```
- 이벤트를 식별하여 타임라인으로 배치하고 중복되거나 잘못된 도메인 이벤트들을 걸러내는 작업을 수행함
- 현업이 사용하는 용어를 그대로 사용(Ubiquitous Language) 
```

## 액터, 커맨드 부착
![액터_커맨드_부착](https://user-images.githubusercontent.com/2360083/123191354-1a0ec000-d4dc-11eb-84e6-82e676c682c4.png)
```
- Event를 발생시키는 Command와 Command를 발생시키는주체, 담당자 또는 시스템을 식별함 
- Command : 선거/후보/공약(생성/추가/삭제), 투표기간 확인, 투표
- Actor : 선거관리자, 후보자, 투표자
```

## 어그리게잇으로 묶기
![어그리게잇으로_묶기](https://user-images.githubusercontent.com/2360083/123191356-1aa75680-d4dc-11eb-97fb-8cae5d756bc0.png)
```
- 연관있는 도메인 이벤트들을 Aggregate 로 묶었음 
- Aggregate : 선거정보, 후보정보, 공약정보, 투표
```
## 바운디드 컨텍스트로 묶기
![바운디드컨텍스트로_묶기](https://user-images.githubusercontent.com/2360083/123191349-19762980-d4dc-11eb-8b5d-08f45b7a0340.png)

## 폴리시 부착/이동 및 컨텍스트 매핑
![컨텍스트_매핑](https://user-images.githubusercontent.com/2360083/123191358-1b3fed00-d4dc-11eb-9f09-be43428685e6.png)
```
- Policy의 이동과 컨텍스트 매핑 (점선은 Pub/Sub, 실선은 Req/Res)
```

## Event Storming 최종 결과
![MSA](https://user-images.githubusercontent.com/2360083/123191344-18dd9300-d4dc-11eb-8fc2-25bde41b513b.png)

## 헥사고날 아키텍처 다이어그램 도출
![헥사고날](https://user-images.githubusercontent.com/2360083/123192196-627aad80-d4dd-11eb-8f3f-94407ba3e6a6.png)

## System Architecture
![시스템_구성](https://user-images.githubusercontent.com/2360083/123192203-673f6180-d4dd-11eb-82bf-7894a0e801cb.png)


# 구현
분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라,구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다
(각자의 포트넘버는 8081 ~ 8084, 8088 이다)
```shell
cd election
mvn spring-boot:run

cd candidate
mvn spring-boot:run 

cd campaign
mvn spring-boot:run 

cd vote 
mvn spring-boot:run

cd gateway
mvn spring-boot:run 
```

## DDD(Domain-Driven-Design)의 적용
msaez.io 를 통해 구현한 Aggregate 단위로 Entity 를 선언 후, 구현을 진행하였다.
Entity Pattern 과 Repository Pattern을 적용하기 위해 Spring Data REST 의 RestRepository 를 적용하였다.

candidate 서비스의 PolicyHandler.java

```Java
@Service
public class PolicyHandler{
    @Autowired CandidateRepository candidateRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCampaignCreated_CampaignCounting(@Payload CampaignCreated campaignCreated){

        if(!campaignCreated.validate()) return;

        System.out.println("\n\n##### listener CampaignCounting : " + campaignCreated.toJson() + "\n\n");

        Optional<Candidate> candidate = candidateRepository.findById(campaignCreated.getCanditateId());
        if(candidate.isPresent()){
            Candidate candidateValue = candidate.get();
            candidateValue.increaseCampaigns();
            candidateRepository.save(candidateValue);
        }
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCampaignDeleted_CampaignCounting(@Payload CampaignDeleted campaignDeleted){

        if(!campaignDeleted.validate()) return;

        System.out.println("\n\n##### listener CampaignCounting : " + campaignDeleted.toJson() + "\n\n");

        Optional<Candidate> candidate = candidateRepository.findById(campaignDeleted.getCanditateId());
        if(candidate.isPresent()){
            Candidate candidateValue = candidate.get();
            candidateValue.decreaseCampaigns();
            candidateRepository.save(candidateValue);
        }
    }
  /** 생략 **/

}

```

campaign 서비스의 PolicyHandler.java

```java
@Service
public class PolicyHandler{
    @Autowired CampaignRepository campaignRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCandidateDeleted_DeleteCanditate(@Payload CandidateDeleted candidateDeleted){

        if(!candidateDeleted.validate()) return;

        System.out.println("\n\n##### listener DeleteCanditate : " + candidateDeleted.toJson() + "\n\n");

        List<Campaign> campaigns = campaignRepository.findByCanditateId(candidateDeleted.getId());
        campaigns.forEach(campaign -> {
            campaignRepository.delete(campaign);
        });
            
    }
    /** 생략 **/
}

```

DDD 적용 후 REST API의 테스트를 통하여 정상적으로 동작하는 것을 확인할 수 있었다.
## Gateway 적용
API GateWay를 통하여 마이크로 서비스들의 진입점을 통일할 수 있다. 
다음과 같이 GateWay를 적용하였다.

```yml
server:
  port: 8088

---

spring:
  profiles: default
  cloud:
    gateway:
      routes:
        - id: election
          uri: http://localhost:8081
          predicates:
            - Path=/elections/** 
        - id: candidate
          uri: http://localhost:8082
          predicates:
            - Path=/candidates/** 
        - id: campaign
          uri: http://localhost:8083
          predicates:
            - Path=/campaigns/** 
        - id: vote
          uri: http://localhost:8084
          predicates:
            - Path=/votes/** 
        - id: dashboard
          uri: http://localhost:8085
          predicates:
            - Path= /myVotes/**,/candidateVotes/**
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true


---

spring:
  profiles: docker
  cloud:
    gateway:
      routes:
        - id: election
          uri: http://election:8080
          predicates:
            - Path=/elections/** 
        - id: candidate
          uri: http://candidate:8080
          predicates:
            - Path=/candidates/** 
        - id: campaign
          uri: http://campaign:8080
          predicates:
            - Path=/campaigns/** 
        - id: vote
          uri: http://vote:8080
          predicates:
            - Path=/votes/** 
        - id: dashboard
          uri: http://dashboard:8080
          predicates:
            - Path= /myVotes/**,/candidateVotes/**
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true

server:
  port: 8080
```
## CQRS
타 마이크로서비스의 데이터 원본에 접근없이(Composite 서비스나 조인SQL 등 없이) 도 내 서비스의 화면 구성과 잦은 조회가 가능하게 구현해 두었다.
본 프로젝트에서 View 역할은 dashboard 서비스가 수행한다.

후보자 생성 후 dashboard/candidateVotes
 
![DASHBOARD-CQRS](https://user-images.githubusercontent.com/2360083/123205990-cc06b600-d4f5-11eb-9567-f404ed5a43e8.png)

## 폴리글랏 퍼시스턴스
dashboard 서비스의 DB와 Election/Candidate/Campaign/Vote 서비스의 DB를 다른 DB를 사용하여 MSA간 서로 다른 종류의 DB간에도 문제 없이 동작하여 다형성을 만족하는지 확인하였다.
(폴리글랏을 만족)

|서비스|DB|pom.xml|
| :--: | :--: | :--: |
|election| H2 |![image](https://user-images.githubusercontent.com/2360083/121104579-4f10e680-c83d-11eb-8cf3-002c3d7ff8dc.png)|
|candidate| H2 |![image](https://user-images.githubusercontent.com/2360083/121104579-4f10e680-c83d-11eb-8cf3-002c3d7ff8dc.png)|
|campaign| H2 |![image](https://user-images.githubusercontent.com/2360083/121104579-4f10e680-c83d-11eb-8cf3-002c3d7ff8dc.png)|
|vote| H2 |![image](https://user-images.githubusercontent.com/2360083/121104579-4f10e680-c83d-11eb-8cf3-002c3d7ff8dc.png)|
|dashboard| HSQL |![image](https://user-images.githubusercontent.com/2360083/120982836-1842be00-c7b4-11eb-91de-ab01170133fd.png)|


## 동기식 호출과 Fallback 처리
분석단계에서의 조건 중 하나로 투표자 투표 기간내에만 투표가 가능하며,
투표(vote) -> 선거(election) 간의 호출은 동기식 일관성을 유지하는 트랜잭션으로 처리한다.
호출 프로토콜은 Controller 에 의해 노출되어있는 REST 서비스를 FeignClient 를 이용하여 호출하도록 한다.
election 서비스의 ElectionController.java
```java
 @RestController
 public class ElectionController {

    @Autowired
    ElectionRepository electionRepository;

    @RequestMapping(value = "elections/canVote",
        method = RequestMethod.GET,
        produces = "application/json;charset=UTF-8")
    public boolean checkAndBookStock(HttpServletRequest request, HttpServletResponse response) {{
        System.out.println("##### /elections/canVote  called #####");

        Long electionId = Long.valueOf(request.getParameter("electionId"));
        Optional<Election> election = electionRepository.findById(electionId);
        if(election.isPresent()){
            Election electionValue = election.get();
            LocalDate now = LocalDate.now();
            LocalDate startDate = electionValue.getVotingDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = startDate.plusDays(electionValue.getVotingPeriod());
            if(startDate.isAfter(now) && endDate.isBefore(now)) return true;
            return false;
        }else{
            return false;
        }
    }

 }

vote 서비스의 ElectionService.java
```java
@FeignClient(name="election", url="http://${api.url.election}:8080")
public interface ElectionService {

    @RequestMapping(method= RequestMethod.GET, path="/elections/canVote")
    public boolean canVote(@RequestParam Long electionId);

}
```

vote 서비스의 Vote.java

```java
@Entity
@Table(name="Vote")
public class Vote {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long electionId;
    private Long candidateId;
    private String voterId;
    private Date votingDate;

    @PostPersist
    public void onPostPersist() throws Exception {

        // 선거 가능 기간 여부 확인
        if(VoteApplication.applicationContext.getBean(omp.external.ElectionService.class)
            .canVote(electionId)){
                Voted voted = new Voted();
                BeanUtils.copyProperties(this, voted);
                voted.publishAfterCommit();
            }else{
                throw new Exception("Not Voting Day.");
            }


    }
    /** 생략 **/
}

```