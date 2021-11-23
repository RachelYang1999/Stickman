package stickman.model.entities.platforms;

import stickman.model.config.Position;
import stickman.model.entities.Entity;
import stickman.model.entities.StaticEntity;

public class LogPlatform extends StaticEntity {
  private static final String LOG_PATH = "/log.png";

  public LogPlatform(Position<Double> position) {
    super(position, LOG_PATH, Layer.FOREGROUND, 9, 76);
  }

  public LogPlatform(LogPlatform logPlatform) {
    super(new Position<>(logPlatform.getXPos(), logPlatform.getYPos()), LOG_PATH, Layer.FOREGROUND, 9, 76);
  }

  @Override
  public Entity deepCopy() {
    return new LogPlatform(this);
  }
}
