package pt.ul.fc.css.example.demo.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.dtos.TemaDTO;
import pt.ul.fc.css.example.demo.handlers.SubmeterTemaHandler;

@Service
public class DocenteService {

  @Autowired private final SubmeterTemaHandler docenteHandler;

  public DocenteService(SubmeterTemaHandler docenteHandler) {
    this.docenteHandler = docenteHandler;
  }

  public TemaDTO submitTema(TemaDTO temaDTO) {
    return docenteHandler.submitTema(temaDTO);
  }

  public List<TemaDTO> getTemas() {
    return docenteHandler.getTemas();
  }

  public Optional<TemaDTO> getTema(Long id) {
    return docenteHandler.getTema(id);
  }

  public void deleteTema(Long id) {
    docenteHandler.deleteTema(id);
  }
}
