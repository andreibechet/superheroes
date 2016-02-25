package superheroes;

public class Database {
  public Status add(Superhero superhero) {
    return Status.Ok;
  }

  public enum Status { Ok }
}
