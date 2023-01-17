package com.BibaSpirt;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

public class SettingsActivity extends BaseActivity {

    ImageView btnOn;
    ImageView btnOff;
    ImageView btnEng;
    ImageView btnRu;
    Boolean musicOn = true;
    String currentLang = "en";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
    }

    private void initViews() {
        currentLang = sharedPreferences.getString(LocaleHelper.SELECTED_LANGUAGE, "en");
        Log.d("@@@", "initViews: " + currentLang);

        btnOn = findViewById(R.id.btnOn);
        btnOff = findViewById(R.id.btnOff);
        btnEng = findViewById(R.id.btnEng);
        btnRu = findViewById(R.id.btnRu);

        if (currentLang.equals("en")) {
            btnEng.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_on, null));
            btnRu.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_off, null));
        } else {
            btnEng.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_off, null));
            btnRu.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_on, null));
        }

        if (isMyServiceRunning(BackgroundSoundService.class)) {
            btnOn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_on, null));
            btnOff.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_off, null));
        } else {
            btnOn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_off, null));
            btnOff.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_on, null));
        }

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound();
                btnOn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_on, null));
                btnOff.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_off, null));
                startService(svc);
                Log.d("@@@", "onClick: " + isMyServiceRunning(BackgroundSoundService.class));

            }

        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound();
                btnOn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_off, null));
                btnOff.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_on, null));
                stopService(svc);
                Log.d("@@@", "onClick: " + isMyServiceRunning(BackgroundSoundService.class));

            }

        });

        btnEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentLang.equals("en")) {
                    playSound();
                    btnEng.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_on, null));
                    btnRu.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_off, null));
                    LocaleHelper.setLocale(SettingsActivity.this, "en");
                    Intent intent = new Intent();
                    intent.putExtra("langChanged", true);
                    setResult(RESULT_OK, intent);
                    finish();

                }
            }
        });

        btnRu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentLang.equals("ru")) {
                    playSound();
                    btnEng.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_off, null));
                    btnRu.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.btn_on, null));
                    LocaleHelper.setLocale(SettingsActivity.this, "ru");
                    Intent intent = new Intent();
                    intent.putExtra("langChanged", true);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }


    public void returnToMenu(View view) {
        playSound();
        finish();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}