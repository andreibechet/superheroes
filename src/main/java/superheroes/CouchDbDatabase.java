package superheroes;

import org.lightcouch.CouchDbClient;

import java.util.Optional;

public class CouchDbDatabase implements Database {
  private final CouchDbClient dbClient = new CouchDbClient("test", false, "http", "127.0.0.1", 5984, null, null);

  public Database.Status add(Superhero superhero) {
    return Database.Status.ItemAddedSuccessfully;
  }

  public Database.Status delete(Superhero superhero) {
    return Database.Status.ItemDeletedSuccessfully;
  }

  public Database.Status add(Superhero superhero, Boolean update) {
    return Database.Status.ItemUpdatedSuccessfully;
  }

  public Optional<Superhero> get(String someSuperheroName) {
    return null;
  }

}
