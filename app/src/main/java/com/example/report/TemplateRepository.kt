package com.example.report

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import com.example.report.TemplateDatabase.Companion.getInstance
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File

class TemplateRepository(app: Application?) {
    /*
    private val templateDao: TemplateDao?
    private val entityDao: EntityDao?
    private val emails: LiveData<List<Email>>

     */
    val entityDao: EntityDao?
    val templateDao: TemplateDao?
    val allNotes: LiveData<List<Template>>
    //val allEmail: LiveData<List<Email>>
    val database: TemplateDatabase?
    val draftDao: DraftDao?
    val allDraft: LiveData<Draft>

    init {//just return maybe, move observation to main or som
        //var allNotes: MediatorLiveData<*>? = MediatorLiveData<Any>()
        database = getInstance(app!!)
        entityDao = database!!.entityDao()
        templateDao = database!!.templateDao()
        allNotes = templateDao!!.allTemplate()
        draftDao = database!!.draftDao()
        allDraft = draftDao!!.readDraft()
        //allEmail = entityDao!!.getAllEmail()
        /*
        entityDao = database!!.entityDao()!!
        allNotes = MediatorLiveData()
        allNotes.addSource(
            database!!.templateDao()!!.allTemplate(),
            Observer { templateEntities: List<Template?>? ->
                if (database.databaseCreated.value != null) {
                    allNotes.postValue(templateEntities)
                }
            })
        allNotes.addSource(
            entityDao.getAllEmail(),
            Observer { emailEntities: List<Email> ->
                if (database.databaseCreated.value != null) {
                    //allNotes.postValue(emailEntities)
                    //postvalue somehow
                    //insertEmail(emailEntities)
                }
            })

         */
    }

    suspend fun insertEmail(email: Email) {
        entityDao!!.insertEntity(email)
    }

    fun getAllEmails(template: String): LiveData<List<Email>> {
        return entityDao!!.getAllEmail(template)
    }

    suspend fun deleteEmail(email: Email) {
        entityDao!!.delete(email)
    }

    suspend fun updateEmail(email: Email) {
        entityDao!!.update(email)
    }

    suspend fun deleteAllEmail() {
        entityDao!!.deleteAllEmail()
    }

    suspend fun insertTemplate(email: Template) {
        templateDao!!.insert(email)
    }

    fun getAllTemplate(): LiveData<List<Template>> {
        return allNotes
    }

    suspend fun deleteTemplate(email: Template) {
        templateDao!!.delete(email)
    }

    suspend fun onTemplateDelete(template: String) {
        entityDao!!.onTemplateDelete(template)
    }

    suspend fun updateTemplate(template: Template) {
        templateDao!!.update(template)
    }

    suspend fun getSelected(): List<String> {
        return templateDao!!.getSelected()
    }

    suspend fun setSelected() { //set other not in focus template to not selected
        templateDao!!.setSelected(templateDao!!.getSelected())
    }

    suspend fun setDefault() { //set other non inuse template to not default
        templateDao!!.setDefault(templateDao!!.getDefault())
    }

    suspend fun selectCurrent(template: String) {
        templateDao!!.selectCurrent(template)
    }

    suspend fun selectDefault(template: String) {
        templateDao!!.selectDefault(template)
    }

    suspend fun getCurrent(): Template {
        return templateDao!!.getCurrent()
    }

    suspend fun usingTemplate(): Template {
        //val temp = templateDao!!.usingTemplate()
        return templateDao!!.usingTemplate()
    }

    suspend fun getLatest(): Template {
        return templateDao!!.getLatest()
    }

    suspend fun deleteAllTemplate() {
        templateDao!!.deleteAllTemplate()
    }

    fun closeDB() {
        database!!.close()
    }

    fun createUri(context: Context): Uri {
        val imageFile: File = File(context.getApplicationContext().getFilesDir(), "camera_photo.jpg")
        return FileProvider.getUriForFile(
            context.getApplicationContext(),
            "com.example.report.fileProvider",
            imageFile
        )
    }

    suspend fun insertDraft(draft: Draft) {
        draftDao!!.insertDraft(draft)
    }

    suspend fun deleteDraft() {
        draftDao!!.deleteDraft()
    }

    suspend fun updateDraft(draft: Draft) {
        draftDao!!.updateDraft(draft)
    }

    suspend fun uploadImage(file: File): Response<Post> {
        /*return try {
            RetrofitInstance.api.uploadImage(
                image = MultipartBody.Part
                    .createFormData(
                        "image",
                        file.name,
                        file.asRequestBody()
                    )
            )
            //true
        } catch (e: IOException) {
            e.printStackTrace()
            //false
        } catch (e: HttpException) {
            e.printStackTrace()
            //false
        }
         */
        return RetrofitInstance.api.uploadImage(
            image = MultipartBody.Part
                .createFormData(
                    "image",
                    file.name,
                    file.asRequestBody()
                )
        )
    }
    suspend fun uploadImage2(file: File, number: Int): Response<Post> {
        //val body = UploadRequestBody(file, "data")
        return RetrofitInstance.api.uploadImage2(
            MultipartBody.Part
                .createFormData(
                    "data",
                    file.name,
                    file.asRequestBody()
                    //body
                ),
            number
        )
    }
    suspend fun getPost(): Response<Post> {

        return RetrofitInstance.api.getPost()
    }
}