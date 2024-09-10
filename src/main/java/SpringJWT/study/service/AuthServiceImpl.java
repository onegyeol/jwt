package SpringJWT.study.service;


import SpringJWT.study.dto.CustomUserInfoDto;
import SpringJWT.study.dto.LoginRequestDto;
import SpringJWT.study.entity.Member;
import SpringJWT.study.jwt.JwtUtil;
import SpringJWT.study.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public String login(LoginRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        Member member = memberRepository.findMemberByEmail(email);

        if(member==null){
            throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
        }

        // 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 exception
        if(!encoder.matches(password, member.getPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // member -> CustomerUserInfoDto 변환
        CustomUserInfoDto info = modelMapper.map(member, CustomUserInfoDto.class);

        String accessToken = jwtUtil.createToken(info);
        return accessToken;
    }
}
