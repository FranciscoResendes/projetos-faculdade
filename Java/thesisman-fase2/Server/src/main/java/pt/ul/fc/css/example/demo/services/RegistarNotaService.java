package pt.ul.fc.css.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.handlers.RegistoNotaDefesaHandler;

@Service
public class RegistarNotaService {
  @Autowired RegistoNotaDefesaHandler regHandler;

  public RegistarNotaService(RegistoNotaDefesaHandler handler) {
    this.regHandler = handler;
  }

  public boolean registarNota(long defId, int nota, int numAluno) {
    return this.regHandler.registarNota(defId, nota, numAluno);
  }
}
