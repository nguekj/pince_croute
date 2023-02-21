import java.time.LocalTime;

public class mseEnAnode{
    private String Equipement;
    private String DayShift;
    private String DayStartTime;
    private String DayEndTime;
    private String NightShift;
    private String NightStartTime;
    private String NightEndTime;
    

    public mseEnAnode(String rowcsv){
        String[] line = null;
        line = rowcsv.split(";");
        this.Equipement = line[0];
        this.DayShift = line[1];
        this.DayStartTime = line[2];
        this.DayEndTime = line[3];
        this.NightShift = line[4];
        this.NightStartTime = line[5];
        this.NightEndTime = line[6];
    }

    public String getEquipement(){
        return this.Equipement;
    }

    public String isDayShift(){
        return this.DayShift;
    }

    public String getDayStartTime(){
        return this.DayStartTime;
    }

    public String getDayEndTime(){
        return this.DayEndTime;
    }

    public String isNightShift(){
        return this.NightShift;
    }

    public String getNightStartTime(){
        return this.NightStartTime;
    }

    public String getNightEndTime(){
        return this.NightEndTime;
    }
    
    public String toString() { 
        return this.Equipement + " " + this.DayShift + " " + this.DayStartTime + " "+ this.DayEndTime+ " " + this.NightShift + " " + this.NightStartTime + " "+ this.NightEndTime;
    } 
    
}