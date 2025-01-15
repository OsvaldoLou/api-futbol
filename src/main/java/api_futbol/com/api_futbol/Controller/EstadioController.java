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

import api_futbol.com.api_futbol.models.Estadio;
import api_futbol.com.api_futbol.repositories.EstadioRepository;

@RestController
@RequestMapping("/estadio")
public class EstadioController {

    @Autowired
    EstadioRepository estadioRepository;

    @GetMapping("/all")
    public List<Estadio> getAllEstadios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (estadioRepository.count() == 0)
            return List.of();

        int start = page * size;

        return estadioRepository.findAllWithPagination(start, size);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewEstadio(@RequestBody Estadio estadio) {

        if (IsValid(estadio)) 
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verifique os seus campos!"); 
         
        Estadio createEstadio = new Estadio();
        createEstadio.setNome(estadio.getNome());
        createEstadio.setCapacidade(estadio.getCapacidade());
        createEstadio.setLocalizacao(estadio.getLocalizacao());
        createEstadio.setEstado(estadio.getEstado());
        estadioRepository.save(createEstadio);

        return ResponseEntity.status(HttpStatus.CREATED).body(createEstadio);
    }

    private boolean IsValid(Estadio estadio){
        return (estadio.getNome() ==  null || estadio.getNome().length() < 2);
    }
 
    @PutMapping("/update")
    public ResponseEntity<?> updateEstadio(@RequestBody Estadio estadio) {
        Optional<Estadio> existingEstadio = estadioRepository.findById(estadio.getId());
        if (!existingEstadio.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Clube não existe");
        }

        Estadio updateEstadio = existingEstadio.get();
        updateEstadio.setNome(estadio.getNome());
        updateEstadio.setCapacidade(estadio.getCapacidade());
        updateEstadio.setLocalizacao(estadio.getLocalizacao());
        updateEstadio.setEstado(estadio.getEstado());
        estadioRepository.save(updateEstadio);

        return ResponseEntity.ok(updateEstadio);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEstadio(@PathVariable Long id) {
        Optional<Estadio> estadio = estadioRepository.findById(id);
        if (!estadio.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Clube não existe");
        }
        Estadio estadoEliminar=estadio.get();
        estadoEliminar.setEstado(false);
        estadioRepository.save(estadoEliminar);
        return ResponseEntity.ok(estadio.get());
    }
}