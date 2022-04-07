package ag04.project.moneyheist.repositories;

import ag04.project.moneyheist.domain.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface MemberRepository extends CrudRepository<Member, Long> {
    boolean existsByEmail(String email);

    Optional<Member> findById(Long id);
}
