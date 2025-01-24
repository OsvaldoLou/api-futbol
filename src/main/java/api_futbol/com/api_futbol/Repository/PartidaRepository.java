package api_futbol.com.api_futbol.Repository;

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

        /*@Query("SELECT DISTINCT p.clubeVisitante.id FROM Partida p WHERE p.clube.id = :idClube")
        List<Long> findAdversariosByClubId(@Param("idClube") Long idClube);

       
        @Query("SELECT COUNT(p) FROM Partida p WHERE p.clube.id = :idClube AND p.clubeVisitante.id = :idAdversario AND p.resultado = 'VITÓRIA'")
        Long countVictoriesAgainstAdversary(@Param("idClube") Long idClube, @Param("idAdversario") Long idAdversario);

        
        @Query("SELECT COUNT(p) FROM Partida p WHERE p.clube.id = :idClube AND p.clubeVisitante.id = :idAdversario AND p.resultado = 'EMPATE'")
        Long countDrawsAgainstAdversary(@Param("idClube") Long idClube, @Param("idAdversario") Long idAdversario);

        
        @Query("SELECT COUNT(p) FROM Partida p WHERE p.clube.id = :idClube AND p.clubeVisitante.id = :idAdversario AND p.resultado = 'DERROTA'")
        Long countDefeatsAgainstAdversary(@Param("idClube") Long idClube, @Param("idAdversario") Long idAdversario);

        
        @Query("SELECT SUM(p.golsMarcados) FROM Partida p WHERE p.clube.id = :idClube AND p.clubeVisitante.id = :idAdversario")
        Long sumGoalsScoredAgainstAdversary(@Param("idClube") Long idClube, @Param("idAdversario") Long idAdversario);

       
        @Query("SELECT SUM(p.golsSofridos) FROM Partida p WHERE p.clube.id = :idClube AND p.clubeVisitante.id = :idAdversario")
        Long sumGoalsConcededAgainstAdversary(@Param("idClube") Long idClube, @Param("idAdversario") Long idAdversario);*/
        
        
}
