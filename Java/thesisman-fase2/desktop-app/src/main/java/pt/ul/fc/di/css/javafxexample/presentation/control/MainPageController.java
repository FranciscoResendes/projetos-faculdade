// MainPageController.java
package pt.ul.fc.di.css.javafxexample.presentation.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.presentation.dtos.AlunoDTO;
import pt.ul.fc.di.css.javafxexample.presentation.dtos.TemaDTO;
import pt.ul.fc.di.css.javafxexample.presentation.model.Model;

public class MainPageController {

  // botao para listar os temas
  @FXML private Button listThemesButton;

  // botao para remover a candidatura aos temas selecionados
  @FXML private Button removeFromSavedButton;

  // botao para cadidatar 1 ou mais temas
  @FXML private Button saveButton;

  @FXML private Button logoutButton;

  // Temas da candidatura
  private List<TemaDTO> savedTemas;

  // Listar os temas para a candidatura
  @FXML private Button seusTemasButton;

  @FXML private Label titleLabel;

  @FXML private Button finalizarCandidaturaButton;

  // lista de temas
  @FXML private ListView<TemaDTO> listView;

  public void initialize() {
    listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    savedTemas = listartemasDoAluno();

    listView.setCellFactory(
        param ->
            new ListCell<TemaDTO>() {
              @Override
              protected void updateItem(TemaDTO item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getTitulo() == null) {
                  setText(null);
                } else {
                  setText(item.getTitulo());
                }
              }
            });
  }

  private List<TemaDTO> listartemasDoAluno() {
    try {
      URL url =
          new URL(
              "http://localhost:8080/api/aluno/"
                  + Model.getInstance().getAlunoDTO().getId()
                  + "/temas");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();
      System.out.println("Response code: " + responseCode);
      if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          response.append(line);
        }
        reader.close();
        ObjectMapper objectMapper = new ObjectMapper();
        return Arrays.asList(objectMapper.readValue(response.toString(), TemaDTO[].class));
      } else {
        throw new Exception(
            "Failed to fetch themes from server. HTTP response code: " + responseCode);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @FXML
  protected void handleListThemesButtonAction(ActionEvent event) {
    try {
      List<TemaDTO> temas = fetchThemesFromServer();
      System.out.println("Temas: " + temas.size());
      listView.getItems().clear();
      for (TemaDTO tema : temas) {
        listView.getItems().add(tema);
      }
      titleLabel.setText("Candidatura a temas (max: 5)");
      removeFromSavedButton.setVisible(false);
      finalizarCandidaturaButton.setVisible(false);
      saveButton.setVisible(true);
    } catch (Exception e) {
      e.printStackTrace();
      titleLabel.setText("Erro ao listar os temas");
    }
  }

  private List<TemaDTO> fetchThemesFromServer() throws Exception {
    int currentYear = Year.now().getValue();

    URL url = new URL("http://localhost:8080/api/temas/" + currentYear);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    int responseCode = connection.getResponseCode();
    System.out.println("Response code: " + responseCode);
    if (responseCode == HttpURLConnection.HTTP_OK) {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();
      ObjectMapper objectMapper = new ObjectMapper();
      return Arrays.asList(objectMapper.readValue(response.toString(), TemaDTO[].class));
    } else {
      throw new Exception(
          "Failed to fetch themes from server. HTTP response code: " + responseCode);
    }
  }

  @FXML
  protected void handleSaveButtonAction(ActionEvent event) {
    ObservableList<TemaDTO> selectedItems = listView.getSelectionModel().getSelectedItems();

    if (savedTemas.size() + selectedItems.size() > 5) {
      System.out.println("Não podes selecionar mais de 5 temas");
    } else if (selectedItems.isEmpty()) {
      System.out.println("No items selected");
    } else {
      for (TemaDTO item : selectedItems) {
        if (!savedTemas.contains(item)) {
          try {
            candidatarTema(Model.getInstance().getAlunoDTO(), item);
          } catch (Exception e) {
            showError("Erro ao candidatar ao tema: " + item.getTitulo());
          }
        }
      }
    }
  }

  private void candidatarTema(AlunoDTO alunoDTO, TemaDTO temaDTO) throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();

    // Serializa os objetos diretamente para JSON
    String alunoJson = objectMapper.writeValueAsString(alunoDTO);
    String temaJson = objectMapper.writeValueAsString(temaDTO);

    // Cria o payload JSON
    String requestBody = String.format("{\"alunoDTO\": %s, \"temaDTO\": %s}", alunoJson, temaJson);

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/candidatar"))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    client
        .sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(
            response -> {
              if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
              } else {
                System.out.println("Failed : HTTP error code : " + response.statusCode());
                throw new RuntimeException("Failed : HTTP error code : " + response.statusCode());
              }
            })
        .thenAccept(System.out::println)
        .join();
  }

  @FXML
  protected void handleSeusTemasButtonAction(ActionEvent event) {
    savedTemas = listartemasDoAluno();
    listView.getItems().setAll(savedTemas);
    titleLabel.setText("Seus temas");
    removeFromSavedButton.setVisible(true);
    finalizarCandidaturaButton.setVisible(true);
    saveButton.setVisible(false);
  }

  @FXML
  protected void handleRemoveFromSavedButtonAction(ActionEvent event) {
    ObservableList<TemaDTO> selectedItems = listView.getSelectionModel().getSelectedItems();

    if (!selectedItems.isEmpty()) {
      try {
        for (TemaDTO item : selectedItems) {
          removerTema(Model.getInstance().getAlunoDTO(), item);
        }
      } catch (Exception e) {
        showError("Erro ao remover o tema");
      }
    } else {
      System.out.println("No items selected");
    }

    savedTemas = listartemasDoAluno();
    listView.getItems().setAll(savedTemas);
  }

  private void removerTema(AlunoDTO alunoDTO, TemaDTO temaDTO) throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();

    // Serializa os objetos diretamente para JSON
    String alunoJson = objectMapper.writeValueAsString(alunoDTO);
    String temaJson = objectMapper.writeValueAsString(temaDTO);

    // Cria o payload JSON
    String requestBody = String.format("{\"alunoDTO\": %s, \"temaDTO\": %s}", alunoJson, temaJson);

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/removerTema"))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    client
        .sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(
            response -> {
              if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
              } else {
                System.out.println("Failed : HTTP error code : " + response.statusCode());
                throw new RuntimeException("Failed : HTTP error code : " + response.statusCode());
              }
            })
        .thenAccept(System.out::println)
        .join();
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

  @FXML
  public void handlerFinalizarCandidaturaAction(ActionEvent event) {
    if (savedTemas.isEmpty()) {
      showError("Não tem temas selecionados");
      return;
    } else {
      try {
        Parent root =
            FXMLLoader.load(
                getClass()
                    .getResource(
                        "/pt/ul/fc/di/css/javafxexample/presentation/view/submissaoTese.fxml"));
        Stage stage = (Stage) listView.getScene().getWindow();
        stage.setScene(new Scene(root));
        updateAlunoFinalizadaCandidatura(Model.getInstance().getAlunoDTO().getId(), true);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  private void updateAlunoFinalizadaCandidatura(Long id, boolean finalizadaCandidatura)
      throws Exception {
    HttpClient client = HttpClient.newHttpClient();

    // Cria o payload JSON
    String requestBody = String.format("{\"finalizadaCandidatura\": %s}", finalizadaCandidatura);

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/aluno/" + id + "/finalizadaCandidatura"))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    client
        .sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(
            response -> {
              if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("Finalizada candidatura atualizada com sucesso");
                return response.body();
              } else {
                System.out.println("Failed : HTTP error code : " + response.statusCode());
                throw new RuntimeException("Failed : HTTP error code : " + response.statusCode());
              }
            })
        .thenAccept(System.out::println)
        .join();
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Erro");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
