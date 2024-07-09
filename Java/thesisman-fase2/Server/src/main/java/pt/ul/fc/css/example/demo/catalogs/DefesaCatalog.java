package pt.ul.fc.css.example.demo.catalogs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.Defesa;
import pt.ul.fc.css.example.demo.repositories.DefesaRepository;

@Component
public class DefesaCatalog {
  @Autowired private DefesaRepository defesaRepository;

  public Defesa getDefesa(long id) {
    return defesaRepository.findById(id).get();
  }

  public void addDefesa(Defesa def) {
    defesaRepository.save(def);
  }

  public List<Defesa> getAllDefesas() {
    return defesaRepository.findAll();
  }
}
