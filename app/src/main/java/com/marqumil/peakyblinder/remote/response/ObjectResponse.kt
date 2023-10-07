package com.marqumil.peakyblinder.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ObjectResponse<T>(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: T?,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
