package stickman.model.levels;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import stickman.model.config.ConfigParser;
import stickman.model.config.LevelConfig;
import stickman.model.config.Position;
import stickman.model.entities.Entity;
import stickman.model.entities.MovableEntity;
import stickman.model.entities.character.Hero;
import stickman.model.entities.platforms.LogPlatform;

/** An empty level, used as the basis for all other levels */
public class EmptyLevel implements Level, Cloneable{

  private double floorHeight;
  private double width;
  private double height;
  private double targetTime;
  private Hero hero = null;
  private List<Entity> staticEntities = new ArrayList<Entity>();
  private List<MovableEntity> dynamicEntities = new ArrayList<MovableEntity>();

  EmptyLevel(ConfigParser configParser) {
    LevelConfig level = configParser.getLevel();
    this.floorHeight = level.getFloorHeight();
    this.height = level.getHeight();
    this.width = level.getWidth();
    this.targetTime = configParser.getTargetTime();
  }

  public EmptyLevel(Level level) {
    if (level != null) {
      this.floorHeight = level.getFloorHeight();
      this.width = level.getWidth();
      this.height = level.getHeight();
      this.targetTime = level.getTargetTime();
      addHero((Hero) level.getHero().deepCopy());
      for (Entity entity : level.getStaticEntities()) {
        this.staticEntities.add(entity.deepCopy());
      }
      for (Entity moveableEntity : level.getDynamicEntities()) {
        if (! (moveableEntity instanceof Hero)) {
          this.dynamicEntities.add((MovableEntity) moveableEntity.deepCopy());
        }
      }
    }
  }

  public Hero getHero() {
    return hero;
  }

  @Override
  public List<Entity> getEntities() {
    // There are no static entities yet, but later there might be!
    return Stream.concat(staticEntities.stream(), dynamicEntities.stream())
        .collect(Collectors.toList());
  }

  @Override
  public List<Entity> getStaticEntities() {
    return staticEntities;
  }

  @Override
  public List<MovableEntity> getDynamicEntities() {
    return dynamicEntities;
  }

  @Override
  public double getHeight() {
    return height;
  }

  @Override
  public double getWidth() {
    return width;
  }

  @Override
  public double getTargetTime() {
    return targetTime;
  }

  @Override
  public void tick() {

    // Ensure nothing falls into the ground with an ad-hoc floor entity
    Position<Double> floorPosition = new Position<Double>(0.0, getFloorHeight());
    LogPlatform floor = new LogPlatform(floorPosition);
    for (MovableEntity a : dynamicEntities) {
      if (a.getYPos() + a.getHeight() > getFloorHeight()) {
        a.feedbackOnTop(floor);
      }
    }
  }

  @Override
  public double getFloorHeight() {
    return floorHeight;
  }

  @Override
  public void addStaticEntity(Entity entity) {
    this.staticEntities.add(entity);
  }

  public void clearDynamicEntity() {
    this.dynamicEntities.clear();
  }

  public void clearStaticEntity() {
    this.staticEntities.clear();
  }

  @Override
  public void addDynamicEntity(MovableEntity entity) {
    this.dynamicEntities.add(entity);
  }

  @Override
  public void addHero(Hero hero) {
    if (this.hero != null) {
      throw new Error("Hero already set for the level");
    }
    this.hero = hero;
    this.dynamicEntities.add(hero);
  }

  @Override
  public Level deepCopy() {
    return new EmptyLevel(this);
  }

}
