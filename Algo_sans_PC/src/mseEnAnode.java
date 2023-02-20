import java.io.*;
import java.util.ArrayList;

public class mseEnAnode{
    private String Equipement;
    private String Operation;
    private String Shift;
    private String StartTime;
    private String EndTime;
    private ArrayList<String> mseInfo = new ArrayList<>();

    public mseEnAnode(String rowcsv){
        String[] line = null;
        line = rowcsv.split(";");
        this.Equipement = line[0];
        this.Operation = line[1];
        this.Shift = line[2];
        this.StartTime = line[3];
        this.EndTime = line[4];
    }

    public String getEquipement(){
        return this.Equipement;
    }
    public String getOperation(){
        return this.Operation;
    }
    public String getShift(){
        return this.Shift;
    }
    public String getStartTime(){
        return this.StartTime;
    }
    public String getEndTime(){
        return this.EndTime;
    }
    public ArrayList<String> OperationAnode(ArrayList<String> mseInfo){
        
        return mseInfo;
    }

    public String toString() { 
        return "Equipement: '" + this.Equipement + " Operation: " + this.Operation + " Shift: " + this.Shift + " StartTime: " + this.StartTime + " EndTime: "+ this.EndTime;
    } 
    
}