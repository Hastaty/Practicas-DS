import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * <h2>Clase Intervalo({@code Interval})</h2>
 * Esta clase representa un intervalo.
 * Un intervalo es el periodo en que una
 * tarea({@code Task}) se está
 * ejecutando. De este periodo necesitamos saber el inicio()
 * , el fin(), y la duración().
 * Además estos intervalos
 * serán los objetos a partir de los cuales actualicemos
 * los tiempos del resto de actividades de la jerarquía. Esta clase
 * implementa la clase {@code Observer} que en este
 * caso recibirá de la clase {@code Observable Clock} información sobre
 * el tiempo para realizar las actualizaciones.
 * <br>
 * Para poder actualizar los tiempos de las
 * diferentes actividades del árbol necesitamos propagar la actualización de los
 * intervalos a niveles superiores y para eso
 * necesitamos saber a que {@code Task} pertenece cada {@code Interval}. Esa información
 * se almacena en el atributo {@code parentTask}.
 */
public class Interval implements Observer {
  static {
    boolean assertsEnabled = false;
    assert assertsEnabled = true; // Intentional side effect!!!
    if (!assertsEnabled) {
      throw new RuntimeException("Asserts must be enabled!!!");
    }
  }

  private Task parentTask;
  private LocalDateTime start;
  private LocalDateTime end;
  private Duration duration;
  private static final Logger logger = LoggerFactory.getLogger("Interval");
  private static final Marker fita1 = MarkerFactory.getMarker("F1");

  /**
   * Implementa toda la classe Interval.
   */
  public Interval() {
    start = LocalDateTime.now();
    end = LocalDateTime.now();
    duration = Duration.ZERO;
    parentTask = null;
    Clock.getInstance().addObserver(this);

    assert start != null : "start null";
    assert start.getYear() >= 0 : "start year negative";
    assert end != null : "end null";
    assert end.getYear() >= 0 : "start year negative";
    assert duration != null : "duration null";
    assert !duration.isNegative() : "duration is negative";
  }

  public Duration getDuration() {
    return duration;
  }

  public LocalDateTime getStart() {
    return  start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public Task getParentTask() {
    return parentTask;
  }

  public void setParentTask(Task parent) {
    parentTask = parent;
  }

  /**
   * Setter de inicio a un intervalo.
   *
   * @param start setStart.
   */
  public void setStart(LocalDateTime start) {
    if (start == null) {
      throw new IllegalArgumentException("LocalDateTime start is null");
    }
    if (start.getYear() < 0) {
      throw new IllegalArgumentException("LocalDateTime start year negative");
    }
    this.start = start;

    assert this.start != null : "this.start is null";
    assert this.start.getYear() >= 0 : "this.start is negtaive";
  }

  /**
   * Setter de duracion a un intervalo.
   *
   * @param duration setDuration.
   */

  public void setDuration(Duration duration) {
    if (duration == null) {
      throw new IllegalArgumentException("duration null ");
    }
    if (duration.isNegative()) {
      throw new IllegalArgumentException("duration negative");
    }
    this.duration = duration;

    assert this.duration != null : "this.duration null";
    assert !this.duration.isNegative() : "this.duration is negative";
  }

  /**
   * Setter de final a un intervalo.
   *
   * @param end setEnd
   */

  public void setEnd(LocalDateTime end) {
    if (end == null) {
      throw new IllegalArgumentException("LocalDateTime end is null");
    }
    if (end.getYear() < 0) {
      throw new IllegalArgumentException("LocalDateTime end year negative");
    }
    this.end = end;

    assert this.end != null : "this.end null";
    assert this.end.getYear() >= 0 : "thisend year negative";
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /**
   * Método que nos permite imprimir toda la información de un intervalo.
   *
   * @deprecated printInfo()
   */

  public void printInfo() {
    logger.info(fita1, "Interval child of " + parentTask.getName()
            + "  " + start + "  " + end + "  "
            + getDuration().toSeconds() + "\n");
  }

  /**
   * Implementación de la función update de la interfaz {@code Observer}. Actualizaremos
   * los parámetros {@code end} y {@code duration} del intervalo a partir del parámetro {@code arg}
   * que realmente es una fecha({@code LocalDateTime}).
   * Después de haber actualizado el intervalo llamamos
   * desde la tarea padre {@code parentTask} al
   * metodo {@code recalculateTimes()} para propagar la actualización del
   * intervalo a esta.
   *
   * @param o {@code Observable} que notifica
   *
   * @param arg parámetro que nos sirve para actualizar el intervalo.
   *            En este caso del tipo ({@code LocalDateTime})
   */

  @Override
  public void update(Observable o, Object arg) {
    assert o != null : "observable null";
    end = (LocalDateTime) arg;
    duration = Duration.between(start, end);
    parentTask.recalculateTimes();
    assert duration != null : "duration is null";
    assert !duration.isNegative() : "duration is negative";
  }
}
