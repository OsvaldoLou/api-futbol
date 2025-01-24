package api_futbol.com.api_futbol.Repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import api_futbol.com.api_futbol.models.Partida;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
        @Query("SELECT e FROM Partida e WHERE e.estado = true ORDER BY e.id")
        Page<Partida> findAllWithPagination(Pageable pageable);

        @Query("SELECT COUNT(p) FROM Partida p " +
                        "WHERE (p.clubeMandante.id = :idClube AND p.golsMandante > p.golsVisitante) " +
                        "OR (p.clubeVisitante.id = :idClube AND p.golsVisitante > p.golsMandante)")
        Long countVictoriesByClub(@Param("idClube") Long idClube);

        @Query("SELECT COUNT(p) FROM Partida p " +
                        "WHERE (p.clubeMandante.id = :idClube OR p.clubeVisitante.id = :idClube) " +
                        "AND p.golsMandante = p.golsVisitante")
        Long countDrawsByClub(@Param("idClube") Long idClube);

        @Query("SELECT COUNT(p) FROM Partida p " +
                        "WHERE (p.clubeMandante.id = :idClube AND p.golsMandante < p.golsVisitante) " +
                        "OR (p.clubeVisitante.id = :idClube AND p.golsVisitante < p.golsMandante)")
        Long countDefeatsByClub(@Param("idClube") Long idClube);

        @Query("SELECT SUM(CASE WHEN p.clubeMandante.id = :idClube THEN p.golsMandante ELSE p.golsVisitante END) " +
                        "FROM Partida p WHERE p.clubeMandante.id = :idClube OR p.clubeVisitante.id = :idClube")
        Long sumGoalsScoredByClub(@Param("idClube") Long idClube);

        @Query("SELECT p FROM Partida p WHERE p.clubeMandante.id = :clubeId OR p.clubeVisitante.id = :clubeId")
        List<Partida> findByClubeId(Long clubeId);

        @Query("SELECT p FROM Partida p WHERE (p.clubeMandante.id = :clube1Id AND p.clubeVisitante.id = :clube2Id) OR (p.clubeMandante.id = :clube2Id AND p.clubeVisitante.id = :clube1Id)")
        List<Partida> findByClubes(Long clube1Id, Long clube2Id);

}
