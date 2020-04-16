package com.example.arenamsk.ui.edit_profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.arenamsk.R
import com.example.arenamsk.datasources.LocalDataSource
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.repositories.AuthRepository
import com.example.arenamsk.repositories.PlaceRepository
import com.example.arenamsk.room.tables.User
import com.example.arenamsk.ui.AuthActivity
import com.example.arenamsk.ui.AuthActivity.Companion.GALLERY_PERMISSION_REQUEST_CODE
import com.example.arenamsk.ui.auth.sign_up.GalleryCallback
import com.example.arenamsk.ui.base.BaseFragment
import com.example.arenamsk.utils.ImageUtils
import com.example.arenamsk.utils.PermissionUtils
import com.example.arenamsk.utils.SharedPreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.circle_crop_image_view
import kotlinx.android.synthetic.main.fragment_edit_profile.name_edit_text
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.*
import uk.co.senab.photoview.PhotoViewAttacher

class EditProfileFragment : BaseFragment(R.layout.fragment_edit_profile), GalleryCallback {

    private val dispatcher by lazy { requireActivity().onBackPressedDispatcher }

    private val repository = PlaceRepository.getInstance()
    private val authRepository = AuthRepository.getInstance()

    private var isAvatarEdited = false

    private val errorHandler = object : RequestErrorHandler {
        override suspend fun networkUnavailableError() {
            showToast("Нет соединения с интернетом")
        }

        override suspend fun requestFailedError(error: ApiError?) {
            showToast("Не удалось обновить данные")
        }

        override suspend fun timeoutException() {
            showToast("Не удалось обновить данные")
        }

        override suspend fun requestSuccessButResponseIsNull() {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            LocalDataSource.getUserData()?.let {
                withContext(Dispatchers.Main) {
                    if (it.imageUrl?.isNotEmpty() == true && profile_avatar != null) {
                        Picasso.get()
                            .load(it.imageUrl)
                            .error(R.drawable.image_placeholder)
                            .placeholder(R.drawable.image_placeholder)
                            .into(circle_crop_image_view)
                    }

                    name_edit_text.getEditText().setText(it.firstName)
                }
            }
        }

        edit_add_new_photo.setOnClickListener {
            getPhotoFromGallery()
        }

        btn_edit.setOnClickListener { saveNewData() }
    }

    override fun galleryPermissionGranted() {
        openGallery()
    }

    override fun galleryPermissionDenied() {
    }

    override fun galleryRequest(bitmap: Bitmap) {
        isAvatarEdited = true

        with(circle_crop_image_view) {
            setImageBitmap(bitmap)
            PhotoViewAttacher(this).update()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            GALLERY_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    galleryPermissionGranted()
                } else {
                    galleryPermissionDenied()
                }
            }

            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun getPhotoFromGallery() {
        PermissionUtils.checkForPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            ::openGallery,
            ::requestExternalStoragePermission
        )
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        activity!!.startActivityForResult(intent, AuthActivity.GALLERY_REQUEST_CODE)
    }

    private fun getAvatar(): Bitmap {
        //Возвращаем  фотку
        return ImageUtils.getImageFromView(circle_crop_image_view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestExternalStoragePermission() {
        PermissionUtils.requestForPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            GALLERY_PERMISSION_REQUEST_CODE
        )
    }

    private fun saveNewData() {
        val name = name_edit_text.getEditText().text.toString()

        if (name.isEmpty()) {
            showToast("Имя не может быть пустым")
            return
        }

        repository.updateUserData(
            name = name,
            success =  ::updateSuccess,
            errorHandler = errorHandler
        )
    }

    //Сохраняем в базу обновленную модельку пользователя и обновляем аватарку
    private fun updateSuccess(user: User) {
        GlobalScope.launch(Dispatchers.IO) {
            LocalDataSource.updateUserData(user)
        }

        val avatar = if (isAvatarEdited) getAvatar() else null

        if (avatar != null) {
            authRepository.uploadAvatar(
                image = avatar,
                success = {
                    //Сохраняем ссылку на картинку
                    user.imageUrl = it.imageUrl
                    GlobalScope.launch(Dispatchers.IO) {
                        LocalDataSource.updateUserData(user)

                        withContext(Dispatchers.Main) {
                            showToast("Данные обновлены!")
                            findNavController().popBackStack()
                        }
                    }
                },
                errorHandler = errorHandler)
        } else {
            showToast("Данные обновлены!")
            findNavController().popBackStack()
        }
    }
}
