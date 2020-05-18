package com.example.arenamsk.ui.profile

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.arenamsk.R
import com.example.arenamsk.datasources.LocalDataSource
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.feedback.FeedbackDialogFragment
import com.example.arenamsk.utils.disable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Экран профиля пользователя */
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private var feedbackFragment: FeedbackDialogFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        acception_text.movementMethod = LinkMovementMethod.getInstance()

        //Setup name
        CoroutineScope(Dispatchers.IO).launch {
            LocalDataSource.getUserData()?.let {
                withContext(Dispatchers.Main) {
                    if (it.imageUrl?.isNotEmpty() == true && profile_avatar != null) {
                        Picasso.get()
                            .load(it.imageUrl)
                            .error(R.drawable.image_placeholder)
                            .placeholder(R.drawable.image_placeholder)
                            .into(profile_avatar)
                    }

                    profile_user_name.text = it.firstName
                }
            }
        }

        if (AuthUtils.isUserDefault()) {
            profile_item_booked.disable()
            profile_item_edit.disable()
        }
        profile_item_edit.setOnClickListener { openEditProfileFragment() }
        profile_item_edit.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    profile_item_edit.setTextColor(resources.getColor(R.color.colorWhite))
                    true
                }
                MotionEvent.ACTION_UP -> {
                    profile_item_edit.setTextColor(resources.getColor(R.color.text_color_grey))
                    view.callOnClick()
                    true
                }
            }
            false
        }

        profile_item_exit.setOnClickListener { exitFromProfile() }
        profile_item_exit.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    profile_item_exit.setTextColor(resources.getColor(R.color.colorWhite))
                    true
                }
                MotionEvent.ACTION_UP -> {
                    profile_item_exit.setTextColor(resources.getColor(R.color.text_color_grey))
                    view.callOnClick()
                    true
                }
            }
            false
        }

        profile_item_settings.setOnClickListener { openSettingsFragment() }
        profile_item_settings.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    profile_item_settings.setTextColor(resources.getColor(R.color.colorWhite))
                    true
                }
                MotionEvent.ACTION_UP -> {
                    profile_item_settings.setTextColor(resources.getColor(R.color.text_color_grey))
                    view.callOnClick()
                    true
                }
            }
            false
        }

        profile_item_booked.setOnClickListener { openBookedFragment() }
        profile_item_booked.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    profile_item_booked.setTextColor(resources.getColor(R.color.colorWhite))
                    true
                }
                MotionEvent.ACTION_UP -> {
                    profile_item_booked.setTextColor(resources.getColor(R.color.text_color_grey))
                    view.callOnClick()
                    true
                }
            }
            false
        }

        profile_item_contact.setOnClickListener { openFeedbackScreen() }
        profile_item_contact.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    profile_item_contact.setTextColor(resources.getColor(R.color.colorWhite))
                    true
                }
                MotionEvent.ACTION_UP -> {
                    profile_item_contact.setTextColor(resources.getColor(R.color.text_color_grey))
                    view.callOnClick()
                    true
                }
            }
            false
        }
    }

    /** Открываем фрагмент с историей бронирования */
    private fun openBookedFragment() {
        findNavController().navigate(R.id.navigation_booked)
    }

    /** Открываем фрагмент редактирования профиля */
    private fun openEditProfileFragment() {
        findNavController().navigate(R.id.navigation_edit_profile)
    }

    /** Открываем фрагмент настроек */
    private fun openSettingsFragment() {
        findNavController().navigate(R.id.navigation_settings)
    }

    private fun openFeedbackScreen() {
        feedbackFragment?.dismiss()
        feedbackFragment = FeedbackDialogFragment.getInstance()
        feedbackFragment?.show(
            activity!!.supportFragmentManager,
            FeedbackDialogFragment.FEEDBACK_TAG
        )
    }
}