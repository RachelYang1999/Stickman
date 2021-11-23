package stickman.model.engine;

import stickman.model.levels.Level;
import java.util.*;

public class Memento {

  private List<Level> levelListCopy = new ArrayList<>();
  private Level levelCopy;
  private int levelIdCopy;
  private long additionalScoreCopy;
  private long previousScoreCopy;
  private long startTime;


  public Memento(List<Level> levelListCopy, int levelIdCopy,
      long additionalScoreCopy, long previousScoreCopy,
      long startTime) {
    this.levelListCopy = levelListCopy;
    this.levelIdCopy = levelIdCopy;
    this.additionalScoreCopy = additionalScoreCopy;
    this.previousScoreCopy = previousScoreCopy;
    this.startTime = startTime;
  }

  public List<Level> getLevelListCopy() {
    return levelListCopy;
  }

  public Level getLevelCopy() {
    return levelCopy;
  }

  public int getLevelIdCopy() {
    return levelIdCopy;
  }

  public long getAdditionalScoreCopy() {
    return additionalScoreCopy;
  }

  public long getPreviousScoreCopy() {
    return previousScoreCopy;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setLevelCopy(Level levelCopy) {
    this.levelCopy = levelCopy;
  }

  public void setLevelIdCopy(int levelIdCopy) {
    this.levelIdCopy = levelIdCopy;
  }

  public void setAdditionalScoreCopy(long additionalScoreCopy) {
    this.additionalScoreCopy = additionalScoreCopy;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public void setPreviousScoreCopy(long previousScoreCopy) {
    this.previousScoreCopy = previousScoreCopy;
  }

}
