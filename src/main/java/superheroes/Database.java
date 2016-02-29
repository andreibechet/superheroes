package superheroes;

import java.util.Optional;

public class Database {
  public Status add(Superhero superhero) {
    return Status.ItemAddedSuccessfully;
  }

  public Status delete(Superhero superhero) {
    return Status.ItemDeletedSuccessfully;
  }

  public Status add(Superhero superhero, Boolean update) {
    return Status.ItemUpdatedSuccessfully;
  }

  public Optional<Superhero> get(String someSuperheroName) {
    return null;
  }

  public enum Status {ItemExists, ItemDoesNotExist, ItemDeletedSuccessfully, Error, ItemUpdatedSuccessfully, ItemAddedSuccessfully}
}
