import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:flutter/services.dart';

enum UpdateRequestType { INIT, UPDATE }
typedef OnUpdateRequest = Function(
    UpdateRequestType type, UpdateRequest request);

class HomeScreenWidgetUpdater {
  static MethodChannel _channel =
      const MethodChannel('home_screen_widget_updater')
        ..setMethodCallHandler((call) {
          if (call.method == "requestWidgetInitialize" ||
              call.method == "requestWidgetUpdate") {
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
          }
          return null;
        });
  static Future<bool> updateHomeScreenWidget({int widgetId, Map<String, dynamic> args}) {
    if (Platform.isAndroid)
      return _channel.invokeMethod('updateHomeScreenWidget', UpdateRequest(widgetId, args).serialize());
    return Future.value(false);
  }

  static OnUpdateRequest _onUpdateRequest;
  static set onUpdateRequest(OnUpdateRequest value) {
    _onUpdateRequest = value;
  }
}

class UpdateRequest {
  final int widgetId;
  final Map<String, dynamic> data;

  UpdateRequest(this.widgetId, this.data);

  factory UpdateRequest.json(dynamic arguments) {
    try {
      final map = JsonDecoder().convert(arguments) as Map<String, dynamic>;
      if (map != null) {
        return UpdateRequest(
          map['widgetId'],
          map['data'] != null ? JsonDecoder().convert(map['data']) : null,
        );
      }
    } catch (_) {}
    return UpdateRequest(null, null);
  }
  String serialize() {
    return JsonEncoder().convert({
      'widgetId': widgetId,
      'data': data == null ? null : JsonEncoder().convert(data),
    });
  }
}
