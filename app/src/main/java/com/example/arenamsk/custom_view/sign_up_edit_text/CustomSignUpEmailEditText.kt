package com.example.arenamsk.custom_view.sign_up_edit_text

import android.content.Context
import android.util.AttributeSet
import com.example.arenamsk.R
import com.example.arenamsk.custom_view.AbstractSignUpCustomEditText

class CustomSignUpEmailEditText: AbstractSignUpCustomEditText {

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
        inflate(context, R.layout.custom_sign_up_email_edit_text, this)

        super.init()
    }
}