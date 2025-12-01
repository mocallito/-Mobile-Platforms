package com.example.report

import android.net.http.HttpException
import androidx.lifecycle.LiveData
import java.io.File
import java.io.IOException
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Response

class EntityRepository(private val entityDao:EntityDao) {
    //val emails: LiveData <List<Email>> = entityDao.getAllEmail()
    /*
    val readPerson: LiveData<List<Person>> = entityDao.getAllPerson()
    val readPersonNoLive: List<Person> = entityDao.getAllPersonNoLive()
    suspend fun insertPerson(person: Person) {
        entityDao.insertEntity(person)
    }

     */
    suspend fun insertEmail(email: Email) {
        entityDao.insertEntity(email)
    }
/*
    fun getAllEmail(): LiveData<List<Email>> {
        return emails
    }
    
 */

    suspend fun deleteEmail(email: Email) {
        entityDao.delete(email)
    }

    suspend fun deleteAllEmail() {
        entityDao.deleteAllEmail()
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
        return RetrofitInstance.api.uploadImage2(
            image = MultipartBody.Part
                .createFormData(
                    "data",
                    file.name,
                    file.asRequestBody()
                ),
            number
        )
    }
    /*
    suspend fun getPost(): Response<JSONObject> {
        return RetrofitInstance.api.getPost()
    }

     */
}