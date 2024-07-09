package pt.ul.fc.css.example.demo.catalogs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.Docente;
import pt.ul.fc.css.example.demo.repositories.DocenteRepository;

@Component
public class DocenteCatalog {
  @Autowired DocenteRepository docRep;

  public DocenteCatalog(DocenteRepository rep) {
    this.docRep = rep;
  }

  public Docente findById(long id) {
    return this.docRep.findById(id);
  }

  public Docente findByNome(String nome) {
    return this.docRep.findByNome(nome);
  }

  public Docente findByNumero(int id) {
    return this.docRep.findByNumero(id);
  }

  public List<Docente> findAllByNome(String nome) {
    return this.docRep.findAllByNome(nome);
  }
}
