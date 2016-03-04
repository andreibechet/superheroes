package superheroes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.lightcouch.CouchDbClient;
import org.lightcouch.DesignDocument;
import org.lightcouch.Response;
import org.lightcouch.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static superheroes.Superhero.*;

// TODO: better naming maybe ?
public class CouchDbDatabase implements Database {

  public static final String DEFAULT_SUPERHEROES_DB_NAME = "superheroes";
  public static final String LOCALHOST_URL = "127.0.0.1";

  public static final String ALL_DOCS_IN_DB = "_all_docs";
  public static final String QUERY_BY_NAME_MAP_FUNC_TEMPLATE =
      "function(doc) { if (doc.name == %s) { emit(doc._id, doc); } }";

  private final CouchDbClient dbClient;

  public CouchDbDatabase(String url, String dbName) {
    dbClient = new CouchDbClient(dbName, false, "http", url, 5984, null, null);
  }

  public CouchDbDatabase() {
    this(LOCALHOST_URL, DEFAULT_SUPERHEROES_DB_NAME);
  }

  public Database.Status add(Superhero superhero) {
    List<JsonObject> existingElements = findExistingDocumentsFor(superhero.name);
    if (existingElements.size() == 0) {
      Response response = dbClient.save(superhero);
      return response.getError() == null ? Status.ItemAddedSuccessfully: Status.Error;
    } else if (existingElements.size() == 1) {
      return Status.ItemExists;
    } else {
      return Status.Error;
    }
  }

  public Database.Status delete(Superhero superhero) {
    Optional<DocumentInfo> superheroInfo = documentDetailsFor(superhero);
    if (superheroInfo.isPresent()) {
      return dbClient.remove(superheroInfo.get().id, superheroInfo.get().rev).getError() == null ?
          Status.ItemDeletedSuccessfully :
          Status.Error;
    }
    return Status.ItemDoesNotExist;
  }

  public Database.Status update(Superhero superhero) {
    delete(superhero);
    return add(superhero);
  }

  public Optional<Superhero> get(String superhero) {
    List<JsonObject> existingElements = findExistingDocumentsFor(superhero);
    if (existingElements.size() == 1) {
      return Optional.of(superheroFromJson(existingElements.get(0)));
    }
    return Optional.ofNullable(null);
  }

  @Override
  public List<Superhero> getAll() {
    List<JsonObject> allDocuments =
        getAllDocsView().query(JsonObject.class);
    List<Superhero> superheros = new ArrayList<>();
    allDocuments.iterator().forEachRemaining((JsonObject superheroData) -> superheros.add(superheroFromJson(superheroData)));
    return superheros;
  }

  private Optional<DocumentInfo> documentDetailsFor(Superhero superhero) {
    List<JsonObject> existingElements = findExistingDocumentsFor(superhero.name);
    if (existingElements.size() == 1) {
      JsonObject superheroData = existingElements.get(0);
      return Optional.of(
          new DocumentInfo(superheroData.get("_id").getAsString(), superheroData.get("_rev").getAsString()));
    }
    return Optional.ofNullable(null);
  }

  private View getAllDocsView() {
    return dbClient.view(ALL_DOCS_IN_DB).includeDocs(true);
  }

  public static Superhero superheroFromJson(JsonObject superheroData) {
    SuperheroImagination imagination = new SuperheroImagination()
        .name(superheroData.get("name").getAsString());

    // TODO: look into  . notation
    if (superheroData.has("pseudonym"))
      imagination = imagination.withPseudonym(superheroData.get("pseudonym").getAsString());
    if (superheroData.has("skills"))
      System.out.println(getListOfPropertiesFromJsonElements("skills", superheroData));
      imagination = imagination.withSkills(getListOfPropertiesFromJsonElements("skills", superheroData));
    if (superheroData.has("allies"))
      System.out.println(getListOfPropertiesFromJsonElements("allies", superheroData));
      imagination = imagination.withAllies(getListOfPropertiesFromJsonElements("allies", superheroData));
    if (superheroData.has("publisher"))
      imagination = imagination.withPublisher(superheroData.get("publisher").getAsString());
    if (superheroData.has("dataOfFirstAppearance"))
      imagination = imagination.withDateOfFirstAppearance(getDate(superheroData));

    return imagination.create();
  }

  private static Date getDate(JsonObject superheroData) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
      return dateFormat.parse(superheroData.get("dataOfFirstAppearance").getAsString());
    } catch (ParseException e) {
      return new Date(0);
    }
  }

  public static List<String> getListOfPropertiesFromJsonElements(String property, JsonObject superheroData) {
    List<String> properties = new ArrayList<>();
    superheroData.get(property).getAsJsonArray().iterator().forEachRemaining(
        (JsonElement jsonElem) -> properties.add(jsonElem.toString()));
    return properties;
  }

  public List<JsonObject> findExistingDocumentsFor(String name) {
    DesignDocument.MapReduce map = new DesignDocument.MapReduce();
    map.setMap(String.format(QUERY_BY_NAME_MAP_FUNC_TEMPLATE, name));
    return getAllDocsView()
        .tempView(map)
        .query(JsonObject.class);
  }

  private class DocumentInfo {
    final String id;
    final String rev;

    public DocumentInfo(String id, String rev) {
      this.id = id;
      this.rev = rev;
    }
  }
}
