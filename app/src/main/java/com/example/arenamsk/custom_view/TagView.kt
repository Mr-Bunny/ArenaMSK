package com.example.arenamsk.custom_view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import com.example.arenamsk.R
import com.example.arenamsk.ui.places.TagSelectedCallback
import kotlinx.android.synthetic.main.custom_place_tag.view.*

class TagView : FrameLayout {

    private var tagId = 0
    private var tagName: String = ""
    private var tagSelected: Boolean = false
    private var tagClickListener: TagSelectedCallback? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, id: Int, name: String) : super(context) {
        tagId = id
        tagName = name

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

    fun setTagClickListener(tagWasClicked: TagSelectedCallback) {
        tagClickListener = tagWasClicked
    }

    private fun init() {
        inflate(context, R.layout.custom_place_tag, null).also { addView(it) }
        place_tag.text = tagName
        place_tag.setOnClickListener {
            tagSelected = !tagSelected
            place_tag.setBackgroundDrawable(resources.getDrawable(if (tagSelected) R.drawable.selected_tag_background else R.drawable.unselected_tag_background))
            tagClickListener?.tagWasSelected(tagSelected, id)
        }
    }

}