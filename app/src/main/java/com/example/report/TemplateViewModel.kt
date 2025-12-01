package com.example.report

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File

class TemplateViewModel(app: Application) : AndroidViewModel(app) {
    private val repository: TemplateRepository
    val liveTemplate: LiveData<List<Template>>
    var myResponse: MutableLiveData<Response<Post>>
    val selected: MutableLiveData<Template>
    val inuse: MutableLiveData<Template>
    var allEmails: LiveData<List<Email>>

    var imageUri: MutableLiveData<Uri>
    var imageUriString: MutableLiveData<String>
    var currentImage: LiveData<Draft>

    var usingEmails: LiveData<List<Email>>

    init {
        repository = TemplateRepository(app)
        myResponse = MutableLiveData()
        selected = MutableLiveData()
        inuse = MutableLiveData()

        imageUri = MutableLiveData()
        imageUriString = MutableLiveData()
        currentImage = repository.allDraft

        liveTemplate = repository.allNotes
        //allEmails = MutableLiveData<List<Email>>()//possibly call the repository here direclty
        allEmails = selected.switchMap { string -> repository.getAllEmails(string.name)  }
        usingEmails = inuse.switchMap { string -> repository.getAllEmails(string.name) }
    }

    fun insert(template: Template) {
        viewModelScope.launch {
            repository.insertTemplate(template)
        }
        //do like entityviewmodel?
        //liveTemplate.value = template
    }

    fun delete(template: Template) {
        viewModelScope.launch {
            repository.deleteTemplate(template)
        }
    }

    fun update(template: Template) {
        viewModelScope.launch {
            repository.updateTemplate(template)
        }
    }

    fun deleteAllTemplates() {
        viewModelScope.launch {
            repository.deleteAllTemplate()
        }
    }

    fun closeDB() {
        repository.closeDB()
    }

    fun setSelected(){//set other not in focus template to not selected
        viewModelScope.launch {
            repository.setSelected()
        }
    }

    fun setDefault(){//set other not inuse template to not default
        viewModelScope.launch {
            repository.setDefault()
        }
    }

    fun selectCurrent(template: String) {//set current to be selected
        viewModelScope.launch {
            repository.setSelected()
            repository.selectCurrent(template)
        }
    }

    fun selectDefault(template: String) {//set current to be default
        viewModelScope.launch {
            repository.setDefault()
            repository.selectDefault(template)
        }
    }

    fun getCurrent() {
        viewModelScope.launch {
            selected.value = repository.getCurrent()
        }
    }

    fun usingTemplate() {
        viewModelScope.launch {
            inuse.value = repository.usingTemplate()
        }
    }

    fun getLatest(){
        viewModelScope.launch {
            selected.value = repository.getLatest()
        }
    }

    fun insertEmail(emails: Email){
        viewModelScope.launch {
            repository.insertEmail(emails)
        }
    }

    fun deleteEmail(email: Email) {
        viewModelScope.launch {
            repository.deleteEmail(email)
        }
    }

    fun onTemplateDelete(template: String){
        viewModelScope.launch {
            repository.onTemplateDelete(template)
        }
    }

    fun updateEmail(email: Email) {
        viewModelScope.launch{
            repository.updateEmail(email)
        }
    }

    fun deleteAllEmail() {
        viewModelScope.launch {
            repository.deleteAllEmail()
        }
    }

    fun getAllEmails(template: String){
        viewModelScope.launch {
            allEmails = repository.getAllEmails(template)
        }
    }

    fun createUri(context: Context){
        imageUri.value = repository.createUri(getApplicationContext())
    }

    fun insertDraft(draft: Draft) {
        viewModelScope.launch {
            repository.insertDraft(draft)
        }
    }

    fun deleteDraft() {
        viewModelScope.launch {
            repository.deleteDraft()
        }
    }

    fun updateDraft(draft: Draft) {
        viewModelScope.launch {
            repository.updateDraft(draft)
        }
    }

    fun uploadImage (file: File){
        viewModelScope.launch {
            //myResponse.value = repository.uploadImage(file)
        }
    }
    fun uploadImage2 (file: File, number: Int){
        viewModelScope.launch {
            myResponse.value = repository.uploadImage2(file, number)
            //
        }
    }
    fun getPost () {
        viewModelScope.launch {
            myResponse.value = repository.getPost()
        }
    }
/*
    fun toDraft (uri: String) {
        currentImage = myResponse.switchMap {
            response ->
            viewModelScope.launch {
                repository.updateDraft(Draft(uri, response.body().keywords.get(0).keyword))
                repository.allDraft
            }
        }
    }

 */
}