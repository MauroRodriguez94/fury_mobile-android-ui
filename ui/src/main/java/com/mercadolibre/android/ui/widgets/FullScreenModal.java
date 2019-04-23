package com.mercadolibre.android.ui.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.mercadolibre.android.ui.widgets.animationManager.DialogDialogAnimationManager;

/**
 * Base class for Meradolibre's full screen modals
 *
 * You can use this class to create full screen modals with a expansible toolbar, and set you custom content
 * @since 02/04/2019
 */
public abstract class FullScreenModal extends DialogFragment {
    /**
     * contentContainer should have one child at most
     */
    private ViewGroup contentContainer;
    /* default */ Button secondaryExitButton;
    /* default */ View closeButton;
    private DialogDialogAnimationManager dialogAnimationManager;
    private final static String EMPTY_TITLE = "";

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
        View root = inflater.inflate(R.layout.ui_layout_fullscreenmodal, container, false);
        contentContainer = root.findViewById(R.id.ui_fullscreenmodal_content_container);
        dialogAnimationManager = new DialogDialogAnimationManager(this, R.style.FullscreenModalAnimation, getContext().getResources().getInteger(R.integer.ui_anim_time));
        setupView(root);
        if (shouldAnimate()) {
            dialogAnimationManager.onCreateView();
        }

        return root;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (shouldAnimate()) {
            dialogAnimationManager.onStop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (shouldAnimate()) {
            dialogAnimationManager.onResume();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof AppCompatActivity)) {
            throw new IllegalArgumentException("Context must extend from AppCompatActivity");
        }
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
        return EMPTY_TITLE;
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
    private void setupView(View root) {
        setUpToolbar(root);
        setupAnimationOnBackPressed();
        setupSecondaryExitButton(root);
        setupContentView();
    }

    /**
     * Sets the toolbar.
     */
    protected void setUpToolbar(View root) {
        if (!(getActivity() instanceof AppCompatActivity)) {
            throw new IllegalArgumentException("Context must extend from AppCompatActivity");
        }

        Toolbar toolbar = root.findViewById(R.id.ui_fullscreenmodal_toolbar);
        toolbar.setTitle(EMPTY_TITLE);

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
    }

    /**
     * Sets the content view up.
     */
    private void setupContentView() {
        final int contentView = getContentView();
        if (contentView <= 0 || contentContainer.getChildCount() > 0) {
            return;
        }
        final View content = LayoutInflater.from(getActivity()).inflate(contentView, contentContainer, false);
        contentContainer.addView(content);
    }

    /**
     * Sets the secondary exit button up.
     */
    private void setupSecondaryExitButton(View root) {
        secondaryExitButton = root.findViewById(R.id.ui_fullscreenmodal_secondary_exit_button);

        if (!shouldShowSecondaryExit()) {
            secondaryExitButton.setVisibility(View.GONE);
            return;
        }

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
                + ", contentContainer=" + contentContainer
                + ", secondaryExitButton=" + secondaryExitButton
                + ", closeButton=" + closeButton
                + '}';
    }
}
