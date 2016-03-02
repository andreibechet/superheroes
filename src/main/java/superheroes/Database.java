package superheroes;

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

  Status add(Superhero superhero, Boolean update);

  Status delete(Superhero superhero);

  Optional<Superhero> get(String someSuperheroName);

}
