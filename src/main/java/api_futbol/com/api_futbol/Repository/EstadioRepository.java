package api_futbol.com.api_futbol.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import api_futbol.com.api_futbol.models.Estadio;

@Repository
public interface EstadioRepository extends JpaRepository<Estadio, Long> {
    @Query("SELECT e FROM Estadio e where e.estado=true ORDER BY e.id LIMIT :size OFFSET :start")
    List<Estadio> findAllWithPagination(@Param("start") int start, @Param("size") int size);

}