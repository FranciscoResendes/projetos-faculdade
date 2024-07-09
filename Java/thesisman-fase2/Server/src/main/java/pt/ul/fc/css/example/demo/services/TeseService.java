package pt.ul.fc.css.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.entities.Tese;
import pt.ul.fc.css.example.demo.handlers.SubmeterTeseHandler;

@Service
public class TeseService {

  @Autowired private SubmeterTeseHandler SubmeterTeseHandler;

  public Tese getTeseById(Long id) {
    return SubmeterTeseHandler.getTeseById(id);
  }

  public void submeterPropostaTese(Tese tese) throws Exception {
    SubmeterTeseHandler.submeterPropostaTese(tese);
  }

  public void submeterTeseFinal(Tese tese) throws Exception {
    SubmeterTeseHandler.submeterTeseFinal(tese);
  }
}
