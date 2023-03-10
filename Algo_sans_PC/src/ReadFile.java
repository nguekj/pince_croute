import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.time.LocalDateTime;


public class ReadFile{
    private File fileEquipement;
    private File fileOperationMse;
    private File fileRfidSural;
    private ArrayList<String[]> antenneLocation = new ArrayList<>();
    private ArrayList<mseEnAnode> mseInfo = new ArrayList<>();
    private ArrayList<RfidSural> rfidsural = new ArrayList<>();
    private List<String> balisePC = new ArrayList<>();
    private String OperationDate;
    public LocalDateTime fiveMinuteStart =null;
    public LocalDateTime fiveMinuteEnd = null;
    
    Util utilities = new Util();

    public ReadFile(File f_equipement, File f_operationMse, File f_rfidSural, LocalDateTime dtStart){
        this.fiveMinuteStart = dtStart;
        this.fileEquipement = f_equipement;
        this.fileOperationMse = f_operationMse;
        this.fileRfidSural = f_rfidSural;
        readEquipement();
        readMSE();
        readRfidSural();
        //System.out.println("MSE info size : "+mseInfo.size());
        //System.out.println(rfidsural.size());
    }

    private void readEquipement(){
        String line;
        String[] mots ;
        //String pont = "PONT";
        String tiret = "-";

        try (BufferedReader br = new BufferedReader(new FileReader(this.fileEquipement))) {        

            ArrayList<String[]> content = new ArrayList<String[]>();
            while ((line = br.readLine()) != null){
                mots = line.split(";");
                content.add(mots);
            }
            
            for(int i=1; i<26;i++){
                if ((content.get(i)[3].toLowerCase().contains(tiret)==false) && (content.get(i)[2] != "")){
                    String [] temp = new String[3];
                    temp[0] = String.valueOf(content.get(i)[3]);//reader_id 
                    temp[1] = String.valueOf(content.get(i)[2]);//tag
                    temp[2] = String.valueOf(content.get(i)[4]);//location
                    antenneLocation.add(temp);
                }
            }
            for(int i=27; i<content.size();i++){
                balisePC.add(content.get(i)[2]);
            }
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void readMSE(){
        String line;
        boolean firstLine = true;
        splitOperationDate();
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileOperationMse))) {        
            while ((line = br.readLine()) != null){
                if (!firstLine){
                    mseEnAnode ms = new mseEnAnode(line, this.OperationDate); //this.fileRfidSural change for this.fileOperation
                    if ((ms.isDayShift() == 1 || ms.isNightShift() == 1)){
                        mseInfo.add(ms);
                    }
                }
                firstLine = false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void readRfidSural(){
        String line;
        boolean firstLine = true;
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileRfidSural))) {        
            while ((line = br.readLine()) != null){
                if (!firstLine){
                    RfidSural rs = new RfidSural(line);
                    if(!balisePC.contains(rs.tagId)){
                        //String d = "2023-02-22 21:56";
                        if(rs.timestamp.compareTo(this.fiveMinuteStart)>=0 && rs.timestamp.compareTo(this.fiveMinuteStart.plusMinutes(30))<=0){
                            rfidsural.add(rs);
                        }
                    }
                }
                firstLine=false;
            }
        }
        catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> getUnUsedEquipement(){
        ArrayList<String[]> temp = new ArrayList<>();
        ArrayList<String[]> tempEquipment = new ArrayList<>(antenneLocation);
        //tempEquipment = antenneLocation;
        //tempEquipment.addAll(antenneLocation);
        for(int j=0; j<this.rfidsural.size(); j++){
            for (int i=0; i<tempEquipment.size();i++){
                if(Objects.equals(String.valueOf(this.rfidsural.get(j).readerId),tempEquipment.get(i)[0]) && Objects.equals(String.valueOf(this.rfidsural.get(j).tagId),tempEquipment.get(i)[1])){
                    String[] s = new String[3];
                    s[0] =  tempEquipment.get(i)[0];
                    s[1] =  tempEquipment.get(i)[1];
                    s[2] =  tempEquipment.get(i)[2];
                    temp.add(s);
                    tempEquipment.remove(i);
                    break;
                }
            }
            if(tempEquipment.size()==0){
                break;
            }
        }

        //System.out.println("nombre d'equipement non utiliser : "+tempEquipment.size());
        //tempEquipment.forEach((n) -> System.out.println(n[2]+" reader "+n[0]+" tag "+n[1]));
        return tempEquipment;
    }

    public ArrayList<mseEnAnode> getMSEOperationInfo(){
        return mseInfo;
    }

    public ArrayList<String[]> getAntenneLocation(){
        return antenneLocation;
    }

    public ArrayList<RfidSural> getRfidSural(){
        return rfidsural;
    }

    public void splitOperationDate(){
        String[] fileDate = this.fileOperationMse.getName().split("OPERATION_")[1].split(".csv");
        String opDate = fileDate[0].replace('_','-');
        this.OperationDate = opDate;
    }
    	
    public void resetAllVariable(){
        this.antenneLocation.clear();
        this.mseInfo.clear();
        this.rfidsural.clear();
        this.balisePC.clear();
    }
}
