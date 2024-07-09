// LoginController.java
package pt.ul.fc.di.css.javafxexample.presentation.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.presentation.dtos.AlunoDTO;
import pt.ul.fc.di.css.javafxexample.presentation.model.Model;

public class LoginController {
  @FXML private TextField userIdField;

  @FXML private PasswordField passwordField;

  @FXML private Label errorMessage;

  @FXML
  protected void handleLoginButtonAction(ActionEvent event) {
    errorMessage.setVisible(false);
    ;
    String userId = userIdField.getText();
    String password = passwordField.getText();
    long userlong = -1;
    Long userIdLong = null;

    try {
      userlong = Long.parseLong(userId);
      userIdLong = Long.valueOf(userlong);
    } catch (NumberFormatException e) {
      showError("Aluno não encontra, tente novamente");
    }

    Optional<AlunoDTO> aluno = getAlunoById(userIdLong);

    if (aluno.isPresent()) {
      Model.getInstance().setAlunoDTO(aluno.get());
      Model.getInstance().getAlunoDTO().setId(userIdLong);
      if (Model.getInstance().getAlunoDTO().isFinalizadaCandidatura()) {
        try {
          Parent root =
              FXMLLoader.load(
                  getClass()
                      .getResource(
                          "/pt/ul/fc/di/css/javafxexample/presentation/view/submissaoTese.fxml"));
          Stage stage = (Stage) userIdField.getScene().getWindow();
          stage.setScene(new Scene(root));
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        try {
          Parent root =
              FXMLLoader.load(
                  getClass()
                      .getResource(
                          "/pt/ul/fc/di/css/javafxexample/presentation/view/mainPage.fxml"));
          Stage stage = (Stage) userIdField.getScene().getWindow();
          stage.setScene(new Scene(root));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } else {
      showError("Aluno não encontrado, tente novamente");
    }
  }

  private Optional<AlunoDTO> getAlunoById(Long userId) {
    try {
      URL url = new URL("http://localhost:8080/api/aluno/" + userId);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      int responseCode = conn.getResponseCode();
      System.out.println("code " + responseCode);
      if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();

        ObjectMapper mapper = new ObjectMapper();
        AlunoDTO aluno = mapper.readValue(response.toString(), AlunoDTO.class);
        return Optional.of(aluno);
      } else {
        return Optional.empty();
      }
    } catch (IOException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  private void showError(String message) {
    errorMessage.setText(message);
    errorMessage.setVisible(true);
    userIdField.clear();
    passwordField.clear();
  }
}
