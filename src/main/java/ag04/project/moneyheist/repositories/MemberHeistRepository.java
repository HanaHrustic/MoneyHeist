package ag04.project.moneyheist.repositories;

import ag04.project.moneyheist.domain.MemberHeist;
import org.springframework.data.repository.CrudRepository;

public interface MemberHeistRepository extends CrudRepository<MemberHeist, Long> {
}
