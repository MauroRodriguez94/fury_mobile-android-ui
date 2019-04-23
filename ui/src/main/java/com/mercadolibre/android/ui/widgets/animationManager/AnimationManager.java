package com.mercadolibre.android.ui.widgets.animationManager;

import android.os.Handler;
import android.support.v4.app.DialogFragment;

import com.mercadolibre.android.ui.R;

public class AnimationManager implements AnimationEvents {
    private DialogFragment dialogFragment;
    private int animations;
    private int animationDuration;
    private static final int defaultAnimation = R.style.NoneFullscreenModalAnimation;

    public AnimationManager (DialogFragment dialogFragment, int animations, int animationDuration) {
        this.dialogFragment = dialogFragment;
        this.animations = animations;
        this.animationDuration = animationDuration;
    }

    @Override
    public void onCreateView() {
        dialogFragment.getDialog().getWindow().setWindowAnimations(animations);
    }

    @Override
    public void onResume() {
        if (dialogFragment.getDialog() != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialogFragment.getDialog().getWindow().setWindowAnimations(R.style.FullscreenModalAnimation);
                }
            }, animationDuration);
        }
    }

    @Override
    public void onStop() {
        if (dialogFragment.getDialog() != null) {
            dialogFragment.getDialog().getWindow().setWindowAnimations(defaultAnimation);
        }
    }
}
