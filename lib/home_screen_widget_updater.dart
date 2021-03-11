import 'dart:async';
import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

enum UpdateRequestType { INIT, UPDATE }
typedef OnUpdateRequest = Function(
    UpdateRequestType type, UpdateRequest request);

class HomeScreenWidgetUpdater {
  static MethodChannel _channel =
      const MethodChannel('home_screen_widget_updater')
        ..setMethodCallHandler((call) {
          switch (call.method) {
            case "requestWidgetInitialize":
            case "requestWidgetUpdate":
              if (_onUpdateRequest != null) {
                UpdateRequestType type = call.method == "requestWidgetUpdate"
                    ? UpdateRequestType.UPDATE
                    : UpdateRequestType.INIT;
                if (call.arguments != null) {
                  _onUpdateRequest(type, UpdateRequest.json(call.arguments));
                } else {
                  _onUpdateRequest(type, null);
                }
              }
              break;
            case "startAppWithData":
              if (_onAppStarted != null) {
                _onAppStarted(call.arguments?.toString());
              }
              break;
          }
          return null;
        });

  static Future<bool> updateHomeScreenWidget(
      {Map<String, dynamic> args, int widgetId, String appGroupName}) {
    try {
      return _channel.invokeMethod(
        'updateHomeScreenWidget',
        UpdateRequest(args, widgetId: widgetId, appGroupName: appGroupName)
            .serialize(),
      );
    } catch (e) {
      return Future.error(e);
    }
  }

  static OnUpdateRequest _onUpdateRequest;
  static set onUpdateRequest(OnUpdateRequest value) {
    _onUpdateRequest = value;
  }

  static ValueChanged<String> _onAppStarted;
  static set onAppStarted(ValueChanged<String> value) {
    _onAppStarted = value;
  }
}

class UpdateRequest {
  final int widgetId;
  final Map<String, dynamic> data;
  final String appGroupName;

  UpdateRequest(this.data, {this.widgetId, this.appGroupName});

  factory UpdateRequest.json(dynamic arguments) {
    try {
      final map = JsonDecoder().convert(arguments) as Map<String, dynamic>;
      if (map != null) {
        return UpdateRequest(
          map['data'] != null ? JsonDecoder().convert(map['data']) : null,
          widgetId: map['widgetId'],
          appGroupName: map['appGroupName'],
        );
      }
    } catch (_) {}
    return UpdateRequest(null);
  }
  String serialize() {
    return JsonEncoder().convert({
      'data': data == null ? null : JsonEncoder().convert(data),
      'widgetId': widgetId,
      'appGroupName': appGroupName,
    });
  }
}
