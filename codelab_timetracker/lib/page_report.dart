import 'package:flutter/material.dart';

class PageReport extends StatefulWidget {
  const PageReport({Key? key}) : super(key: key);

  @override
  _PageReportState createState() => _PageReportState();
}

class _PageReportState extends State<PageReport> {
  String? periodSelection;
  String? contentSelection;
  String? formatSelection;
  DateTimeRange? otherFromTo;
  dynamic selectedFromDate;
  dynamic selectedToDate;

  @override
  void initState() {
    super.initState();
    periodSelection="Today";
    contentSelection="Brief";
    formatSelection="Web page";
    selectedFromDate = DateTime.now();
    selectedToDate = DateTime.now().add(const Duration(days:1));
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title:Text("Report"),
      ),
      body: Container(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                Container(
                  child: Text("Period")
                ),
                DropdownButton<String>(
                    value: periodSelection,
                    items:
                      const <DropdownMenuItem<String>>[
                        DropdownMenuItem(child: Text("Last Week"),value:"Last Week"),
                        DropdownMenuItem(child: Text("This Week"),value:"This Week"),
                        DropdownMenuItem(child: Text("Yesterday"),value:"Yesterday"),
                        DropdownMenuItem(child: Text("Today"),value:"Today"),
                        DropdownMenuItem(child: Text("Other"),value:"Other"),
                      ],
                    onChanged: (String? newVal){
                      setState(() {
                        periodSelection=newVal;
                      });
                    },
                    )
              ],
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                Container(
                  child:Text("From"),
                ),
                Text(selectedFromDate.toString()),
                IconButton(icon: Icon(Icons.calendar_today_rounded),
                    onPressed: ()async{
                      await _pickFromDate(context);
                    }
                    //TODO show calendar
                )
              ],
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                Container(
                  child:Text("To"),
                ),
                Text(selectedToDate.toString()),
                IconButton(icon: Icon(Icons.calendar_today_rounded),
                    onPressed: ()async{
                      await _pickToDate(context);
                    }
                  //TODO show calendar
                )
              ],
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                Container(
                  child: Text("Content")
                    ),
                    DropdownButton<String>(
                      value: contentSelection,
                      items:
                        const <DropdownMenuItem<String>>[
                        DropdownMenuItem(child: Text("Brief"),value:"Brief"),
                        DropdownMenuItem(child: Text("Detailed"),value:"Detailed"),
                        DropdownMenuItem(child: Text("Statistic"),value:"Statistic"),
                      ],
                      onChanged: (String? newVal){
                        setState(() {
                          contentSelection=newVal;
                        });
                      },
                      )
              ],
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                Container(
                    child: Text("Format")
                ),
                DropdownButton<String>(
                  value: formatSelection,
                  items:
                  const <DropdownMenuItem<String>>[
                    DropdownMenuItem(child: Text("Web page"),value:"Web page"),
                    DropdownMenuItem(child: Text("PDF"),value:"PDF"),
                    DropdownMenuItem(child: Text("Text"),value:"Text"),
                  ],
                  onChanged: (String? newVal){
                    setState(() {
                      formatSelection=newVal;
                    });
                  },
                )
              ],
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                Container(
                  child: Center(
                    child: TextButton(
                      child:Text("Generate"),
                      style: TextButton.styleFrom(
                        textStyle: const TextStyle(
                          color: Colors.lightBlue,
                          fontSize: 20,
                        )
                      ),
                      onPressed: (){},
                    ),
                  ),
                )
              ],
            ),
          ],
        )
      ),
    );
  }

  Future<void> _pickFromDate(BuildContext context) async{
    final picked = await showDatePicker(
        context: context,
        firstDate: DateTime(DateTime.now().year - 5),
        lastDate: DateTime(DateTime.now().year + 5),
        initialDate: DateTime.now(),
    );
    DateTime end=selectedToDate;
    if(end.difference(picked!)>= const Duration(days:0))
    {
      otherFromTo = DateTimeRange(start:picked,end:end);
      setState(() {
        selectedFromDate = picked;
        periodSelection = "Other";
      });
    }
    else{
      _showAlertDates(context);
    }
  }

  Future<void> _pickToDate(BuildContext context) async{
    final picked = await showDatePicker(
      context: context,
      firstDate: DateTime(DateTime.now().year - 5),
      lastDate: DateTime(DateTime.now().year + 5),
      initialDate: DateTime.now(),
    );
    DateTime start=selectedFromDate;
    if(start.difference(picked!)<= const Duration(days:0))
    {
      otherFromTo = DateTimeRange(start:start,end:picked);
      setState(() {
        selectedToDate = picked;
        periodSelection = "Other";
      });
    }
    else{
      _showAlertDates(context);
    }
  }

  Future<dynamic> _showAlertDates(BuildContext context) {
    return showDialog(context: context,
        builder: (ctx)=>AlertDialog(
          title:const Text("Invalid custom dates"),
          content: const Text("Try again. To date must be after From date"),
          actions: [
            TextButton(
              child:const Text("Okay"),
              style:TextButton.styleFrom(
                  textStyle: const TextStyle(
                    color: Colors.lightBlue,
                    fontSize: 20,
                  ),
              ),
              onPressed: ()
              {
                Navigator.of(ctx).pop();
              },
            )
          ],
        )
    );
  }
}
