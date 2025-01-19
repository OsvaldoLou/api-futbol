package api_futbol.com.api_futbol.Controller;

import java.time.LocalDateTime;
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
}