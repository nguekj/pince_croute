import java.time.LocalDateTime;

public class mseEnAnode {
    private String Equipement;
    private int DayShift;
    private LocalDateTime DayStartTime;
    private LocalDateTime DayEndTime;
    private int NightShift;
    private LocalDateTime NightStartTime;
    private LocalDateTime NightEndTime;
    protected String DayGroupe;
    protected String NightGroupe; 
    
    //private String mseFilename;
     
    Util utilities = new Util();

    public mseEnAnode(String rowcsv, String fl){
        //this.mseFilename = fl;
        utilities.MseOperationFileDate = fl;
        String[] line = null;
        line = rowcsv.split(";");
        this.Equipement = line[0];
        this.DayShift = Integer.parseInt(line[1]);
        this.DayStartTime = utilities.convertMseTimeToDateTime(line[2]);
        this.DayEndTime = utilities.convertMseTimeToDateTime(line[3]);
        this.DayGroupe = line[4];
        this.NightShift = Integer.parseInt(line[5]);
        this.NightStartTime = utilities.convertMseTimeToDateTime(line[6]);
        this.NightEndTime = utilities.convertMseTimeToDateTime(line[7]);
        if(line.length==9){
            this.NightGroupe = line[8];
        }
        else{
            this.NightGroupe="-";
        }
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