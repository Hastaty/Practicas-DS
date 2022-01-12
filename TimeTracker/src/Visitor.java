/**
 * <h2>Interfaz {@code Visitor}</h2>
 * Representa la interfaz del patrón {@code Visitor} que en este caso define
 * 3 métodos visit. Estos 3 métodos nos sirven para implementar distintas funcionalidades( según las
 * implementaciones sobre esta interfaz) en las clases {@code Task, Project y Interval}
 */
public interface Visitor {
  void visit(Task task);

  void visit(Project project);

  void visit(Interval interval);
}
