package com.example.msa.member.service;

import com.example.msa.member.domain.Member;
import com.example.msa.member.dto.LoginDto;
import com.example.msa.member.dto.MemberSaveReqDto;
import com.example.msa.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //회원가입 시점 - save() 호출시
    public Long save(MemberSaveReqDto memberSaveReqDto){
        System.out.println("<<< MemberService - save >>>");
        Optional<Member> optionalMember = memberRepository.findByEmail(memberSaveReqDto.getEmail());
        if (optionalMember.isPresent()){
            throw new IllegalArgumentException("기존에 존재하는 회원입니다.");
        }

        //암호화
        String password = passwordEncoder.encode(memberSaveReqDto.getPassword());

        Member member = memberRepository.save(memberSaveReqDto.toEntity(password));

        return member.getId();

    }

    //로그인
    public Member login(LoginDto dto){
        System.out.println("<<< MemberService - login >>>");

        boolean check = true;

        //email 존재 여부
        Optional<Member> optionalMember = memberRepository.findByEmail(dto.getEmail());
        if(!optionalMember.isPresent()){
            check = false;
        }

        //password 일치 여부
        if (!passwordEncoder.matches(dto.getPassword(), optionalMember.get().getPassword())){
            check = false;
        }

        if(!check){
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }

        return optionalMember.get();
    }
}
