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

import api_futbol.com.api_futbol.models.Clube;
import api_futbol.com.api_futbol.repositories.ClubeRepository;

@RestController
@RequestMapping("/clube")
public class ClubeController {
    @Autowired
    ClubeRepository clubeRepository;

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

        if (IsValid(clube)) 
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verifique os seus campos!"); 
         
        Clube createClube = new Clube();
        createClube.setNome(clube.getNome());
        createClube.setPais(clube.getPais());
        createClube.setCidade(clube.getCidade());
        createClube.setEstado(clube.getEstado());
        clubeRepository.save(createClube);

        return ResponseEntity.status(HttpStatus.CREATED).body(createClube);
    }

    private boolean IsValid(Clube clube){
        return (clube.getNome() ==  null || clube.getNome().length() < 2 ||
        clube.getPais() ==  null || clube.getPais().length() < 2  ||
        clube.getCidade() ==  null || clube.getCidade().length() < 2 
          );
    }
 
    @PutMapping("/update")
    public ResponseEntity<?> updateClube(@RequestBody Clube clube) {
        Optional<Clube> existingClube = clubeRepository.findById(clube.getId());
        if (!existingClube.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Clube não existe");
        }

        Clube updateClube = existingClube.get();
        updateClube.setNome(clube.getNome());
        updateClube.setPais(clube.getPais());
        updateClube.setCidade(clube.getCidade());
        updateClube.setEstado(clube.getEstado());
        clubeRepository.save(updateClube);

        return ResponseEntity.ok(updateClube);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClube(@PathVariable Long id) {
        Optional<Clube> clube = clubeRepository.findById(id);
        if (!clube.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Clube não existe");
        }
        Clube estadoEliminar=clube.get();
        estadoEliminar.setEstado(false);
        clubeRepository.save(estadoEliminar);
        return ResponseEntity.ok(clube.get());
    }
}
