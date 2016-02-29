package superheroes;

import webserver.Status;

public class SuperheroCollection {

  private final Database db;

  public SuperheroCollection(Database db) {
    this.db = db;
  }

  public Status addNewSuperhero(Superhero superhero) {
    switch (db.add(superhero)) {
      case ItemAddedSuccessfully: return Status.OkSuperheroAdded;
      case ItemExists: return Status.SuperheroAlreadyExists;
      default: return Status.Error;
    }
  }

  public Status deleteSuperhero(Superhero superhero) {
    switch (db.delete(superhero)) {
      case ItemDeletedSuccessfully: return Status.OkSuperheroDeleted;
      case ItemDoesNotExist: return Status.SuperheroDoesNotExist;
      default: return Status.Error;
    }
  }
}
