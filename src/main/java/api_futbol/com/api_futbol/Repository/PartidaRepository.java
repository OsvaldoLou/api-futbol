package api_futbol.com.api_futbol.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import api_futbol.com.api_futbol.models.Partida;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    @Query("SELECT e FROM Partida e WHERE e.estado = true ORDER BY e.id")
    Page<Partida> findAllWithPagination(Pageable pageable);
}
