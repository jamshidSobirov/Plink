package com.plink6746;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleHelper {
    public static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static void setLocale(Context context, String language) {
        persist(context, language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language);
            return;
        }
        updateResourcesLegacy(context, language);
    }

    public static Context updateLocale(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        persist(context, preferences.getString(SELECTED_LANGUAGE, "ru"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, preferences.getString(SELECTED_LANGUAGE, "ru"));
        }
        return updateResourcesLegacy(context, preferences.getString(SELECTED_LANGUAGE, "ru"));
    }


    public static String getLocale(Context context) {
        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        return configuration.locale.getLanguage();
    }

    private static void persist(Context context, String language) {
        SharedPreferences preferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }


    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }


    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }
}
