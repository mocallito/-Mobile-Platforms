package com.example.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import retrofit2.Response

class EntityViewModel (
    private val repository: EntityRepository
): ViewModel() {
    var myResponse: MutableLiveData<Response<JSONObject>> = MutableLiveData()
    fun uploadImage (file: File){
        viewModelScope.launch {
            //myResponse.value = repository.uploadImage(file)
        }
    }
    fun uploadImage2 (file: File, number: Int){
        viewModelScope.launch {
            //myResponse.value = repository.uploadImage2(file, number)
        }
    }
    fun getPost () {
        viewModelScope.launch {
            //myResponse.value = repository.getPost()
        }
    }
}