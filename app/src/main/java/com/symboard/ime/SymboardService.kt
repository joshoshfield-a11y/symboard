package com.symboard.ime

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ScrollView
import android.widget.TextView

/**
 * Symboard — a symbol-swipe keyboard, v1.1.
 *
 * Each layout is a pure grid of symbol keys inside a vertically scrolling
 * viewport, so a page can hold hundreds of symbols without growing the IME
 * window. The navigation bar (◀ ▶ ⌫ ↵ space) is a permanent button bar
 * below the scroll area — it can never be scrolled off screen.
 *
 * Page cycling: nav buttons, or swipe left/right across the keyboard.
 * The current category is shown in the label strip above the grid.
 */
class SymboardService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var categoryLabel: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var pages: List<Keyboard>
    private var pageIndex = 0

    override fun onCreate() {
        super.onCreate()
        pages = PAGE_RESOURCES.map { Keyboard(this, it) }
    }

    override fun onCreateInputView(): View {
        val root = LayoutInflater.from(this).inflate(R.layout.keyboard_view, null)
        categoryLabel = root.findViewById(R.id.category_label)
        scrollView = root.findViewById(R.id.keyboard_scroll)
        keyboardView = root.findViewById(R.id.keyboard_view)
        keyboardView.keyboard = pages[pageIndex]
        categoryLabel.text = PAGE_NAMES[pageIndex]
        keyboardView.setOnKeyboardActionListener(this)

        root.findViewById<View>(R.id.nav_prev).setOnClickListener {
            showPage((pageIndex - 1 + pages.size) % pages.size)
        }
        root.findViewById<View>(R.id.nav_next).setOnClickListener {
            showPage((pageIndex + 1) % pages.size)
        }
        root.findViewById<View>(R.id.nav_space).setOnClickListener {
            currentInputConnection?.commitText(" ", 1)
        }
        root.findViewById<View>(R.id.nav_delete).setOnClickListener {
            currentInputConnection?.deleteSurroundingText(1, 0)
        }
        root.findViewById<View>(R.id.nav_enter).setOnClickListener {
            val ic = currentInputConnection ?: return@setOnClickListener
            val ei = currentInputEditorInfo
            val action = ei?.imeOptions?.and(EditorInfo.IME_MASK_ACTION)
                ?: EditorInfo.IME_ACTION_NONE
            if (action != EditorInfo.IME_ACTION_NONE) ic.performEditorAction(action)
            else ic.commitText("\n", 1)
        }
        return root
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        // Pages contain only keyOutputText symbol keys (routed to onText);
        // kept defensively for any plain-code key.
        currentInputConnection?.commitText(primaryCode.toChar().toString(), 1)
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
        scrollView.scrollTo(0, 0)
        keyboardView.invalidateAllKeys()
    }

    override fun swipeLeft() {
        showPage((pageIndex - 1 + pages.size) % pages.size)
    }

    override fun swipeRight() {
        showPage((pageIndex + 1) % pages.size)
    }

    override fun swipeUp() {}
    override fun swipeDown() {}
    override fun onPress(primaryCode: Int) {}
    override fun onRelease(primaryCode: Int) {}

    companion object {
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
