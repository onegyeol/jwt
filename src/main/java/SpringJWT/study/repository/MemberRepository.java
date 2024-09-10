package SpringJWT.study.repository;

import SpringJWT.study.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findMemberByEmail(String email);
}
