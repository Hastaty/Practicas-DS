import 'package:flutter/material.dart';

class PageSearchByTag extends SearchDelegate{

  final List<List<String>> activities;
  List<List<String>> result = [];
  PageSearchByTag(this.activities);

  @override
  List<Widget> buildActions(BuildContext context) {
    return [
      IconButton(
        icon: Icon(Icons.clear),
        onPressed: () {
          this.query = '';
        }
      )
    ];
  }

  @override
  Widget buildLeading(BuildContext context) {
    return IconButton(
      icon: Icon(Icons.arrow_back),
      tooltip: 'Back',
      onPressed: () {close(context,result);},

    );
  }

  @override
  Widget buildResults(BuildContext context) {
    return Text('');
  }

  @override
  Widget buildSuggestions(BuildContext context) {
    List<int> filter = [];
    if(query == ''){
      for (int i = 0; i < activities.length; i++) {
          filter.add(i);
      }
    }
    else{
      for (int i = 0; i < activities.length; i++) {
        if(query == activities[i][1]){
          filter.add(i);
        }
      }
    }
    return ListView.builder(
        itemCount: filter.length,
        itemBuilder: (BuildContext context, int index) {
          return ListTile(
            title: Text(activities[filter[index]][0]),
            onTap: (){
              query = activities[filter[index]][0];
              String tag = activities[filter[index]][1];
              showDialog(
                  context:context,
                  builder: (context) => AlertDialog(
                      title: Text('You have selected the activity '+query+' with tag '+tag+''),
                      content: Text('Do you want to continue?'),
                      actions: <Widget>[
                        FlatButton(
                          child: Text('Continue'),
                          onPressed: () {
                            close(context, result);
                          },
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
          );
        }
    );
  }
}


