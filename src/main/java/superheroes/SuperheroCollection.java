package superheroes;

import webserver.Status;

import java.util.Optional;

public class SuperheroCollection {

  public static final boolean PLEASE_UPDATE = true;
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

  public Status updateSuperhero(Superhero superhero) {
    switch (db.add(superhero, PLEASE_UPDATE)) {
      case ItemUpdatedSuccessfully: return Status.OkSuperheroUpdated;
      default: return Status.Error;
    }
  }

  public Optional<Superhero> getSuperhero(String superheroName) {
    return db.get(superheroName);
  }
}
