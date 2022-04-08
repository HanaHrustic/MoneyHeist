package ag04.project.moneyheist.repositories;

import ag04.project.moneyheist.domain.Heist;
import org.springframework.data.repository.CrudRepository;

public interface HeistRepository extends CrudRepository<Heist, Long> {
    boolean existsByName(String name);
}
