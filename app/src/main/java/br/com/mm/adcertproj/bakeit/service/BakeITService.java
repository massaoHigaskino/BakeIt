package br.com.mm.adcertproj.bakeit.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import br.com.mm.adcertproj.bakeit.BuildConfig;
import br.com.mm.adcertproj.bakeit.widget.BakeITWidget;

public class BakeITService extends IntentService {
    public BakeITService() {
        super(BakeITService.class.getName());
    }

    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, BakeITService.class);
        intent.setAction(BuildConfig.ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null) {
            final String action = intent.getAction();
            if(BuildConfig.ACTION_UPDATE_WIDGET.equals(action)) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakeITWidget.class));
                //Now update all widgets
                BakeITWidget.updateAppWidget(this, appWidgetManager, appWidgetIds);
            }
        }
    }
}
