package superheroes;

import org.junit.Before;
import org.junit.Test;
import webserver.Status;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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
    when(db.add(any(Superhero.class))).thenReturn(Database.Status.ItemAddedSuccessfully);

    Status expected = Status.OkSuperheroAdded;
    assertEquals(superheroCollection.addNewSuperhero(someSuperhero), expected);
  }

  @Test
  public void deletesSuperheroFromDbWhenNewRequestToDeleteWasMade() throws Exception {
    when(db.delete(any(Superhero.class))).thenReturn(Database.Status.ItemDeletedSuccessfully);

    Status expected = Status.OkSuperheroDeleted;
    assertEquals(superheroCollection.deleteSuperhero(someSuperhero), expected);
  }

  @Test
  public void warningIsReturnedWhenNewRequestToDeleteWasMadeButSuperheroDoesNotExist() throws Exception {
    when(db.delete(any(Superhero.class))).thenReturn(Database.Status.ItemDoesNotExist);

    Status expected = Status.SuperheroDoesNotExist;
    assertEquals(superheroCollection.deleteSuperhero(someSuperhero), expected);
  }

  @Test
  public void addSuperheroWillNotAddTheInfoToDbWhenItAlreadyExists() throws Exception {
    when(db.add(any(Superhero.class))).thenReturn(Database.Status.ItemExists);

    Status expected = Status.SuperheroAlreadyExists;
    assertEquals(superheroCollection.addNewSuperhero(someSuperhero), expected);
  }

  @Test
  public void yieldInAnErrorMsgIfTheDbErrors() throws Exception {
    when(db.add(any(Superhero.class))).thenReturn(Database.Status.Error);
    when(db.delete(any(Superhero.class))).thenReturn(Database.Status.Error);

    Status expected = Status.Error;
    assertEquals(superheroCollection.addNewSuperhero(someSuperhero), expected);
    assertEquals(superheroCollection.deleteSuperhero(someSuperhero), expected);
  }
}