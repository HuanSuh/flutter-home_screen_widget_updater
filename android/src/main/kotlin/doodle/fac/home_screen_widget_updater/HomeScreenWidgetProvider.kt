package doodle.fac.home_screen_widget_updater

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import org.json.JSONObject
import java.lang.Exception

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
            requestInitialize(context)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if(context == null || intent == null) return
        when(intent.action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                AppWidgetManager.getInstance(context.applicationContext).let {
                    it.getAppWidgetIds(ComponentName(context.applicationContext, this::class.java))?.let { ids ->
                        ids.forEach {widgetId ->
                            parseData(intent)?.let { request ->
                                if(request.widgetId == null || request.widgetId == widgetId) {
                                    updateAppWidget(context, it, widgetId, request.data)
                                }
                            }
                        }
                    }
                }
            }
            HOME_SCREEN_WIDGET_UPDATE_ACTION -> {
                val targetWidgetId = if(intent.hasExtra("widgetId"))
                    intent.getIntExtra("widgetId", -1) else null
                requestUpdate(context, targetWidgetId, intent.getStringExtra("data"))
            }
        }
    }

    /**
     * Implement updateAppWidget and call appWidgetManager.updateAppWidget(appWidgetId, remoteView).
     * */
    abstract fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, updateRequest: JSONObject?)

    private fun parseData(intent: Intent): UpdateRequest? {
        intent.getStringExtra(HOME_SCREEN_WIDGET_DATA_KEY).let {
            if (!it.isNullOrEmpty()) {
                var widgetId : Int? = null
                var data: JSONObject? = null
                val jsonObj = JSONObject(it)
                try {
                    widgetId = jsonObj.getInt("widgetId")
                } catch (ignore: Exception) {}
                try {
                    data = JSONObject(jsonObj.getString("data"))
                } catch (ignore: Exception) {}
                return UpdateRequest(widgetId, data)
            }
        }
        return null
    }


    /**
     * Use setUpdateRequestListener to request update from widget
     * */
    protected fun setUpdateRequestListener(context: Context?, appWidgetId: Int?, remoteViews: RemoteViews, viewId: Int, updateRequest: UpdateRequest?) {
        val intent = Intent(context, javaClass)
        intent.action = HOME_SCREEN_WIDGET_UPDATE_ACTION
        updateRequest?.widgetId?.let{
            intent.putExtra("widgetId", it)
        }
        updateRequest?.data?.let {
            intent.putExtra("data", it.toString())
        }

        var flags: Int = PendingIntent.FLAG_UPDATE_CURRENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags = flags or PendingIntent.FLAG_IMMUTABLE
        }
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, flags)
        remoteViews.setOnClickPendingIntent(viewId, pendingIntent)
    }

    private fun requestInitialize(context: Context?) {
        context?.let {
            HomeScreenWidgetUpdaterPlugin.requestWidgetInitialize()
        }
    }
    private fun requestUpdate(context: Context?, targetWidgetId: Int?, data: String?) {
        context?.let{
            HomeScreenWidgetUpdaterPlugin.requestWidgetUpdate(
                UpdateRequest(
                    targetWidgetId,
                    if(data != null) JSONObject(data) else null
                )
            )
        }
    }
}

class UpdateRequest(val widgetId: Int?, val data: JSONObject?) {
    constructor(jsonObject: JSONObject?): this(jsonObject?.getInt("widgetId"), jsonObject?.getJSONObject("data"))

    fun serialize(): String {
        val map: HashMap<String, Any?> = HashMap()
        map["widgetId"] = widgetId
        map["data"] = data?.toString()
        return JSONObject(map).toString()
    }
}
