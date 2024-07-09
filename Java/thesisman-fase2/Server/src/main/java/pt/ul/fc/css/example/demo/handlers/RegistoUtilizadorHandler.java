package pt.ul.fc.css.example.demo.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.DocenteCatalog;
import pt.ul.fc.css.example.demo.catalogs.UtilizadorEmpresarialCatalog;
import pt.ul.fc.css.example.demo.entities.Docente;
import pt.ul.fc.css.example.demo.entities.UtilizadorEmpresarial;

@Component
public class RegistoUtilizadorHandler {
  @Autowired private UtilizadorEmpresarialCatalog utilizadorEmpresarialCat;
  @Autowired private DocenteCatalog docenteCat;

  public RegistoUtilizadorHandler(
      UtilizadorEmpresarialCatalog empresaCatalog, DocenteCatalog docCatalog) {
    this.utilizadorEmpresarialCat = empresaCatalog;
    this.docenteCat = docCatalog;
  }

  public Object authenticate(String username, String password) {
    int numeroUtilizador = Integer.parseInt(username);
    Docente docente = docenteCat.findByNumero(numeroUtilizador);
    if (docente != null) {
      return docente;
    }
    UtilizadorEmpresarial utilizadorEmpresarial =
        utilizadorEmpresarialCat.findByNumero(numeroUtilizador);
    if (utilizadorEmpresarial != null) {
      return utilizadorEmpresarial;
    }
    return null;
  }

  public void saveUtilizador(UtilizadorEmpresarial utilizador) {
    utilizadorEmpresarialCat.saveUtilizador(utilizador);
  }
}
