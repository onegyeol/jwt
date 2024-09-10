package SpringJWT.study.dto;

import SpringJWT.study.entity.RoleType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CustomUserInfoDto {
    private Long memberId;
    private String email;
    private String name;
    private String password;
    private RoleType role;
}
