package com.mercadolibre.android.ui.widgets;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mercadolibre.android.ui.R;

/**
 * Created by Mauro Rodriguez on 02/04/2019
 */
public abstract class FullScreenModal extends DialogFragment {
    private View root;
    private ViewGroup contentContainer;
    /* default */ Button secondaryExitButton;
    /* default */ View closeButton;
    private final static String EMPTY = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenModal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final View onCreateView(@NonNull final LayoutInflater inflater,
                                   @Nullable final ViewGroup container,
                                   @Nullable final Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.ui_layout_fullscreenmodal, container, false);
        contentContainer = root.findViewById(R.id.ui_fullscreenmodal_content_container);
        setupView();
        if (shouldAnimate()) {
            getDialog().getWindow().getAttributes().windowAnimations = R.style.FullscreenModalAnimation;
        }

        return root;
    }

    /**
     * Override to set the content view.
     *
     * @return An integer representing a layout id.
     */
    @LayoutRes
    public abstract int getContentView();

    /**
     * Override to set a title.
     *
     * @return A string to be set as a title, {@code null} if no title is set.
     */
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
    @Nullable
    public String getTitle() {
        return null;
    }

    /**
     * Override to set the secondary exit string.
     * The secondary exit button won't be visible unless you override {@link FullScreenModal#getSecondaryExitClickListener()} too.
     *
     * @return A string to be set to the secondary exit button.
     */
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
    @Nullable
    public String getSecondaryExitString() {
        return null;
    }

    /**
     * Override to set the secondary exit's click listener.
     * The secondary exit button won't be visible unless you override {@link FullScreenModal#getSecondaryExitString()} too.
     *
     * @return The secondary exit's OnClickListener.
     */
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
    @Nullable
    public View.OnClickListener getSecondaryExitClickListener() {
        return null;
    }

    /**
     * Override to set a OnDismissListener.
     * If set, this listener will be called every time the dialog is dismissed.
     *
     * @return An OnClickListener to be called when the dialog is dismissed.
     */
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
    @Nullable
    public View.OnClickListener getOnDismissListener() {
        return null;
    }

    /**
     * Override to avoid the dialog from animating when showed and dismissed.
     * Dialogs are animated by default.
     *
     * @return {@code true} if the dialog should be animated, {@code false} otherwise.
     */
    public boolean shouldAnimate() {
        return true;
    }

    /**
     * Sets the view up.
     */
    private void setupView() {
        setUpToolbar();
        setupAnimationOnBackPressed();
        setupSecondaryExitButton();
        setupContentView();
    }

    /**
     * Sets the toolbar.
     */
    protected void setUpToolbar() {
        Toolbar toolbar = root.findViewById(R.id.ui_fullscreenmodal_toolbar);
        toolbar.setTitle(EMPTY);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ui_ic_clear_fullscreen);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View.OnClickListener dismissListener = getOnDismissListener();
                if (dismissListener != null) {
                    dismissListener.onClick(view);
                }
                dismiss();
            }
        });

        toolbar.setTitle(getTitle());

        final AppBarLayout appBar = root.findViewById(R.id.ui_fullscreenmodal_appbar);
        final View shadow = root.findViewById(R.id.ui_fullscreenmodal_toolbar_shadow);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
                if (Math.abs(offset) == appBarLayout.getTotalScrollRange()) {
                    shadow.setVisibility(View.VISIBLE);
                } else {
                    shadow.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Sets the content view up.
     */
    private void setupContentView() {
        final int contentView = getContentView();
        if (contentView <= 0) {
            return;
        }
        final View content = LayoutInflater.from(getActivity()).inflate(contentView, contentContainer, false);
        contentContainer.addView(content);
    }

    /**
     * Sets the secondary exit button up.
     */
    private void setupSecondaryExitButton() {
        if (!shouldShowSecondaryExit()) {
            return;
        }

        secondaryExitButton = root.findViewById(R.id.ui_fullscreenmodal_secondary_exit_button);
        secondaryExitButton.setText(getSecondaryExitString());
        secondaryExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (getSecondaryExitClickListener() != null) {
                    getSecondaryExitClickListener().onClick(v);
                    dismiss();
                }
            }
        });
        secondaryExitButton.setVisibility(View.VISIBLE);
    }

    /**
     * Whether or not secondary exit button should be shown.
     *
     * @return {@code true} if it should be show, {@code false} otherwise.
     */
    /* default */ boolean shouldShowSecondaryExit() {
        return !TextUtils.isEmpty(getSecondaryExitString()) && getSecondaryExitClickListener() != null;
    }

    /**
     * Sets custom behaviour for the back button to play the out animation when the dialog is dismissed.
     */
    private void setupAnimationOnBackPressed() {
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(final DialogInterface dialog, final int keyCode, final KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (getOnDismissListener() != null) {
                        getOnDismissListener().onClick(getView());
                    }
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public String toString() {
        return "FullScreenModal{"
                + "root=" + root
                + ", contentContainer=" + contentContainer
                + ", secondaryExitButton=" + secondaryExitButton
                + ", closeButton=" + closeButton
                + '}';
    }
}
