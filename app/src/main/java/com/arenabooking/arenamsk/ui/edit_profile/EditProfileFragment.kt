package com.arenabooking.arenamsk.ui.edit_profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.datasources.LocalDataSource
import com.arenabooking.arenamsk.network.models.ApiError
import com.arenabooking.arenamsk.network.models.RequestErrorHandler
import com.arenabooking.arenamsk.repositories.AuthRepository
import com.arenabooking.arenamsk.repositories.PlaceRepository
import com.arenabooking.arenamsk.room.tables.User
import com.arenabooking.arenamsk.ui.AuthActivity
import com.arenabooking.arenamsk.ui.AuthActivity.Companion.GALLERY_PERMISSION_REQUEST_CODE
import com.arenabooking.arenamsk.ui.auth.sign_up.GalleryCallback
import com.arenabooking.arenamsk.ui.base.BaseFragment
import com.arenabooking.arenamsk.utils.ImageUtils
import com.arenabooking.arenamsk.utils.PermissionUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.circle_crop_image_view
import kotlinx.android.synthetic.main.fragment_edit_profile.name_edit_text
import kotlinx.coroutines.*
import uk.co.senab.photoview.PhotoViewAttacher

/** Экран изменения информации о пользователе */
class EditProfileFragment : BaseFragment(R.layout.fragment_edit_profile), GalleryCallback {

    private val dispatcher by lazy { requireActivity().onBackPressedDispatcher }

    private val repository = PlaceRepository.getInstance()
    private val authRepository = AuthRepository.getInstance()

    private var isAvatarEdited = false

    private val editProfileViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(EditProfileViewModel::class.java)
    }

    private val passwordChangeErrorHandler = object : RequestErrorHandler {
        override suspend fun networkUnavailableError() {
            showToast("Нет соединения с интернетом")
        }

        override suspend fun requestFailedError(error: ApiError?) {
            showToast("Не удалось поменять пароль, проверьте введенные данные")
        }

        override suspend fun timeoutException() {
            showToast("Не удалось поменять пароль, проверьте введенные данные")
        }

        override suspend fun requestSuccessButResponseIsNull() {
            showToast("Не удалось поменять пароль, проверьте введенные данные")
        }
    }

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

        btn_back.setOnClickListener { findNavController().popBackStack() }

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

        delete_profile.setOnClickListener { showDeleteAccountConfirmWindow() }

        //Смена пароля
        current_password_edit_text.setHintText("Текущий пароль")
        new_password_edit_text.setHintText("Новый пароль")

        change_password_btn.setOnClickListener {
            val currentPassword = current_password_edit_text.getEditText().text.toString()
            val newPassword = new_password_edit_text.getEditText().text.toString()

            if (currentPassword.isEmpty()) {
                showToast("Введите текущий пароль")
            } else if (newPassword.isEmpty()) {
                showToast("Введите новый пароль")
            } else if (currentPassword.length < 6 || newPassword.length < 6) {
                showToast("Минимальная длина пароля 6 символов")
            } else {
                editProfileViewModel.changePassword(
                    currentPassword = current_password_edit_text.getEditText().text.toString(),
                    newPassword = new_password_edit_text.getEditText().text.toString(),
                    success = {
                        current_password_edit_text.getEditText().setText("")
                        new_password_edit_text.getEditText().setText("")
                        showToast("Пароль изменен!")
                    },
                    errorHandler = passwordChangeErrorHandler
                )
            }
        }
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
            success = ::updateSuccess,
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
                errorHandler = errorHandler
            )
        } else {
            showToast("Данные обновлены!")
            findNavController().popBackStack()
        }
    }

    private fun showDeleteAccountConfirmWindow() {
        AlertDialog.Builder(requireContext(), R.style.FavouriteAlertDialog)
            .setTitle("Вы уверены?")
            .setMessage("Ваш аккаунт будет удален навсегда")
            .setPositiveButton("Удалить") { _, _ ->
                sendDeleteRequest()
            }
            .setNegativeButton("Отменить") { _, _ ->
            }
            .setCancelable(false)
            .show()
    }

    private fun sendDeleteRequest() {
        repository.deleteAccount(
            success = {
                exitFromProfile()
                showToast("Аккаунт удален!")
            }, errorHandler = object : RequestErrorHandler {
                override suspend fun networkUnavailableError() {
                    showToast("Нет соединения с интернетом")
                }

                override suspend fun requestFailedError(error: ApiError?) {
                    showToast("Не удалось удалить аккаунт")
                }

                override suspend fun timeoutException() {
                    showToast("Не удалось удалить")
                }

                override suspend fun requestSuccessButResponseIsNull() {
                    showToast("Не удалось удалить")
                }
            })

    }
}
