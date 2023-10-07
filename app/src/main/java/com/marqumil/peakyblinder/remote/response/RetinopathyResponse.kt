package com.marqumil.peakyblinder.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class RetinopathyResponse(

	@field:SerializedName("kumilcintabh")
	val kumilcintabh: Kumilcintabh? = null,

	@field:SerializedName("prediction")
	val prediction: Int? = null,

	@field:SerializedName("label")
	val label: String? = null
) : Parcelable

@Parcelize
data class Kumilcintabh(

	@field:SerializedName("general_recommendation")
	val generalRecommendation: List<String?>? = null,

	@field:SerializedName("specific_recomendation")
	val specificRecomendation: List<String?>? = null,

	@field:SerializedName("title")
	val title: String? = null
) : Parcelable
