import 'package:flutter/material.dart';
import 'package:codelab_timetracker/tree.dart' as Tree hide getTree;
import 'package:codelab_timetracker/requests.dart' as requests;
import 'dart:async';

class PageIntervals extends StatefulWidget {
  final int id;
  PageIntervals(this.id);

  @override
  _PageIntervalsState createState() => _PageIntervalsState();
}

class _PageIntervalsState extends State<PageIntervals> {
  late int id;
  late Future<Tree.Tree> futuretree;
  late Timer _timer;
  static const int periodeRefresh=6;
  @override
  Widget build(BuildContext context) {
    return FutureBuilder<Tree.Tree>(
      future: futuretree,
      builder:(context,snapshot){
        if(snapshot.hasData){
          int numChildren=snapshot.data!.root.children.length;
          return Scaffold(
            appBar: AppBar(
                title: Text(snapshot.data!.root.name),
                actions: <Widget>[
                  IconButton(icon: Icon(Icons.home),
                      onPressed: () {}
                  ),
                  //TODO other actions
                ]
            ),
            body: ListView.separated(
              itemBuilder: (BuildContext context, int index)=>_buildRow(snapshot.data!.root.children[index], index),
              separatorBuilder: (BuildContext context, int index)=>const Divider(),
              itemCount: snapshot.data!.root.children.length,
            ),
          );
        }
        else if(snapshot.hasError){
          return Text("${snapshot.error}");
        }
        return Container(
            height: MediaQuery.of(context).size.height,
            color: Colors.white,
            child: Center(
              child: CircularProgressIndicator(),
            ));
      },
    );
  }

  Widget _buildRow(Tree.Interval interval, int index){
    String strDuration = Duration(seconds: interval.duration).toString().split(".").first;
    String strInitDate = interval.initialDate.toString().split('.')[0];
    String strFinalDate = interval.finalDate.toString().split('.')[0];
    Container leading=(interval.active as bool)? Container(
        width: 100,
        height: 30,
        decoration: BoxDecoration(
          shape:BoxShape.rectangle,
          color:Colors.red,
        ),
        child:
        Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children:[
              const Text("ACTIVE",
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
    )
    :
    Container(width: 30, height: 30,);
    return ListTile(
      leading: leading,
      title:Text('from ${strInitDate} to ${strFinalDate}'),
      trailing:Text('${strDuration}'),
    );
  }
  @override
  void initState() {
    super.initState();
    id = widget.id;
    futuretree = requests.getTree(id);
    _activateTimer();
    //root task and children intervals
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
}
