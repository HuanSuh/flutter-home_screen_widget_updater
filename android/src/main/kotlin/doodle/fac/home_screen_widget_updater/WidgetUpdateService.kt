package doodle.fac.home_screen_widget_updater

import android.annotation.TargetApi
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.HashMap

class WidgetUpdateService(private val context: Context?) {
    @TargetApi(Build.VERSION_CODES.N)
    fun updateWidget(jsonStr : String?): Boolean {
        val intent = Intent()
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(HOME_SCREEN_WIDGET_DATA_KEY, jsonStr)
        context?.let {
            it.sendBroadcast(intent)
            return true
        } ?: run {
            return false
        }
    }
}