import Flutter
import UIKit
import WidgetKit

public class SwiftHomeScreenWidgetUpdaterPlugin: NSObject, FlutterPlugin {
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "home_screen_widget_updater", binaryMessenger: registrar.messenger())
        let instance = SwiftHomeScreenWidgetUpdaterPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "updateHomeScreenWidget":
            if let jsonStr: String = call.arguments as? String, let json: [String:Any?] = convertToDictionary(text: jsonStr), let dataJsonStr: String = json["data"] as? String, let dataDict: [String:Any?] = convertToDictionary(text: dataJsonStr) {
                if let appGroupName = json["appGroupName"] as? String, let sharedContainer = UserDefaults(suiteName:  appGroupName) {
                    sharedContainer.set(dataDict as NSDictionary, forKey: "HOME_SCREEN_WIDGET_DATA_KEY")
                    refreshTimelines()
                    result(true)
                    break
                }
            }
            result(false)
            break
        default:
            result(FlutterMethodNotImplemented)
        }
    }

    private func convertToDictionary(text: String) -> [String: Any]? {
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }

    private func refreshTimelines() {
        if #available(iOS 14.0, *) {
            WidgetCenter.shared.reloadAllTimelines()
        }
    }
}
