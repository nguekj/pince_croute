import java.time.LocalDateTime;

public class mseEnAnode {
    private String Equipement;
    private int DayShift;
    private LocalDateTime DayStartTime;
    private LocalDateTime DayEndTime;
    private int NightShift;
    private LocalDateTime NightStartTime;
    private LocalDateTime NightEndTime;
    private String mseFilename;
     
    Util utilities = new Util();

    public mseEnAnode(String rowcsv, String fl){
        this.mseFilename = fl;
        String[] line = null;
        line = rowcsv.split(";");
        this.Equipement = line[0];
        this.DayShift = Integer.parseInt(line[1]);
        this.DayStartTime = utilities.convertMseTimeToDateTime(line[2], this.mseFilename);
        this.DayEndTime = utilities.convertMseTimeToDateTime(line[3], this.mseFilename);
        this.NightShift = Integer.parseInt(line[4]);
        this.NightStartTime = utilities.convertMseTimeToDateTime(line[5], this.mseFilename);
        this.NightEndTime = utilities.convertMseTimeToDateTime(line[6], this.mseFilename);
    }

    public String getEquipement(){
        return this.Equipement;
    }

    public int isDayShift(){
        return this.DayShift;
    }

    public LocalDateTime getDayStartTime(){
        return this.DayStartTime;
    }

    public LocalDateTime getDayEndTime(){
        return this.DayEndTime;
    }

    public int isNightShift(){
        return this.NightShift;
    }

    public LocalDateTime getNightStartTime(){
        return this.NightStartTime;
    }

    public LocalDateTime getNightEndTime(){
        return this.NightEndTime;
    }
    
    @Override
    public String toString() { 
        return this.Equipement + " " + this.DayShift + " " + this.DayStartTime + " "+ this.DayEndTime+ " " + this.NightShift + " " + this.NightStartTime + " "+ this.NightEndTime;
    } 
    
}