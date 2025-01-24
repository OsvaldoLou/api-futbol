package api_futbol.com.api_futbol.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import api_futbol.com.api_futbol.Repository.ClubeRepository;
import api_futbol.com.api_futbol.models.Clube;

@Service
public class ClubeService {

    @Autowired
    private ClubeRepository clubeRepository;

    public Clube salvarClube(Clube clube) {

        if (isValid(clube)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verifique os seus campos!");
        }

        Clube createClube = new Clube();
        createClube.setNome(clube.getNome());
        createClube.setPais(clube.getPais());
        createClube.setCidade(clube.getCidade());
        createClube.setSiglaDoEstado(clube.getSiglaDoEstado());
        createClube.setDataDeCriacao(clube.getDataDeCriacao());
        createClube.setEstado(clube.getEstado());
        clubeRepository.save(createClube);
        return createClube;
    }

    private boolean isValid(Clube clube) {
        return (clube.getNome() == null || clube.getNome().length() < 2 ||
                clube.getPais() == null || clube.getPais().length() < 2 ||
                clube.getCidade()== null || clube.getCidade().length() < 2);

    }

    public Clube editarClube(Clube clube) {

        if (clube == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não existe");
        }
        Clube updateClube = new Clube();
        updateClube.setNome(clube.getNome());
        updateClube.setPais(clube.getPais());
        updateClube.setCidade(clube.getCidade());
        updateClube.setSiglaDoEstado(clube.getSiglaDoEstado());
        updateClube.setDataDeCriacao(clube.getDataDeCriacao());
        updateClube.setEstado(clube.getEstado());
        clubeRepository.save(updateClube);
        return updateClube;

    }

    @Autowired
    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public Optional<Clube> findById(Long id) {
        return clubeRepository.findById(id);
    }

    public void deactivateClube(Clube clube) {
        clube.setEstado(false);
        clubeRepository.save(clube);
    }

}
