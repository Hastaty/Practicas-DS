import 'package:codelab_timetracker/tree.dart';
import 'package:flutter/material.dart';

class PageActDetails extends StatefulWidget{
  final Activity activity;
  const PageActDetails(this.activity);
  @override
  _PageActDetailsState createState()=>_PageActDetailsState();
}
class _PageActDetailsState extends State<PageActDetails>{
  Activity? act;
  @override
  void initState() {
    act=widget.activity;
    super.initState();
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("${act!.name} details"),
      ),
      body:
        Container(
          padding: const EdgeInsets.all(16),
          child:
            Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children:[
                Container(
                  padding: const EdgeInsets.all(16),
                  child:
                  Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text("Initial date"),
                      Spacer(),
                      Text("${act!.initialDate}")
                    ],
                  ),
                ),
                Container(
                  padding: const EdgeInsets.all(16),
                  child:
                  Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text("Final date"),
                      Spacer(),
                      Text("${act!.finalDate}")
                    ],
                  ),
                ),
              ]
          ),
        ),
    );
  }
}