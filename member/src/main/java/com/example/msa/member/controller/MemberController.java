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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Qualifier("rtdb")
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider, @Qualifier("rtdb") RedisTemplate<String, Object> redisTemplate) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    //회원가입
    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody MemberSaveReqDto memberSaveReqDto){
        System.out.println("<<< MemberController - /create >>>");

        Long memberId = memberService.save(memberSaveReqDto);
        return new ResponseEntity<>(memberId, HttpStatus.CREATED);
    }

    //로그인
    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody LoginDto dto){
        System.out.println("<<< MemberController - /doLogin >>>");

        Member member = memberService.login(dto);

        String token = jwtTokenProvider.createToken(member.getId().toString(), member.getRole().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail(), member.getRole().toString());

        redisTemplate.opsForValue().set(member.getEmail(), refreshToken, 200, TimeUnit.DAYS);

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", token);
        loginInfo.put("refreshToken", refreshToken);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }


    //마이페이지
    @GetMapping("/mypage")
    public ResponseEntity<?> mypage_myinfo(@RequestHeader("X-User-Id") String userId){
        System.out.println("<<< MemberController - /mypage_myinfo >>>");

        return new ResponseEntity<>(memberService.myinfo(userId), HttpStatus.OK);
    }

    //유저 정보 수정
    @PutMapping("/updatemyinfo")
    public ResponseEntity<?> updateMyinfo(@RequestBody Member member) {
        System.out.println("<<< MemberController - /updateMyinfo >>>");

        return new ResponseEntity<>(memberService.updatemyinfo(member), HttpStatus.OK);
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody MemberRefreshDto token) {
        System.out.println("<<< MemberController - /logout >>>");


        // rt 디코딩 후 email 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyRt)
                .build()
                .parseClaimsJws(token.getRefreshToken())
                .getBody();


        String email = claims.getSubject();

        //rt를 redis의 rt 비교 검증
        Object rt = redisTemplate.opsForValue().get(claims.getSubject());
        if (rt == null || !rt.toString().equals(token.getRefreshToken())){
            return new ResponseEntity<>((Object) null, HttpStatus.BAD_REQUEST);
        }

        // Redis에서 Refresh Token 삭제
        redisTemplate.delete(email);

        return new ResponseEntity<>(HttpStatus.OK);
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
