import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFile{
    private File fileEquipement;
    private File fileOperationMse;
    private File fileRfidSural;
    private ArrayList<String[]> antenneLocation = new ArrayList<>();
    private ArrayList<mseEnAnode> mseInfo = new ArrayList<>();
    private ArrayList<RfidSural> rfidsural = new ArrayList<>();
    private List<String> balisePC = new ArrayList<>();
     
    public ReadFile(File f_equipement, File f_operationMse, File f_rfidSural){
        this.fileEquipement = f_equipement;
        this.fileOperationMse = f_operationMse;
        this.fileRfidSural = f_rfidSural;
        readEquipement();
        readMSE();
        readRfidSural();
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
                    String [] temp = new String[2];
                    temp[0] = String.valueOf(content.get(i)[3]);
                    temp[1] = String.valueOf(content.get(i)[4]);
                    antenneLocation.add(temp);
                }
            }
            for(int i=27; i<content.size();i++){
                balisePC.add(content.get(i)[2]);
            }
        }
        
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }

    private void readMSE(){
        String line;
        boolean firstLine = true;
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileOperationMse))) {        
            while ((line = br.readLine()) != null){
                mseEnAnode ms = new mseEnAnode(line);
                if (!firstLine){
                    if ((Integer.valueOf(ms.isDayShift()) == 1 || Integer.valueOf(ms.isNightShift()) == 1)){
                        mseInfo.add(ms);
                    }
                }
                firstLine = false;
                //System.out.println(ms.toString());
            }
        }
        catch(Exception e){
            System.out.println(e);
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
                        rfidsural.add(rs);
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

    public ArrayList<mseEnAnode> getMSEOperationInfo(){
        return mseInfo;
    }

    public ArrayList<String[]> getAntenneLocation(){
        return antenneLocation;
    }

    public ArrayList<RfidSural> getRfidSural(){
        return rfidsural;
    }

}