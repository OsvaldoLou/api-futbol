package api_futbol.com.api_futbol.Dto;

import java.time.LocalDateTime;

public class DtoInputModel {
    private Long clubeMandanteId;
    private Long clubeVisitante;
    private Long estadioId;
    private LocalDateTime dataPartida;
    private Integer golsMandante;
    private Integer golsVisitante;

    public DtoInputModel(Long clubeMandanteId, Long clubeVisitante, Long estadioId, LocalDateTime dataPartida,
            Integer golsMandante, Integer golsVisitante) {
        this.clubeMandanteId = clubeMandanteId;
        this.clubeVisitante = clubeVisitante;
        this.estadioId = estadioId;
        this.dataPartida = dataPartida;
        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
    }

    public Long getClubeMandanteId() {
        return clubeMandanteId;
    }

    public void setClubeMandanteId(Long clubeMandanteId) {
        this.clubeMandanteId = clubeMandanteId;
    }

    public Long getClubeVisitante() {
        return clubeVisitante;
    }

    public void setClubeVisitante(Long clubeVisitante) {
        this.clubeVisitante = clubeVisitante;
    }

    public Long getEstadioId() {
        return estadioId;
    }

    public void setEstadioId(Long estadioId) {
        this.estadioId = estadioId;
    }

    public LocalDateTime getDataPartida() {
        return dataPartida;
    }

    public void setDataPartida(LocalDateTime dataPartida) {
        this.dataPartida = dataPartida;
    }

    public Integer getGolsMandante() {
        return golsMandante;
    }

    public void setGolsMandante(Integer golsMandante) {
        this.golsMandante = golsMandante;
    }

    public Integer getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(Integer golsVisitante) {
        this.golsVisitante = golsVisitante;
    }
    

    

}
