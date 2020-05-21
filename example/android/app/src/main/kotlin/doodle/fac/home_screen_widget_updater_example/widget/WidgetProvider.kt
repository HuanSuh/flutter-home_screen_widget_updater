package doodle.fac.home_screen_widget_updater_example.widget

import android.app.PendingIntent
import android.widget.RemoteViews
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import doodle.fac.home_screen_widget_updater.HomeScreenWidgetProvider
import doodle.fac.home_screen_widget_updater.UpdateRequest
import doodle.fac.home_screen_widget_updater_example.MainActivity
import doodle.fac.home_screen_widget_updater_example.R
import org.json.JSONObject
import java.util.*

class WidgetProvider: HomeScreenWidgetProvider() {

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        if(context == null || appWidgetManager == null || appWidgetIds == null) return
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val remoteView = RemoteViews(context.packageName, R.layout.homescreen_widget)
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        remoteView.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)
//        setUpdateRequestListener(context, appWidgetId, remoteView, R.id.widget_btn_request_update,
//            UpdateRequest(appWidgetId, null)
//        );
        appWidgetManager.updateAppWidget(appWidgetId, remoteView)
    }

    override fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, updateRequest: JSONObject?) {
        val remoteView = RemoteViews(context.packageName, R.layout.homescreen_widget)
        val pendingIntent = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0)
        remoteView.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)
        remoteView.setTextViewText(R.id.widget_text_value, updateRequest?.get("data")?.toString() ?: "")
        Calendar.getInstance().let {
            remoteView.setTextViewText(R.id.widget_text_time,
                    "${it.get(Calendar.YEAR)}." +
                            "${it.get(Calendar.MONTH)}." +
                            "${it.get(Calendar.DATE)} " +
                            "${it.get(Calendar.HOUR_OF_DAY)}:" +
                            "${it.get(Calendar.MINUTE)}:" +
                            "${it.get(Calendar.SECOND)}"
            );
        }
        appWidgetManager.updateAppWidget(appWidgetId, remoteView)
    }

}