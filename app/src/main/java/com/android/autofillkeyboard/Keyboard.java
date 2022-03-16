package com.android.autofillkeyboard;

import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

/** Controls the visible virtual keyboard view. */
final class Keyboard {

    private static final int NUM_STATES = 4;
    private static final int STATE_SHIFT = 1;
    private static final int STATE_SYMBOL = 2;

    private final AutofillImeService mAutofillImeService;
    private final int mViewResId;
    private final SparseArray<String> mKeyMapping;
    private View mKeyboardView;
    private int mState;

    private Keyboard(AutofillImeService autofillImeService, int viewResId,
                     SparseArray<String> keyMapping) {
        this.mAutofillImeService = autofillImeService;
        this.mViewResId = viewResId;
        this.mKeyMapping = keyMapping;
        this.mState = 0;
    }

    private static String getLabel(String data) {
        if ("SHI".equals(data)) {
            return "↑";
        } else if ("DEL".equals(data)) {
            return "←";
        } else if ("SYM".equals(data)) {
            return "?123";
        } else if ("SPA".equals(data)) {
            return "[            ]";
        } else if ("ENT".equals(data)) {
            return "↩";
        } else {
            return data;
        }
    }

    static Keyboard qwerty(AutofillImeService autofillImeService) {
        SparseArray<String> keyMapping = new SparseArray<>();
        keyMapping.put(R.id.key_pos_0_0, "qQ1\u007E");
        keyMapping.put(R.id.key_pos_0_1, "wW2\u0060");
        keyMapping.put(R.id.key_pos_0_2, "eE3\u007C");
        keyMapping.put(R.id.key_pos_0_3, "rR4\u2022");
        keyMapping.put(R.id.key_pos_0_4, "tT5\u221A");
        keyMapping.put(R.id.key_pos_0_5, "yY6\u03C0");
        keyMapping.put(R.id.key_pos_0_6, "uU7\u00F7");
        keyMapping.put(R.id.key_pos_0_7, "iI8\u00D7");
        keyMapping.put(R.id.key_pos_0_8, "oO9\u00B6");
        keyMapping.put(R.id.key_pos_0_9, "pP0\u2206");
        keyMapping.put(R.id.key_pos_1_0, "aA@\u00A3");
        keyMapping.put(R.id.key_pos_1_1, "sS#\u00A2");
        keyMapping.put(R.id.key_pos_1_2, "dD$\u20AC");
        keyMapping.put(R.id.key_pos_1_3, "fF_\u00A5");
        keyMapping.put(R.id.key_pos_1_4, "gG&\u005E");
        keyMapping.put(R.id.key_pos_1_5, "hH-=");
        keyMapping.put(R.id.key_pos_1_6, "jJ+{");
        keyMapping.put(R.id.key_pos_1_7, "kK(}");
        keyMapping.put(R.id.key_pos_1_8, "lL)\\");
        keyMapping.put(R.id.key_pos_2_0, "zZ*%");
        keyMapping.put(R.id.key_pos_2_1, "xX\"\u00A9");
        keyMapping.put(R.id.key_pos_2_2, "cC'\u00AE");
        keyMapping.put(R.id.key_pos_2_3, "vV:\u2122");
        keyMapping.put(R.id.key_pos_2_4, "bB;\u2713");
        keyMapping.put(R.id.key_pos_2_5, "nN![");
        keyMapping.put(R.id.key_pos_2_6, "mM?]");
        keyMapping.put(R.id.key_pos_bottom_0, ",,,<");
        keyMapping.put(R.id.key_pos_bottom_1, "...>");
        keyMapping.put(R.id.key_pos_shift, "SHI");
        keyMapping.put(R.id.key_pos_del, "DEL");
        keyMapping.put(R.id.key_pos_symbol, "SYM");
        keyMapping.put(R.id.key_pos_space, "SPA");
        keyMapping.put(R.id.key_pos_enter, "ENT");
        return new Keyboard(autofillImeService, R.layout.keyboard, keyMapping);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    View inflateKeyboardView(LayoutInflater inflater, InputView inputView) {
        mKeyboardView = inflater.inflate(mViewResId, inputView, false);
        mapKeys();
        return mKeyboardView;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    void reset() {
        mState = 0;
        mapKeys();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void mapKeys() {
        for (int i = 0; i < mKeyMapping.size(); i++) {
            TextView softKey = mKeyboardView.findViewById(mKeyMapping.keyAt(i));
            String rawData = mKeyMapping.valueAt(i);
            String data = rawData.length() != NUM_STATES ? rawData : rawData.substring(mState,
                    mState + 1);
            softKey.setText(getLabel(data));
            softKey.setOnClickListener(v -> handle(data));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void handle(String data) {
        if ("SHI".equals(data)) {
            // Toggle STATE_SHIFT.
            mState = mState ^ STATE_SHIFT;
            mapKeys();
        } else if ("SYM".equals(data)) {
            // Toggle STATE_SYMBOL and clear STATE_SHIFT.
            mState = (mState ^ STATE_SYMBOL) & ~STATE_SHIFT;
            mapKeys();
        } else {
            mAutofillImeService.handle(data);
        }
    }
}

