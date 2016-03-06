package webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import controllers.AddSuperheroController;
import controllers.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightcouch.CouchDbClient;
import org.lightcouch.DesignDocument;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import database.CouchDbDatabase;
import superheroes.Superhero;
import superheroes.SuperheroCollection;

import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import static database.CouchDbDatabase.QUERY_BY_NAME_MAP_FUNC_TEMPLATE;
import static database.CouchDbDatabase.ALL_DOCS_IN_DB;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AddSuperheroShould {

  public static final String TEST_DB_URL = "127.0.0.1";
  public static final String TEST_DB_NAME = "test";

  private static String SOME_SUPERHERO_NAME = "Iron Man";

  private MockMvc mvc;
  private CouchDbClient dbClient = new CouchDbClient(TEST_DB_NAME, false, "http", TEST_DB_URL, 5984, null, null);

  @Before
  public void setup() {
    SuperheroCollection testSuperheroCollection = new SuperheroCollection(new CouchDbDatabase(TEST_DB_URL, TEST_DB_NAME));
    mvc = standaloneSetup(new AddSuperheroController(testSuperheroCollection)).build();

    clearTestDb();
  }

  @After
  public void tearDown() throws Exception {
    clearTestDb();
  }

  @Test
  public void replyStatusIsOkForRequest() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String expectedValue = mapper.writeValueAsString(Reply.Status.OkSuperheroAdded);

    mvc.perform(MockMvcRequestBuilders.get("/new/superhero?name=" + SOME_SUPERHERO_NAME)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string(expectedValue));
  }

  @Test
  public void addNewSuperheroToDb() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String expectedValue = mapper.writeValueAsString(Reply.Status.OkSuperheroAdded);

    mvc.perform(MockMvcRequestBuilders.get("/new/superhero?name=" + SOME_SUPERHERO_NAME)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string(expectedValue));


    List<JsonObject> existingDocuments = getAllStoredDocuments();
    assertTrue(existingDocuments.size() == 1, "There are none or more documents in the DB with the name " +
        SOME_SUPERHERO_NAME);

    Superhero expectedSuperhero = new Superhero
        .SuperheroImagination()
        .name(SOME_SUPERHERO_NAME)
        .create();
    Superhero queriedSuperhero = CouchDbDatabase.superheroFromJson(existingDocuments.get(0));

    assertTrue(queriedSuperhero.equals(expectedSuperhero),
        "The retrieved superhero is not the same as the one stored");
  }

  @Test
  public void retrieveSuperheroesFromDb() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String expectedValue = mapper.writeValueAsString(Reply.Status.OkSuperheroAdded);

    mvc.perform(MockMvcRequestBuilders.get("/new/superhero?name=" + SOME_SUPERHERO_NAME)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string(expectedValue));


    Superhero expectedSuperhero = new Superhero
        .SuperheroImagination()
        .name(SOME_SUPERHERO_NAME)
        .create();
    String expectedSuperheroes = mapper.writeValueAsString(Arrays.asList(expectedSuperhero));
    mvc.perform(MockMvcRequestBuilders.get("/get/all")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string(expectedSuperheroes));
  }

  private void clearTestDb() {
    getAllStoredDocuments().iterator().forEachRemaining(dbClient::remove);
  }

  private List<JsonObject> getAllStoredDocuments() {
    DesignDocument.MapReduce map = new DesignDocument.MapReduce();
    map.setMap(String.format(QUERY_BY_NAME_MAP_FUNC_TEMPLATE, SOME_SUPERHERO_NAME));
    return dbClient.view(ALL_DOCS_IN_DB)
        .includeDocs(true)
        .tempView(map)
        .query(JsonObject.class);
  }
}
