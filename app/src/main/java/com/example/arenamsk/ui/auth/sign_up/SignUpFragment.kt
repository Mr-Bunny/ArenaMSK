package com.example.arenamsk.ui.auth.sign_up

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.arenamsk.R
import com.example.arenamsk.ui.AuthActivity
import com.example.arenamsk.ui.AuthActivity.Companion.GALLERY_PERMISSION_REQUEST_CODE
import com.example.arenamsk.ui.AuthActivity.Companion.GALLERY_REQUEST_CODE
import com.example.arenamsk.ui.base.BaseAuthFragment
import com.example.arenamsk.utils.EnumUtils.SignUpStatus
import com.example.arenamsk.utils.ImageUtils
import com.example.arenamsk.utils.PermissionUtils
import com.example.arenamsk.utils.enable
import com.example.arenamsk.utils.hide
import kotlinx.android.synthetic.main.fragment_sign_up.*
import uk.co.senab.photoview.PhotoViewAttacher
import java.io.ByteArrayOutputStream

class SignUpFragment : BaseAuthFragment(R.layout.fragment_sign_up), SignUpFragmentCallback {

    private val signUpViewModel by lazy {
        ViewModelProviders.of(this).get(SignUpViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_arrow.setOnClickListener { activity!!.onBackPressed() }

        text_add_photo.setOnClickListener { getPhotoFromGallery() }

        text_without_registration.setOnClickListener { signUpViewModel.skipAuth() }

        btn_sign_up.setOnClickListener {
            name_edit_text.clearFocus()
            email_edit_text.clearFocus()
            password_edit_text.clearFocus()

            signUpViewModel.startSignUp(
                name_edit_text.getText().trim(),
                email_edit_text.getText().trim(),
                password_edit_text.getText().trim(),
                getAvatar()
            )
        }

        signUpViewModel.getSignUpStatus()
            .observe(viewLifecycleOwner, Observer { handleSignUpStatus(it) })
    }

    override fun galleryPermissionGranted() {
        openGallery()
    }

    override fun galleryPermissionDenied() {
    }

    override fun galleryRequest(bitmap: Bitmap) {
        text_add_photo.hide()
        avatar_view.enable()
        circle_crop_image_view.setImageBitmap(bitmap)

        with(PhotoViewAttacher(circle_crop_image_view)) {
            update()
        }
    }

    private fun handleSignUpStatus(signUpStatus: SignUpStatus) {
        when (signUpStatus) {
            SignUpStatus.USERNAME_EMPTY -> {
                name_edit_text.setError(getString(com.example.arenamsk.R.string.text_hint_empty_field_error))
            }

            SignUpStatus.EMAIL_EMPTY -> {
                email_edit_text.setError(getString(com.example.arenamsk.R.string.text_hint_empty_field_error))
            }

            SignUpStatus.EMAIL_INCORRECT -> {
                email_edit_text.setError(getString(com.example.arenamsk.R.string.text_hint_email_incorrect_error))
            }

            SignUpStatus.PASSWORD_EMPTY -> {
                password_edit_text.setError(getString(com.example.arenamsk.R.string.text_hint_empty_field_error))
            }

            SignUpStatus.PASSWORD_MIN_LENGTH_ERROR -> {
                password_edit_text.setError(getString(com.example.arenamsk.R.string.text_hint_min_length_error))
            }

            SignUpStatus.EMAIL_EXIST -> {
                email_edit_text.setError(getString(com.example.arenamsk.R.string.text_hint_email_exist_error))
            }

            SignUpStatus.SIGN_UP_FAIL -> {
                showToast(getString(com.example.arenamsk.R.string.text_hint_sign_up_fail))
            }

            SignUpStatus.SIGN_UP_SUCCESS -> {
                openApp(activity as AuthActivity)
            }
        }
    }

    private fun getPhotoFromGallery() {
        PermissionUtils.checkForPermission(
            activity!!,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            ::openGallery,
            ::requestExternalStoragePermission
        )
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        activity!!.startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestExternalStoragePermission() {
        PermissionUtils.requestForPermission(
            activity!!,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            GALLERY_PERMISSION_REQUEST_CODE
        )
    }

    private fun getAvatar(): ByteArray? {
        //Если не была выбрана никакая фотография
        if (avatar_view.visibility != View.VISIBLE) return null

        val croppedAvatar = ImageUtils.createCircleBitmap(
            ImageUtils.getImageFromView(
                circle_crop_image_view
            )
        )

        val stream = ByteArrayOutputStream()
        croppedAvatar.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()
        croppedAvatar.recycle()

        return byteArray
    }
}