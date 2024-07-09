package pt.ul.fc.css.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.dtos.UtilizadorEmpresarialDTO;
import pt.ul.fc.css.example.demo.entities.UtilizadorEmpresarial;
import pt.ul.fc.css.example.demo.handlers.RegistoUtilizadorHandler;

@Service
public class UtilizadorService {

  @Autowired private RegistoUtilizadorHandler regUtilizadorHandler;

  public UtilizadorService(RegistoUtilizadorHandler handler) {
    this.regUtilizadorHandler = handler;
  }

  public Object authenticate(String username, String password) {
    try {
      return regUtilizadorHandler.authenticate(username, password);
    } catch (Exception e) {
      // Log the exception
      System.err.println("Error during authentication: " + e.getMessage());
      // You can rethrow the exception, return a default value, or handle the error in
      // another way
      return null;
    }
  }

  public void registarUtilizador(UtilizadorEmpresarial utilizador) {
    regUtilizadorHandler.saveUtilizador(utilizador);
  }

  public UtilizadorEmpresarial toUtilizadorEmpresa(UtilizadorEmpresarialDTO dto) {
    String nome = dto.getNome();
    int numero = Integer.parseInt(dto.getNumero());
    String empresa = dto.getEmpresa();
    return new UtilizadorEmpresarial(nome, numero, empresa);
  }
}
