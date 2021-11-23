package stickman.model.engine;

import java.util.ArrayList;
import java.util.List;
import stickman.model.config.ConfigParser;
import stickman.model.entities.Entity;
import stickman.model.entities.MovableEntity;
import stickman.model.entities.character.Hero;
import stickman.model.entities.character.Slime;
import stickman.model.levels.EmptyLevel;
import stickman.model.levels.Level;
import stickman.model.levels.LevelBuilder;
import stickman.view.SoundPlayer;

/** An implementation of the Game Engine interface, allows control of the player */
public class GameEngineImpl implements GameEngine, Observer, Cloneable {
  private Level currentLevel;
  private int currentLevelId = 0;
  private List<Level> levelList = new ArrayList<>();
  private List<ConfigParser> configParserList = new ArrayList<>();

  private ConfigParser config;
  private String headsUpDisplayMessage = "";
  private boolean needsRefresh = false;
  private boolean gameFinished = false;
  private SoundPlayer soundPlayer = new SoundPlayer();
  private long startTime;
  private long endTime;
  private long additionalScore;
  private long currentScore;
  private long previousScore;
  private long timeWhenSaving;
  private long timeWhenRestore;
  private boolean needRestore = false;
  private boolean restart = false;


  public GameEngineImpl(List<ConfigParser> configParserList) {
    this.configParserList = configParserList;
    for (ConfigParser configParser: configParserList) {
      levelList.add(LevelBuilder.fromConfig(configParser).build());
    }
  }

  @Override
  public Memento saveMemento() {
    this.timeWhenSaving = System.currentTimeMillis();
    List<Level> levelListCopy = new ArrayList<>();
    for (Level level : levelList) {
      levelListCopy.add(level.deepCopy());
    }
    return new Memento(levelListCopy, currentLevelId, additionalScore, previousScore, startTime);
  }

  @Override
  public void restoreMemento(Memento memento){
    needRestore = true;
    this.timeWhenRestore = System.currentTimeMillis();
    this.levelList.clear();
    for (Level level : memento.getLevelListCopy()) {
      this.levelList.add(level.deepCopy());
    }
    this.currentLevelId = memento.getLevelIdCopy();
    this.currentLevel = levelList.get(currentLevelId);
    this.additionalScore = memento.getAdditionalScoreCopy();
    this.startTime = memento.getStartTime() + timeWhenRestore - timeWhenSaving;
    this.previousScore = memento.getPreviousScoreCopy();
    for (Entity entity: currentLevel.getDynamicEntities()) {
      if (entity instanceof Slime && ! ((Slime) entity).getObserverList().contains(this)) {
        ((Slime) entity).addObserver(this);
      }
    }
  }

  @Override
  public Level getCurrentLevel() {
    return this.currentLevel;
  }

  @Override
  public void startLevel() {
    if (!restart) {
      if (currentLevel != null) {
        // If we're restarting delete all the entities, so they're no longer rendered
        currentLevel.getEntities().forEach(Entity::delete);
        needsRefresh = true;
      }
      currentLevel = levelList.get(currentLevelId);
      startTime = System.currentTimeMillis();

    } else {
      this.levelList.clear();
      for (ConfigParser configParser: configParserList) {
        levelList.add(LevelBuilder.fromConfig(configParser).build());
      }
      currentLevel = levelList.get(currentLevelId);
      startTime = System.currentTimeMillis();
      restart = false;
    }
    for (Entity slime : currentLevel.getDynamicEntities()) {
      if (slime instanceof Slime) {
        if (! ((Slime) slime).getObserverList().contains(this)) {
          ((Slime) slime).addObserver(this);
        }
      }
    }
  }

  @Override
  public boolean jump() {
    if (currentLevel.getHero().jump()) {
      soundPlayer.playJumpSound();
      return true;
    }
    return false;
  }

  @Override
  public boolean moveLeft() {
    return currentLevel.getHero().moveLeft();
  }

  @Override
  public boolean moveRight() {
    return currentLevel.getHero().moveRight();
  }

  @Override
  public boolean stopMoving() {
    return currentLevel.getHero().stopMoving();
  }

  @Override
  public long getTimeSinceStart() {
    long timeSinceStart;
    if (gameFinished) {
      timeSinceStart = endTime - startTime;
      return timeSinceStart;
    }
    timeSinceStart = System.currentTimeMillis() - startTime;
    return timeSinceStart;
  }

  @Override
  public long getHeroHealth() {
    if (currentLevel.getHero() != null) {
      return currentLevel.getHero().getHealth();
    }
    return 0;
  }

  @Override
  public long getCurrentScore() {
    if (currentLevel.getTargetTime() - getTimeSinceStart() / 1000 + additionalScore >= 0) {
      currentScore = (long) (currentLevel.getTargetTime() - getTimeSinceStart() / 1000 + additionalScore);
    } else {
      currentScore = 0;
    }
    return currentScore;
  }

  @Override
  public long getPreviousScore() {
    return previousScore;
  }

  @Override
  public void update() {
    this.additionalScore += 100;
  }

  private void updateEntities() {
    // Collect the entities from the current level
    List<Entity> staticEntities = currentLevel.getStaticEntities();
    List<MovableEntity> dynamicEntities = currentLevel.getDynamicEntities();

    // Remove any dead entities
    staticEntities.removeIf(Entity::isDeleted);
    dynamicEntities.removeIf(Entity::isDeleted);

    // Move everything that can move
    for (MovableEntity a : dynamicEntities) {
      a.moveTick();
    }

    // Check for collisions
    for (MovableEntity a : dynamicEntities) {
      for (Entity b : currentLevel.getEntities()) {
        if (a != b && a.overlappingSameLayer(b)) {
          b.handleCollision(a);
          // Only do one collision at a time
          break;
        }
      }
    }
  }

  private void updateState() {
    Hero hero = currentLevel.getHero();
    // Check if we need to change state based on the hero
    if (hero.isFinished()) {
      endTime = System.currentTimeMillis();
      currentLevel.getEntities().forEach(Entity::delete);
      if (currentLevelId < levelList.size() - 1) {
        headsUpDisplayMessage = "FINISHED LEVEL " + (currentLevelId + 1) + "\n"
                              + "IT'S LEVEL " + (currentLevelId + 2) + " NOW";
        previousScore += currentScore;
        currentScore = 0;
        additionalScore = 0;
        startTime = System.currentTimeMillis();
        currentLevelId ++;
        startLevel();
      } else {
        headsUpDisplayMessage = "YOU WIN!\n" +
                                "TOTAL SCORE IS " +
                                (this.currentScore + this.previousScore);
        needsRefresh = true;
        gameFinished = true;
      }
    } else if (hero.isDeleted()) {
      headsUpDisplayMessage = "YOU LOSE\nTRY AGAIN!";
      restart = true;
      currentLevelId = 0;
      currentScore = 0;
      additionalScore = 0;
      previousScore = 0;
      needsRefresh = true;
      startLevel();
    } else if (headsUpDisplayMessage != null && hero.getXVelocity() != 0) {
      headsUpDisplayMessage = null;
    }
  }

  @Override
  public void tick() {
    // Don't update anything once we've completed the game
    if (gameFinished) {
      return;
    }

    updateEntities();
    updateState();

    // Make the level tick if it has anything to do
    this.currentLevel.tick();
  }

  @Override
  public boolean needsRefresh() {
    return needsRefresh;
  }

  public boolean getNeedRestore() {
    return this.needRestore;
  }

  public void setNeedRestore() {
    this.needRestore = false;
  }

  @Override
  public void clean() {
    needsRefresh = false;
  }

  public String getHeadsUpDisplayMessage() {
    return headsUpDisplayMessage;
  }

  @Override
  public Object clone() {
    GameEngineImpl gameEngineCopy = null;
    try{
      gameEngineCopy = (GameEngineImpl) super.clone();
    }catch(CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return gameEngineCopy;
  }

}
