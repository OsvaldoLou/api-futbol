package api_futbol.com.api_futbol.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RestController;

import api_futbol.com.api_futbol.Repository.ClubeRepository;
import api_futbol.com.api_futbol.Repository.EstadioRepository;
import api_futbol.com.api_futbol.Repository.PartidaRepository;
import api_futbol.com.api_futbol.models.Clube;
import api_futbol.com.api_futbol.models.Estadio;
import api_futbol.com.api_futbol.models.Partida;
import api_futbol.com.api_futbol.Dto.DtoInputModel;

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
    public ResponseEntity<?> createPartida(@RequestBody DtoInputModel partida) {
        Optional<Clube> clubeMandanteOpt = clubeRepository.findById(partida.getClubeMandanteId());
        Optional<Clube> clubeVisitanteOpt = clubeRepository.findById(partida.getClubeVisitante());
        Optional<Estadio> estadioOpt = estadioRepository.findById(partida.getEstadioId());

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
        createPartida.setDataPartida(partida.getDataPartida());
        createPartida.setGolsMandante(partida.getGolsMandante());
        createPartida.setGolsVisitante(partida.getGolsVisitante());
        partidaRepository.save(createPartida);

        return ResponseEntity.status(HttpStatus.CREATED).body(createPartida);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePartida(@RequestBody DtoInputModel partida, @PathVariable Long id) {
        Optional<Partida> existingPartidaOpt = partidaRepository.findById(id);
        if (!existingPartidaOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partida inexistente");
        }

        Optional<Clube> clubeMandanteOpt = clubeRepository.findById(partida.getClubeMandanteId());
        Optional<Clube> clubeVisitanteOpt = clubeRepository.findById(partida.getClubeVisitante());
        Optional<Estadio> estadioOpt = estadioRepository.findById(partida.getEstadioId());

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
        updatePartida.setGolsMandante(partida.getGolsMandante());
        updatePartida.setGolsVisitante(partida.getGolsVisitante());
        updatePartida.setDataPartida(partida.getDataPartida());

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

    /*public static class PartidaInputModel {
        public Long clubeMandanteId;
        public Long clubeVisitanteId;
        public Long estadioId;
        public LocalDateTime dataPartida;
        public Integer golsMandante;
        public Integer golsVisitante;

    }*/

    @GetMapping("/retospetoGeral/{idClube}")
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

    @GetMapping("/retrospectoAdversario/{clubeId}")
    public ResponseEntity<?> getRetrospecto(@PathVariable Long clubeId) {
        Optional<Clube> clubeOpt = clubeRepository.findById(clubeId);
        if (!clubeOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Clube não encontrado");
        }

        List<Partida> partidas = partidaRepository.findByClubeId(clubeId);
        if (partidas.isEmpty()) {
            return ResponseEntity.ok().body(partidas);
        }

        List<Retrospecto> retrospectos = partidas.stream().collect(Collectors.groupingBy(p -> {
            if (p.getClubeMandante().getId() == clubeId) {
                return p.getClubeVisitante();
            } else {
                return p.getClubeMandante();
            }
        })).entrySet().stream().map(entry -> {
            Clube adversario = entry.getKey();
            List<Partida> partidasContraAdversario = entry.getValue();

            long victories = partidasContraAdversario.stream()
                    .filter(p -> (p.getClubeMandante().getId() == clubeId && p.getGolsMandante() > p.getGolsVisitante())
                            ||
                            (p.getClubeVisitante().getId() == clubeId && p.getGolsVisitante() > p.getGolsMandante()))
                    .count();

            long draws = partidasContraAdversario.stream().filter(p -> p.getGolsMandante().equals(p.getGolsVisitante()))
                    .count();

            long defeats = partidasContraAdversario.stream()
                    .filter(p -> (p.getClubeMandante().getId() == clubeId && p.getGolsMandante() < p.getGolsVisitante())
                            ||
                            (p.getClubeVisitante().getId() == clubeId && p.getGolsVisitante() < p.getGolsMandante()))
                    .count();

            long goalsScored = partidasContraAdversario.stream()
                    .mapToLong(
                            p -> p.getClubeMandante().getId() == clubeId ? p.getGolsMandante() : p.getGolsVisitante())
                    .sum();

            long goalsConceded = partidasContraAdversario.stream()
                    .mapToLong(
                            p -> p.getClubeMandante().getId() == clubeId ? p.getGolsVisitante() : p.getGolsMandante())
                    .sum();

            return new Retrospecto(adversario, victories, draws, defeats, goalsScored, goalsConceded);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(retrospectos);
    }

    @GetMapping("/confrontos/{clube1Id}/{clube2Id}")
    public ResponseEntity<?> getConfrontos(@PathVariable Long clube1Id, @PathVariable Long clube2Id) {
        Optional<Clube> clube1Opt = clubeRepository.findById(clube1Id);
        Optional<Clube> clube2Opt = clubeRepository.findById(clube2Id);

        if (!clube1Opt.isPresent() || !clube2Opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Um dos clubes não foi encontrado");
        }

        List<Partida> partidas = partidaRepository.findByClubes(clube1Id, clube2Id);
        if (partidas.isEmpty()) {
            RetrospectoConfronto retrospecto = new RetrospectoConfronto(
                    clube1Opt.get(),
                    clube2Opt.get(),
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L);

            return ResponseEntity.ok().body(new Confronto(partidas, retrospecto));
        }

        long victoriesClube1 = partidas.stream()
                .filter(p -> (p.getClubeMandante().getId() == clube1Id && p.getGolsMandante() > p.getGolsVisitante()) ||
                        (p.getClubeVisitante().getId() == clube1Id && p.getGolsVisitante() > p.getGolsMandante()))
                .count();

        long victoriesClube2 = partidas.stream()
                .filter(p -> (p.getClubeMandante().getId() == clube2Id && p.getGolsMandante() > p.getGolsVisitante()) ||
                        (p.getClubeVisitante().getId() == clube2Id && p.getGolsVisitante() > p.getGolsMandante()))
                .count();

        long draws = partidas.stream().filter(p -> p.getGolsMandante().equals(p.getGolsVisitante())).count();

        long goalsScoredClube1 = partidas.stream()
                .mapToLong(p -> p.getClubeMandante().getId() == clube1Id ? p.getGolsMandante() : p.getGolsVisitante())
                .sum();

        long goalsConcededClube1 = partidas.stream()
                .mapToLong(p -> p.getClubeMandante().getId() == clube1Id ? p.getGolsVisitante() : p.getGolsMandante())
                .sum();

        long goalsScoredClube2 = partidas.stream()
                .mapToLong(p -> p.getClubeMandante().getId() == clube2Id ? p.getGolsMandante() : p.getGolsVisitante())
                .sum();

        long goalsConcededClube2 = partidas.stream()
                .mapToLong(p -> p.getClubeMandante().getId() == clube2Id ? p.getGolsVisitante() : p.getGolsMandante())
                .sum();

        RetrospectoConfronto retrospecto = new RetrospectoConfronto(clube1Opt.get(), clube2Opt.get(), victoriesClube1,
                draws, victoriesClube2, goalsScoredClube1, goalsConcededClube1, goalsScoredClube2, goalsConcededClube2);
        return ResponseEntity.ok().body(new Confronto(partidas, retrospecto));
    }

    public static class Confronto {
        public List<Partida> partidas;
        public RetrospectoConfronto retrospecto;

        public Confronto(List<Partida> partidas, RetrospectoConfronto retrospecto) {
            this.partidas = partidas;
            this.retrospecto = retrospecto;
        }
    }

    public static class RetrospectoConfronto {
        public Clube clube1;
        public Clube clube2;
        public Long victoriesClube1;
        public Long draws;
        public Long victoriesClube2;
        public Long goalsScoredClube1;
        public Long goalsConcededClube1;
        public Long goalsScoredClube2;
        public Long goalsConcededClube2;

        public RetrospectoConfronto(Clube clube1, Clube clube2, Long victoriesClube1, Long draws, Long victoriesClube2,
                Long goalsScoredClube1, Long goalsConcededClube1, Long goalsScoredClube2, Long goalsConcededClube2) {
            this.clube1 = clube1;
            this.clube2 = clube2;
            this.victoriesClube1 = victoriesClube1;
            this.draws = draws;
            this.victoriesClube2 = victoriesClube2;
            this.goalsScoredClube1 = goalsScoredClube1;
            this.goalsConcededClube1 = goalsConcededClube1;
            this.goalsScoredClube2 = goalsScoredClube2;
            this.goalsConcededClube2 = goalsConcededClube2;
        }
    }

    public static class Retrospecto {
        public Clube adversario;
        public Long victories;
        public Long draws;
        public Long defeats;
        public Long goalsScored;
        public Long goalsConceded;

        public Retrospecto(Clube adversario, Long victories, Long draws, Long defeats, Long goalsScored,
                Long goalsConceded) {
            this.adversario = adversario;
            this.victories = victories;
            this.draws = draws;
            this.defeats = defeats;
            this.goalsScored = goalsScored;
            this.goalsConceded = goalsConceded;
        }
    }

}
