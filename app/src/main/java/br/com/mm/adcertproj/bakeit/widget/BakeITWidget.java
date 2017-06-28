package br.com.mm.adcertproj.bakeit.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.helpers.StringHelper;
import br.com.mm.adcertproj.bakeit.model.Ingredient;
import br.com.mm.adcertproj.bakeit.model.Recipe;

import static br.com.mm.adcertproj.bakeit.provider.BakeITContentProvider.buildRecipeIdContentUri;

/**
 * Implementation of App Widget functionality.
 */
public class BakeITWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        final int recipeId =
                context.getSharedPreferences("br.com.mm.adcertproj.bakeit.shared", Context.MODE_PRIVATE)
                        .getInt("br.com.mm.adcertproj.bakeit.shared.recipe", -1);
        Recipe[] recipes = recipeId == -1 ? null :
                Recipe.listFromCursor(context, context.getContentResolver()
                        .query(buildRecipeIdContentUri(recipeId), null, null, null, null));

        String widgetText = context.getString(R.string.appwidget_text);

        if(recipes != null && recipes.length > 0) {
            widgetText = recipes[0].getName();
            for(Ingredient ingredient : recipes[0].getIngredients()) {
                if(!StringHelper.isNullOrEmpty(widgetText)) {
                    widgetText += "\n";
                }
                widgetText += StringHelper.buildIngredientString(context, ingredient);
            }
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_itwidget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

