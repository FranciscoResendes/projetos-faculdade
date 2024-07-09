package pt.ul.fc.di.css.javafxexample;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Hello extends Application {

  @Override
  public void start(Stage primaryStage) {

    try {
      FXMLLoader loginLoader =
          new FXMLLoader(
              getClass()
                  .getResource("/pt/ul/fc/di/css/javafxexample/presentation/view/login.fxml"));
      Parent root = loginLoader.load();
      Scene scene = new Scene(root, 800, 600);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
