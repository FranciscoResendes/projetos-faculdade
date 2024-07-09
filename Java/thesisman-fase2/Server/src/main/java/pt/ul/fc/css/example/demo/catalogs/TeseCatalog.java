package pt.ul.fc.css.example.demo.catalogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.Tese;
import pt.ul.fc.css.example.demo.repositories.TeseRepository;

@Component
public class TeseCatalog {

  @Autowired private TeseRepository teseRepository;

  public Tese findById(Long teseId) {
    return teseRepository
        .findById(teseId)
        .orElseThrow(() -> new IllegalArgumentException("Tese não encontrada"));
  }

  public void save(Tese tese) {
    teseRepository.save(tese);
  }
}
