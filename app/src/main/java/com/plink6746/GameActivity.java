package com.plink6746;

import android.os.Bundle;
import android.view.View;

import com.plink6746.fragment.BaseFragment;
import com.plink6746.fragment.GameFragment;
import com.plink6746.sound.SoundManager;

public class GameActivity extends BaseActivity {
    private static final String TAG_FRAGMENT = "content";

    private SoundManager mSoundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (savedInstanceState == null) {
            startGame();
        }

        mSoundManager = new SoundManager(this);

    }

    public SoundManager getSoundManager() {
        return mSoundManager;
    }

    public void startGame() {
        // Get the difficulty from intent
        String difficulty = getIntent().getStringExtra("difficulty");
        
        // Create new fragment and pass difficulty
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString("difficulty", difficulty);
        fragment.setArguments(args);
        
        // Navigate to the fragment
        navigateToFragment(fragment);
    }

    public void navigateToFragment(BaseFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out)
                .replace(R.id.container, fragment, TAG_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    public void navigateBack() {
        // Do a push on the navigation history
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {
        final BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment == null || !fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

//        if (hasFocus) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
    }
}