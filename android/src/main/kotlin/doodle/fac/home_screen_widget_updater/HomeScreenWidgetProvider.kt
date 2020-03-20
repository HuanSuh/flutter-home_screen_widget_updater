package doodle.fac.home_screen_widget_updater

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import org.json.JSONObject
import java.io.Serializable

const val HOME_SCREEN_WIDGET_DATA_KEY: String = "HOME_SCREEN_WIDGET_DATA"
abstract class HomeScreenWidgetProvider: AppWidgetProvider() {
    companion object {
        private const val HOME_SCREEN_WIDGET_UPDATE_ACTION: String = "doodle.fac.home_screen_widget_updater.action.update"
    }

    private var initialized: Boolean = false

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        if(!initialized) {
            initialized = true
            Log.d("HomeScreenWidget", "Initialize")
            requestUpdate(context, null, "INIT")
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if(context == null || intent == null) return
        when(intent.action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                AppWidgetManager.getInstance(context.applicationContext).let {
                    val appWidgetIds = it.getAppWidgetIds(ComponentName(context.applicationContext, this::class.java))
                    if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
                        for (appWidgetId in appWidgetIds) {
                            updateAppWidget(context, it, appWidgetId, parseData(intent))
                        }
                    }
                }
            }
            HOME_SCREEN_WIDGET_UPDATE_ACTION -> {
                requestUpdate(context, intent.getStringExtra("data"), "UPDATE")
            }
        }
    }

    abstract fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, jsonObject: JSONObject?)
    private fun parseData(intent: Intent): JSONObject? {
        intent.getStringExtra(HOME_SCREEN_WIDGET_DATA_KEY).let {
            if (!it.isNullOrEmpty()) return JSONObject(it)
        }
        return null
    }

    protected fun setUpdateRequestListener(context: Context?, remoteViews: RemoteViews, viewId: Int) {
        setUpdateRequestListener(context, remoteViews, viewId, null)
    }
    protected fun setUpdateRequestListener(context: Context?, remoteViews: RemoteViews, viewId: Int, data: JSONObject?) {
        val intent = Intent(context, javaClass)
        intent.action = HOME_SCREEN_WIDGET_UPDATE_ACTION
        if(data != null) {
            intent.putExtra("data", data.toString())
        }
        val pendingIntent = PendingIntent.getBroadcast(context, viewId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        remoteViews.setOnClickPendingIntent(viewId, pendingIntent)
    }

    private fun requestUpdate(context: Context?, data: String?, type: String) {
        context?.let{
            val map = HashMap<String, Any?>()
            map["type"] = type
            map["data"] = if(data != null) JSONObject(data).toString() else null
            HomeScreenWidgetUpdaterPlugin.requestWidgetUpdate(JSONObject(map).toString())
        }
    }
}