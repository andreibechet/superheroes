package superheroes;

import webserver.Status;

public class SuperheroCollection {

  private final Database db;

  public SuperheroCollection(Database db) {
    this.db = db;
  }

  public Status addNewSuperhero(Superhero superhero) {
    return Status.OkSuperheroAdded;
  }

  public Status deleteSuperhero(Superhero superhero) {
    if (db.exists(superhero) == Database.Status.ItemExists) {
      return db.delete(superhero) == Database.Status.Ok ? Status.OkSuperheroDeleted : Status.Error;
    }
    else {
      return Status.SuperheroDoesNotExist;
    }
  }
}
