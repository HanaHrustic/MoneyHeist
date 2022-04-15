package ag04.project.moneyheist.repositories;

import ag04.project.moneyheist.domain.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends CrudRepository<Member, Long> {
    boolean existsByEmail(String email);

    Optional<Member> findById(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM MEMBER WHERE ID IN (SELECT MEMBER_ID FROM MEMBER_SKILL JOIN SKILL ON SKILL.ID = MEMBER_SKILL.SKILL_ID WHERE SKILL.NAME IN :skills)")
    List<Member> findByMemberBySkillNameIn(@Param("skills") List<String> skills);
}
