package sk.tuke.vmir.fliesinfestation;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Results {
    Results(){}
    Results(String gameDate, String level, int killed,
            int max, boolean result){
        this.GameDate = gameDate;
        this.Level = level;
        this.Killed = killed;
        this.Max = max;
        this.Result = result;
    }
    @PrimaryKey(autoGenerate = true)
    private long Id;
    @ColumnInfo(name="date")
    private String GameDate;
    @ColumnInfo(name="level")
    private String Level;
    @ColumnInfo(name="killed")
    private int Killed;
    @ColumnInfo(name="max")
    private int Max;
    @ColumnInfo(name="result")
    private boolean Result;

    public long getId() {
        return Id;
    }
    public void setId(long id) {
        Id = id;
    }

    public String getGameDate(){
        return GameDate;
    }
    public void setGameDate(String date){
        GameDate = date;
    }

    public String getLevel(){
        return Level;
    }
    public void setLevel(String level){
        Level = level;
    }

    public int getKilled(){
        return Killed;
    }
    public void setKilled(int killed){
        Killed = killed;
    }

    public int getMax(){
        return Max;
    }
    public void setMax(int max){
        Max = max;
    }

    public boolean getResult(){
        return Result;
    }
    public void setResult(boolean result){
        Result = result;
    }
}
