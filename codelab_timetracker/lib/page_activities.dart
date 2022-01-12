import 'dart:ui';
import 'dart:async';
import 'package:codelab_timetracker/page_actDetails.dart';
import 'package:codelab_timetracker/page_addActivity.dart';
import 'package:codelab_timetracker/page_report.dart';
import 'package:flutter/material.dart';
import 'package:codelab_timetracker/tree.dart';
import 'package:codelab_timetracker/page_intervals.dart';
import 'package:codelab_timetracker/page_search_by_tag.dart';
import 'package:codelab_timetracker/requests.dart' as requests;
// has the new getTree() that sends an http request to the server
const activities = [
  ['software design','1'],
  ['software testing','2'],
  ['databases','3'],
  ['task transportation','5'],
  ['problems','5'],
  ['project time tracker','6'],
  ['read handout','7'],
  ['first milestone','8'],

];

class PageActivities extends StatefulWidget {
  final int id;
  PageActivities(this.id);

  @override
  _PageActivitiesState createState() => _PageActivitiesState();
}

class _PageActivitiesState extends State<PageActivities> {
  late int id;
  late Future<Tree> futuretree;
  late Timer _timer;
  static const int periodeRefresh=1;
  @override
  void initState()
  {
    super.initState();
    id = widget.id;
    futuretree = requests.getTree(id);
    _activateTimer();
  }
  @override
  Widget build(BuildContext context) {
    return FutureBuilder<Tree>(
      future:futuretree,
      builder:(context,snapshot)
      {
        if(snapshot.hasData)
        {
          return Scaffold(
            appBar:AppBar(
              title:Text(snapshot.data!.root.name),
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
                    PageActivities(0);
                  },
                ),
                IconButton(icon: Icon(Icons.analytics_outlined),
                  onPressed: () => _navigateReport(),
                )
              ],
            ),
            body: ListView.separated(
              padding: const EdgeInsets.all(16.0),
              itemCount: snapshot.data!.root.children.length,
              itemBuilder: (BuildContext context, int index) => _buildRow(snapshot.data!.root.children[index],index),
              separatorBuilder: (BuildContext context, int index)=>const Divider(),
            ),
            floatingActionButton: Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                FloatingActionButton(
                  onPressed: () =>_navigateToCreation(id),
                  tooltip: 'Add Activity',
                  child: const Icon(Icons.add),
                ),
                FloatingActionButton(
                  onPressed: () async{
                    final result = await showSearch(
                      context: context,
                      delegate: PageSearchByTag(activities)
                    );
                    print(result);
                  },
                  tooltip: 'Search by tag',
                  child: const Icon(Icons.search),
                ),
              ],
            ),
          );
        }else if(snapshot.hasError){
          return Text("${snapshot.error}");
        }
        //By default, show progress indicator
        return Container(
            height: MediaQuery.of(context).size.height,
            color: Colors.white,
            child: Center(
              child: CircularProgressIndicator(),
            ));
        }
    );

  }
  Widget _buildRow(Activity activity, int index){
    String strDuration = Duration(seconds: activity.duration).toString().split(".").first;
    // we remove the microsec part of the duration str
    if(activity is Project){
      return ListTile(
        leading: Container(
          width: 30,
          height: 30,
          decoration: BoxDecoration(
            //border: Border.all(width: 2),
            shape:BoxShape.circle,
            color:Colors.amberAccent,
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
                      color: Colors.white,
                      fontSize: 18,
                    )
                ),
              ]
            )
        ),
        title:Text("${activity.name}"),
        trailing:
          SizedBox(
              width: 200,
              child:Row(
                  children:[
                    Text("$strDuration"),
                    TextButton(
                      onPressed:()=>_navigateToDetails(activity),
                      child: const Text("DETAILS"),
                    ),
                  ]
              )
          ),
        onTap: ()=>_navigateDownActivities(activity.id),
        //TODO, navigate down to show children tasks and projects
      );
    }else if(activity is Task){ //Task
      Task task = activity as Task;// NO ENTIENDO
      Icon taskIcon= (task.active)?const Icon(Icons.pause):const Icon(Icons.play_circle_fill);
      Widget trailing;
      trailing = Text(strDuration);
      return ListTile(
        leading: Container(
          width: 30,
          height: 30,
          decoration: BoxDecoration(
            shape:BoxShape.circle,
            color:Colors.cyanAccent,
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
                      color: Colors.white,
                      fontSize: 18,
                    )
                ),
              ]
          )
        ),
        title: Text(activity.name),
        trailing:
            SizedBox(
              width: 200,
              child:Row(
                children:[
                  IconButton(
                      onPressed: (){
                        if (task.active) {
                          requests.stop(activity.id);
                          task.active = false;
                          _refresh(); // to show immediately that task has started
                        } else {
                          requests.start(activity.id);
                          task.active=true;
                          _refresh(); // to show immediately that task has stopped
                        }
                      }
                      ,
                      icon: taskIcon
                  ),
                  trailing,
                  TextButton(
                    onPressed:()=>_navigateToDetails(activity),
                    child: const Text("DETAILS"),
                  ),
                ]
              )
            ),
            //trailing
        onTap:() =>_navigateDownIntervals(activity.id),
        //navigate button to show intervals
        /*onLongPress: () {
          if (task.active) {
            requests.stop(activity.id);
            task.active = false;
            _refresh(); // to show immediately that task has started
          } else {
            requests.start(activity.id);
            task.active=true;
            _refresh(); // to show immediately that task has stopped
          }
        },*/
        //TODO start/stop counting time for this task
      );
    }
    else{
      throw(Exception("Activity that is neither a Task or a Project"));
      // solves the problem of returning a non nullable object.
    }
  }
  void _refresh() async {
    futuretree = requests.getTree(id); // to be used in build()
    setState(() {});
  }
  void _navigateDownIntervals(int childId){
    _timer.cancel();
    Navigator.of(context)
        .push(MaterialPageRoute<void>(builder:(context)=>PageIntervals(childId),
    )).then((var value){
      _refresh();
    });
  }
  void _navigateReport()
  {
    Navigator.of(context)
        .push(MaterialPageRoute<void>(builder: (context)=>PageReport()));
  }
  void _navigateDownActivities(int childId)
  {
    _timer.cancel();
    Navigator.of(context)
        .push(MaterialPageRoute<void>(builder:(context)=>PageActivities(childId),
    )).then((var value){
      _refresh();
    });
  }
  void _activateTimer() {
    _timer = Timer.periodic(Duration(seconds: periodeRefresh), (Timer t) {
      futuretree = requests.getTree(id);
      setState(() {});
    });
  }
  @override
  void dispose() {
    // "The framework calls this method when this State object will never build again"
    // therefore when going up
    _timer.cancel();
    super.dispose();
  }

  void _navigateToCreation(int parentId)
  {
    //print(parentId);
    Navigator.of(context)
        .push(MaterialPageRoute<void>(builder:(context)=>PageAddActivity(parentId),
    )).then((var value){
      _refresh();
    });
  }

  void _navigateToDetails(Activity activity)
  {
    Navigator.of(context)
        .push(MaterialPageRoute<void>(builder:(context)=>PageActDetails(activity),
    )).then((var value){
      _refresh();
    });
  }
}
