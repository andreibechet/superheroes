package webserver;

public class Reply {

  public enum Status {
    OkSuperheroAdded,
    SuperheroDoesNotExist,
    OkSuperheroDeleted,
    SuperheroAlreadyExists,
    OkSuperheroUpdated,
    Error
  }

}
