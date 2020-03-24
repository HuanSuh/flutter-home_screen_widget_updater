import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:flutter/services.dart';

typedef OnUpdateRequest = Function(UpdateRequest request);

class HomeScreenWidgetUpdater {
  static MethodChannel _channel =
      const MethodChannel('home_screen_widget_updater')
        ..setMethodCallHandler((call) {
          if (call.method == "requestWidgetUpdate") {
            if (_onUpdateRequest != null) {
              if (call.arguments != null) {
                _onUpdateRequest(UpdateRequest.json(call.arguments));
              } else {
                _onUpdateRequest(null);
              }
            }
          }
          return null;
        });
  static Future<bool> updateHomeScreenWidget([Map<String, dynamic> args]) {
    if (Platform.isAndroid)
      return _channel.invokeMethod('updateHomeScreenWidget',
          args != null ? JsonEncoder().convert(args) : null);
    return Future.value(false);
  }

  static OnUpdateRequest _onUpdateRequest;
  static set onUpdateRequest(OnUpdateRequest value) {
    _onUpdateRequest = value;
  }
}

enum UpdateRequestType { INIT, UPDATE }

class UpdateRequest {
  final UpdateRequestType type;
  final Map<String, dynamic> data;

  UpdateRequest(String type, this.data)
      : this.type = (type == 'INIT')
            ? UpdateRequestType.INIT
            : UpdateRequestType.UPDATE;
  factory UpdateRequest.json(dynamic arguments) {
    try {
      final map = JsonDecoder().convert(arguments) as Map<String, dynamic>;
      if (map != null) {
        return UpdateRequest(map['type'],
            map['data'] != null ? JsonDecoder().convert(map['data']) : null);
      }
    } catch (_) {}
    return UpdateRequest(null, null);
  }
}
