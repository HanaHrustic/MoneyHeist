package ag04.project.moneyheist.repositories;

import ag04.project.moneyheist.domain.MemberSkill;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MemberSkillRepository extends CrudRepository<MemberSkill, Long> {
    void deleteById(Long id);

    List<MemberSkill> findAll();
}
