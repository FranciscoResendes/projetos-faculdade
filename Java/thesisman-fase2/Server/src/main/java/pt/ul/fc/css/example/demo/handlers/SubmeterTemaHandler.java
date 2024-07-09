package pt.ul.fc.css.example.demo.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.ul.fc.css.example.demo.catalogs.TemaCatalog;
import pt.ul.fc.css.example.demo.dtos.TemaDTO;
import pt.ul.fc.css.example.demo.entities.Tema;

@Component
public class SubmeterTemaHandler {

  private final TemaCatalog temaCatalog;

  public SubmeterTemaHandler(TemaCatalog temaCatalog) {
    this.temaCatalog = temaCatalog;
  }

  @Transactional
  public TemaDTO submitTema(TemaDTO temaDTO) {
    Tema tema =
        new Tema(
            temaDTO.getTitulo(),
            temaDTO.getRemuneracao(),
            temaDTO.getDescricao(),
            temaDTO.getAnoLetivo(),
            temaDTO.getMestradosCompativeis(),
            temaDTO.getAutorId());

    temaCatalog.addTema(tema);

    return tema.toTemaDTO();
  }

  public List<TemaDTO> getTemas() {
    List<Tema> temas = new ArrayList<>();
    temaCatalog.getAllTemas().forEach(temas::add);
    return temas.stream().map(Tema::toTemaDTO).collect(Collectors.toList());
  }

  public Optional<TemaDTO> getTema(Long id) {
    return temaCatalog.getTema(id).map(Tema::toTemaDTO);
  }

  public void deleteTema(Long id) {
    temaCatalog.deleteTema(id);
  }
}
