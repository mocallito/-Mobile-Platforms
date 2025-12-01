/*package com.example.report;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.example.report.EntityDao;
import com.example.report.EntityDatabase;
import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
@Metadata(
        mv = {1, 8, 0},
        k = 1,
        d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0007J\b\u0010\t\u001a\u00020\bH\u0007J\b\u0010\n\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.¢\u0006\u0002\n\u0000¨\u0006\u000b"},
        d2 = {"Lcom/example/report/data/local/EntityDaoTest;", "", "()V", "db", "Lcom/example/report/EntityDatabase;", "entityDao", "Lcom/example/report/EntityDao;", "addition_isCorrect", "", "closeDB", "createDB", "report.app.androidTest"}
)
@ExperimentalCoroutinesApi
public final class EntityDaoTest {
    private EntityDao entityDao;
    private EntityDatabase db;

    @Before
    public final void createDB() {
        Context var10000 = ApplicationProvider.getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(var10000, "ApplicationProvider.getApplicationContext()");
        Context context = var10000;
        this.db = (EntityDatabase)Room.inMemoryDatabaseBuilder(context, EntityDatabase.class).allowMainThreadQueries().build();
        EntityDatabase var10001 = this.db;
        if (var10001 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("db");
        }

        this.entityDao = var10001.entityDao();
    }

    @After
    public final void closeDB() throws IOException {
        EntityDatabase var10000 = this.db;
        if (var10000 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("db");
        }

        var10000.close();
    }

    @Test
    public final void addition_isCorrect() {
        Assert.assertEquals(4L, (long)4);
    }
}


 */