package superheroes;

import org.junit.Test;
import webserver.Status;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SuperheroCollectionShould {

  private Superhero someSuperhero = new Superhero();

  @Test
  public void addSuperheroToDbWhenNewRequestToAddWasMade() throws Exception {

    Database db = mock(Database.class);
    when(db.add(any(Superhero.class))).thenReturn(Database.Status.Ok);

    Status expected = Status.OkSuperheroAdded;
    assertEquals(SuperheroCollection.addNewSuperhero(someSuperhero), expected);


  }
}