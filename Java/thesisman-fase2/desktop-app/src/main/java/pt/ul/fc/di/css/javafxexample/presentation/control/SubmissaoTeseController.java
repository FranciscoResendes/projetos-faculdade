package pt.ul.fc.di.css.javafxexample.presentation.control;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.presentation.model.Model;

public class SubmissaoTeseController {

  @FXML private Button logoutButton, submitProposalButton, submitFinalDocumentButton;

  public void initialize() {
    submitProposalButton.setOnAction(
        e -> {
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Submeter Documento de Proposta de Tese");
          File file = fileChooser.showOpenDialog(submitProposalButton.getScene().getWindow());
          if (file != null) {
            Long teseId = (Long) Model.getInstance().getAlunoDTO().getTeseId();
            System.out.println("Tese ID: " + teseId);
            submeterPropostaTese(teseId, file);
          }
        });
    submitFinalDocumentButton.setOnAction(
        e -> {
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Submeter Documento Final de Tese");
          File file = fileChooser.showOpenDialog(submitFinalDocumentButton.getScene().getWindow());
          if (file != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Submissão");
            alert.setHeaderText("Você está prestes a submeter o documento final de tese.");
            alert.setContentText("Tem certeza que deseja continuar?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
              Long teseId = (Long) Model.getInstance().getAlunoDTO().getTeseId();
              submeterTeseFinal(teseId, file);
            }
          }
        });
  }

  private void submeterTeseFinal(Long teseId, File file) {
    try {
      String boundary =
          Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
      String CRLF = "\r\n"; // Line separator required by multipart/form-data.

      URL url = new URL("http://localhost:8080/api/tese/submeterFinal");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

      try (OutputStream output = connection.getOutputStream();
          PrintWriter writer =
              new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true); ) {
        // Send normal param.
        writer.append("--" + boundary).append(CRLF);
        writer
            .append("Content-Disposition: form-data; name=\"id\"")
            .append(CRLF); // Use "id" instead of "teseId"
        writer.append("Content-Type: text/plain; charset=UTF-8").append(CRLF);
        writer.append(CRLF).append(String.valueOf(teseId)).append(CRLF).flush();

        // Send binary file.
        writer.append("--" + boundary).append(CRLF);
        writer
            .append(
                "Content-Disposition: form-data; name=\"file\"; filename=\""
                    + file.getName()
                    + "\"")
            .append(CRLF);
        writer
            .append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()))
            .append(CRLF);
        writer.append("Content-Transfer-Encoding: binary").append(CRLF);
        writer.append(CRLF).flush();
        Files.copy(file.toPath(), output);
        output.flush(); // Important before continuing with writer!
        writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

        // End of multipart/form-data.
        writer.append("--" + boundary + "--").append(CRLF).flush();
      }

      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        envioRealizado();
      } else {
        System.out.println("Erro ao submeter a tese final: " + responseCode);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void submeterPropostaTese(Long teseId, File file) {
    System.out.println("Submeter Proposta de Tese" + teseId);
    try {
      String boundary =
          Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
      String CRLF = "\r\n"; // Line separator required by multipart/form-data.

      URL url = new URL("http://localhost:8080/api/tese/submeterProposta");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

      try (OutputStream output = connection.getOutputStream();
          PrintWriter writer =
              new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true); ) {
        // Send normal param.
        writer.append("--" + boundary).append(CRLF);
        writer
            .append("Content-Disposition: form-data; name=\"id\"")
            .append(CRLF); // Use "id" instead of "teseId"
        writer.append("Content-Type: text/plain; charset=UTF-8").append(CRLF);
        writer.append(CRLF).append(String.valueOf(teseId)).append(CRLF).flush();

        // Send binary file.
        writer.append("--" + boundary).append(CRLF);
        writer
            .append(
                "Content-Disposition: form-data; name=\"file\"; filename=\""
                    + file.getName()
                    + "\"")
            .append(CRLF);
        writer
            .append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()))
            .append(CRLF);
        writer.append("Content-Transfer-Encoding: binary").append(CRLF);
        writer.append(CRLF).flush();
        Files.copy(file.toPath(), output);
        output.flush(); // Important before continuing with writer!
        writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

        // End of multipart/form-data.
        writer.append("--" + boundary + "--").append(CRLF).flush();
      }

      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        envioRealizado();
      } else {
        System.out.println("Erro ao submeter a proposta de tese: " + responseCode);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  protected void handleLogoutButtonAction(ActionEvent event) {
    try {
      // Carregar a cena da página de login
      Parent loginPage =
          FXMLLoader.load(
              getClass()
                  .getResource("/pt/ul/fc/di/css/javafxexample/presentation/view/login.fxml"));

      // Obter o palco atual e definir a nova cena
      Stage stage = (Stage) logoutButton.getScene().getWindow();
      stage.setScene(new Scene(loginPage));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void envioRealizado() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Envio Realizado");
    alert.setHeaderText("Envio Realizado com Sucesso");
    alert.setContentText("O documento foi enviado com sucesso.");
    alert.showAndWait();
  }
}
