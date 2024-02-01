package sk.tuke.vmir.fliesinfestation;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ResultsDao {
    @Query("SELECT * FROM Results")
    List<Results> getAll();

    @Query("SELECT * FROM Results WHERE Id LIKE :Id")
    Results getById(int Id);

    @Insert
    void insertResults(Results... results);

    @Delete
    void deleteMovie(Results movie);
}
