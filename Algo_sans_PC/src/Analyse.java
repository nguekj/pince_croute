import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class Analyse {

    public ArrayList<String[]> antenneLocation = new ArrayList<>();
    public ArrayList<mseEnAnode> mseInfo = new ArrayList<>();
    public ArrayList<RfidSural> rfidsuralData = new ArrayList<>();
    public static ArrayList<RfidSural> finalrfidsuralData = new ArrayList<>();
    public ArrayList<String[]> readerAnode = new ArrayList<>();
    public ArrayList<String[]> paireAntenneBaliseInOperation = new ArrayList<>();
    public ArrayList<String[]> equipementDefectueux = new ArrayList<>();
    private int indexLine = 0;
    public static DetectionCoupPince5min DcpInstance = null;
    public ReadFile ReadFileInstance = null;
    Util utilities = new Util();
    public File fileForDetectionAnode;
    private ArrayList<RfidSural> UniquePaireRfidsuralData = new ArrayList<>();
    

    public Analyse(File fileOperation, File fileEquipement, File fileRfidSural, File fileDest, LocalDateTime dtStart){

        ReadFile rf = new ReadFile(fileEquipement, fileOperation,fileRfidSural, dtStart);

        this.antenneLocation = rf.getAntenneLocation();
        this.mseInfo = rf.getMSEOperationInfo();
        this.rfidsuralData = rf.getRfidSural();
        this.fileForDetectionAnode = fileDest;
        this.equipementDefectueux = rf.getUnUsedEquipement();
        equipementDefectueux();
        rfidSuralDataInAnode();
        DetectionCoupPince5min dCP = new DetectionCoupPince5min(finalrfidsuralData,null,fileDest);
        
    }

    public Analyse(File fileOperation, File fileEquipement, File fileRfidSural, File fileDest, File fileTraite, LocalDateTime dtStart){

        ReadFile rf = new ReadFile(fileEquipement, fileOperation,fileRfidSural, dtStart);
        this.ReadFileInstance = rf;
        this.antenneLocation = rf.getAntenneLocation();
        this.mseInfo = rf.getMSEOperationInfo();
        this.rfidsuralData = rf.getRfidSural();
        this.fileForDetectionAnode = fileDest;
        //this.equipementDefectueux = rf.getUnUsedEquipement();
        //equipementDefectueux();
        rfidSuralDataInAnode();
        uniquePaireEquipement();

        //UniquePaireRfidsuralData.forEach((u)-> System.out.println(u.toString()));
        
        DetectionCoupPince5min dCP = new DetectionCoupPince5min(finalrfidsuralData,UniquePaireRfidsuralData,fileDest);
        Analyse.DcpInstance = dCP;
        write_csv(finalrfidsuralData,fileTraite);
        
    }

    public Analyse(){}

    enum shiftEnum{
            NONE,
            DAY,
            NIGHT,
            BOTH
        }
    
    
    public void rfidSuralDataInAnode(){
        shiftEnum mseShift = shiftEnum.NONE;
        paireAntenneBaliseInOperation();
        removeUnAnodeMse();

        for(int j=0; j<mseInfo.size(); j++){
            for (int m=0; m<paireAntenneBaliseInOperation.size();m++){
                if( Objects.equals(paireAntenneBaliseInOperation.get(m)[2], mseInfo.get(j).getEquipement())){
                    for(int i=0;i<rfidsuralData.size();i++){
                        if(Objects.equals(rfidsuralData.get(i).readerId, paireAntenneBaliseInOperation.get(m)[0])){
                            if(mseInfo.get(j).isDayShift()==1 && mseInfo.get(j).isNightShift()==0 ){
                                mseShift = shiftEnum.DAY;
                            }
                            else if(mseInfo.get(j).isDayShift()==0 && mseInfo.get(j).isNightShift()==1 ){
                                mseShift = shiftEnum.NIGHT;
                            }
                            else if(mseInfo.get(j).isDayShift()==1 && mseInfo.get(j).isNightShift()==1 ){
                                mseShift = shiftEnum.BOTH;
                            }
                            
                            shiftCase(mseShift, i, j);

                        }
                        }
                    }
                    //break;
                }
            }
        //System.out.println("line total : "+finalrfidsuralData.size());
        addMseToFinalRfidsural();
    }

    public void uniquePaireEquipement(){
        if(finalrfidsuralData.size()!=0){
            UniquePaireRfidsuralData.add(finalrfidsuralData.get(0));
            for(int i=0;i<finalrfidsuralData.size();i++){
                if(!Objects.equals(finalrfidsuralData.get(i).readerId, UniquePaireRfidsuralData.get(UniquePaireRfidsuralData.size()-1).readerId)){
                    UniquePaireRfidsuralData.add(finalrfidsuralData.get(i));
                }
            }
        }
    }

    public void addMseToFinalRfidsural(){
        ArrayList<RfidSural> tempData = new ArrayList<>();
        //int count = 0;
        for(int i=0; i<paireAntenneBaliseInOperation.size(); i++){
            for(int j=0; j<finalrfidsuralData.size();j++){
                if( Objects.equals(paireAntenneBaliseInOperation.get(i)[0], finalrfidsuralData.get(j).readerId)){
                    finalrfidsuralData.get(j).mse = paireAntenneBaliseInOperation.get(i)[2];
                    finalrfidsuralData.get(j).index = this.indexLine + 1;
                    
                    tempData.add(finalrfidsuralData.get(j));
                    this.indexLine++;
                }
            }
        }
        finalrfidsuralData = tempData;
    }

    public void paireAntenneBaliseInOperation(){
        for(int i=0; i<mseInfo.size(); i++){
            for(int j=0; j<antenneLocation.size();j++){
                if( Objects.equals(antenneLocation.get(j)[2], mseInfo.get(i).getEquipement())){
                    String [] temp = new String[3];
                    temp[0] = antenneLocation.get(j)[0];//reader
                    temp[1] = antenneLocation.get(j)[1];//tag
                    temp[2] = antenneLocation.get(j)[2];//mse
                    paireAntenneBaliseInOperation.add(temp);
                    
                    break;
                }
            }
        }
        //System.out.println("paire utilise :  "+paireAntenneBaliseInOperation.size());
    }

    public void equipementDefectueux(){
        ArrayList<String []> tempData = new ArrayList<>();
        for(int i=0; i<mseInfo.size(); i++){
            for(int j=0; j<equipementDefectueux.size();j++){
                if( Objects.equals(mseInfo.get(i).getEquipement(), equipementDefectueux.get(j)[2])){
                    tempData.add(equipementDefectueux.get(j));
                }
            }
        }

        this.equipementDefectueux = tempData;
        //System.out.println("nombre d'equipement defectueux "+this.equipementDefectueux.size());
        //this.equipementDefectueux.forEach((n) -> System.out.println(n[2]+" reader "+n[0]+" tag "+n[1]));
    }

    public void removeUnAnodeMse(){
        ArrayList<RfidSural> tempData = new ArrayList<>();

        for(int i=0;i<this.rfidsuralData.size();i++){
            for(int j=0; j<this.paireAntenneBaliseInOperation.size();j++){
                if( Objects.equals(this.rfidsuralData.get(i).readerId, this.paireAntenneBaliseInOperation.get(j)[0]) && 
                Objects.equals(this.rfidsuralData.get(i).tagId, this.paireAntenneBaliseInOperation.get(j)[1]))
                {
                    tempData.add(this.rfidsuralData.get(i));
                    //rfidsuralData.remove(i);
                    break;
                }
            }
        }
        this.rfidsuralData = tempData;
        
        //System.out.println("rfidsurad final size : "+this.rfidsuralData.size());
    }

    public void shiftCase(shiftEnum se,int i, int j){
        switch (se) {
            case DAY:
                
                if(utilities.isInShiftDayRange(rfidsuralData.get(i).timestamp, mseInfo.get(j).getDayStartTime(),mseInfo.get(j).getDayEndTime()))
                {
                    rfidsuralData.get(i).Groupe = mseInfo.get(j).DayGroupe;
                    finalrfidsuralData.add(rfidsuralData.get(i));
                    
                }
                
                break;
    
            case NIGHT:
                
                if(utilities.isInShiftNightRange(rfidsuralData.get(i).timestamp, mseInfo.get(j).getNightStartTime(), mseInfo.get(j).getNightEndTime()))
                {
                    rfidsuralData.get(i).Groupe = mseInfo.get(j).NightGroupe;
                    finalrfidsuralData.add(rfidsuralData.get(i));
                }
                break;
    
            case BOTH:
                shiftCase(shiftEnum.DAY, i, j);
                shiftCase(shiftEnum.NIGHT, i, j);
                
                break;

            // Default case
            default:
                System.out.println("error");
        }
        

    }

    public void write_csv(ArrayList<RfidSural> myArrayList, File myfile){
        String encoding = "UTF-8";
        try{
            if(!myfile.exists()){
                FileWriter writer = new FileWriter(myfile,StandardCharsets.UTF_8);
                writer.write("reader_id");
                writer.write(";");
                writer.write("tag_id");
                writer.write(";");
                writer.write("distance");
                writer.write(";");
                writer.write("insert_timestamp");
                writer.write(";");
                writer.write("Location");
                writer.write(";");
                writer.write("Index");
                writer.write(";");
                writer.write("Group");
                writer.write(";");   
                writer.write("\n");
                writer.close();
            }
            FileWriter writer = new FileWriter(myfile,StandardCharsets.UTF_8, true);
            for (int j = 0; j < myArrayList.size(); j ++){
                
                writer.write(myArrayList.get(j).readerId);
                writer.write(";");
                writer.write(myArrayList.get(j).tagId);
                writer.write(";"); 
                writer.write(String.valueOf(myArrayList.get(j).distance));
                writer.write(";"); 
                writer.write(utilities.convertDateTimeToString(myArrayList.get(j).timestamp));
                writer.write(";");
                writer.write(myArrayList.get(j).mse);
                writer.write(";");
                writer.write(String.valueOf(myArrayList.get(j).index));
                writer.write(";");
                writer.write(myArrayList.get(j).Groupe);
                writer.write(";");   
                //}
                writer.write("\n");
            }

            writer.close();
        }
        catch (Exception e)
        {
            System.out.print(e);
            e.printStackTrace();
        }
   
    }

    public DetectionCoupPince5min getDetectionCoupPinceInstance(){
        return Analyse.DcpInstance;
    }

    public void resetAllVariable()
    {
        this.antenneLocation.clear(); 
        this.mseInfo.clear();
        this.rfidsuralData.clear();
        this.finalrfidsuralData.clear();
        this.UniquePaireRfidsuralData.clear();
        this.readerAnode.clear();
        this.paireAntenneBaliseInOperation.clear();
        this.equipementDefectueux.clear();
        this.indexLine = 0;
    }

}
