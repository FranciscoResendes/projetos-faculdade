package pt.ul.fc.css.example.demo.catalogs;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.entities.Utilizador;
import pt.ul.fc.css.example.demo.repositories.UtilizadorRepository;

@Service
public class UtilizadorCatalog {

  @Autowired private final UtilizadorRepository utilizadorRepository;

  public UtilizadorCatalog(UtilizadorRepository utilizadorRepository) {
    this.utilizadorRepository = utilizadorRepository;
  }

  public Optional<Utilizador> getUtilizador(Integer id) {
    return utilizadorRepository.findById(id);
  }
}
