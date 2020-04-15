package com.example.arenamsk.ui.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.arenamsk.R
import com.example.arenamsk.datasources.LocalDataSource
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.ui.base.PlaceDialogFragment
import com.example.arenamsk.ui.feedback.FeedbackDialogFragment
import com.example.arenamsk.ui.place_filter.PlaceFilterFragment
import com.example.arenamsk.utils.disable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.*

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val profileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    private var feedbackFragment: FeedbackDialogFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        profile_item_exit.setOnClickListener { exitFromProfile() }

        profile_item_booked.setOnClickListener {
            openBookedFragment()
        }

        profile_item_contact.setOnClickListener {
            openFeedbackScreen()
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

    private fun openFeedbackScreen() {
        feedbackFragment?.dismiss()
        feedbackFragment = FeedbackDialogFragment.getInstance()
        feedbackFragment?.show(
            activity!!.supportFragmentManager,
            FeedbackDialogFragment.FEEDBACK_TAG
        )
    }
}