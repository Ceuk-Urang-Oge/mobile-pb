package com.marqumil.peakyblinder.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("token")
	val loginToken: String? = null
)

