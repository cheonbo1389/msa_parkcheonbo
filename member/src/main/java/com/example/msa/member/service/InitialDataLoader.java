package com.example.msa.member.service;


import com.example.msa.member.domain.Member;
import com.example.msa.member.domain.Role;
import com.example.msa.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//CommandLineRunner를 상속함으로서 해당 컴포넌트가 스프링빈으로 등록되는 시점에서 run 메서드 자동 실행
@Component
public class InitialDataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataLoader(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        //이미 테스트용 관리자 계정이 존재하면 종료
        if(memberRepository.findByEmail("admin@naver.com").isPresent())
            return;

        //테스트용 관리자 계정 생성
        Member member = Member.builder()
                .name("admin")
                .email("admin@naver.com")
                .password(passwordEncoder.encode("admin1234"))
                .role(Role.ADMIN)
                .build();

        memberRepository.save(member);
    }
}
