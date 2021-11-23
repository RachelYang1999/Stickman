package stickman.model.entities.character;

import java.util.ArrayList;
import java.util.List;
import stickman.model.engine.Observer;
import stickman.model.entities.Entity;
import stickman.model.entities.MovableEntity;
import stickman.model.entities.character.movementStrategies.MovementStrategy;
import stickman.model.entities.utilities.MovableEntityAnimator;

public class Slime extends AbstractCharacter implements Observable {
  private static final double RATIO = 1.4;
  private static final double HEIGHT = 20;

  private static String[] frames = new String[] {"/slimeBa.png", "/slimeBb.png"};
  private MovableEntityAnimator slimeAnimator = new MovableEntityAnimator(frames, this);
  private MovementStrategy strategy;
  private List<Observer> observerList= new ArrayList<>();


  public Slime(double xPosition, double yPosition, MovementStrategy strategy) {
    super(xPosition, yPosition, "/slimeBa.png");
    this.strategy = strategy;
  }

  public Slime(Slime slime) {
//    super(slime.getXPos(), slime.getYPos(), "/slimeBa.png");
    super(slime.getXPos(), slime.getYPos(), "/slimeBa.png");
    this.strategy = slime.getStrategy();
  }

  @Override
  public Entity deepCopy() {
    return new Slime(this);
  }

  @Override
  public double getHeight() {
    return HEIGHT;
  }

  @Override
  public double getWidth() {
    return getHeight() * RATIO;
  }

  @Override
  public Layer getLayer() {
    return Layer.FOREGROUND;
  }

  @Override
  public void moveTick() {
    super.moveTick();
    slimeAnimator.setNextFrame();
    strategy.move(this);
  }

  @Override
  public void collisionUnder(MovableEntity e) {
    if (e.getYPos() + e.getHeight() / 2 < this.getYPos()) {
      die();
      notifyObserver();
    }
    super.collisionUnder(e);
  }

  @Override
  public void addObserver(Observer o) {
    observerList.add(o);
  }

  @Override
  public void notifyObserver() {
    for (Observer observer: observerList) {
      observer.update();
    }
  }

  public MovementStrategy getStrategy() {
    return this.strategy;
  }

  public List<Observer> getObserverList() {
    return this.observerList;
  }
}
