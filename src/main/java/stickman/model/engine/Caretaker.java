package stickman.model.engine;

import stickman.model.levels.EntityMomento;

public class Caretaker {
  private Memento memento;
  private EntityMomento entityMomento;

  public Memento getMemento() {
    return memento;
  }

  public EntityMomento getEntityMomento() {
    return entityMomento;
  }

  public void setEntityMomento(EntityMomento entityMomento) {
    this.entityMomento = entityMomento;
  }

  public void setMemento(Memento memento) {
    this.memento = memento;
  }
}
