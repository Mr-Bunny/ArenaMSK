package com.example.arenamsk.utils

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.example.arenamsk.R
import com.example.arenamsk.models.PlaceItem
import com.example.arenamsk.models.PlaceModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.google.maps.android.ui.SquareTextView

class MapRenderer(
    val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<PlaceItem>,
    private val markerColorRes: Int,
    private val clusterColorRes: Int
) : DefaultClusterRenderer<PlaceItem>(context, map, clusterManager) {

    companion object {
        const val TYPEFACE = "sans-serif-black"
        const val NUMBER_PADDING = 24
        const val ALPHA_TEXT_VIEW = 0.5f
        const val ALPHA_CLUSTER_BACKGROUND = 127
    }

    lateinit var imageView: ImageView
    private lateinit var textView: SquareTextView

    //Вызывается при отрисовке маркера
    override fun onBeforeClusterItemRendered(item: PlaceItem?, markerOptions: MarkerOptions?) {
//        markerOptions?.icon(BitmapUtil.bitmapDescriptorFromVector(
//            context,
//            R.drawable.ic_map_marker,
//            markerColorRes))

        super.setMinClusterSize(1)
        super.onBeforeClusterItemRendered(item, markerOptions)
    }

    //Вызывается при отрисовке кластера, то есть когда алгоритм позволяет объединить точки в области
    override fun onBeforeClusterRendered(cluster: Cluster<PlaceItem>?, markerOptions: MarkerOptions?) {
        textView = SquareTextView(context).apply {
            text = cluster?.size.toString()
            setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            val params = LinearLayout.LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
            params.setMargins(NUMBER_PADDING, NUMBER_PADDING, NUMBER_PADDING, NUMBER_PADDING)
            layoutParams = params
        }

        ContextCompat.getDrawable(context, R.drawable.ic_map_cursor)

        val iconGenerator = IconGenerator(context)
        iconGenerator.setContentView(textView)

        iconGenerator.setBackground(context.resources.getDrawable(R.drawable.ic_menu_map))
        val icon = iconGenerator.makeIcon()
        markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }
}
