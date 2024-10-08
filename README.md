## Project Architecture
<img src="./image/spring_plus.png" width = 120%>

- 연결된 도메인으로 접속 시 사용자는 회원가입을 진행하여 일정 생성 및 일정 담당자를 등록할 수 있습니다. 
<br>

---

## Optimizing Large-Scale Data Retrieval with Caching

- 1,000,000건의 데이터에서 조회 성능을 향상시키기 위해 캐싱 도입
- 효율성과 신뢰성이 높은 Caffeine 캐싱 라이브러리를 사용
- Cache 사용 전 **623ms** 였던 조회 시간은 **31ms** 로 향상되었습니다. 
<img src="./image/output.png">

<br>

---
## Health Check API
- 엔드포인트: songdo-spring.site/actuator/health
- 해당 API 를 통해 서버 상태 및 DB 상태를 알 수 있음
