package SpringJWT.study.service;

import SpringJWT.study.dto.LoginRequestDto;

public interface AuthService {
    String login(LoginRequestDto request);
}
