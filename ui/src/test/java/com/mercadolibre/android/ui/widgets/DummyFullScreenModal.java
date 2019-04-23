package com.mercadolibre.android.ui.widgets;

import android.content.Context;
import android.view.View;

import com.mercadolibre.android.ui.R;

public class DummyFullScreenModal extends FullScreenModal {
    public static final String TITLE = "Title";
    public static final String SECONDARY_EXIT = "Secondary exit";

    public static final View.OnClickListener LISTENER = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            // Do nothing;
        }
    };

    @Override
    public int getContentView() {
        return R.layout.ui_layout_melidialog_test;
    }

    @Override
    public void onAttach(Context context) {
        try {
            super.onAttach(context);
        } catch (Exception e) {
            //do nothing
        }
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public String getSecondaryExitString() {
        return SECONDARY_EXIT;
    }

    @Override
    protected void setUpToolbar(View root) { /*Do nothing because AppCompatActivity its not supported by Robolectric Shadow's*/ }

    @Override
    public View.OnClickListener getSecondaryExitClickListener() {
        return LISTENER;
    }
}
