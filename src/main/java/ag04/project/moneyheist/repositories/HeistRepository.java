package ag04.project.moneyheist.repositories;

import ag04.project.moneyheist.domain.Heist;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface HeistRepository extends CrudRepository<Heist, Long> {
    boolean existsByName(String name);

    Optional<Heist> findById(Long id);
}
