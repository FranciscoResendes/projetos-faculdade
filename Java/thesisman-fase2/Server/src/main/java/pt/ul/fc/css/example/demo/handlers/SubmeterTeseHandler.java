package pt.ul.fc.css.example.demo.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.TeseCatalog;
import pt.ul.fc.css.example.demo.entities.Tese;

@Component
public class SubmeterTeseHandler {

  @Autowired private TeseCatalog teseCatalog;

  public Tese getTeseById(Long id) {
    return teseCatalog.findById(id);
  }

  public void submeterPropostaTese(Tese tese) throws Exception {
    teseCatalog.save(tese);
  }

  public void submeterTeseFinal(Tese tese) throws Exception {
    teseCatalog.save(tese);
  }
}
