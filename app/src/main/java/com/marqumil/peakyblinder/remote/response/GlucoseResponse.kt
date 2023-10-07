package com.marqumil.peakyblinder.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class GlucoseResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("bloodGlucoseAverage")
	val bloodGlucoseAverage: Double?,

	@field:SerializedName("bloodGlucoseLevel")
	val bloodGlucoseLevel: String? = null
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("meal")
	val meal: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("value")
	val value: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
) : Parcelable
