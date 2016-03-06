package database;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.lightcouch.CouchDbClient;
import org.lightcouch.DesignDocument;
import org.lightcouch.Response;
import org.lightcouch.View;
import superheroes.Superhero;
import util.Utils;

import java.util.*;

import static superheroes.Superhero.*;

public class CouchDbDatabase implements Database {


  public static final String DEFAULT_SUPERHEROES_DB_NAME = "superheroes";
  public static final String LOCALHOST_URL = "127.0.0.1";

  public static final String ALL_DOCS_IN_DB = "_all_docs";
  public static final String QUERY_BY_NAME_MAP_FUNC_TEMPLATE =
      "function(doc) { if (doc.name == \"%s\") { emit(doc._id, doc); } }";
  public static final String DESIGN_VIEW = "_design/";
  public static final String DESIGN_ID = "mydesign";
  public static final String MAP_KEY = "getSuperhero";

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

  public static Superhero superheroFromJson(JsonObject superheroData) {
    SuperheroImagination imagination = new SuperheroImagination()
        .name(superheroData.get("name").getAsString());

    if (superheroData.has("pseudonym"))
      imagination = imagination.withPseudonym(superheroData.get("pseudonym").getAsString());
    if (superheroData.has("skills"))
      imagination = imagination.withSkills(getListOfPropertiesFromJsonElements("skills", superheroData));
    if (superheroData.has("allies"))
      imagination = imagination.withAllies(getListOfPropertiesFromJsonElements("allies", superheroData));
    if (superheroData.has("publisher"))
      imagination = imagination.withPublisher(superheroData.get("publisher").getAsString());
    if (superheroData.has("dataOfFirstAppearance"))
      imagination = imagination.withDateOfFirstAppearance(
          Utils.getDateFromString(superheroData.get("dataOfFirstAppearance").getAsString()));

    return imagination.create();
  }

  public static List<String> getListOfPropertiesFromJsonElements(String property, JsonObject superheroData) {
    List<String> properties = new ArrayList<>();
    superheroData.get(property).getAsJsonArray().iterator().forEachRemaining(
        (JsonElement jsonElem) -> properties.add(jsonElem.toString()));
    return properties;
  }

  public List<JsonObject> findExistingDocumentsFor(String name) {
    setMapViewFor(name);
    List<JsonObject> documents = Collections.emptyList();
    try {
      documents = dbClient.view(DESIGN_ID + "/" + MAP_KEY).includeDocs(true).query(JsonObject.class);
    } catch (org.lightcouch.NoDocumentException e) {
      // no documents were found, nothing to do
    }

    clearCouchDbDesignViews();
    return documents;
  }

  private void setMapViewFor(String name) {
    DesignDocument designDocument = designDocument();
    DesignDocument.MapReduce map = mapReduce(name);

    Map<String, DesignDocument.MapReduce> view = new HashMap<>();
    view.put(MAP_KEY, map);

    designDocument.setViews(view);
    dbClient.design().synchronizeWithDb(designDocument);
  }

  private DesignDocument.MapReduce mapReduce(String name) {
    DesignDocument.MapReduce map = new DesignDocument.MapReduce();
    map.setMap(String.format(QUERY_BY_NAME_MAP_FUNC_TEMPLATE, name));
    return map;
  }

  private DesignDocument designDocument() {
    DesignDocument designDocument = new DesignDocument();
    designDocument.setId(DESIGN_VIEW + DESIGN_ID);
    designDocument.setLanguage("javascript");
    return designDocument;
  }

  private void clearCouchDbDesignViews() {
    dbClient.remove(DESIGN_VIEW + DESIGN_ID, dbClient.design().getFromDb(DESIGN_VIEW + DESIGN_ID).getRevision());
  }

  private View getAllDocsView() {
    return dbClient.view(ALL_DOCS_IN_DB).includeDocs(true);
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

  private class DocumentInfo {
    final String id;
    final String rev;

    public DocumentInfo(String id, String rev) {
      this.id = id;
      this.rev = rev;
    }
  }
}
