package stickman.model.entities.character;

import stickman.model.entities.Entity;
import stickman.model.entities.MovableEntity;
import stickman.model.entities.utilities.MovableEntityAnimator;

/**
 * This represents the stickman / hero / player. It's unclear if they're distinct at the moment The
 * player should be controlled by the keyboard
 */
public class Hero extends AbstractCharacter {
  private static final double RATIO = 0.5882352941176471;

  private static final String[] standingRightFrames =
      new String[] {"/ch_stand1.png", "/ch_stand2.png", "/ch_stand3.png"};
  private static final String[] standingLeftFrames =
      new String[] {"/ch_stand4.png", "/ch_stand5.png", "/ch_stand6.png"};
  private static final String[] walkingRightFrames =
      new String[] {"/ch_walk1.png", "/ch_walk2.png", "/ch_walk3.png", "/ch_walk4.png"};
  private static final String[] walkingLeftFrames =
      new String[] {"/ch_walk5.png", "/ch_walk6.png", "/ch_walk7.png", "/ch_walk8.png"};

  private Size size;
  private MovableEntityAnimator heroAnimator = new MovableEntityAnimator(standingRightFrames, this);
  private boolean finished = false;
  private long health = 100;

  public Hero(double xPosition, double yPosition, Size size) {
    super(xPosition, yPosition - sizeToPixel(size), "/ch_stand1.png");
    this.size = size;
  }

  public Hero (Hero hero) {
      super(hero.getXPos(), hero.getYPos() - sizeToPixel(hero.getSize()), "/ch_stand1.png");
      this.size = hero.getSize();
      heroAnimator = new MovableEntityAnimator(standingRightFrames, this);
      health = hero.getHealth();
      finished = false;
  }
  private static double sizeToPixel(Size size) {
    switch (size) {
      case TINY:
        return 20;
      case NORMAL:
        return 35;
      case LARGE:
        return 50;
      case GIANT:
        return 75;
      default:
        throw new IllegalStateException("Invalid size for hero");
    }
  }

  public boolean jump() {
    return super.jump();
  }

  public boolean moveRight() {
    heroAnimator.setImagePaths(walkingRightFrames);
    return super.moveRight();
  }

  public boolean moveLeft() {
    heroAnimator.setImagePaths(walkingLeftFrames);
    return super.moveLeft();
  }

  public boolean stopMoving() {
    // Determine which direction we're facing, set the current animator
    if (getXVelocity() < 0) {
      heroAnimator.setImagePaths(standingLeftFrames);
    } else {
      heroAnimator.setImagePaths(standingRightFrames);
    }
    setXVelocity(0.0);
    return true;
  }

  @Override
  public double getHeight() {
    return sizeToPixel(size);
  }

  @Override
  public double getWidth() {
    return getHeight() * RATIO;
  }

  @Override
  public Entity deepCopy() {
    return new Hero(this);
  }

  @Override
  public void moveTick() {
    super.moveTick();
    heroAnimator.setNextFrame();
  }

  @Override
  public void touchedGoal() {
    finished = true;
  }

  public Size getSize() {
    return size;
  }

  public boolean isFinished() {
    return finished;
  }

  @Override
  public void collisionLeft(MovableEntity e) {
    handleDamage(e.damageInflicted());
  }

  @Override
  public void collisionRight(MovableEntity e) {
    handleDamage(e.damageInflicted());
  }

  @Override
  public void collisionUnder(MovableEntity e) {
    handleDamage(e.damageInflicted());
  }

  @Override
  public void feedbackOnTop(Entity entity) {
    super.feedbackOnTop(entity);
    if (entity.isDeleted()) {
      // Bounce off them
      accelerate(0, -3.0);
    }
  }

  private void handleDamage(int damage) {
    health -= damage;

    if (0 >= health) {
      die();
    }
  }

  public long getHealth() {
    return health;
  }

  public enum Size {
    TINY,
    NORMAL,
    LARGE,
    GIANT
  }
}
