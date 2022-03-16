package com.android.autofillkeyboard;


import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;

/**
 * Decodes key data and applies changes to {@link InputConnection}.
 */
final class Decoder {

    private final InputConnection mInputConnection;

    Decoder(InputConnection inputConnection) {
        this.mInputConnection = inputConnection;
    }

    void decodeAndApply(String data) {
        if ("DEL".equals(data)) {
            mInputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else if ("ENT".equals(data)) {
            mInputConnection.sendKeyEvent(
                    new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
        } else if ("SPA".equals(data)) {
            mInputConnection.sendKeyEvent(
                    new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE));
        } else {
            mInputConnection.commitText(data, 1);
        }
    }

    boolean isEmpty() {
        if (mInputConnection.getTextBeforeCursor(1, 0).length() == 0
                && mInputConnection.getTextAfterCursor(1, 0).length() == 0) {
            return true;
        }
        return false;
    }
}
