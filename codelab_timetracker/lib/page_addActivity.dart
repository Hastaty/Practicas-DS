import 'package:flutter/material.dart';
import 'package:codelab_timetracker/requests.dart' as requests;
import 'package:codelab_timetracker/page_activities.dart' as _activityutils;

class PageAddActivity extends StatefulWidget{
  final int initialParentId;
  const PageAddActivity(this.initialParentId);
  @override
  _PageAddActivityState createState()=>_PageAddActivityState();
}
class _PageAddActivityState extends State<PageAddActivity>{
  String? activity;
  String? name;
  String? tag;
  int? parentid;
  Color TaskButtonColor = Colors.lightBlue;
  Color ProjectButtonColor = Colors.white;
  Color TaskBorderColor=Colors.lightBlue;
  Color ProjectBorderColor=Colors.grey;
  late TextEditingController _namecontroller;
  late TextEditingController _tagcontroller;

  @override
  void initState(){
    super.initState();
    parentid = widget.initialParentId;
    _namecontroller = TextEditingController();
    _tagcontroller = TextEditingController();
    activity="Task";
    name="";
    tag="";
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title:Text("New Activity"),
        actions:<Widget>[
          IconButton(icon: Icon(Icons.home),
            onPressed: (){
              while(Navigator.of(context).canPop()) {
                print("pop");
                Navigator.of(context).pop();
              }
              /* this works also:
                    Navigator.popUntil(context, ModalRoute.withName('/'));
                    */
              _activityutils.PageActivities(0);
            },
          ),
        ],
      ),
      body:Container(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              padding: EdgeInsets.all(16.0),
              child:
                Row(
                  children: [
                    Text("Type of Activity"),
                    Spacer(),
                    InkWell(
                      onTap:() {
                        activity = "Task";
                        setState(() {
                          if(TaskButtonColor==Colors.white){
                            TaskButtonColor=Colors.lightBlue;
                            TaskBorderColor=Colors.lightBlue;
                            ProjectButtonColor=Colors.white;
                            ProjectBorderColor=Colors.grey;
                          }
                          else{
                            TaskButtonColor = Colors.white;
                            TaskBorderColor=Colors.grey;
                            ProjectBorderColor=Colors.lightBlue;
                            ProjectButtonColor=Colors.lightBlue;
                          }
                        });
                      },
                      child:Container(
                        width: 30,
                        height: 30,
                        decoration: BoxDecoration(
                          border: Border.all(
                            color: TaskBorderColor,
                            width:1,
                          ),
                          shape:BoxShape.circle,
                          color:TaskButtonColor,
                        ),
                        child:
                        Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children:[
                              const Text("T",
                                  textAlign: TextAlign.center,
                                  style: TextStyle(
                                    //fontStyle: FontStyle.italic,
                                    fontWeight: FontWeight.bold,
                                    color: Colors.grey,
                                    fontSize: 18,
                                  )
                              ),
                            ]
                        )
                    ),
                    ),
                    SizedBox(width: 50),
                    InkWell(
                      onTap:() {
                        activity = "Project";
                        setState(() {
                          if(ProjectButtonColor==Colors.white){
                            ProjectButtonColor=Colors.lightBlue;
                            ProjectBorderColor=Colors.lightBlue;
                            TaskButtonColor = Colors.white;
                            TaskBorderColor=Colors.grey;
                          }
                          else{
                            ProjectButtonColor = Colors.white;
                            ProjectBorderColor=Colors.grey;
                            TaskButtonColor=Colors.lightBlue;
                            TaskBorderColor=Colors.lightBlue;
                          }
                        });
                      },
                      child:Container(
                          width: 30,
                          height: 30,
                          decoration: BoxDecoration(
                            border: Border.all(
                              color: ProjectBorderColor,
                              width:1,
                            ),
                            shape:BoxShape.circle,
                            color:ProjectButtonColor,
                          ),
                          child:
                          Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children:[
                                const Text("P",
                                    textAlign: TextAlign.center,
                                    style: TextStyle(
                                      //fontStyle: FontStyle.italic,
                                      fontWeight: FontWeight.bold,
                                      color: Colors.grey,
                                      fontSize: 18,
                                    )
                                ),
                              ]
                          )
                      ),
                    ),
                  ],
                ),
            ),
            Container(
              padding: EdgeInsets.all(16.0),
              child:
                Row(
                  children: [
                    Text("Activity Name"),
                    Spacer(),
                    SizedBox(
                      height: 50.0,
                      width: 200.0,
                      child:
                      TextField(
                        controller: _namecontroller,
                        onSubmitted: (String value) async {
                          setState(() {
                            name=value;
                            print(name);
                          });
                        },
                      ),
                    )

                ])
            ),
            Container(
                padding: EdgeInsets.all(16.0),
                child:
                Row(
                    children: [
                      Text("Activity Tag"),
                      Spacer(),
                      SizedBox(
                        height: 50.0,
                        width: 200.0,
                        child:
                        TextField(
                          controller: _tagcontroller,
                          onSubmitted: (String value) async {
                            setState(() {
                              tag=value;
                              print(tag);
                            });
                          },
                        ),
                      )

                    ])
            )
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          showDialog(
              context:context,
              builder: (context) => AlertDialog(
                  title: Text('Are you sure you want to add this activity?'),
                  actions: <Widget>[
                    FlatButton(
                      child: Text('Continue'),
                      onPressed: ()=> createActivity()
                    ),
                    FlatButton(
                      child: Text('Cancel'),
                      onPressed: () {
                        Navigator.of(context).pop('Cancel');
                      },
                    )
                  ]

              )
          );
        },
        tooltip: 'Add Activity',
        child: const Icon(IconData(0xe156, fontFamily: 'MaterialIcons')),
      ),
    );
  }
  void createActivity()
  {
    requests.newActivity(name as String, tag as String, parentid as int, activity as String);
    print("pop");
    Navigator.of(context)
        .push(MaterialPageRoute<void>(builder:(context)=>_activityutils.PageActivities(parentid as int),
    ));
  }
}