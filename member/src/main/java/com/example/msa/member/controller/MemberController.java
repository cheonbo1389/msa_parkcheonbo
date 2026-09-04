package com.example.msa.member.controller;


import com.example.msa.member.domain.Member;
import com.example.msa.member.dto.LoginDto;
import com.example.msa.member.dto.MemberRefreshDto;
import com.example.msa.member.dto.MemberSaveReqDto;
import com.example.msa.member.service.JwtTokenProvider;
import com.example.msa.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

//    @Qualifier?
//    같은 타입의 빈(Bean)이 여러 개 있을 때,
//    스프링이 어떤 빈을 주입해야 할지 명확히 선택하도록 이름을 지정
    @Qualifier("rtdb")
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider, @Qualifier("rtdb") RedisTemplate<String, Object> redisTemplate) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody MemberSaveReqDto memberSaveReqDto){
        System.out.println("<<< MemberController - /create >>>");

        Long memberId = memberService.save(memberSaveReqDto);
        return new ResponseEntity<>(memberId, HttpStatus.CREATED);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody LoginDto dto){
        System.out.println("<<< MemberController - /doLogin >>>");

        //        email,password 검증
        Member member = memberService.login(dto);

//        토큰 생성 및 return
        String token = jwtTokenProvider.createToken(member.getId().toString(), member.getRole().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail(), member.getRole().toString());

//        redis에 rt 저장
        redisTemplate.opsForValue().set(member.getEmail(), refreshToken, 200, TimeUnit.DAYS);

//        사용자에게 at, rt 지급
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", token);
        loginInfo.put("refreshToken", refreshToken);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> generateNewAt(@RequestBody MemberRefreshDto dto){
        System.out.println("<<< MemberController - /refresh-token >>>");

        //        rt 디코딩 후 email 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyRt)
                .build()
                .parseClaimsJws(dto.getRefreshToken())
                .getBody();

//        rt를 redis의 rt 비교 검증
        Object rt = redisTemplate.opsForValue().get(claims.getSubject());
        if (rt == null || !rt.toString().equals(dto.getRefreshToken())){
            return new ResponseEntity<>((Object) null, HttpStatus.BAD_REQUEST);
        }

//        at 생성하여 지급
        String token = jwtTokenProvider.createToken(claims.getSubject(), claims.get("role").toString());
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("token",token);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }
}
