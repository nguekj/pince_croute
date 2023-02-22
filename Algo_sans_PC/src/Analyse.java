import java.io.*;
import java.util.ArrayList;



public class Analyse {

    public ArrayList<String[]> antenneLocation = new ArrayList<>();
    public ArrayList<mseEnAnode> mseInfo = new ArrayList<>();
    public ArrayList<RfidSural> rfidsuralData = new ArrayList<>();
    public ArrayList<RfidSural> temprfidsuralData = new ArrayList<>();
    public ArrayList<String[]> readerAnode = new ArrayList<>();
    Util utilities = new Util();


    public Analyse(File fileOperation, File fileEquipement, File fileRfidSural, File fileDest){

        ReadFile rf = new ReadFile(fileEquipement, fileOperation,fileRfidSural);
        
        this.antenneLocation = rf.getAntenneLocation();
        this.mseInfo = rf.getMSEOperationInfo();
        this.rfidsuralData = rf.getRfidSural();
        rfidSuralDataInMseInfoRange();
        write_csv(temprfidsuralData,fileDest);
    }

    public void rfidSuralDataInMseInfoRange(){
        int combine = 0;
        for (int j=0; j<mseInfo.size(); j++){
            for(int i=0;i<rfidsuralData.size();i++){
            
                for(int h=0; h<antenneLocation.size();h++){
                    //System.out.println(antenneLocation.get(h)[0]);
                    //System.out.println(rfidsuralData.get(i).readerId);
                    if (antenneLocation.get(h)[0].compareTo(rfidsuralData.get(i).readerId)==0){
                        if(mseInfo.get(j).isDayShift()==1 && mseInfo.get(j).isNightShift()==0){
                            combine = 1;
                            //System.out.println(rfidsuralData.get(i).toString());
                        }
                        else if(mseInfo.get(j).isDayShift()==0 && mseInfo.get(j).isNightShift()==1){
                            combine = 2;
                        }
                        else if(mseInfo.get(j).isDayShift()==1 && mseInfo.get(j).isNightShift()==1){
                            combine = 3;
                        }

                        switch (combine) {
                            case 1:
                                if(rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getDayStartTime().minusMinutes(20))>=0 && 
                                   rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getDayEndTime().plusMinutes(20))<=0){
                                    temprfidsuralData.add(rfidsuralData.get(i));
                                    //System.out.println("case 1 : "+temprfidsuralData.size());                                    
                                }
                                else if(rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getDayStartTime().minusDays(1).minusMinutes(20))>=0 && 
                                        rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getDayEndTime().minusDays(1).plusMinutes(20))<=0){
                                    temprfidsuralData.add(rfidsuralData.get(i));
                                    //System.out.println("case 1 : "+temprfidsuralData.size());                                    
                                }
                                break;
                    
                            case 2:
                                if(rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getNightStartTime().minusMinutes(20))>=0 && 
                                   rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getNightEndTime().plusMinutes(20))<=0){
                                    temprfidsuralData.add(rfidsuralData.get(i));
                                    //System.out.println("case 1 : "+temprfidsuralData.size());                                    
                                }
                                else if(rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getNightStartTime().minusDays(1).minusMinutes(20))>=0 && 
                                        rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getNightEndTime().minusDays(1).plusMinutes(20))<=0){
                                    temprfidsuralData.add(rfidsuralData.get(i));
                                    //System.out.println("case 1 : "+temprfidsuralData.size());                                    
                                }
                                break;
                    
                            case 3:
                                if(rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getDayStartTime().minusMinutes(20))>=0 && 
                                   rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getDayEndTime().plusMinutes(20))<=0 &&
                                   rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getNightStartTime().minusMinutes(20))>=0 && 
                                   rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getNightEndTime().plusMinutes(20))<=0){
                                    temprfidsuralData.add(rfidsuralData.get(i));
                                    //System.out.println("case 1 : "+temprfidsuralData.size());                                    
                                }
                                else if(rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getDayStartTime().minusDays(1).minusMinutes(20))>=0 && 
                                        rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getDayEndTime().minusDays(1).plusMinutes(20))<=0 &&
                                        rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getNightStartTime().minusDays(1).minusMinutes(20))>=0 && 
                                        rfidsuralData.get(i).timestamp.compareTo(mseInfo.get(j).getNightEndTime().minusDays(1).plusMinutes(20))<=0){
                                    temprfidsuralData.add(rfidsuralData.get(i));
                                    //System.out.println("case 1 : "+temprfidsuralData.size());                                    
                                }
                                break;

                            // Default case
                            default:
                                System.out.println("error");
                        }
                        break;
                    }
                }
            }
        }
        System.out.println(temprfidsuralData.size());
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
