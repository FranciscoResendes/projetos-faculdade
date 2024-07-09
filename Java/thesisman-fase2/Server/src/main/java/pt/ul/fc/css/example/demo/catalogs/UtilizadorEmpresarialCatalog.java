package pt.ul.fc.css.example.demo.catalogs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.UtilizadorEmpresarial;
import pt.ul.fc.css.example.demo.repositories.UtilizadorEmpresarialRepository;

@Component
public class UtilizadorEmpresarialCatalog {
  @Autowired UtilizadorEmpresarialRepository utilEmpresaRep;

  public UtilizadorEmpresarialCatalog(UtilizadorEmpresarialRepository rep) {
    this.utilEmpresaRep = rep;
  }

  public UtilizadorEmpresarial findById(long id) {
    return this.utilEmpresaRep.findById(id);
  }

  public UtilizadorEmpresarial findByNome(String nome) {
    return this.utilEmpresaRep.findByNome(nome);
  }

  public UtilizadorEmpresarial findByNumero(int id) {
    return this.utilEmpresaRep.findByNumero(id);
  }

  public List<UtilizadorEmpresarial> findAllByNome(String nome) {
    return this.utilEmpresaRep.findAllByNome(nome);
  }

  public void saveUtilizador(UtilizadorEmpresarial utilizador) {
    utilEmpresaRep.save(utilizador);
  }
}
