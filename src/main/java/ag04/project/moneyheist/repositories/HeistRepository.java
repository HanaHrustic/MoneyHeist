package ag04.project.moneyheist.repositories;

import ag04.project.moneyheist.domain.Heist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HeistRepository extends CrudRepository<Heist, Long> {
    boolean existsByName(String name);

    Optional<Heist> findById(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM Heist JOIN Member_Heist ON Heist.Id = Member_Heist.Heist_Id WHERE Member_Heist.Member_Id = :memberId")
    List<Heist> findAllByMemberId(@Param("memberId") Long memberId);

    @Query(nativeQuery = true, value = "SELECT * FROM Heist")
    List<Heist> findAllHeists();
}
