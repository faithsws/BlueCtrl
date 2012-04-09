/*
 * Copyright (C) 2012
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ronsdev.bluectrl;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import java.util.ArrayList;


/**
 * Handles Key events and redirects them to a HID Keyboard.
 */
public class KeyboardInputHandler implements OnKeyListener {

    private static final String TAG = "KeyboardInputHandler";
    private static final boolean V = false;


    private View mView;
    private HidKeyboard mKeyboard;
    private CharKeyReportMap mCharKeyMap;


    public KeyboardInputHandler(View view, HidKeyboard keyboard, DeviceSettings settings) {
        mView = view;
        mKeyboard = keyboard;

        mCharKeyMap = new CharKeyReportMap(settings.getKeyMap(), mView.getContext().getAssets());

        view.setOnKeyListener(this);
    }


    /**
     * Converts a Android key code to a HID Keyboard code.
     * Only language independent keys that doesn't create characters are handled.
     */
    private static int convertToHidKeyCode(int keyCode) {
        switch (keyCode) {
        case KeyEventFuture.KEYCODE_BREAK:
            return 72;
        case KeyEventFuture.KEYCODE_CAPS_LOCK:
            return 57;
        case KeyEvent.KEYCODE_CLEAR:
            return 156;
        case KeyEvent.KEYCODE_DEL:
            return 42;
        case KeyEvent.KEYCODE_DPAD_CENTER:
            return 40;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            return 81;
        case KeyEvent.KEYCODE_DPAD_LEFT:
            return 80;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            return 79;
        case KeyEvent.KEYCODE_DPAD_UP:
            return 82;
        case KeyEvent.KEYCODE_ENTER:
            return 40;
        case KeyEventFuture.KEYCODE_ESCAPE:
            return 41;
        case KeyEventFuture.KEYCODE_F1:
            return 58;
        case KeyEventFuture.KEYCODE_F10:
            return 67;
        case KeyEventFuture.KEYCODE_F11:
            return 68;
        case KeyEventFuture.KEYCODE_F12:
            return 69;
        case KeyEventFuture.KEYCODE_F2:
            return 59;
        case KeyEventFuture.KEYCODE_F3:
            return 60;
        case KeyEventFuture.KEYCODE_F4:
            return 61;
        case KeyEventFuture.KEYCODE_F5:
            return 62;
        case KeyEventFuture.KEYCODE_F6:
            return 63;
        case KeyEventFuture.KEYCODE_F7:
            return 64;
        case KeyEventFuture.KEYCODE_F8:
            return 65;
        case KeyEventFuture.KEYCODE_F9:
            return 66;
        case KeyEventFuture.KEYCODE_FORWARD_DEL:
            return 76;
        case KeyEventFuture.KEYCODE_INSERT:
            return 73;
        case KeyEventFuture.KEYCODE_MOVE_END:
            return 77;
        case KeyEventFuture.KEYCODE_MOVE_HOME:
            return 74;
        case KeyEventFuture.KEYCODE_NUMPAD_0:
            return 98;
        case KeyEventFuture.KEYCODE_NUMPAD_1:
            return 89;
        case KeyEventFuture.KEYCODE_NUMPAD_2:
            return 90;
        case KeyEventFuture.KEYCODE_NUMPAD_3:
            return 91;
        case KeyEventFuture.KEYCODE_NUMPAD_4:
            return 92;
        case KeyEventFuture.KEYCODE_NUMPAD_5:
            return 93;
        case KeyEventFuture.KEYCODE_NUMPAD_6:
            return 94;
        case KeyEventFuture.KEYCODE_NUMPAD_7:
            return 95;
        case KeyEventFuture.KEYCODE_NUMPAD_8:
            return 96;
        case KeyEventFuture.KEYCODE_NUMPAD_9:
            return 97;
        case KeyEventFuture.KEYCODE_NUMPAD_ADD:
            return 87;
        case KeyEventFuture.KEYCODE_NUMPAD_COMMA:
            return 133;
        case KeyEventFuture.KEYCODE_NUMPAD_DIVIDE:
            return 84;
        case KeyEventFuture.KEYCODE_NUMPAD_DOT:
            return 99;
        case KeyEventFuture.KEYCODE_NUMPAD_ENTER:
            return 88;
        case KeyEventFuture.KEYCODE_NUMPAD_EQUALS:
            return 103;
        case KeyEventFuture.KEYCODE_NUMPAD_LEFT_PAREN:
            return 182;
        case KeyEventFuture.KEYCODE_NUMPAD_MULTIPLY:
            return 85;
        case KeyEventFuture.KEYCODE_NUMPAD_RIGHT_PAREN:
            return 183;
        case KeyEventFuture.KEYCODE_NUMPAD_SUBTRACT:
            return 86;
        case KeyEventFuture.KEYCODE_NUM_LOCK:
            return 83;
        case KeyEvent.KEYCODE_PAGE_DOWN:
            return 78;
        case KeyEvent.KEYCODE_PAGE_UP:
            return 75;
        case KeyEventFuture.KEYCODE_SCROLL_LOCK:
            return 71;
        case KeyEvent.KEYCODE_SPACE:
            return 44;
        case KeyEventFuture.KEYCODE_SYSRQ:
            return 70;
        case KeyEvent.KEYCODE_TAB:
            return 43;
        case KeyEvent.KEYCODE_VOLUME_DOWN:
            return 129;
        case KeyEventFuture.KEYCODE_VOLUME_MUTE:
            return 127;
        case KeyEvent.KEYCODE_VOLUME_UP:
            return 128;
        default:
            return 0;
        }
    }

    /** Converts a Android key code to a HID Keyboard modifier. */
    private static int convertToHidModifier(int keyCode) {
        switch (keyCode) {
        case KeyEventFuture.KEYCODE_CTRL_LEFT:
            return HidKeyboard.MODIFIER_LEFT_CTRL;
        case KeyEventFuture.KEYCODE_CTRL_RIGHT:
            return HidKeyboard.MODIFIER_RIGHT_CTRL;
        case KeyEvent.KEYCODE_SHIFT_LEFT:
            return HidKeyboard.MODIFIER_LEFT_SHIFT;
        case KeyEvent.KEYCODE_SHIFT_RIGHT:
            return HidKeyboard.MODIFIER_RIGHT_SHIFT;
        case KeyEvent.KEYCODE_ALT_LEFT:
            return HidKeyboard.MODIFIER_LEFT_ALT;
        case KeyEvent.KEYCODE_ALT_RIGHT:
            return HidKeyboard.MODIFIER_RIGHT_ALT;
        case KeyEventFuture.KEYCODE_META_LEFT:
            return HidKeyboard.MODIFIER_LEFT_GUI;
        case KeyEventFuture.KEYCODE_META_RIGHT:
            return HidKeyboard.MODIFIER_RIGHT_GUI;
        default:
            return 0;
        }
    }

    /** Converts a Android key code to a HID Hardware key code. */
    private static int convertToHidHardwareKey(int keyCode) {
        switch (keyCode) {
        case KeyEventFuture.KEYCODE_MEDIA_EJECT:
            return HidKeyboard.HARDWARE_KEY_EJECT;
        default:
            return 0;
        }
    }

    /** Converts a Android key code to a HID Media key code. */
    private static int convertToHidMediaKey(int keyCode) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            return HidKeyboard.MEDIA_KEY_PLAY_PAUSE;
        case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
            return HidKeyboard.MEDIA_KEY_FORWARD;
        case KeyEvent.KEYCODE_MEDIA_REWIND:
            return HidKeyboard.MEDIA_KEY_REWIND;
        case KeyEvent.KEYCODE_MEDIA_NEXT:
            return HidKeyboard.MEDIA_KEY_SCAN_NEXT_TRACK;
        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
            return HidKeyboard.MEDIA_KEY_SCAN_PREV_TRACK;
        default:
            return 0;
        }
    }

    public boolean isActive() {
        return (mView.isShown() && mKeyboard.isConnected());
    }

    /** Converts a text to a list of Keyboard Reports. */
    private ArrayList<CharKeyReportMap.KeyReport> convertToKeyReports(String text) {
        ArrayList<CharKeyReportMap.KeyReport> result = new ArrayList<CharKeyReportMap.KeyReport>();

        for (int i = 0; i < text.length(); i++) {
            final char character = text.charAt(i);

            CharKeyReportMap.KeyReportSequence keyReportSequence = mCharKeyMap.get(character);
            if (keyReportSequence != null) {
                result.addAll(keyReportSequence);
            } else {
                Log.w(TAG, String.format("unknown Keymap character '%c'", character));
            }
        }

        return result;
    }

    /** Types a complete text with the Keyboard. */
    private void typeText(String text) {
        ArrayList<CharKeyReportMap.KeyReport> reportList = convertToKeyReports(text);

        if (reportList.size() < 1) {
            return;
        }

        int index = 0;
        CharKeyReportMap.KeyReport keyReport;
        CharKeyReportMap.KeyReport nextKeyReport = reportList.get(index);
        while (nextKeyReport != null) {
            keyReport = nextKeyReport;
            index++;
            nextKeyReport = (index < reportList.size()) ? reportList.get(index) : null;

            final int modifier = keyReport.getModifier();
            final int keyCode = keyReport.getKeyCode();

            mKeyboard.pressModifierKey(modifier);
            mKeyboard.pressKey(keyCode);
            mKeyboard.releaseKey(keyCode);

            // If the next Modifier value equals the current value then don't reset the Modifier.
            // This saves two unnecessary HID Keyboard Reports.
            if ((nextKeyReport == null) || (nextKeyReport.getModifier() != modifier)) {
                mKeyboard.releaseModifierKey(modifier);
            }
        }
    }

    private boolean onMultipleKeyEvents(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_UNKNOWN) {
            if (V) Log.v(TAG, String.format("multiple key events (Text='%s')", event.getCharacters()));

            typeText(event.getCharacters());
            return true;
        } else {
            if (V) Log.v(TAG, String.format("multiple key events (Repeat='%d')", event.getRepeatCount()));

            // Repeated keys are ignored because only key changes have to be reported
            return true;
        }
    }

    private boolean handleHardwareKey(int keyCode, KeyEvent event) {
        final int hardwareKey = convertToHidHardwareKey(keyCode);

        if (hardwareKey != 0) {
            switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                mKeyboard.pressHardwareKey(hardwareKey);
                return true;
            case KeyEvent.ACTION_UP:
                mKeyboard.releaseHardwareKey(hardwareKey);
                return true;
            }
        }

        return false;
    }

    private boolean handleMediaKey(int keyCode, KeyEvent event) {
        final int mediaKey = convertToHidMediaKey(keyCode);

        if (mediaKey != 0) {
            switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                mKeyboard.pressMediaKey(mediaKey);
                return true;
            case KeyEvent.ACTION_UP:
                mKeyboard.releaseMediaKey(mediaKey);
                return true;
            }
        }

        return false;
    }

    private boolean onSingleKeyEvent(int keyCode, KeyEvent event) {
        final int keyChar = event.getUnicodeChar(0);

        if (V) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                Log.v(TAG, String.format("Key Down (Key=%d   Char=%c)", keyCode, keyChar));
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                Log.v(TAG, String.format("Key Up (Key=%d   Char=%c)", keyCode, keyChar));
            }
        }

        if (handleHardwareKey(keyCode, event) || handleMediaKey(keyCode, event)) {
            return true;
        }

        int hidModifier = convertToHidModifier(keyCode);
        int hidKeyCode = convertToHidKeyCode(keyCode);

        // Try to find a compatible Keyboard Report in the CharKeyReportMap if the key code
        // couldn't be converted
        if ((hidModifier == 0) && (hidKeyCode == 0) && (keyChar != 0)) {
            CharKeyReportMap.KeyReportSequence keyReportSequence = mCharKeyMap.get((char)keyChar);
            if ((keyReportSequence != null) && (keyReportSequence.size() == 1)) {
                hidModifier = keyReportSequence.get(0).getModifier();
                hidKeyCode = keyReportSequence.get(0).getKeyCode();
            }
        }

        boolean handled = false;

        switch (event.getAction()) {
        case KeyEvent.ACTION_DOWN:
            if (hidModifier != 0) {
                mKeyboard.pressModifierKey(hidModifier);
                handled = true;
            }
            if (hidKeyCode != 0) {
                mKeyboard.pressKey(hidKeyCode);
                handled = true;
            }
            break;
        case KeyEvent.ACTION_UP:
            if (hidKeyCode != 0) {
                mKeyboard.releaseKey(hidKeyCode);
                handled = true;
            }
            if (hidModifier != 0) {
                mKeyboard.releaseModifierKey(hidModifier);
                handled = true;
            }
            break;
        }

        return handled;
    }

    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (!isActive()) {
            return false;
        }

        switch (event.getAction()) {
        case KeyEvent.ACTION_MULTIPLE:
            return onMultipleKeyEvents(event);
        case KeyEvent.ACTION_DOWN:
        case KeyEvent.ACTION_UP:
            return onSingleKeyEvent(keyCode, event);
        }

        return false;
    }
}