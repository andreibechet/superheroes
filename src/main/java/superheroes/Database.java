package superheroes;

public class Database {
  public Status add(Superhero superhero) {
    return Status.Ok;
  }

  public Status delete(Superhero superhero) {
    return Status.Ok;
  }

  public Status exists(Superhero superhero) {
    return Status.ItemExists;
  }

  public enum Status {ItemExists, ItemDoesNotExist, Ok }
}
