#import "HomeScreenWidgetUpdaterPlugin.h"
#if __has_include(<home_screen_widget_updater/home_screen_widget_updater-Swift.h>)
#import <home_screen_widget_updater/home_screen_widget_updater-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "home_screen_widget_updater-Swift.h"
#endif

@implementation HomeScreenWidgetUpdaterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftHomeScreenWidgetUpdaterPlugin registerWithRegistrar:registrar];
}
@end
