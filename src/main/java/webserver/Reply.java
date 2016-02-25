package webserver;

public class Reply {

  private final Status status;

  public Reply(Status status) {
    this.status = status;
  }

  public Status getStatus() {
    return status;
  }
}
