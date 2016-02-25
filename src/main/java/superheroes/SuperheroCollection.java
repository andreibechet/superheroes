package superheroes;

import webserver.Status;

public class SuperheroCollection {
  public static Status addNewSuperhero(Superhero superhero) {
    return Status.OkSuperheroAdded;
  }
}
