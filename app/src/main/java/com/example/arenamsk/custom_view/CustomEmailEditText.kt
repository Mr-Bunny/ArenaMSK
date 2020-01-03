package com.example.arenamsk.custom_view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.example.arenamsk.R
import kotlinx.android.synthetic.main.custom_email_edit_text.view.*

class CustomEmailEditText: AbstractCustomEditText {

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle) {
        init()
    }

    override fun init() {
        inflate(context, R.layout.custom_email_edit_text, this)

        super.init()
    }
}