import "package:intl/intl.dart";
import "dart:convert" as convert;

final DateFormat _dateFormatter = DateFormat("yyyy-MM-DD HH:mm:ss");

abstract class Activity{
  int id;
  String name;
  DateTime? initialDate;
  DateTime? finalDate;
  int duration;
  List<dynamic> children = List<dynamic>.empty(growable: true);// initialized because of null safety
  Activity.fromJson(Map<String,dynamic> json)
    : id = json["id"],
    name = json["name"],
    initialDate = json["initial_data"] == null ? null : _dateFormatter.parse(json["initial_data"].toString().replaceAll("T", " ")),
    finalDate = json["final_data"] == null ? null : _dateFormatter.parse(json["final_data"].toString().replaceAll("T", " ")),
    duration = (double.parse(json["duration"].toString().replaceAll("PT", "").replaceAll("S", ""))).toInt();
}

class Project extends Activity{
  Project.fromJson(Map<String,dynamic> json) : super.fromJson(json){
    if(json.containsKey("activities")){
      // json has only one level because depth=1 or 0 in time_tracker
      for(Map<String, dynamic> jsonChild in json["activities"]){
        if(jsonChild["type"] == "Project"){
          children.add(Project.fromJson(jsonChild));
        }else if(jsonChild["type"] == "Task"){
          children.add(Task.fromJson(jsonChild));
        }else{
          assert(false);
        }

      }
    }
  }
}

class Task extends Activity{
  late bool active;
  Task.fromJson(Map<String,dynamic> json): active = json["active"], super.fromJson(json){
    for(Map<String, dynamic> jsonChild in json["intervals"]){
      children.add(Interval.fromJson(jsonChild));
    }
  }
}

class Interval{
  int? id;
  DateTime? initialDate;
  DateTime? finalDate;
  int duration;
  bool? active;

  Interval.fromJson(Map<String, dynamic> json) :
      id= json["id"],
      initialDate = json["start"] == null ? null: _dateFormatter.parse(json["start"].toString().replaceAll("T", " ")),
      finalDate = json["end"] ==null ? null: _dateFormatter.parse(json["end"].toString().replaceAll("T", " ")),
      duration = (double.parse(json["duration"].toString().replaceAll("PT", "").replaceAll("S", ""))).toInt(),
      active = json["active"];
}

class Tree{
  late Activity root;
  Tree(Map<String, dynamic> dec)
  {
    // 1 level tree, only children and root. If root project we have Activities
    // as its children, otherwise we have Intervals( case of task)
    if(dec["type"] == "Project")
    {
      root = Project.fromJson(dec);
    }else if(dec["type"] == "Task")
    {
      root = Task.fromJson(dec);
    }
    else{assert(false,"neither project nor task");}
  }
}
Tree getTree(){
  String strJson="{"
      "\"name\":\"root\", \"class\":\"project\", \"id\":0, \"initialDate\":\"2020-09-22 16:04:56\", \"finalDate\":\"2020-09-22 16:05:22\", \"duration\":26,"
      "\"activities\": [ "
      "{ \"name\":\"software design\", \"class\":\"project\", \"id\":1, \"initialDate\":\"2020-09-22 16:05:04\", \"finalDate\":\"2020-09-22 16:05:16\", \"duration\":16 },"
      "{ \"name\":\"software testing\", \"class\":\"project\", \"id\":2, \"initialDate\": null, \"finalDate\":null, \"duration\":0 },"
      "{ \"name\":\"databases\", \"class\":\"project\", \"id\":3,  \"finalDate\":null, \"initialDate\":null, \"duration\":0 },"
      "{ \"name\":\"transportation\", \"class\":\"task\", \"id\":6, \"active\":false, \"initialDate\":\"2020-09-22 16:04:56\", \"finalDate\":\"2020-09-22 16:05:22\", \"duration\":10, \"intervals\":[] }"
      "] "
      "}";
  Map<String, dynamic> decoded = convert.jsonDecode(strJson);
  Tree tree = Tree(decoded);
  return tree;
}

Tree getTreeTask(){
  String strJson = "{"
      "\"name\":\"transportation\",\"class\":\"task\", \"id\":10, \"active\":false, \"initialDate\":\"2020-09-22 13:36:08\", \"finalDate\":\"2020-09-22 13:36:34\", \"duration\":10,"
      "\"intervals\":["
      "{\"class\":\"interval\", \"id\":11, \"active\":false, \"initialDate\":\"2020-09-22 13:36:08\", \"finalDate\":\"2020-09-22 13:36:14\", \"duration\":6 },"
      "{\"class\":\"interval\", \"id\":12, \"active\":false, \"initialDate\":\"2020-09-22 13:36:30\", \"finalDate\":\"2020-09-22 13:36:34\", \"duration\":4}"
      "]}";
  Map<String, dynamic> decoded = convert.jsonDecode(strJson);
  Tree tree = Tree(decoded);
  return tree;
}

testLoadTree(){
  Tree tree = getTree();
  print("root name ${tree.root.name}, duration ${tree.root.duration}");
  for(Activity act in tree.root.children){
    print("child name ${act.name}, duration ${act.duration}");
  }
}

//MAIN
void main()
{
  testLoadTree();
}
