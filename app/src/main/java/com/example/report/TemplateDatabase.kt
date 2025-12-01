package com.example.report

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Draft::class, Template::class, Email::class], version = 1, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class TemplateDatabase : RoomDatabase() {
    abstract fun templateDao(): TemplateDao?
    abstract fun entityDao(): EntityDao?
    abstract fun draftDao(): DraftDao?
    private val IsDatabaseCreated = MutableLiveData<Boolean>()
    private fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath("template_database").exists()) IsDatabaseCreated.postValue(true)
    }

    val databaseCreated: LiveData<Boolean>
        get() = IsDatabaseCreated /*
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //new PopulateAsyncTask(instance).execute();
        }
    };

    private static class PopulateAsyncTask extends AsyncTask<Void, Void, Void> {
        private TemplateDao templateDao;

        private PopulateAsyncTask(TemplateDatabase db) {
            templateDao = db.templateDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Log.d("insert", String.valueOf(templateDao.insert(new Template("lordorderon@highcastle.com", "draft", "empty", false))));
            return null;
        }
    }

     */

    companion object {
        private var instance: TemplateDatabase? = null
        @Synchronized
        fun getInstance(context: Context): TemplateDatabase? {
            if (instance == null) {
                instance = databaseBuilder(
                    context.applicationContext,
                    TemplateDatabase::class.java, "template_database"
                )
                    .fallbackToDestructiveMigration() //.addCallback(roomCallback)
                    .build()
                instance!!.updateDatabaseCreated(context.applicationContext)
            }
            return instance
        }
    }
}