package api_futbol.com.api_futbol.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import api_futbol.com.api_futbol.models.Clube;

@Repository
public interface ClubeRepository extends JpaRepository<Clube, Long> {
    @Query("SELECT e FROM Clube e where e.estado=true ORDER BY e.id LIMIT :size OFFSET :start")
    List<Clube> findAllWithPagination(@Param("start") int start, @Param("size") int size);
}
