package pt.ul.fc.css.example.demo.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.dtos.TemaDTO;
import pt.ul.fc.css.example.demo.handlers.SubmeterTemaHandler;

@Service
public class UtilizadorEmpresarialService {

  @Autowired private final SubmeterTemaHandler utilizadorEmpresarialHandler;

  public UtilizadorEmpresarialService(SubmeterTemaHandler utilizadorEmpresarialHandler) {
    this.utilizadorEmpresarialHandler = utilizadorEmpresarialHandler;
  }

  public TemaDTO submitTema(TemaDTO temaDTO) {
    return utilizadorEmpresarialHandler.submitTema(temaDTO);
  }

  public List<TemaDTO> getTemas() {
    return utilizadorEmpresarialHandler.getTemas();
  }

  public Optional<TemaDTO> getTema(Long id) {
    return utilizadorEmpresarialHandler.getTema(id);
  }

  public void deleteTema(Long id) {
    utilizadorEmpresarialHandler.deleteTema(id);
  }
}
