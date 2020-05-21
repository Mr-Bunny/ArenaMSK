package com.arenabooking.arenamsk.custom_view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.ui.places.TagSelectedCallback
import kotlinx.android.synthetic.main.custom_place_tag.view.*

/** Кастомная View для отображения вида спорта */
class TagView : FrameLayout {

    /** Id вида спорта */
    private var tagId = 0
    /** Название вида спорта */
    private var tagName: String = ""
    /** Флаг - выбран вид спорта или нет */
    private var tagSelected: Boolean = false
    /** Callback нажатия на тег */
    private var tagClickListener: TagSelectedCallback? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, tagClickListener: TagSelectedCallback, id: Int, name: String) : super(context) {
        tagId = id
        tagName = name
        this.tagClickListener = tagClickListener

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

    /** Ставим флаг что тег выбран и обновляем view */
    fun setTagCheck(isCheck: Boolean = true) {
        tagSelected = isCheck
        setBorder()
    }

    fun getTagName() = tagName

    private fun init() {
        inflate(context, R.layout.custom_place_tag, null).also { addView(it) }
        place_tag.text = tagName
        place_tag.setOnClickListener {
            tagSelected = !tagSelected
            setBorder()
            tagClickListener?.tagWasSelected(tagSelected, tagName)
        }
    }

    /** Ставим цвет border-а на основе того выбран тег или нет */
    private fun setBorder() {
        with(place_tag) {
            if (tagSelected) {
                setBackgroundDrawable(resources.getDrawable(R.drawable.selected_tag_background))
                setTextColor(resources.getColor(R.color.colorWhite))
            } else {
                setBackgroundDrawable(resources.getDrawable(R.drawable.unselected_tag_background))
                setTextColor(resources.getColor(R.color.text_color_grey_light))
            }
        }
    }

}