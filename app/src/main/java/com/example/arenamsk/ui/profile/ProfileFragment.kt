package com.example.arenamsk.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.arenamsk.R
import com.example.arenamsk.datasources.LocalDataSource
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.ui.AuthActivity
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.utils.Constants
import com.example.arenamsk.utils.disable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.*

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val profileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setup name
        CoroutineScope(Dispatchers.IO).launch {
            LocalDataSource.getUserData()?.let {
                withContext(Dispatchers.Main) {
                    if (it.imageUrl.isNotEmpty()) {
                        Picasso.get()
                            .load(it.imageUrl)
                            .error(R.drawable.auth_background)
                            .placeholder(R.drawable.auth_background)
                            .into(profile_avatar)
                    }

                    profile_user_name.text = it.firstName
                }
            }
        }

        if (AuthUtils.isUserDefault()) profile_item_edit.disable()

        profile_item_exit.setOnClickListener {
            with(AuthUtils) {
                setUserIsAuthorized(false)
                setUserIsDefault(false)
                saveAuthToken("")
                saveRefreshToken("")
            }

            startActivity(Intent(activity, AuthActivity::class.java))
            activity?.finish()
        }
    }
}