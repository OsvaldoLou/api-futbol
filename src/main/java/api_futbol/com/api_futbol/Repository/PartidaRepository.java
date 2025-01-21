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

    @Query("SELECT COUNT(p) FROM Partida p WHERE p.golsMandante > p.golsVisitante")
    Long countVictories();

    @Query("SELECT COUNT(p) FROM Partida p WHERE p.golsMandante = p.golsVisitante")
    Long countDraws();

    @Query("SELECT COUNT(p) FROM Partida p WHERE p.golsMandante < p.golsVisitante")
    Long countDefeats();

    @Query("SELECT SUM(p.golsMandante) FROM Partida p")
    Long sumGoalsScored();

    @Query("SELECT SUM(p.golsVisitante) FROM Partida p")
    Long sumGoalsConceded();

    @Query("SELECT p.clubeVisitante.nome AS adversario, " +
       "SUM(CASE WHEN p.resultado = 'V' THEN 1 ELSE 0 END) AS victories, " +
       "SUM(CASE WHEN p.resultado = 'E' THEN 1 ELSE 0 END) AS draws, " +
       "SUM(CASE WHEN p.resultado = 'D' THEN 1 ELSE 0 END) AS defeats, " +
       "SUM(p.golsClubeMandante) AS goalsScored, " +
       "SUM(p.golsClubeVisitante) AS goalsConceded " +
       "FROM Partida p " +
       "WHERE p.clubeMandante.id = :clubeId " +
       "GROUP BY p.clubeVisitante.nome")
List<Object[]> findRetrospectoPorClube(@Param("clubeId") Long clubeId);
}
