package api_futbol.com.api_futbol.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api_futbol.com.api_futbol.Repository.ClubeRepository;
import api_futbol.com.api_futbol.Repository.EstadioRepository;
import api_futbol.com.api_futbol.Repository.PartidaRepository;
import api_futbol.com.api_futbol.models.Clube;
import api_futbol.com.api_futbol.models.Estadio;
import api_futbol.com.api_futbol.models.Partida;

@RestController
@RequestMapping("/partida")
public class PartidaController {

    @Autowired
    PartidaRepository partidaRepository;

    @Autowired
    ClubeRepository clubeRepository;

    @Autowired
    EstadioRepository estadioRepository;

    @GetMapping("/all")
    public List<Partida> getAllPartidas() {

        return partidaRepository.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPartida(@RequestBody PartidaInputModel partida) {
        Optional<Clube> clubeMandanteOpt = clubeRepository.findById(partida.clubeMandanteId);
        Optional<Clube> clubeVisitanteOpt = clubeRepository.findById(partida.clubeVisitanteId);
        Optional<Estadio> estadioOpt = estadioRepository.findById(partida.estadioId);

        if (!clubeMandanteOpt.isPresent() || !clubeVisitanteOpt.isPresent() || !estadioOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Impossível criar partida: clube ou estádio não encontrado");
        }

        Clube clubeMandante = clubeMandanteOpt.get();
        Clube clubeVisitante = clubeVisitanteOpt.get();
        Estadio estadio = estadioOpt.get();

        Partida createPartida = new Partida();
        createPartida.setClubeMandante(clubeMandante);
        createPartida.setClubeVisitante(clubeVisitante);
        createPartida.setEstadio(estadio);
        createPartida.setGolsMandante(partida.golsMandante);
        createPartida.setGolsVisitante(partida.golsVisitante);
        createPartida.setDataPartida(partida.dataPartida);
        partidaRepository.save(createPartida);

        return ResponseEntity.status(HttpStatus.CREATED).body(createPartida);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePartida(@RequestBody PartidaInputModel partida, @PathVariable Long id) {
        Optional<Partida> existingPartidaOpt = partidaRepository.findById(id);
        if (!existingPartidaOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partida inexistente");
        }

        Optional<Clube> clubeMandanteOpt = clubeRepository.findById(partida.clubeMandanteId);
        Optional<Clube> clubeVisitanteOpt = clubeRepository.findById(partida.clubeVisitanteId);
        Optional<Estadio> estadioOpt = estadioRepository.findById(partida.estadioId);

        if (!clubeMandanteOpt.isPresent() || !clubeVisitanteOpt.isPresent() || !estadioOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Impossível atualizar partida: clube ou estádio não encontrado");
        }

        Clube clubeMandante = clubeMandanteOpt.get();
        Clube clubeVisitante = clubeVisitanteOpt.get();
        Estadio estadio = estadioOpt.get();

        Partida updatePartida = existingPartidaOpt.get();
        updatePartida.setClubeMandante(clubeMandante);
        updatePartida.setClubeVisitante(clubeVisitante);
        updatePartida.setEstadio(estadio);
        updatePartida.setGolsMandante(partida.golsMandante);
        updatePartida.setGolsVisitante(partida.golsVisitante);
        updatePartida.setDataPartida(partida.dataPartida);

        partidaRepository.save(updatePartida);

        return ResponseEntity.ok(updatePartida);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePartida(@PathVariable Long id) {
        Optional<Partida> partidaOpt = partidaRepository.findById(id);
        if (!partidaOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partida não existe");
        }

        Partida partida = partidaOpt.get();
        partidaRepository.delete(partida);
        return ResponseEntity.ok(partida);
    }

    public static class PartidaInputModel {
        public Long clubeMandanteId;
        public Long clubeVisitanteId;
        public Long estadioId;
        public LocalDateTime dataPartida;
        public Integer golsMandante;
        public Integer golsVisitante;
        
    }
    @GetMapping("/summary/{idClube}")
    public ResponseEntity<Summary> getSummaryByClub(@PathVariable Long idClube) {
        try {
            
            if (!clubeRepository.existsById(idClube)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Long victories = partidaRepository.countVictoriesByClub(idClube);
            Long draws = partidaRepository.countDrawsByClub(idClube);
            Long defeats = partidaRepository.countDefeatsByClub(idClube);
            Long goalsScored = partidaRepository.sumGoalsScoredByClub(idClube);
            Long goalsConceded = partidaRepository.sumGoalsConcededByClub(idClube);

            Summary summary = new Summary(victories, draws, defeats, goalsScored, goalsConceded);
    
        
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null);
        }
    }
public class Summary {
    private Long victories;
    private Long draws;
    private Long defeats;
    private Long goalsScored;
    private Long goalsConceded;

    
    public Summary(Long victories, Long draws, Long defeats, Long goalsScored, Long goalsConceded) {
        this.victories = victories;
        this.draws = draws;
        this.defeats = defeats;
        this.goalsScored = goalsScored;
        this.goalsConceded = goalsConceded;
    }

    public Long getVictories() {
        return victories;
    }

    public void setVictories(Long victories) {
        this.victories = victories;
    }

    public Long getDraws() {
        return draws;
    }

    public void setDraws(Long draws) {
        this.draws = draws;
    }

    public Long getDefeats() {
        return defeats;
    }

    public void setDefeats(Long defeats) {
        this.defeats = defeats;
    }

    public Long getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(Long goalsScored) {
        this.goalsScored = goalsScored;
    }

    public Long getGoalsConceded() {
        return goalsConceded;
    }

    public void setGoalsConceded(Long goalsConceded) {
        this.goalsConceded = goalsConceded;
    }
}

@GetMapping("/retrospecto/{clubeId}")
public ResponseEntity<?> getRetrospecto(@PathVariable Long clubeId) {
    List<Object[]> resultados = partidaRepository.findRetrospectoPorClube(clubeId);

    if (resultados.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum retrospecto encontrado para o clube especificado.");
    }

    List<Retrospecto> retrospectos = new ArrayList<>();

    for (Object[] resultado : resultados) {
        Retrospecto retrospecto = new Retrospecto(
            (String) resultado[0],  
            (Long) resultado[1],   
            (Long) resultado[2],   
            (Long) resultado[3],   
            (Long) resultado[4],   
            (Long) resultado[5]    
        );
        retrospectos.add(retrospecto);
    }

    return ResponseEntity.ok(retrospectos);
}
public static class Retrospecto {
    public String adversario;
    public Long victories;
    public Long draws;
    public Long defeats;
    public Long goalsScored;
    public Long goalsConceded;

    public Retrospecto(String adversario, Long victories, Long draws, Long defeats, Long goalsScored, Long goalsConceded) {
        this.adversario = adversario;
        this.victories = victories;
        this.draws = draws;
        this.defeats = defeats;
        this.goalsScored = goalsScored;
        this.goalsConceded = goalsConceded;
    }
}
public PartidaRepository getPartidaRepository() {
    return partidaRepository;
}

public void setPartidaRepository(PartidaRepository partidaRepository) {
    this.partidaRepository = partidaRepository;
}

public ClubeRepository getClubeRepository() {
    return clubeRepository;
}

public void setClubeRepository(ClubeRepository clubeRepository) {
    this.clubeRepository = clubeRepository;
}

public EstadioRepository getEstadioRepository() {
    return estadioRepository;
}

public void setEstadioRepository(EstadioRepository estadioRepository) {
    this.estadioRepository = estadioRepository;
}
@GetMapping("/confrontos-diretos")
public ResponseEntity<DirectConfrontationResponse> getDirectConfrontations(
        @RequestParam Long idClube1, 
        @RequestParam Long idClube2) {
    try {
        
        if (!clubeRepository.existsById(idClube1) || !clubeRepository.existsById(idClube2)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

       
        List<Partida> partidas = partidaRepository.findMatchesBetweenClubs(idClube1, idClube2);

        
        DirectConfrontationRetrospect retrospect = partidaRepository.getDirectConfrontationRetrospect(idClube1, idClube2);

        
        DirectConfrontationResponse response = new DirectConfrontationResponse(partidas, retrospect);

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
public class DirectConfrontationResponse {
    private List<Partida> partidas; 
    private DirectConfrontationRetrospect retrospect; 

    
    public DirectConfrontationResponse(List<Partida> partidas, DirectConfrontationRetrospect retrospect) {
        this.partidas = partidas;
        this.retrospect = retrospect;
    }

    
    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }

    public DirectConfrontationRetrospect getRetrospect() {
        return retrospect;
    }

    public void setRetrospect(DirectConfrontationRetrospect retrospect) {
        this.retrospect = retrospect;
    }
}
public class DirectConfrontationRetrospect {
    private Long victoriesClub1;
    private Long victoriesClub2;
    private Long draws;
    private Long goalsClub1;
    private Long goalsClub2;

    
    public DirectConfrontationRetrospect(Long victoriesClub1, Long victoriesClub2, Long draws, Long goalsClub1, Long goalsClub2) {
        this.victoriesClub1 = victoriesClub1;
        this.victoriesClub2 = victoriesClub2;
        this.draws = draws;
        this.goalsClub1 = goalsClub1;
        this.goalsClub2 = goalsClub2;
    }

    
    public Long getVictoriesClub1() {
        return victoriesClub1;
    }

    public void setVictoriesClub1(Long victoriesClub1) {
        this.victoriesClub1 = victoriesClub1;
    }

    public Long getVictoriesClub2() {
        return victoriesClub2;
    }

    public void setVictoriesClub2(Long victoriesClub2) {
        this.victoriesClub2 = victoriesClub2;
    }

    public Long getDraws() {
        return draws;
    }

    public void setDraws(Long draws) {
        this.draws = draws;
    }

    public Long getGoalsClub1() {
        return goalsClub1;
    }

    public void setGoalsClub1(Long goalsClub1) {
        this.goalsClub1 = goalsClub1;
    }

    public Long getGoalsClub2() {
        return goalsClub2;
    }

    public void setGoalsClub2(Long goalsClub2) {
        this.goalsClub2 = goalsClub2;
    }
}


}
   


