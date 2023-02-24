import java.io.*;
import java.util.ArrayList;
import java.util.Objects;


public class Analyse {

    public ArrayList<String[]> antenneLocation = new ArrayList<>();
    public ArrayList<mseEnAnode> mseInfo = new ArrayList<>();
    public ArrayList<RfidSural> rfidsuralData = new ArrayList<>();
    public ArrayList<RfidSural> temprfidsuralData = new ArrayList<>();
    public ArrayList<String[]> readerAnode = new ArrayList<>();
    public ArrayList<String[]> paireAntenneBaliseInOperation = new ArrayList<>();

    Util utilities = new Util();
    public File fileForDetectionAnode;


    public Analyse(File fileOperation, File fileEquipement, File fileRfidSural, File fileDest){

        ReadFile rf = new ReadFile(fileEquipement, fileOperation,fileRfidSural);

        this.antenneLocation = rf.getAntenneLocation();
        this.mseInfo = rf.getMSEOperationInfo();
        this.rfidsuralData = rf.getRfidSural();
        this.fileForDetectionAnode = fileDest;

        rfidSuralDataInMseInfoRange();
        write_csv(temprfidsuralData,fileDest);
    }
    enum shiftEnum{
            NONE,
            DAY,
            NIGHT,
            BOTH
        }
    public void rfidSuralDataInMseInfoRange(){
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
        System.out.println("line total : "+temprfidsuralData.size());
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
        System.out.println("paire utilise :  "+paireAntenneBaliseInOperation.size());
    }

    public void removeUnAnodeMse(){
        ArrayList<RfidSural> tempData = new ArrayList<>();

        for(int i=0;i<rfidsuralData.size();i++){
            for(int j=0; j<paireAntenneBaliseInOperation.size();j++){
                if( Objects.equals(rfidsuralData.get(i).readerId, paireAntenneBaliseInOperation.get(j)[0]) && 
                Objects.equals(rfidsuralData.get(i).tagId, paireAntenneBaliseInOperation.get(j)[1]))
                {
                    tempData.add(rfidsuralData.get(i));
                    //rfidsuralData.remove(i);
                    break;
                }
            }
        }
        rfidsuralData = tempData;
        
        System.out.println("rfidsurad final size : "+rfidsuralData.size());
        write_csv(rfidsuralData, new File("C:/Users/nguekj/Hiver2023/temp.csv"));
    }
    public void shiftCase(shiftEnum se,int i, int j){
        switch (se) {
            case DAY:
                
                if(utilities.isInShiftDayRange(rfidsuralData.get(i).timestamp, mseInfo.get(j).getDayStartTime(),mseInfo.get(j).getDayEndTime()))
                {
                    temprfidsuralData.add(rfidsuralData.get(i));
                }
                
                break;
    
            case NIGHT:
                
                if(utilities.isInShiftNightRange(rfidsuralData.get(i).timestamp, mseInfo.get(j).getNightStartTime(), mseInfo.get(j).getNightEndTime())){
                        temprfidsuralData.add(rfidsuralData.get(i));
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

            PrintWriter writer = new PrintWriter(myfile, encoding);
            
            writer.print("reader_id");
            writer.print(";");
            writer.print("tag_id");
            writer.print(";");
            writer.print("distance");
            writer.print(";");
            writer.print("insert_timestamp");
            writer.print(";"); 
            writer.print("\n");

            for (int j = 0; j < myArrayList.size(); j ++){
                //for (int i = 0; i < myArrayList.get(i).ColumnRange; i ++){
                    
                    writer.print(myArrayList.get(j).readerId);
                    writer.print(";");
                    writer.print(myArrayList.get(j).tagId);
                    writer.print(";"); 
                    writer.print(myArrayList.get(j).distance);
                    writer.print(";"); 
                    writer.print(utilities.convertDateTimeToString(myArrayList.get(j).timestamp));
                    writer.print(";");   
                //}
                writer.print("\n");
            }

            writer.close();
        }
        catch (Exception e)
        {
            System.out.print(e);
            e.printStackTrace();
        }
   
    }

}
