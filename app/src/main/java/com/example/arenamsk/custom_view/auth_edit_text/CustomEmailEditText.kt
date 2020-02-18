package com.example.arenamsk.custom_view.auth_edit_text

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
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

        custom_edit_text.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, start: Int, p2: Int, after: Int) {
                try {
                    //Если была введена цифра - добавляем +7 и передвигаем курсор в конец edit text
                    if (p0.toString().toInt() in 0..9) {
                        val formattedPhone = "+7 $p0"
                        custom_edit_text.setText(formattedPhone)
                        custom_edit_text.setSelection(4)
                    }
                } catch (e: NumberFormatException) {
                }

                //Если было удаление последней введенной цифры - убираем +7
                if (p0.toString() == "+7" && after < start) {
                    custom_edit_text.setText("")
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    override fun getText(): String {
        with(custom_edit_text) {
            return if (text.toString().startsWith("+")) {
                text.toString().substring(3)
            } else {
                text.toString()
            }
        }
    }
}