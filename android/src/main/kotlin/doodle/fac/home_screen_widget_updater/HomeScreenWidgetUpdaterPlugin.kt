package doodle.fac.home_screen_widget_updater

import android.content.Context
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.HashMap

/** HomeScreenWidgetUpdaterPlugin */
public class HomeScreenWidgetUpdaterPlugin: FlutterPlugin, MethodCallHandler {

  private var context: Context? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    HomeScreenWidgetUpdaterPlugin().let {
      channel = MethodChannel(flutterPluginBinding.binaryMessenger, "home_screen_widget_updater")
      it.context = flutterPluginBinding.applicationContext
      channel?.setMethodCallHandler(it)
    }
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  companion object {
    private var channel: MethodChannel? = null

    @JvmStatic
    fun registerWith(registrar: Registrar) {
      HomeScreenWidgetUpdaterPlugin().let{
        it.context = registrar.context()
        channel = MethodChannel(registrar.messenger(), "home_screen_widget_updater")
        channel?.setMethodCallHandler(it)
      }
    }

    fun requestWidgetUpdate(args: String?) {
      channel?.invokeMethod("requestWidgetUpdate", args)
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    context = null
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when(call.method) {
      "updateHomeScreenWidget" -> {
        result.success(WidgetUpdateService(context).updateWidget(call.arguments?.toString()))
      }
      else -> result.notImplemented()
    }
  }
}
