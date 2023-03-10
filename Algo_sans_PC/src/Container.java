import java.io.*;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


public class Container extends Analyse {
    private String dirOnline = "O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/";
    private String dirOffline = "C:/Users/nguekj/Hiver2023/";
    private String dir = dirOnline;
    private String filedate = "2023_02_23";

    private File fileOperation = new File(dir+"opération_mse/OPERATION_"+filedate+".csv");
    private File fileEquipement = new File(dir+"Attribue/Liste antene et balise UWB-csv.csv");
    private File fileRfidSural = new File(dir+"données_csv/RFID_SURAL_"+filedate+".csv");
    private File fileTraite = new File(dir+"/données_analysé_5min/données_traitées/MSE_RFID_SURAL_"+filedate+".csv");
    private File fileDectionAnode = new File(dir+"/données_analysé_5min/données_détection_anode/DETECTION_5min_RFID_SURAL.csv"); //filedate file will only be DETECTION_RFID_SURAL
    
    Util ut = new Util();

    public Container(){
        LocalDateTime start = ut.convertStringToDateTime("2023-02-22 00:00"); //"2023-02-22 21:56" - 2023-02-22 22:01
        LocalDateTime end = ut.convertStringToDateTime("2023-02-23 22:06");
        int dure = 64; //multiplier par 5 pour le nombre de minute 
        if(fileDectionAnode.exists()){
            fileDectionAnode.delete();
        }
        if(fileTraite.exists()){
            fileTraite.delete();
        }
        while (dure!=0){//dure!=0
            try {
                System.out.println(start);
                Analyse analyse = new Analyse(this.fileOperation,this.fileEquipement,this.fileRfidSural,this.fileDectionAnode,this.fileTraite, start); //to generate fichier traité
                analyse.resetAllVariable();
                DetectionCoupPince5min.resetAllVariable();
                analyse.ReadFileInstance.resetAllVariable();
                analyse = null;
                start = start.plusMinutes(30);
                dure--;
            } catch (Exception e) {
                e.printStackTrace();;
            }
            
            
            // try        
            // {
            //     TimeUnit.SECONDS.sleep(2);
            // } 
            // catch(InterruptedException ex) 
            // {
            //     Thread.currentThread().interrupt();
            // }
        }
        System.out.println(DetectionCoupPince5min.getlistnoncompletsize());
    }


}
