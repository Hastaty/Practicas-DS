import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;


/** <h2> Clase Project</h2>
 * Esta clase representa los proyectos que tendremos en nuestra aplicación.
 * Los proyectos contienen todos los atributos de Activity, y una lista de actividades.
 */

public class Project extends Activity {
  static {
    boolean assertsEnabled = false;
    assert assertsEnabled = true; // Intentional side effect!!!
    if (!assertsEnabled) {
      throw new RuntimeException("Asserts must be enabled!!!");
    }
  }

  private static final Logger logger = LoggerFactory.getLogger("Project");

  private final List<Activity> activities = new ArrayList<>();

  private static final Marker fita1 = MarkerFactory.getMarker("F1");
  /*
    public Project(String name, Project parentProject){
    this.setName(name);
    this.setParentProject(parentProject);
  }*/
  /**<h2>Constructor de la clase</h2>.
   *
   * @param name -> nombre del proyecto (string).
   */

  public Project(String name) {
    //assert name != null: "null name";
    if (name == null) {
      throw new IllegalArgumentException("name null");
    }
    this.setName(name);

    assert name_correct() : "name null";
    //assert this.getName() == name: "name not passed correctly";
  }
  /**<h2>Función para añadir una actividad</h2>.
   *Añadimos una actividad hijo al proyecto actual.
   *
   * @deprecated addActivity.
   *
   */

  public void addActivity(Activity act) {
    if (act == null) {
      throw new IllegalArgumentException("Activity null");
    }
    if (act.getName() == null) {
      throw new IllegalArgumentException("Activity name null");
    }
    final int last_num = activities.size();
    activities.add(act);
    act.setParentProject(this);

    assert act.getParentProject() != null : "parent project null";
    assert !activities.isEmpty() : "activities empty later of puting an activity";
    assert activities.size() == last_num + 1 : "not coherent num of activities";
    assert activities.contains(act) : "act not contained";
  }

  public List<Activity> getActivities() {
    return activities;
  }

  @Override
  public Duration totalTimeSpent(Timeperiods period) {
    if (period == null) {
      throw new IllegalArgumentException("period null");
    }
    Duration totalTime = Duration.ZERO;
    for (Activity act : activities) {
      totalTime.plus(act.totalTimeSpent(period));
    }
    assert totalTime != null : "totaltime null";
    assert !totalTime.isNegative() : "total time negative";
    return totalTime;
  }
  /**<h2> Funcion para recalcular tiempos</h2>
   * Miramos para cada actividad que contiene el proyecto
   * que actividad ha terminado ultima (finalDate mayor).
   * Esa fecha se pasa a la fecha final del proyecto
   * Calculamos la duracion del proyecto:
   *  -. ( fecha final - fecha incial )
   *  Si el proyecto actual tiene un proyecto padre,
   *  hacemos lo mismo para el.
   */

  public void recalculateTimes() {
    LocalDateTime finalDate = getFinalDateTime();
    for (Activity a : activities) {
      if (finalDate.isBefore(a.getFinalDateTime())) {
        finalDate = a.getFinalDateTime();
      }
    }
    setFinalDateTime(finalDate);
    setDuration(Duration.between(getInitialDateTime(), finalDate));
    Project parent = getParentProject();
    if (parent != null) {
      parent.recalculateTimes();
    }
    assert finalTime_correct() : "FinalDateTime not correct";
    assert duration_correct() : "duration not correct";

    //printInfo();
  }
  /** <h2> accept </h2>
   * En esta función estamos acceptando la clase visitante
   * para visitarla y que aplique nuevas funcionalidades
   * externas a la clase proyecto. Aplicamos el patron de
   * diseño visitor.
   *
   * @param visitor - objeto visitor para aplicar patrón.
   */

  @Override
  public void accept(Visitor visitor) {
    if (visitor == null) {
      throw new IllegalArgumentException("Visitor null");
    }
    visitor.visit(this);
    for (Activity a : activities) {
      a.accept(visitor);
    }
  }
  /**
   * La información mostrada consiste en:
   *  .- nombre del proyecto
   *  .- nombre del proyecto padre
   *  .- fecha inicial, final y duración.
   *
   *  @deprecated printInfo
   */

  @Override
  public void printInfo() {
    logger.info(fita1, "Project " + getName() + " child of " + ((getParentProject() != null)
            ? getParentProject().getName() : "null")
            + "  " + getInitialDateTime() + "  " + getFinalDateTime()
            + "  " + getDuration().toSeconds() + "\n");
  }
}
