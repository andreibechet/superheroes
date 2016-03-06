package superheroes;

import database.CouchDbDatabase;
import database.Database;
import org.junit.Before;
import org.junit.Test;
import webserver.Reply;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SuperheroCollectionShould {

  private Superhero someSuperhero = new Superhero.SuperheroImagination()
      .name("Iron Man")
      .withPseudonym("Tony Stark")
      .withSkills(Arrays.asList("Strength", "Intelligence"))
      .withAllies(Arrays.asList("Thor", "Hulk"))
      .withPublisher("Marvel")
      .withDateOfFirstAppearance(new Date(0))
      .create();
  private CouchDbDatabase db;
  private SuperheroCollection superheroCollection;

  @Before
  public void setUp() throws Exception {
    db = mock(CouchDbDatabase.class);
    superheroCollection = new SuperheroCollection(db);
  }

  @Test
  public void addSuperheroToDbWhenNewRequestToAddWasMade() throws Exception {
    when(db.add(any(Superhero.class))).thenReturn(Database.Status.ItemAddedSuccessfully);

    Reply.Status expected = Reply.Status.OkSuperheroAdded;
    assertEquals(superheroCollection.addNewSuperhero(someSuperhero), expected);
  }

  @Test
  public void deletesSuperheroFromDbWhenNewRequestToDeleteWasMade() throws Exception {
    when(db.delete(any(Superhero.class))).thenReturn(Database.Status.ItemDeletedSuccessfully);

    Reply.Status expected = Reply.Status.OkSuperheroDeleted;
    assertEquals(superheroCollection.deleteSuperhero(someSuperhero), expected);
  }

  @Test
  public void warningIsReturnedWhenNewRequestToDeleteWasMadeButSuperheroDoesNotExist() throws Exception {
    when(db.delete(any(Superhero.class))).thenReturn(Database.Status.ItemDoesNotExist);

    Reply.Status expected = Reply.Status.SuperheroDoesNotExist;
    assertEquals(superheroCollection.deleteSuperhero(someSuperhero), expected);
  }

  @Test
  public void addSuperheroWillNotAddTheInfoToDbWhenItAlreadyExists() throws Exception {
    when(db.add(any(Superhero.class))).thenReturn(Database.Status.ItemExists);

    Reply.Status expected = Reply.Status.SuperheroAlreadyExists;
    assertEquals(superheroCollection.addNewSuperhero(someSuperhero), expected);
  }

  @Test
  public void yieldInAnErrorMsgIfTheDbErrors() throws Exception {
    when(db.add(any(Superhero.class))).thenReturn(Database.Status.Error);
    when(db.delete(any(Superhero.class))).thenReturn(Database.Status.Error);

    Reply.Status expected = Reply.Status.Error;
    assertEquals(superheroCollection.addNewSuperhero(someSuperhero), expected);
    assertEquals(superheroCollection.deleteSuperhero(someSuperhero), expected);
  }

  @Test
  public void addUpdatedSuperheroIfUpdatedFlagIsTrue() throws Exception {
    when(db.add(any(Superhero.class))).thenReturn(Database.Status.ItemExists);
    Reply.Status expected = Reply.Status.SuperheroAlreadyExists;
    assertEquals(superheroCollection.addNewSuperhero(someSuperhero), expected);

    when(db.update(someSuperhero)).thenReturn(Database.Status.ItemAddedSuccessfully);
    expected = Reply.Status.OkSuperheroUpdated;
    assertEquals(superheroCollection.updateSuperhero(someSuperhero), expected);
  }

  @Test
  public void returnSuperheroWhichWasStored() throws Exception {
    when(db.add(someSuperhero)).thenReturn(Database.Status.ItemAddedSuccessfully);
    when(db.get(someSuperhero.name)).thenReturn(Optional.ofNullable(someSuperhero));

    Reply.Status expected = Reply.Status.OkSuperheroAdded;
    assertEquals(superheroCollection.addNewSuperhero(someSuperhero), expected);

    Optional<Superhero> expectedSuperhero = Optional.ofNullable(someSuperhero);
    assertEquals(superheroCollection.getSuperhero(someSuperhero.name).isPresent(), expectedSuperhero.isPresent());
    verify(db, times(1)).get(someSuperhero.name);
  }
}