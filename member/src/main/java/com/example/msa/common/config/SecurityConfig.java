package com.example.msa.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class SecurityConfig {
    // member>service>MemberService - save : 회원가입시 PasswordEnvoder 주입받을때 여기서 가져와서  비밀번호 암호화함
    @Bean
    public PasswordEncoder makePassword(){

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}