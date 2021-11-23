package stickman.view;

import javafx.scene.layout.Pane;
import stickman.model.engine.GameEngine;

public interface BackgroundDrawer {
  void draw(GameEngine model, Pane pane);

  void update(double xViewportOffset);
}
