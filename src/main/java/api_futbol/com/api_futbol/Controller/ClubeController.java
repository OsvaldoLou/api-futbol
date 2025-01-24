package api_futbol.com.api_futbol.Controller;

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
import api_futbol.com.api_futbol.Service.ClubeService;
import api_futbol.com.api_futbol.models.Clube;

@RestController
@RequestMapping("/clube")
public class ClubeController {
    @Autowired
    ClubeRepository clubeRepository;
    @Autowired
    ClubeService clubeService;

    @GetMapping("/all")
    public List<Clube> getAllClubes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (clubeRepository.count() == 0)
            return List.of();

        int start = page * size;

        return clubeRepository.findAllWithPagination(start, size);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewClube(@RequestBody Clube clube) {
        Clube createClube = clubeService.salvarClube(clube);
        return ResponseEntity.status(HttpStatus.CREATED).body(createClube);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateClube(@RequestBody Clube clube) {

        Optional<Clube> clubOptional = clubeRepository.findById(clube.getId());
        Clube updateClube = clubeService.editarClube(clube);

        return ResponseEntity.ok(updateClube);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClube(@PathVariable Long id) {
        Optional<Clube> clube = clubeRepository.findById(id);
        if (!clube.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Clube não existe");
        }
        Clube estadoEliminar = clube.get();
        estadoEliminar.setEstado(false);
        clubeRepository.save(estadoEliminar);
        return ResponseEntity.ok(clube.get());
    }

}
