package api_futbol.com.api_futbol.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import api_futbol.com.api_futbol.Controller.PartidaController.DirectConfrontationRetrospect;
import api_futbol.com.api_futbol.models.Partida;



@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    @Query("SELECT e FROM Partida e WHERE e.estado = true ORDER BY e.id")
    Page<Partida> findAllWithPagination(Pageable pageable);

   @Query("SELECT COUNT(p) FROM Partida p WHERE p.clube.id = :idClube AND p.resultado = 'VICTORY'")
    Long countVictoriesByClub(@Param("idClube") Long idClube);

    @Query("SELECT COUNT(p) FROM Partida p WHERE p.clube.id = :idClube AND p.resultado = 'DRAW'")
    Long countDrawsByClub(@Param("idClube") Long idClube);

    @Query("SELECT COUNT(p) FROM Partida p WHERE p.clube.id = :idClube AND p.resultado = 'DEFEAT'")
    Long countDefeatsByClub(@Param("idClube") Long idClube);

    @Query("SELECT SUM(p.golsMarcados) FROM Partida p WHERE p.clube.id = :idClube")
    Long sumGoalsScoredByClub(@Param("idClube") Long idClube);

    @Query("SELECT SUM(p.golsSofridos) FROM Partida p WHERE p.clube.id = :idClube")
    Long sumGoalsConcededByClub(@Param("idClube") Long idClube);

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
    @Query("SELECT p FROM Partida p " +
    "WHERE (p.clube.id = :idClube1 AND p.clubeAdversario.id = :idClube2) " +
    "   OR (p.clube.id = :idClube2 AND p.clubeAdversario.id = :idClube1)")
    List<Partida> findMatchesBetweenClubs(@Param("idClube1") Long idClube1, @Param("idClube2") Long idClube2);

    @Query("SELECT new com.example.dto.DirectConfrontationRetrospect( " +
       "    SUM(CASE WHEN p.clube.id = :idClube1 AND p.resultado = 'VICTORY' THEN 1 ELSE 0 END), " +
       "    SUM(CASE WHEN p.clube.id = :idClube2 AND p.resultado = 'VICTORY' THEN 1 ELSE 0 END), " +
       "    SUM(CASE WHEN p.resultado = 'DRAW' THEN 1 ELSE 0 END), " +
       "    SUM(CASE WHEN p.clube.id = :idClube1 THEN p.golsMarcados ELSE 0 END), " +
       "    SUM(CASE WHEN p.clube.id = :idClube2 THEN p.golsMarcados ELSE 0 END) " +
       ") " +
       "FROM Partida p " +
       "WHERE (p.clube.id = :idClube1 AND p.clubeAdversario.id = :idClube2) " +
       "   OR (p.clube.id = :idClube2 AND p.clubeAdversario.id = :idClube1)")
DirectConfrontationRetrospect getDirectConfrontationRetrospect(
        @Param("idClube1") Long idClube1, 
        @Param("idClube2") Long idClube2);
  
   
}
