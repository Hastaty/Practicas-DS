import java.time.Duration;
import java.time.LocalDateTime;

/**
 * <h2>Clase Actividad({@code Activity})</h2>
 * Esta clase contiene toda la información(atributos)
 * y métodos que una Actividad(proyecto o tarea)
 * debería tener. De una
 * actividad necesitamos saber su nombre({@code name}),
 * las fechas inicial({@code initialDateTime}) y
 * final({@code finalDateTime}),
 * su duración({@code duration}), si está activa() y cual
 * es su proyecto padre({@code parentProject})
 */

public abstract class Activity {
  static {
    boolean assertsEnabled = false;
    assert assertsEnabled = true; // Intentional side effect!!!
    if (!assertsEnabled) {
      throw new RuntimeException("Asserts must be enabled!!!");
    }
  }

  private String name;
  private String tag;
  private  LocalDateTime initialDateTime;
  private  LocalDateTime finalDateTime;
  private Duration duration;
  private boolean active;
  private Project parentProject;

  protected boolean name_correct() {
    if (this.name == null) {
      return false;
    }
    return true;
  }

  protected boolean tag_correct() {
    if (this.tag == null) {
      return false;
    }
    return true;
  }

  protected boolean duration_correct() {
    if (this.duration == null) {
      return false;
    } else if (this.duration.isNegative()) {
      return false;
    }
    return true;
  }

  protected boolean initialTime_correct() {
    if (this.initialDateTime == null) {
      return false;
    } else if (this.initialDateTime.getYear() < 0) {
      return false;
    }
    return true;
  }

  protected boolean finalTime_correct() {
    if (this.finalDateTime == null) {
      return false;
    } else if (this.finalDateTime.getYear() < 0) {
      return false;
    }
    return true;
  }

  public String getName() {
    return name;
  }

  public String getTag() {
    return tag;
  }

  public LocalDateTime getInitialDateTime() {

    return initialDateTime;
  }

  public LocalDateTime getFinalDateTime() {
    return finalDateTime;
  }

  public Duration getDuration() {
    return duration;
  }

  public Project getParentProject() {
    return parentProject;
  }

  public boolean isActive() {
    return active;
  }

  /**
   * Setters for name, finalDateTime and parentProject.
   */
  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("name null");
    }
    this.name = name;
    assert name_correct() : "name null";
    //assert this.name == name:"name passed correctly";
  }

  /**
   * Setters for name.
   *
   */
  public void setTag(String tag) {
    if (tag == null) {
      throw new IllegalArgumentException("Tag null");
    }
    this.tag = tag;

    assert tag_correct() : "tag null";
    //assert this.tag == tag:"tag not passed correctly";
  }

  // When we set a finalDateTime we also
  // calculate the Duration of the Activity.
  // Because we should only use this method
  //when we finish an Activity we also calculate its duration

  /**
   *dsads.
   *
   */
  public  void setInitialDateTime(LocalDateTime time) {
    if (time == null) {
      throw new IllegalArgumentException("Time null");
    }
    if (time.getYear() < 0) {
      throw new IllegalArgumentException("Negative Yearl");
    }

    initialDateTime = time;

    assert initialTime_correct() : "this.initialTime error value atribute";
  }

  /**
   *cxzc.
   */
  public void setFinalDateTime(LocalDateTime time) {
    if (time == null) {
      throw new IllegalArgumentException("Time null");
    }
    if (time.getYear() < 0) {
      throw new IllegalArgumentException("Negative Yearl");
    }
    /*if(time.isBefore(initialDateTime)){
      throw new IllegalArgumentException("finalDate before initialDate");
    }*/
    finalDateTime = time;

    assert initialTime_correct() : "this.finalTime error value atribute";
  }
  //duration=Duration.between(initialDateTime,finalDateTime);}

  /**
   *czxcxz.
   *
   */
  public void setDuration(Duration d) {
    if (d == null) {
      throw new IllegalArgumentException("duration null");
    }
    if (d.isNegative()) {
      throw new IllegalArgumentException("negative duration");
    }
    duration = d;

    assert duration_correct() : "duration atribute value not correct";
    //assert this.duration == d:"duration not pased";
  }

  public void setParentProject(Project p) {
    parentProject = p;
    //assert parentProject == p:"parent passed correctly";
  }
  // if the Activity is currently active,
  // now will be deactivated, and if it
  // is deactivated this method will activate it.

  public void changeState() {
    active = (!active);
  }


  /**
   *cxzsd.
   *
   */
  public Activity() {
    name = "No name has been assigned to the activity";
    tag = "NO TAG";
    initialDateTime = LocalDateTime.now();
    finalDateTime = LocalDateTime.now();
    duration = Duration.ZERO;
    active = false;
    parentProject = null;
    assert name_correct() : "name atribue error";
    assert tag_correct() : "tag atribute error";
    assert initialTime_correct() : "initialtime atribute error";
    assert finalTime_correct() : "finalTime atribute error";
    assert duration_correct() : "duration atribute error";
  }

  /*
  public Activity(String name)
  {
    this.name = name;
    initialDateTime = LocalDateTime.now();
    finalDateTime = null;
    duration = Duration.ZERO;
    active = false;
  }*/

  /**
   * Método que nos permite implementar el patrón visitor en
   * actividades. Es abstracta porque se va a redefinir de
   * forma distinta en {@code Project} o {@code Task}
   *
   * @param visitor instancia de la clase que implementa al visitor
   */
  public abstract void accept(Visitor visitor);

  /**
   * Función que nos permite imprimir la información de una Actividad.
   * Redefinida en {@code Project} y {@code Task}
   *
   */
  public abstract void printInfo();

  public abstract Duration totalTimeSpent(Timeperiods period);

  /**
   * Método que recalcula los parámetros de
   * tiempo({@code initialDateTime,finalDateTime y duration})
   * de una actividad
   * cuando sucede una actualización en el tiempo.
   * Será diferente en el caso de {@code Task}  que en el de {@code Project}
   */
  public abstract void recalculateTimes();
}
