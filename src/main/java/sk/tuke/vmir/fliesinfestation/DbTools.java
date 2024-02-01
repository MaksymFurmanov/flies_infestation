package sk.tuke.vmir.fliesinfestation;

import android.content.Context;
import androidx.room.Room;
import java.lang.ref.WeakReference;

public class DbTools {
    private static ResultsDatabase _db;

    public DbTools(WeakReference<Context> refContext) {
        getDbContext(refContext);
    }

    public static ResultsDatabase getDbContext(WeakReference<Context> refContext){
        if(_db != null) return _db;
        return Room.databaseBuilder(refContext.get(), ResultsDatabase.class, "results-db").build();
    }
}
