package api_futbol.com.api_futbol.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.sql.Timestamp;
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
        createPartida.setDataPartida(partida.dataPartida);
        createPartida.setGolsMandante(partida.golsMandante);
        createPartida.setGolsVisitante(partida.golsVisitante);
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            Long victories = partidaRepository.countVictoriesByClub(idClube);
            Long draws = partidaRepository.countDrawsByClub(idClube);
            Long defeats = partidaRepository.countDefeatsByClub(idClube);
            Long goalsScored = partidaRepository.sumGoalsScoredByClub(idClube);
            Long goalsConceded = partidaRepository.sumGoalsScoredByClub(idClube);

            Summary summary = new Summary(victories, draws, defeats, goalsScored, goalsConceded);

            return ResponseEntity.ok(summary);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    public static class Summary {
        public Long victories;
        public Long draws;
        public Long defeats;
        public Long goalsScored;
        public Long goalsConceded;

        public Summary(Long victories, Long draws, Long defeats, Long goalsScored, Long goalsConceded) {
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

    /*@GetMapping("/retrospectoAdv/{idClube}")
    public ResponseEntity<List<RetrospectoContraAdversarios>> getRetrospectoContraAdversarios(@PathVariable Long idClube) {
        try {
            
            if (!clubeRepository.existsById(idClube)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
    
            
            List<Long> adversariosIds = partidaRepository.findAdversariosByClubId(idClube);
    
            
            if (adversariosIds.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>()); 
            }
    
            
            List<Clube> adversarios = clubeRepository.findAllByIds(adversariosIds);
            if (adversarios == null || adversarios.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>()); 
            }
    
            
            Map<Long, String> adversariosMap = adversarios.stream()
                .collect(Collectors.toMap(Clube::getId, Clube::getNome));
    
            
            List<RetrospectoContraAdversarios> retrospectos = new ArrayList<>();
    
            
            for (Long adversarioId : adversariosIds) {
                String adversarioNome = adversariosMap.get(adversarioId);
    
                Long totalVictories = partidaRepository.countVictoriesAgainstAdversary(idClube, adversarioId);
                Long totalDraws = partidaRepository.countDrawsAgainstAdversary(idClube, adversarioId);
                Long totalDefeats = partidaRepository.countDefeatsAgainstAdversary(idClube, adversarioId);
                Long goalsScored = partidaRepository.sumGoalsScoredAgainstAdversary(idClube, adversarioId);
                Long goalsConceded = partidaRepository.sumGoalsConcededAgainstAdversary(idClube, adversarioId);
    
                
                retrospectos.add(new RetrospectoContraAdversarios(adversarioNome, totalVictories, totalDraws, totalDefeats, goalsScored, goalsConceded));
            }
    
            
            return ResponseEntity.ok(retrospectos);
    
        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public static class RetrospectoContraAdversarios {
        public String adversarioNome;
        public Long totalVictories;
        public Long totalDraws;
        public Long totalDefeats;
        public Long goalsScored;
        public Long goalsConceded;

        public RetrospectoContraAdversarios(String adversarioNome, Long totalVictories, Long totalDraws,
                Long totalDefeats, Long goalsScored, Long goalsConceded) {
            this.adversarioNome = adversarioNome;
            this.totalVictories = totalVictories;
            this.totalDraws = totalDraws;
            this.totalDefeats = totalDefeats;
            this.goalsScored = goalsScored;
            this.goalsConceded = goalsConceded;
        }

        public String getAdversarioNome() {
            return adversarioNome;
        }

        public void setAdversarioNome(String adversarioNome) {
            this.adversarioNome = adversarioNome;
        }

        public Long getTotalVictories() {
            return totalVictories;
        }

        public void setTotalVictories(Long totalVictories) {
            this.totalVictories = totalVictories;
        }

        public Long getTotalDraws() {
            return totalDraws;
        }

        public void setTotalDraws(Long totalDraws) {
            this.totalDraws = totalDraws;
        }

        public Long getTotalDefeats() {
            return totalDefeats;
        }

        public void setTotalDefeats(Long totalDefeats) {
            this.totalDefeats = totalDefeats;
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

    }*/

}
