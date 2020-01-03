package com.example.arenamsk.custom_view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.arenamsk.R

class CustomEmailEditText: LinearLayout {

    companion object {
        enum class State {
            EMPTY,
            FILLED,
            ERROR
        }
    }

    var currentState: State = State.EMPTY
        set(value) {
            field = value
            updateView()
        }

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        inflate(context, R.layout.custom_edit_text, this)
    }

    private fun updateView() {
        when(currentState) {
            State.EMPTY -> {

            }

            State.FILLED -> {

            }

            State.ERROR -> {

            }
        }
    }
}