package stickman.view;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import stickman.model.engine.GameEngine;

public class HeadsUpDisplayDrawer implements ForegroundDrawer {
  private static final double X_PADDING = 50;
  private static final double Y_PADDING = 5;
  private Pane pane;
  private GameEngine model;
  private Label timeElapsed = new Label();
  private Label health = new Label();
  private Label message = new Label();
  private Label currentScore = new Label();
  private Label totalScore = new Label();


  @Override
  public void draw(GameEngine model, Pane pane) {
    this.model = model;
    this.pane = pane;
    update();
    timeElapsed.setFont(new Font("Monospaced Regular", 20));
    health.setFont(new Font("Monospaced Regular", 20));
    currentScore.setFont(new Font("Monospaced Regular", 20));
    totalScore.setFont(new Font("Monospaced Regular", 20));
    message.setFont(new Font("Monospaced Regular", 55));
    this.pane.getChildren().addAll(timeElapsed, health, currentScore, totalScore, message);
  }

  @Override
  public void update() {
    // Set the positions of the labels
    timeElapsed.setLayoutX(X_PADDING / 2);
    timeElapsed.setLayoutY(Y_PADDING);

    health.setLayoutX(timeElapsed.getLayoutX() + X_PADDING + timeElapsed.getWidth());
    health.setLayoutY(Y_PADDING);

    currentScore.setLayoutX(health.getLayoutX() + X_PADDING + health.getWidth());
    currentScore.setLayoutY(Y_PADDING);

    totalScore.setLayoutX(currentScore.getLayoutX() + X_PADDING + currentScore.getWidth());
    totalScore.setLayoutY(Y_PADDING);


    message.setLayoutX(pane.getWidth() / 2 - message.getWidth() / 2);
    message.setLayoutY(pane.getHeight() / 4);
    message.setTextAlignment(TextAlignment.CENTER);

    // Set the text
    timeElapsed.setText(String.format("TIME%n %03d", model.getTimeSinceStart() / 1000));
    health.setText(String.format("HEALTH%n   %03d", model.getHeroHealth()));
    currentScore.setText(String.format("CURRENT SCORE%n   %03d", model.getCurrentScore()));
    totalScore.setText(String.format("TOTAL SCORE%n   %03d", model.getPreviousScore()));

    message.setText(model.getHeadsUpDisplayMessage());
  }
}
