package superheroes;

import org.junit.Before;
import org.junit.Test;
import webserver.Status;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SuperheroCollectionShould {

  private Superhero someSuperhero;
  private Database db;
  private SuperheroCollection superheroCollection;

  @Before
  public void setUp() throws Exception {
    someSuperhero = new Superhero();
    db = mock(Database.class);
    superheroCollection = new SuperheroCollection(db);
  }

  @Test
  public void addSuperheroToDbWhenNewRequestToAddWasMade() throws Exception {
    when(db.add(any(Superhero.class))).thenReturn(Database.Status.Ok);

    Status expected = Status.OkSuperheroAdded;
    assertEquals(superheroCollection.addNewSuperhero(someSuperhero), expected);
  }

  @Test
  public void deletesSuperheroFromDbWhenNewRequestToDeleteWasMade() throws Exception {
    when(db.exists(someSuperhero)).thenReturn(Database.Status.ItemExists);
    when(db.delete(any(Superhero.class))).thenReturn(Database.Status.Ok);

    Status expected = Status.OkSuperheroDeleted;
    assertEquals(superheroCollection.deleteSuperhero(someSuperhero), expected);
  }

  @Test
  public void warningIsReturnedWhenNewRequestToDeleteWasMadeButSuperheroDoesNotExist() throws Exception {
    when(db.exists(someSuperhero)).thenReturn(Database.Status.ItemDoesNotExist);
    when(db.delete(any(Superhero.class))).thenReturn(Database.Status.Ok);

    Status expected = Status.SuperheroDoesNotExist;
    assertEquals(superheroCollection.deleteSuperhero(someSuperhero), expected);
  }
}