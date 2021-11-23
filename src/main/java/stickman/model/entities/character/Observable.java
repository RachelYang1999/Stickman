package stickman.model.entities.character;
import stickman.model.engine.Observer;

public interface Observable {
  void addObserver(Observer o);

  void notifyObserver();
}
