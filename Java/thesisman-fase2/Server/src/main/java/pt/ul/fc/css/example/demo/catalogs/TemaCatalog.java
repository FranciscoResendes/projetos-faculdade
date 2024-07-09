package pt.ul.fc.css.example.demo.catalogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.dtos.TemaDTO;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;

@Component
public class TemaCatalog {

  private final TemaRepository temaRepository;

  public TemaCatalog(TemaRepository temaRepository) {
    this.temaRepository = temaRepository;
  }

  public Tema addTema(Tema tema) {
    return temaRepository.save(tema);
  }

  public Optional<Tema> getTema(Long id) {
    return temaRepository.findById(id);
  }

  public Tema getTemaById(Long id) {
    return temaRepository.findById(id).get();
  }

  public Iterable<Tema> getAllTemas() {
    return temaRepository.findAll();
  }

  public void deleteTema(Long id) {
    temaRepository.deleteById(id);
  }

  public List<TemaDTO> getTemasByAnoLetivo(int anoLetivo) {
    List<TemaDTO> temas = new ArrayList<TemaDTO>();

    for (Tema tema : temaRepository.findByAnoLetivo(anoLetivo)) {
      if (tema.isAvailable()) {
        TemaDTO temaDTO = tema.toTemaDTO();
        temas.add(temaDTO);
      }
      ;
    }

    return temas;
  }
}
