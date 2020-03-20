import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:home_screen_widget_updater/home_screen_widget_updater.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('HomeScreenWidgetUpdater example app'),
        ),
        body: Center(
          child: RaisedButton(
            onPressed: () {
              HomeScreenWidgetUpdater.updateHomeScreenWidget();
            },
            child: Text('Update Widget'),
          ),
        ),
      ),
    );
  }
}
