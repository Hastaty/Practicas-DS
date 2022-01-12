import java.util.List;

/**
 * <h2>Clase {@code SearcherByTag}</h2>
 *  Implementación de la interfaz visitor que nos permite encontrar una
 *  actividad según su nombre. La usaremos para recuperar
 *  las actividades según su tag. Los {@code visit()} de
 *  {@code Task y Project} comprobaran que el tag sea coincidente
 *  y en tal caso añadirán la actividad a la lista de actividades
 *  coincidentes almacenada en el atributo de la superclase {@code Searcher, objList}.
 */

public class SearcherByTag extends Searcher {
  private String tag;

  private boolean tag_correct() {
    if (tag == null) {
      return false;
    }
    return true;
  }

  /**Función que implementa la funcionalidad de la classe.
   *
   * @param tag SearcherByTag.
   */

  public SearcherByTag(String tag) {

    if (tag == null) {
      throw new IllegalArgumentException("tag null");
    }
    this.tag = tag;
    assert tag_correct() : "this.tag null";
  }

  @Override
  public void visit(Project project) {
    if (project == null) {
      throw new IllegalArgumentException("project null");
    }
    if (project.getTag() == null) {
      throw new IllegalArgumentException("project null");
    }
    if (project.getTag().equals(tag)) {
      List<Activity> objectList = getObjList();
      objectList.add(project);
    }
  }

  @Override
  public void visit(Task task) {
    if (task == null) {
      throw new IllegalArgumentException("project null");
    }
    if (task.getTag() == null) {
      throw new IllegalArgumentException("project null");
    }
    if (task.getName().equals(tag)) {
      List<Activity> objectList = getObjList();
      objectList.add(task);
    }
  }

  @Override
  public void visit(Interval interval) {
    return;
  }
}
