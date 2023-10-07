package com.marqumil.peakyblinder.remote.post

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class RegisterBody(

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("confirmPassword")
	val confirmPassword: String? = null
)
