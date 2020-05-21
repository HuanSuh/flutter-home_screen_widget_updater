import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:home_screen_widget_updater/home_screen_widget_updater.dart';

void main() =>
    runApp(MaterialApp(home: Scaffold(appBar: AppBar(), body: MyPage())));

class MyPage extends StatefulWidget {
  @override
  _MyPageState createState() => _MyPageState();
}

class _MyPageState extends State<MyPage> {
  final TextEditingController _controller = TextEditingController();

  @override
  void dispose() {
    _controller?.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          Container(
            width: MediaQuery.of(context).size.width * 0.5,
            child: TextField(
              controller: _controller,
              autofocus: true,
              decoration: InputDecoration(
                border: OutlineInputBorder(),
                hintText: 'hello widget',
              ),
              maxLines: 1,
            ),
          ),
          SizedBox(height: 8),
          RaisedButton(
            onPressed: () async {
              try {
                bool result =
                    await HomeScreenWidgetUpdater.updateHomeScreenWidget(
                  args: {
                    'data': _controller?.text?.isNotEmpty == true
                        ? _controller.text
                        : '-',
                    'type': 'type'
                  },
                  appGroupName: 'group.widgetupdater',
                );
                Scaffold.of(context)
                    .showSnackBar(SnackBar(content: Text('result : $result')));
              } catch (e) {
                Scaffold.of(context)
                    .showSnackBar(SnackBar(content: Text('result : $e')));
              }
            },
            child: Text('Update Widget'),
          )
        ],
      ),
    );
  }
}
