package ag04.project.moneyheist.repositories;

import ag04.project.moneyheist.domain.Skill;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends CrudRepository<Skill, Long> {
    Optional<Skill> findByName(String name);

    List<Skill> findAllByNameIn(List<String> names);
}
