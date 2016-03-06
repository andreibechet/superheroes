package database;

import superheroes.Superhero;

import java.util.List;
import java.util.Optional;

public interface Database {
  enum Status {
    ItemExists,
    ItemDoesNotExist,
    ItemDeletedSuccessfully,
    Error,
    ItemUpdatedSuccessfully,
    ItemAddedSuccessfully
  }

  Status add(Superhero superhero);

  Status update(Superhero superhero);

  Status delete(Superhero superhero);

  Optional<Superhero> get(String someSuperheroName);

  List<Superhero> getAll();

}
