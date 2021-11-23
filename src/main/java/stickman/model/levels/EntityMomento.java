package stickman.model.levels;

import java.util.ArrayList;
import java.util.List;
import stickman.model.entities.Entity;
import stickman.model.entities.MovableEntity;

public class EntityMomento {
  private List<Entity> staticEntities = new ArrayList<Entity>();
  private List<MovableEntity> dynamicEntities = new ArrayList<MovableEntity>();

  public EntityMomento(List<Entity> staticEntities, List<MovableEntity> dynamicEntities) {
    this.staticEntities = staticEntities;
    this.dynamicEntities = dynamicEntities;
  }

  public List<Entity> getStaticEntities() {
    return staticEntities;
  }

  public List<MovableEntity> getDynamicEntities() {
    return dynamicEntities;
  }

  public void setStaticEntities(List<Entity> staticEntities) {
    this.staticEntities = staticEntities;
  }

  public void setDynamicEntities(List<MovableEntity> dynamicEntities) {
    this.dynamicEntities = dynamicEntities;
  }
}
