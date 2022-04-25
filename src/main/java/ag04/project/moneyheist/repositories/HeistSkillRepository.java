package ag04.project.moneyheist.repositories;

import ag04.project.moneyheist.domain.HeistSkill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HeistSkillRepository extends CrudRepository<HeistSkill, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM HEIST_SKILL WHERE ID IN (SELECT HEIST_SKILL.ID FROM HEIST_SKILL JOIN HEIST ON HEIST.ID = HEIST_SKILL.HEIST_ID WHERE HEIST.ID = :heistId)")
    List<HeistSkill> findSkillsByHeistId(Long heistId);
}
