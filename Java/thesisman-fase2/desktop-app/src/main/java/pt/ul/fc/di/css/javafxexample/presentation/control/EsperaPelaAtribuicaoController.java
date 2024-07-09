package pt.ul.fc.di.css.javafxexample.presentation.control;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class EsperaPelaAtribuicaoController {

  @FXML private Label dot1, dot2, dot3, dot4;

  public void initialize() {
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0),
                e -> {
                  dot1.setVisible(false);
                  dot2.setVisible(false);
                  dot3.setVisible(false);
                  dot4.setVisible(false);
                }),
            new KeyFrame(Duration.seconds(1), e -> dot1.setVisible(true)),
            new KeyFrame(Duration.seconds(2), e -> dot2.setVisible(true)),
            new KeyFrame(Duration.seconds(3), e -> dot3.setVisible(true)),
            new KeyFrame(Duration.seconds(4), e -> dot4.setVisible(true)));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }
}
