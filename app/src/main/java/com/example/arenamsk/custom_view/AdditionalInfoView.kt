package com.example.arenamsk.custom_view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.arenamsk.R
import kotlinx.android.synthetic.main.place_additional_info_item.view.*

class AdditionalInfoView : FrameLayout {

    private var stringTypeId = 0
    private var iconId = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, stringTypeId: Int, iconId: Int) : super(context) {
        this.stringTypeId = stringTypeId
        this.iconId = iconId

        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        inflate(context, R.layout.place_additional_info_item, null).also { addView(it) }
        place_additional_item_text_view.text = resources.getString(stringTypeId)
        place_additional_item_image.setImageResource(iconId)
    }

}