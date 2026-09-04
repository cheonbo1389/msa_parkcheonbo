package com.example.msa.common.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// 동기 요청을 위해 RestTemplate을 사용
// OrderingService에서 RestTemplate을 주입받아서 사용
// LoadBalance(부하 분산) : 서버에 가해지는 트래픽이나 작업요청을 여러대의 서버로 나누어 처리하는 기술
// 로그밸런서 : 클라이언트와 서버 그룹 사이의 요청을 여러 서버로 분배하는 장치나 소프트웨어
// => eureka에 등록된 서비스명을 사용해서 내부서비스 호출(내부통신)
// => 즉, OrderingService에서 restTemplate을 주입
// => eureka에 등록된 서비스명 "http://product-service/" 으로 요청 => application.yaml에 등록
// => eureka에서 이 서비스가 어디있는지 질의한다.
@Configuration
public class RestTemplateConfig {

    @Bean  //@Bean 객체를 만들면 싱글톤으로 객체가 만들어진다.
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
