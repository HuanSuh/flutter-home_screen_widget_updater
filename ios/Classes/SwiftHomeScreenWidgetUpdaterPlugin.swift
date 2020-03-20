import Flutter
import UIKit

public class SwiftHomeScreenWidgetUpdaterPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "home_screen_widget_updater", binaryMessenger: registrar.messenger())
    let instance = SwiftHomeScreenWidgetUpdaterPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
