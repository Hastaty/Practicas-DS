package core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * <h2>Clase {@code JSONWriter}</h2>
 * Implementación de la interfaz {@code Visitor} que
 * permite construir un {@code JSONArray}, guardado en el atributo
 * {@code jsonArray}, que nos permite almacenar
 * la información de la jerarquía actual. Los métodos {@code visit()} construiran
 * segun el caso({@code Task, Project o Interval})
 * un objeto {@code JSONObject} y lo meteran en {@code jsonArray}
 */
public class Jsonwriter implements Visitor {

  static {

    boolean assertsEnabled = false;
    assert assertsEnabled = true; // Intentional side effect!!!
    if (!assertsEnabled) {

      throw new RuntimeException("Asserts must be enabled!!!");

    }

  }

  private JSONArray jsonArray = new JSONArray();

  /**
   * retorno JSON array.
   *
   * @return jsonArray retorno array JSON.
   */
  public JSONArray getJsonArray() {

    return jsonArray;

  }

  private JSONObject buildActivity(JSONObject jact, Activity activity) {

    assert activity != null : " null activity";
    assert activity.getName() != null : "null name";
    assert jact != null : "null JAct";

    jact.put("name", activity.getName());
    jact.put("id",activity.getId());
    jact.put("tag", activity.getTag());
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    jact.put("initial_data", activity.getInitialDateTime().format(formatter));
    jact.put("final_data", activity.getFinalDateTime().format(formatter));
    jact.put("duration", activity.getDuration().toSecondsPart());
    jact.put("parentname", (activity.getParentProject() == null)
        ? "null" : activity.getParentProject().getName());

    assert jact.has("name") : "key name not exists";
    assert jact.get("name").toString() != null : "name null";
    assert jact.has("tag") : "key tag not exists";
    assert jact.get("tag").toString() != null : "tag null";
    assert jact.has("initial_data") : "key initial_data not exists";
    assert jact.get("initial_data").toString() != null : "initial_data null";
    assert LocalDateTime.parse(jact.get("initial_data").toString()).getYear() >= 0
        : "yaer negative";
    assert jact.has("final_data") : "key final_data not exists";
    assert jact.get("final_data").toString() != null : "initial_data null";
    assert LocalDateTime.parse(jact.get("final_data").toString()).getYear() >= 0 : "yaer negative";
    assert jact.has("duration") : "key duration not exists";
    assert jact.get("duration").toString() != null : "initial_data null";
    //assert !Duration.parse(Jact.get("duration").toString()).isNegative(): "duration negative";

    return jact;
  }

  @Override

  public void visit(Project project) {

    if (project == null) {

      throw new IllegalArgumentException("project null");

    }
    JSONObject jproject = new JSONObject();
    int beforenum = jsonArray.length();
    jproject.put("type", "Project");
    jproject = buildActivity(jproject, project);
    beforenum = beforenum + 0;
    jsonArray.put(jproject);

    assert !jsonArray.isEmpty() : "jsonArray empty later of putting a project";
    assert jsonArray.length() == beforenum + 1 : "jsonArray size incorrect";
    assert jsonArray.getJSONObject(beforenum).get("type") == "Project" : "type not correct";
  }

  @Override
  public void visit(Task task) {
    if (task == null) {

      throw new IllegalArgumentException("task null");

    }
    JSONObject jtask = new JSONObject();
    int beforenum = jsonArray.length();
    jtask.put("type", "Task");
    jtask = buildActivity(jtask, task);
    jtask.put("active",task.isActive());

    assert jtask.has("active") : "key active not exists";
    assert jtask.get("active").toString() != null : "bad boolean";

    beforenum = beforenum + 0;
    jsonArray.put(jtask);


    assert !jsonArray.isEmpty() : "jsonArray empty later of putting a task";
    assert jsonArray.length() == beforenum + 1 : "jsonArray size incorrect";
    assert jsonArray.getJSONObject(beforenum).get("type") == "Task";
  }

  @Override

  public void visit(Interval interval) {

    if (interval == null) {

      throw new IllegalArgumentException("interval null");

    }
    JSONObject jinterval = new JSONObject();
    jinterval.put("start", interval.getStart());
    jinterval.put("end", interval.getEnd());
    jinterval.put("duration", interval.getDuration().toSecondsPart());
    jinterval.put("active",interval.isActive());
    jinterval.put("parentname", interval.getParentTask().getName());
    jinterval.put("type", "Interval");
    int numbefore = jsonArray.length();
    jsonArray.put(jinterval);


    assert !jsonArray.isEmpty() : "jsonArray empty later of putting an interval";
    assert jsonArray.length() == numbefore + 1 : "lenght incorrect";
    assert jsonArray.getJSONObject(jsonArray.length() - 1).get("type")
        == "Interval" : "Type not correct";
    assert LocalDateTime.parse(jsonArray.getJSONObject(jsonArray.length()
        - 1).get("start").toString()).getYear() >= 0  : "Year Negative";
    assert LocalDateTime.parse(jsonArray.getJSONObject(jsonArray.length()
        - 1).get("end").toString()).getYear() >= 0  : "Year Negative";

  }
}
