package com.plink6746.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.plink6746.GameActivity;
public class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ViewTreeObserver obs = view.getViewTreeObserver();
        obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public synchronized void onGlobalLayout() {
                ViewTreeObserver viewTreeObserver = getView().getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                    onLayoutCompleted();
                }
            }
        });
    }

    protected void onLayoutCompleted() {
    }

    public GameActivity getMainActivity() {
        return (GameActivity) getActivity();
    }

    public boolean onBackPressed() {
        return false;
    }

}

