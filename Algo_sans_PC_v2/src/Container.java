import java.io.*;



public class Container extends Analyse {
    private String dirOnline = "O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/";
    private String dirOffline = "C:/Users/nguekj/Hiver2023/";
    private String dir = dirOnline;
    //Scanner myScanner = new Scanner(System.in);
    //System.out.println("Enter wanted date yyyy_mm_dd: "); //2023_02_23
    private String filedate = "2023_02_23"; //myScanner.nextLine();

    //myScanner.close();
    private File fileOperation = new File(dir+"opération_mse/OPERATION_"+filedate+".csv");
    private File fileEquipement = new File(dir+"Attribue/Liste antene et balise UWB-csv.csv");
    private File fileRfidSural = new File(dir+"données_csv/RFID_SURAL_"+filedate+".csv");
    private File fileTraite = new File(dir+"données_traitées/MSE_RFID_SURAL_"+filedate+".csv");
    private File fileDectionAnode = new File(dir+"données_détection_anode/DETECTION_YVAN_dont_open_RFID_SURAL.csv"); //filedate file will only be DETECTION_RFID_SURAL
    
    //Analyse analyse = new Analyse(fileOperation,fileEquipement,fileRfidSural,fileDectionAnode);

    DetectionCoupPince dectectionCP = DetectionCoupPince.getInstance();
    
    //System.out.println(dectectionCP.message());
    public Container(){
        Analyse analyse = new Analyse(this.fileOperation,this.fileEquipement,this.fileRfidSural,this.fileDectionAnode,this.fileTraite); //to generate fichier traité
        System.out.println(analyse.getDetectionCoupPinceInstance().getcounterPtbeforeBerne());
        System.out.println(analyse.getDetectionCoupPinceInstance().getdetectionConsecutif());
        


    }


}
