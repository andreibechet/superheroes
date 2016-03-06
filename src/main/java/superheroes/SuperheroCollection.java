package superheroes;

import database.CouchDbDatabase;
import database.Database;
import webserver.Reply;

import java.util.List;
import java.util.Optional;

public class SuperheroCollection {

  public final Database db;

  public SuperheroCollection(Database db) {
    this.db = db;
  }

  public SuperheroCollection() {
    this(new CouchDbDatabase());
  }

  public Reply.Status addNewSuperhero(Superhero superhero) {
    switch (db.add(superhero)) {
      case ItemAddedSuccessfully: return Reply.Status.OkSuperheroAdded;
      case ItemExists: return Reply.Status.SuperheroAlreadyExists;
      default: return Reply.Status.Error;
    }
  }

  public Reply.Status deleteSuperhero(Superhero superhero) {
    switch (db.delete(superhero)) {
      case ItemDeletedSuccessfully: return Reply.Status.OkSuperheroDeleted;
      case ItemDoesNotExist: return Reply.Status.SuperheroDoesNotExist;
      default: return Reply.Status.Error;
    }
  }

  public Reply.Status updateSuperhero(Superhero superhero) {
    switch (db.update(superhero)) {
      case ItemAddedSuccessfully: return Reply.Status.OkSuperheroUpdated;
      default: return Reply.Status.Error;
    }
  }

  public Optional<Superhero> getSuperhero(String superheroName) {
    return db.get(superheroName);
  }

  public List<Superhero> getAll() {
    return db.getAll();
  }
}
