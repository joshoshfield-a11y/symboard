package com.symboard.ime

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView

/**
 * Symboard — a symbol-swipe keyboard.
 *
 * 36 symbol layouts (generated res/xml/sym_*.xml) are cycled with the
 * ◀ / ▶ keys on the bottom row of every layout; the current category is
 * shown in the label strip above the keyboard. Every symbol key uses
 * android:keyOutputText, which routes through onText() and is committed
 * verbatim to the target field.
 */
class SymboardService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var categoryLabel: TextView
    private lateinit var pages: List<Keyboard>
    private var pageIndex = 0

    override fun onCreate() {
        super.onCreate()
        pages = PAGE_RESOURCES.map { Keyboard(this, it) }
    }

    override fun onCreateInputView(): View {
        val root = LayoutInflater.from(this).inflate(R.layout.keyboard_view, null)
        categoryLabel = root.findViewById(R.id.category_label)
        keyboardView = root.findViewById(R.id.keyboard_view)
        keyboardView.keyboard = pages[pageIndex]
        categoryLabel.text = PAGE_NAMES[pageIndex]
        keyboardView.setOnKeyboardActionListener(this)
        return root
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val ic = currentInputConnection ?: return
        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> ic.deleteSurroundingText(1, 0)
            Keyboard.KEYCODE_DONE -> {
                val ei = currentInputEditorInfo
                val action = ei?.imeOptions?.and(EditorInfo.IME_MASK_ACTION)
                    ?: EditorInfo.IME_ACTION_NONE
                if (action != EditorInfo.IME_ACTION_NONE) ic.performEditorAction(action)
                else ic.commitText("\n", 1)
            }
            KEYCODE_NEXT -> showPage((pageIndex + 1) % pages.size)
            KEYCODE_PREV -> showPage((pageIndex - 1 + pages.size) % pages.size)
            else -> ic.commitText(primaryCode.toChar().toString(), 1)
        }
    }

    override fun onText(text: CharSequence?) {
        if (!text.isNullOrEmpty()) {
            currentInputConnection?.commitText(text, 1)
        }
    }

    private fun showPage(i: Int) {
        pageIndex = i
        keyboardView.keyboard = pages[i]
        categoryLabel.text = PAGE_NAMES[i]
        keyboardView.invalidateAllKeys()
    }

    override fun onPress(primaryCode: Int) {}
    override fun onRelease(primaryCode: Int) {}
    override fun onSwipeLeft() {}
    override fun onSwipeRight() {}

    companion object {
        private const val KEYCODE_PREV = -201
        private const val KEYCODE_NEXT = -202

        private val PAGE_RESOURCES = listOf(
        R.xml.sym_01,
        R.xml.sym_02,
        R.xml.sym_03,
        R.xml.sym_04,
        R.xml.sym_05,
        R.xml.sym_06,
        R.xml.sym_07,
        R.xml.sym_08,
        R.xml.sym_09,
        R.xml.sym_10,
        R.xml.sym_11,
        R.xml.sym_12,
        R.xml.sym_13,
        R.xml.sym_14,
        R.xml.sym_15,
        R.xml.sym_16,
        R.xml.sym_17,
        R.xml.sym_18,
        R.xml.sym_19,
        R.xml.sym_20,
        R.xml.sym_21,
        R.xml.sym_22,
        R.xml.sym_23,
        R.xml.sym_24,
        R.xml.sym_25,
        R.xml.sym_26,
        R.xml.sym_27,
        R.xml.sym_28,
        R.xml.sym_29,
        R.xml.sym_30,
        R.xml.sym_31,
        R.xml.sym_32,
        R.xml.sym_33,
        R.xml.sym_34,
        R.xml.sym_35,
        R.xml.sym_36
        )

        private val PAGE_NAMES = listOf(
        "Bold",
        "Bold Italic",
        "Italic",
        "Script",
        "Bold Script",
        "Fraktur",
        "Bold Fraktur",
        "Double-Struck",
        "Monospace",
        "Sans",
        "Sans Bold",
        "Sans Italic",
        "Sans Bold Italic",
        "Circled",
        "Parenthesized",
        "Negative Circled",
        "Squared",
        "Negative Squared",
        "Fullwidth",
        "Upside Down",
        "Small Caps",
        "Superscript",
        "Subscript",
        "Combining Deco",
        "Arrows",
        "Math",
        "Greek",
        "Box Drawing",
        "Shapes",
        "Cards & Chess",
        "Music & Sky",
        "Currency & Legal",
        "Punctuation",
        "Zodiac",
        "Fancy Numbers",
        "Roman & Fractions"
        )
    }
}
