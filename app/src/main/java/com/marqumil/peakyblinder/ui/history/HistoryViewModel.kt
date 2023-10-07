package com.marqumil.peakyblinder.ui.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marqumil.peakyblinder.remote.ResultState
import com.marqumil.peakyblinder.remote.post.GlucoseBody
import com.marqumil.peakyblinder.remote.response.GlucoseResponse
import com.marqumil.peakyblinder.remote.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val _getHistory = MutableLiveData<ResultState<GlucoseResponse>>()
    val getHistory: MutableLiveData<ResultState<GlucoseResponse>> = _getHistory

    private val _postGlucose = MutableLiveData<ResultState<GlucoseResponse>>()
    val postGlucose: MutableLiveData<ResultState<GlucoseResponse>> = _postGlucose

    private val _cekInputHistory = MutableLiveData<ResultState<Boolean>>()
    val cekInputHistory: MutableLiveData<ResultState<Boolean>> = _cekInputHistory

    private val _forceLogout = MutableLiveData(false)
    val forceLogout: MutableLiveData<Boolean> = _forceLogout

    fun getGlucoseHistory() {
        _getHistory.value = ResultState.Loading
         viewModelScope.launch(Dispatchers.IO) {

             val response = ApiConfig.getApiService().getGlucose()
             try {
                 val data = response.data
                 Log.d("data", data.toString())
                 if (data != null) {
                     _getHistory.postValue(ResultState.Success(data))
                 } else {
                     _getHistory.postValue(ResultState.Failure(Throwable("Data Kosong")))
                 }
             } catch (e: Exception) {
                    _getHistory.postValue(ResultState.Failure(Throwable(e.message)))
             }
         }
    }

    fun checkInputHistory(
        date: String,
        time: String,
        value: Int,
        meal: String
    ) {
        if (date == "" || date.isEmpty()) {
            Log.d("AddHistoryActivity", "checkInputHistory: date")
            _cekInputHistory.value = ResultState.Failure(Throwable("Date must be filled"))
        } else if (time == "" || time.isEmpty()) {
            Log.d("AddHistoryActivity", "checkInputHistory: time")
            _cekInputHistory.value = ResultState.Failure(Throwable("Time must be filled"))
        } else if (value == 0 || value.toString().isEmpty() || value.toString() == "" || value.toString().isBlank()) {
            Log.d("AddHistoryActivity", "checkInputHistory: value")
            _cekInputHistory.value = ResultState.Failure(Throwable("Blood Glucose must be filled"))
        } else if (meal == "" || meal.isEmpty()) {
            Log.d("AddHistoryActivity", "checkInputHistory: meal")
            _cekInputHistory.value = ResultState.Failure(Throwable("Meal must be filled"))
        } else {
            _cekInputHistory.value = ResultState.Success(true)
            postGlucoseHistory(date, time, value, meal)
        }
    }
    private fun postGlucoseHistory(date: String, time: String, value: Int, meal: String) {
        _postGlucose.value = ResultState.Loading
         viewModelScope.launch(Dispatchers.IO) {
             val glucoseBody = GlucoseBody(date, time, value, meal)
             val response = ApiConfig.getApiService().postGlucose(glucoseBody)
             try {
                 val data = response.data
                 Log.d("data", data.toString())
                 if (data != null) {
                     _postGlucose.postValue(ResultState.Success(data))
                 } else {
                     _postGlucose.postValue(ResultState.Failure(Throwable("Data Kosong")))
                 }
             } catch (e: Exception) {
                    _postGlucose.postValue(ResultState.Failure(Throwable(e.message)))
             }
         }
    }


}