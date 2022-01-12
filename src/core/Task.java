package core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
/** <h2> Clase Task </h2>
 * Esta clase representa una tarea.
 * Las tareas heredan metodos y atributos de actividad.
 * Una Tarea contiene ademas:
 *    - Lista de intervalos (intervals)
 *    - Numeros de intervalos (nIntervals)
 *    - Su utlima duración actualizada (lastUpdateDuration)
 */

public class Task extends Activity {
  static {
    boolean assertsEnabled = false;
    assert assertsEnabled = true; // Intentional side effect!!!
    if (!assertsEnabled) {
      throw new RuntimeException("Asserts must be enabled!!!");
    }
  }

  private static final Logger logger = LoggerFactory.getLogger("Task");
  private static final Marker fita1 = MarkerFactory.getMarker("F1");
  private static final Marker fita2 = MarkerFactory.getMarker("F2");
  private final List<Interval> intervals;
  private int nintervals;
  private Duration lastUpdateDuration;
  private boolean active;
  public boolean isActive() {
    return active;
  }
  public void changeState() {
    active = (!active);
  }

  /*
  public Task(String name, Project parent){
     this.setName(name);
     this.setParentProject(parent);
  }
  */
  /**<h2>constructor de Task</h2>.
   *
   * @param name -> nombre de la Tarea (string)
   *             es necsario para construir una tarea.
   */
  public Task(String name) {
    //assert name != null: "null name";

    if (name == null) {
      throw new IllegalArgumentException("name null");
    }
    this.setName(name);
    nintervals = 0;
    intervals = new ArrayList<Interval>();
    lastUpdateDuration = Duration.ZERO;

    assert name_correct() : "name null";
    assert nintervals_correct() : "nIntervals negative";
    assert lastupdate_correct() : "duration negative";

  }

  private boolean nintervals_correct() {

    if (nintervals < 0) {
      return false;
    }
    return true;
  }

  private boolean lastupdate_correct() {
    if (lastUpdateDuration.isNegative()) {
      return false;
    }
    return true;
  }

  public int getnIntervals() {
    return nintervals;
  }

  /**
   * Calcula el tiempo total de una tarea.
   *
   * @param period Objeto de la classe timePeriods.
   * @return Devuelve el tiempo total.
   */
  public Duration totalTimeSpent(Timeperiods period) {
    //  this should return the sum of the durations of
    //  all the intervals of the task that
    //  fit the period
    switch (period) {
      case ALL -> {

      }
      default -> {

      }
    }
    /*
    Duration totalDuration=Duration.ZERO;
    * for(Interval interval:intervals)
    * {
    *   totalDuration.plus(interval.getDuration());
    * }*/
    return Duration.ZERO;
  }
  /**<h2> Start Task</h2>
  * En esta función empezariamos o activaremos
  * una tarea (no crearla). Añadimos un intervalo
  * a la lista de intervalos de la tarea. Ese inter-
  * valo se añade al reloj (como observador), donde en
  * paralelo contaremos su duracion (Observer).
  * Si queremos activar la tarea y ya esta previamente
  * activada, mostraremos un mensaje.
  * */

  public void startTask() {
    logger.trace(fita2, "Trying to start task " + getName() + "... \n");
    int lastnum = intervals.size();
    int suma = 0;
    if (!isActive()) {
      Interval i = new Interval();
      addInterval(i);
      changeState();
      logger.debug(fita1, getName() + " starts\n");
      suma++;
    } else {
      assert isActive() : "isActive not positive";
      logger.warn(fita1, "Cannot start task because it is already active");
    }

    assert !intervals.isEmpty() : "intervals is empty";
    assert intervals.size() == lastnum + 1 : "num intervals not correct";
    assert isActive() : "isActive not positive";

  }

  /**
  * <h2>Stop task</h2>
  * En esta función paramos la activación de
  * una tarea (ya no la estamos realizando).
  * Seleccionamos el ultimo intervalo de la lista
  * de intervalos de la tarea y hacemos que deje de actualizar
  * sus tiempos con el reloj (observer)
  */

  public void stopTask() {
    logger.trace(fita2, "Trying to stop task " + getName() + "... \n");
    if (isActive()) {
      Interval intervalToDelete = intervals.get(nintervals - 1);
      Clock.getInstance().deleteObserver(intervalToDelete);
      intervalToDelete.changeState();
      changeState();
      lastUpdateDuration = Duration.ZERO;
      logger.debug(fita1, getName() + " stops\n");
    } else {
      assert !isActive() : "isActive true";
      logger.warn(fita1, "Cannot start task because it is already unactive");
    }
    assert !isActive() : "isActive true";
    assert lastupdate_correct() : "duration not negative";
  }
  /** <h2>Eliminar un intervalo</h2>
   * actualmente no tiene uso, puede servir
   * para el destructor. Elimina un intervalo.
   */

  private void deleteInterval(Interval i) {
    assert !intervals.isEmpty() : "list intervals empty";
    assert i != null : "interval null";
    assert nintervals_correct() : "nIntervals negative";
    assert nintervals != 0 : "nIntervals 0";

    nintervals--;
    int beforenum = intervals.size();
    intervals.remove(i);

    assert nintervals_correct() : "nIntervals negative";
    assert intervals.size() == beforenum - 1 : "intervals num not correct";
    assert !intervals.contains(i) : "intervals contain interval";
    assert nintervals == intervals.size() : "not equal size nintervals intervals";
  }

  /**
   * Añade un intervalo a la tarea.
   *
   * @param interval Objeto de la classe Interval.
   */
  public void addInterval(Interval interval) {
    if (interval == null) {
      throw new IllegalArgumentException("interval null");
    }
    interval.setParentTask(this);
    interval.changeState();
    intervals.add(interval);
    nintervals++;
    assert nintervals_correct() : "nIntervals 0 or negative" + nintervals;
    assert !intervals.isEmpty() : "intervals empty";
    assert interval.getParentTask() != null : "interval parent null";
    assert intervals.contains(interval) : "interval not added";
  }
  /**
   *  Actualmente gracias al patron visitor
   *  no es necesaria. La implementación InfoPrinter ya
   *  permite imprimir la información de la clase a través
   *  del método visit()
   *
   *  @deprecated Imprimir
   */

  @Override
  public void printInfo() {
    logger.info(fita1, "Task " + getName() + " child of " + getParentProject().getName()
            + "  " + getInitialDateTime() + "  "
            + getFinalDateTime() + "  " + getDuration().toSeconds() + "\n");
  }
  /** <h2> accept </h2>
   * En esta función estamos acceptando la clase visitante
   * para visitarla y que aplique nuevas funcionalidades
   * externas a la clase Task. Aplicamos el patron de
   * diseño visitor.
   *
   * @param visitor - objeto visitor para aplicar patrón.
   */

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
    for (Interval i : intervals) {
      i.accept(visitor);
    }
  }
  /**<h2> Recalculate times</h2>
   * En esta función actualizamos fechas y duración de
   * la tarea. Fecha final = Fecha final del ultimo
   * intervalo de la lista de intervalos.
   * La duracion = duración - ultima actualización de duracion
   * + la duración del ultimo intervalo de la lista de intervalos.
   */

  public void recalculateTimes() {
    // sumar la duración del último intervalo a la tarea
    Duration currentDuration = getDuration();
    currentDuration = currentDuration.minus(lastUpdateDuration);
    Interval lastInterval = intervals.get(nintervals - 1);
    lastUpdateDuration = lastInterval.getDuration();
    currentDuration = currentDuration.plus(lastInterval.getDuration());
    setDuration(currentDuration);
    Project parent = getParentProject();
    assert parent != null : "parent null";
    assert parent.getName() != null : "parent name null";
    setFinalDateTime(lastInterval.getEnd());
    parent.recalculateTimes();
    //printInfo();
    assert lastupdate_correct() : "LastUpdateDuratin negative";
    assert duration_correct() : "Update negative";
  }
  @Override
  public JSONObject toJson(int depth) {
    JSONObject jact = new JSONObject();
    JSONArray intervalss = new JSONArray();

    jact.put("type","Task");
    jact.put("name", this.getName());
    jact.put("id",this.getId());
    jact.put("tag", this.getTag());
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    jact.put("initial_data", this.getInitialDateTime().format(formatter));
    jact.put("final_data", this.getFinalDateTime().format(formatter));
    jact.put("duration", this.getDuration().toSecondsPart());
    jact.put("parentname", (this.getParentProject() == null)
        ? "null" : this.getParentProject().getName());
    jact.put("active",this.isActive());

    assert jact.has("type") :"key \"type\" does not exist";
    assert jact.get("type").toString() =="Task":"Not a Task";
    assert jact.has("name") : "key name not exists";
    assert jact.get("name").toString() != null : "name null";
    assert jact.has("tag") : "key tag not exists";
    assert jact.get("tag").toString() != null : "tag null";
    assert jact.has("initial_data") : "key initial_data not exists";
    assert jact.get("initial_data").toString() != null : "initial_data null";
    assert LocalDateTime.parse(jact.get("initial_data").toString()).getYear() >= 0
        : "year negative";
    assert jact.has("final_data") : "key final_data not exists";
    assert jact.get("final_data").toString() != null : "initial_data null";
    assert LocalDateTime.parse(jact.get("final_data").toString()).getYear() >= 0 : "year negative";
    assert jact.has("duration") : "key duration not exists";
    assert jact.get("duration").toString() != null : "initial_data null";
    //assert !Duration.parse(Jact.get("duration").toString()).isNegative(): "duration negative";
    if(depth>0)
    {

      for(Interval interval:intervals)
      {
        intervalss.put(interval.toJson());
      }

    }
    jact.put("intervals",intervalss);
    return jact;
  }
}
