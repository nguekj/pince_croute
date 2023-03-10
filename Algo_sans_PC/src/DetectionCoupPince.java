import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.FileWriter;

public class DetectionCoupPince {
    private int nbrPtConsecutif = 5;
    private int counterPtConsecutif = 0;
    private int detectionConsecutif=0;
    private double insertitude = 0.6;
    private int nbrePtbeforeBerne = 10;
    private int counterPtbeforeBerne = 0;
    private double minDistance = 7.393;
    private double maxDistance = 15.82;
    private int maxScopeTime = 95;
    private int indexStart=0;
    private int indexEnd = 0;
    private double meanDistanceDetect = 0;
    private int lineCounter = 0;
    private File fileDest;
    private static DetectionCoupPince instance;
    private ArrayList<double[]> anode = new ArrayList<>();
    private ArrayList<RfidSural> rfidSuralData = new ArrayList<>();
    private ArrayList<RfidSural> finalrfidSuralData = new ArrayList<>();
    private List<Double> listPointConse = new ArrayList<>();
    private ArrayList<String[]> detection = new ArrayList<>();
    private ArrayList<RfidSural> rfidSuralDataEncoursdeBerne = new ArrayList<>();
    private ArrayList<RfidSural> uniquePaireRfidsuralData = new ArrayList<>();
    private static List<RfidSural> ListDetectionNonComplet = new ArrayList<>();
    private static List<RfidSural> ListeDetectionNonCompletFilter = new ArrayList<>();
    private static List<RfidSural> ListeTempPaire = new ArrayList<>();

    Util utilities = new Util();
    
    public DetectionCoupPince(ArrayList<RfidSural> rd, ArrayList<RfidSural> up, File fileDest){
        System.out.println("******DETECTION COUP PINCE *******");
        this.rfidSuralData = rd;
        this.finalrfidSuralData = rd;
        this.fileDest = fileDest;
        this.uniquePaireRfidsuralData = up;
        System.out.println("input size : "+rfidSuralData.size());

        putAnodeInArray();
        this.uniquePaireRfidsuralData.forEach((u) ->process(u.readerId));
        
        //this.rfidSuralData.get(0).setEnumDetectionNonComplet(RfidSural.DetectionEnCours.ENCOURSDEBERNE);
        //System.out.println(this.rfidSuralData.toString());
        write_csv(detection);
        //Overwrite_csv(detection);
        System.out.println("nombre de detection total : "+this.lineCounter);
    }

    public DetectionCoupPince(){}
    
    public static DetectionCoupPince getInstance() {
        // if (instance == null) {
        //     instance = new DetectionCoupPince(value);
        // }
        return instance;
    }

    private void sortData(String reader){
        ArrayList<RfidSural> rfTemp = new ArrayList<>();
        for(int i=0;i<this.finalrfidSuralData.size();i++){
            if(Objects.equals(this.finalrfidSuralData.get(i).readerId, reader)){
                rfTemp.add(this.finalrfidSuralData.get(i));
            }
        }
        this.rfidSuralData = null;
        this.rfidSuralData = rfTemp;
    }

    private void ListeDetectionNonCompletFilter(String reader){
        ArrayList<RfidSural> ldetct = new ArrayList<>();
        for(int i=0;i<this.ListDetectionNonComplet.size();i++){
            if(Objects.equals(this.ListDetectionNonComplet.get(i).readerId, reader)){
                ldetct.add(this.ListDetectionNonComplet.get(i));
            }
        }
        this.ListeDetectionNonCompletFilter = null;
        this.ListeDetectionNonCompletFilter = ldetct;
    }
    private void ClearPaireListeDetectionNonComplet(String reader){
        for(int i=0;i<this.ListDetectionNonComplet.size();i++){
            if(Objects.equals(this.ListDetectionNonComplet.get(i).readerId, reader)){
                ListDetectionNonComplet.remove(i);
            }
        }
        //this.ListDetectionNonComplet = this.ListDetectionNonComplet.stream().filter(r->reader.equals(r.readerId)).collect(Collectors.toList());
        //this.ListeDetectionNonCompletFilter = null;
        //this.ListeDetectionNonCompletFilter = ldetct;
    }

    private  void process(String readerId){

        int ptcons = 0;
        int ptberne = 0;
        sortData(readerId);
        ListeDetectionNonCompletFilter(readerId);
        
        for(int i=0; i<this.rfidSuralData.size();i++){
            //si j'étais precedemment entrain d'aller dans la berne, continuer la détection de berne.
            //coupPince(i);
            //if(Objects.equals(this.rfidSuralData.get(i).readerId, readerId)){
            //distance dans le range des anodes
            if(this.ListeDetectionNonCompletFilter.size()!=0){
                if(this.ListeDetectionNonCompletFilter.get(this.ListeDetectionNonCompletFilter.size()-1).getEnumDetectionNonComplet()==RfidSural.DetectionEnCours.ENCOURSDEPTCONSECUTIF){
                    encoursdeptconsecutif();
                    i = nouvelleDetection(i);
                    ClearPaireListeDetectionNonComplet(readerId);
                    break;
                }
            }
            else{
                i = nouvelleDetection(i);
            }
            if(i==-1){
                this.ListeTempPaire.forEach((l)-> System.out.println(l.toString()));
                addTempToListNonComplet();
                break;
            }
            
        }
        System.out.println(this.ListDetectionNonComplet.size());
        
    }

    private void encoursdeptconsecutif(){
        this.detectionConsecutif = 0;
        for(int i=0; i<ListeDetectionNonCompletFilter.size();i++){
            if(this.ListeDetectionNonCompletFilter.get(i).getEnumDetectionNonComplet()==RfidSural.DetectionEnCours.ENCOURSDEPTCONSECUTIF){
                this.detectionConsecutif ++;
            }
        }
        this.counterPtConsecutif = this.detectionConsecutif; //je met le counter au nombre de detection deja fait precedemment
    }

    private void addTempToListNonComplet(){
        for(int i=0; i<this.ListeTempPaire.size(); i++){
            //this.ListeTempPaire.get(i).setEnumDetectionNonComplet(RfidSural.DetectionEnCours.ENCOURSDEBERNE);
            this.ListDetectionNonComplet.add(this.ListeTempPaire.get(i));
        }
        this.ListeTempPaire.clear();
    }

    private int nouvelleDetection(int i){
        int ptcons = 0;
        int ptberne = 0;
        if(this.rfidSuralData.get(i).distance >= this.minDistance && this.rfidSuralData.get(i).distance<=this.maxDistance)
        {
            //distance consécutif
            ptcons = pointConsecutif(i);
            //si nbrecount > au nbreconsecutif voulu regarder pour la berne a croute
            if(ptcons>0){
                ptberne = pointBerne(ptcons);
            }
            if(ptberne>0){
                i = this.indexStart;
                coupPince(i);
                ListeTempPaire.clear(); //cp complet
                i = this.indexEnd;
            }

            this.indexStart = 0;
            if(ptcons==-1 || ptberne==-1){
                System.out.println("-----end-----");
                return -1;
            }
        }
        
        //else {ListeTempPaire.clear();}

        ptcons = 0;
        ptberne = 0;
        return i;
    }

    private void coupPince(int i){
        //i = this.indexStart;
        for(int a=0;a<anode.size();a++){
            if(rfidSuralData.get(i).distance > (anode.get(a)[2] - 0.5305) && rfidSuralData.get(i).distance <= (anode.get(a)[2] + 0.5305))
            {
                String[] st = new String[6];
                st[0] = String.valueOf(anode.get(a)[0])+"-"+anode.get(a)[1];
                st[1] = String.valueOf(rfidSuralData.get(i).mse);
                st[2] = String.valueOf(rfidSuralData.get(i).Groupe);
                st[3] = String.valueOf(this.detectionConsecutif*5)+"sec";
                st[4] = String.valueOf(utilities.convertDateTimeToString(rfidSuralData.get(i).timestamp));
                st[5] = String.valueOf(rfidSuralData.get(i).index);
                detection.add(st);
                //i = this.indexEnd;  //aller directement au dernier index du point de berne a croute detecter.
                this.counterPtbeforeBerne = 0;
                this.detectionConsecutif = 0;
                
            }
        }
    }

    private int pointBerne(int index){
        int berneCount = nbrePtbeforeBerne;

        for(int i=index; i<this.rfidSuralData.size(); i++){
            berneCount--;
            this.rfidSuralData.get(i).setEnumDetectionNonComplet(RfidSural.DetectionEnCours.ENCOURSDEBERNE);
            if(this.rfidSuralData.get(i).distance > 3.8 && this.rfidSuralData.get(i).distance < 5.2 && berneCount>=0){
                this.indexEnd = i;
                this.counterPtbeforeBerne = berneCount;
                this.ListeTempPaire.add(this.rfidSuralData.get(i));//
                //if(i+1==rfidSuralData.size()){return -1;}
                return i;
            }
            if(berneCount<0){
                return 0;
            }
            if(i+1==rfidSuralData.size()){this.ListeTempPaire.add(this.rfidSuralData.get(i));return -1;}
        }
        
        return 0;
    }

    private int pointConsecutif(int index){
        double tempDistance=0;
        double moinsIncert = 0;
        double plusIncert = 0;
        int count = this.counterPtConsecutif;
        tempDistance = this.rfidSuralData.get(index).distance;
        for(int n = index; n < this.rfidSuralData.size(); n++){
            moinsIncert = this.rfidSuralData.get(n).distance - insertitude;
            plusIncert = this.rfidSuralData.get(n).distance + insertitude;
            
            if(tempDistance >= moinsIncert  && tempDistance <= plusIncert){
                ListeTempPaire.add(this.rfidSuralData.get(n)); //
                count++;
                this.rfidSuralData.get(n).setEnumDetectionNonComplet(RfidSural.DetectionEnCours.ENCOURSDEPTCONSECUTIF);
                if(n+1==this.rfidSuralData.size()){return -1;}
            }
            else if(count>=this.nbrPtConsecutif){
                this.indexStart = index + (count/2);
                this.detectionConsecutif = count;
                return n;
            }
            else{
                return 0;
            }
        }
        return 0;
    }

    public void resetVariable(){
        //this.nbrPtConsecutif = 5;
        this.detectionConsecutif=0;
        //this.insertitude = 0.6;
        //this.nbrePtbeforeBerne = 10;
        //this.minDistance = 7.393;
        //this.maxDistance = 15.82;
        //this.maxScopeTime = 95;
        this.counterPtbeforeBerne = 0;
        this.fileDest = null;
        this.anode.clear();
        this.rfidSuralData.clear();
        this.indexStart=0;
        this.indexEnd = 0;
        this.meanDistanceDetect = 0;
        this.lineCounter = 0;
        this.listPointConse.clear();
        this.detection.clear();
    }

    public int getcounterPtbeforeBerne(){
        return this.counterPtbeforeBerne;
    }

    public int getcounterPtConsecutif(){
        return this.counterPtConsecutif;
    }

    public int getdetectionConsecutif(){
        return this.detectionConsecutif;
    }

    

    private void putAnodeInArray(){
        double[] a1 = {8,  9,  7.893 };
        double[] a2 = {7, 10,  8.954 };
        double[] a3 = {6, 11, 10.015 };//10,015
        double[] a4 = {5, 12, 11.076};
        double[] a5 = {4, 13, 12.137};
        double[] a6 = {3, 14, 13.198};
        double[] a7 = {2, 15, 14.259};
        double[] a8 = {1, 16,  15.32 };

        this.anode.add(a1);
        this.anode.add(a2);
        this.anode.add(a3);
        this.anode.add(a4);
        this.anode.add(a5);
        this.anode.add(a6);
        this.anode.add(a7);
        this.anode.add(a8);
        
    }

    private void write_csv(ArrayList<String[]> myArrayList){

        try{
            if(!this.fileDest.exists()){
                FileWriter writer = new FileWriter(this.fileDest,StandardCharsets.UTF_8); //true for append mode
                writer.write("anode_number;");  
                writer.write("location_name;");  
                writer.write("groupe;");   
                writer.write("scope_time;");  
                writer.write("timestamp;");    
                writer.write("line_number;");
                writer.write("\n");
                writer.close();
            }
            FileWriter writer = new FileWriter(this.fileDest,StandardCharsets.UTF_8, true);
            for(int i=0;i<myArrayList.size();i++){
                if(Integer.valueOf(myArrayList.get(i)[3].replace("sec", "")) < this.maxScopeTime){
                    for(int j=0; j<myArrayList.get(0).length;j++){
                        //int s = Integer.valueOf(myArrayList.get(i)[3].replace("sec", ""));
                        
                            writer.write(myArrayList.get(i)[j]);
                            writer.write(";");
                        }
                        writer.write("\n");
                        this.lineCounter++;
                }
                
            }
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    private void Overwrite_csv(ArrayList<String[]> myArrayList){

        try{
            
            FileWriter writer = new FileWriter(this.fileDest,StandardCharsets.UTF_8); //true for append mode
            writer.write("anode_number;");  
            writer.write("location_name;");  
            writer.write("groupe;");   
            writer.write("scope_time;");  
            writer.write("timestamp;");    
            writer.write("line_number;");
            writer.write("\n");
            
            for(int i=0;i<myArrayList.size();i++){
                if(Integer.valueOf(myArrayList.get(i)[3].replace("sec", "")) < this.maxScopeTime){
                    for(int j=0; j<myArrayList.get(0).length;j++){
                        //int s = Integer.valueOf(myArrayList.get(i)[3].replace("sec", ""));
                        
                            writer.write(myArrayList.get(i)[j]);
                            writer.write(";");
                        }
                    writer.write("\n");
                    this.lineCounter++;
                }
                
            }
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
