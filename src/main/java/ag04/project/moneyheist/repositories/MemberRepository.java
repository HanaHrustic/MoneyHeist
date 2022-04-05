package ag04.project.moneyheist.repositories;

import ag04.project.moneyheist.domain.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {
}
