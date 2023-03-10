import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.FileWriter;

public class DetectionCoupPince5min {
    private static int nbrPtConsecutif = 5;
    private static int counterPtConsecutif = 0;
    private static int detectionConsecutif=0;
    private static double insertitude = 0.6;
    private static int nbrePtbeforeBerne = 10;
    private static int counterPtbeforeBerne = 0;
    private static double minDistance = 7.393;
    private static double maxDistance = 15.82;
    private static int maxScopeTime = 95;
    private static int indexStart=0;
    private static int indexEnd = 0;
    private double meanDistanceDetect = 0;
    private static int lineCounter = 0;
    private static File fileDest;
    private static DetectionCoupPince instance;
    private static ArrayList<double[]> anode = new ArrayList<>();
    private static ArrayList<RfidSural> rfidSuralData = new ArrayList<>();
    private static ArrayList<RfidSural> finalrfidSuralData = new ArrayList<>();
    private static ArrayList<String[]> detection = new ArrayList<>();
    private ArrayList<RfidSural> rfidSuralDataEncoursdeBerne = new ArrayList<>();
    private ArrayList<RfidSural> uniquePaireRfidsuralData = new ArrayList<>();
    private static List<RfidSural> ListDetectionNonComplet = new ArrayList<>();
    private static List<RfidSural> ListeDetectionNonCompletFilter = new ArrayList<>();
    private static List<RfidSural> ListeTempPaire = new ArrayList<>();
    private static List<RfidSural> ListeToContinuesDetection = new ArrayList<>();

    static Util utilities = new Util();

    public DetectionCoupPince5min(ArrayList<RfidSural> rd, ArrayList<RfidSural> up, File fileDest){
        //System.out.println("******DETECTION COUP PINCE *******");
        DetectionCoupPince5min.rfidSuralData = rd;
        DetectionCoupPince5min.finalrfidSuralData = rd;
        DetectionCoupPince5min.fileDest = fileDest;
        this.uniquePaireRfidsuralData = up;
        putAnodeInArray();

        this.uniquePaireRfidsuralData.forEach((u) ->process(u.readerId));
        
        write_csv(detection);
        //Overwrite_csv(detection);
        //System.out.println("nombre de detection total : "+DetectionCoupPince5min.lineCounter);
    }

    public DetectionCoupPince5min(){}

    private static void sortData(String reader){
        ArrayList<RfidSural> rfTemp = new ArrayList<>();
        for(int i=0;i<DetectionCoupPince5min.finalrfidSuralData.size();i++){
            if(Objects.equals(DetectionCoupPince5min.finalrfidSuralData.get(i).readerId, reader)){
                rfTemp.add(DetectionCoupPince5min.finalrfidSuralData.get(i));
            }
        }
        DetectionCoupPince5min.rfidSuralData = null;
        DetectionCoupPince5min.rfidSuralData = rfTemp;
    }
    
    private static void process(String readerid ){
        int ptcons = 0;
        int ptberne = 0;
        sortData(readerid);
        /*
        if(Objects.equals(readerid, "23819") ){
            int sf =5;
        }
         */
        if(DetectionCoupPince5min.ListDetectionNonComplet.stream().anyMatch(item -> readerid.equals(item.readerId))){
            //List<RfidSural> filterList = new ArrayList<>();
            //filterList = ListDetectionNonComplet.stream().filter(item -> readerid.equals(item.readerId)).collect(Collectors.toList());
            DetectionCoupPince5min.rfidSuralData.addAll(0,ListDetectionNonComplet.stream().filter(item -> readerid.equals(item.readerId)).collect(Collectors.toList()) );
        }

        for(int i=0; i<DetectionCoupPince5min.rfidSuralData.size();i++){
            
            if(DetectionCoupPince5min.rfidSuralData.get(i).distance >= minDistance && DetectionCoupPince5min.rfidSuralData.get(i).distance<=maxDistance)
            {
                /* 
                if(Objects.equals(DetectionCoupPince5min.rfidSuralData.get(i).distance, 15.08) ){
                    int ssdf =5;
                }
                */
                ptcons = pointConsecutif(i);
                if(ptcons>0){
                    ptberne = pointBerne(ptcons);
                }
                if(ptberne>0){
                    i = DetectionCoupPince5min.indexStart;
                    coupPince(i);
                    DetectionCoupPince5min.ListeTempPaire.clear(); //cp complet
                    DetectionCoupPince5min.ListDetectionNonComplet.removeIf(item -> readerid.equals(item.readerId)); 
                    i = DetectionCoupPince5min.indexEnd;
                }
                
            }

            if(ptberne==-1 || ptcons==-1){
                //DetectionCoupPince5min.ListeTempPaire.forEach((l)-> System.out.println(l.toString()));
                DetectionCoupPince5min.ListDetectionNonComplet.addAll(ListeTempPaire);
                //System.out.println(DetectionCoupPince5min.ListDetectionNonComplet.size());
                break;
            }
            ptcons = 0;
            ptberne = 0;
            indexStart = 0;
        }
        resetVaraible();

    }

    /*
     * @param index start of berne
     * @return 0 if there is no berne detected
     * @return -1 if end of detection
     */
    private static int pointBerne(int index){
        int berneCount = DetectionCoupPince5min.nbrePtbeforeBerne;
        for(int i=index; i<DetectionCoupPince5min.rfidSuralData.size(); i++){
            berneCount--;
            DetectionCoupPince5min.rfidSuralData.get(i).setEnumDetectionNonComplet(RfidSural.DetectionEnCours.ENCOURSDEBERNE);
            DetectionCoupPince5min.rfidSuralData.get(i).counterPtBerne = berneCount;
            DetectionCoupPince5min.ListeTempPaire.add(DetectionCoupPince5min.rfidSuralData.get(i));
            if(DetectionCoupPince5min.rfidSuralData.get(i).distance > 3.8 && DetectionCoupPince5min.rfidSuralData.get(i).distance < 5.2 && berneCount>=0){
                DetectionCoupPince5min.indexEnd = i;
                DetectionCoupPince5min.counterPtbeforeBerne = berneCount;
                DetectionCoupPince5min.ListeTempPaire.add(DetectionCoupPince5min.rfidSuralData.get(i));
                return i;
            }
            if(berneCount<0){
                return 0;
            }
            if(i+1==DetectionCoupPince5min.rfidSuralData.size()){return -1;}
        }
        return 0;
    }

    
    private static int pointBerneSuite()
    {
        return 0;
    }

    /*
     * @param index start of consecutif point
     * @return 0 if there is no min consecutif point
     * @return -1 if end of detection
     */
    private static int pointConsecutif(int index){
        double tempDistance=0;
        double moinsIncert = 0;
        double plusIncert = 0;
        int count = DetectionCoupPince5min.counterPtConsecutif;
        tempDistance = DetectionCoupPince5min.rfidSuralData.get(index).distance;
        for(int n = index; n < DetectionCoupPince5min.rfidSuralData.size(); n++){
            moinsIncert = DetectionCoupPince5min.rfidSuralData.get(n).distance - insertitude;
            plusIncert = DetectionCoupPince5min.rfidSuralData.get(n).distance + insertitude;
            if(tempDistance >= moinsIncert  && tempDistance <= plusIncert){
                count++;
                DetectionCoupPince5min.ListeTempPaire.add(DetectionCoupPince5min.rfidSuralData.get(n)); //
                DetectionCoupPince5min.rfidSuralData.get(n).setEnumDetectionNonComplet(RfidSural.DetectionEnCours.ENCOURSDEPTCONSECUTIF);
                DetectionCoupPince5min.rfidSuralData.get(n).counterPtConsecutif = count;
                if(n+1==DetectionCoupPince5min.rfidSuralData.size()){
                    return -1;}
            }
            else if(count>=nbrPtConsecutif){
                DetectionCoupPince5min.indexStart = index + (count/2);
                DetectionCoupPince5min.detectionConsecutif = count;
                return n;
            }
            else{
                return 0;
            }
        }
        return 0;
    }

    private static int pointConsecutifSuite()
    {
        return 0;
    }

    private static void coupPince(int i){
        for(int a=0;a<anode.size();a++){
            if(rfidSuralData.get(i).distance > (anode.get(a)[2] - 0.5305) && rfidSuralData.get(i).distance <= (anode.get(a)[2] + 0.5305))
            {
                String[] st = new String[6];
                st[0] = String.valueOf(anode.get(a)[0])+"-"+anode.get(a)[1];
                st[1] = String.valueOf(rfidSuralData.get(i).mse);
                st[2] = String.valueOf(rfidSuralData.get(i).Groupe);
                st[3] = String.valueOf(detectionConsecutif*5)+"sec";
                st[4] = String.valueOf(utilities.convertDateTimeToString(rfidSuralData.get(i).timestamp));
                st[5] = String.valueOf(rfidSuralData.get(i).index);
                detection.removeIf(s -> s.equals(st));
                detection.add(st);
                counterPtbeforeBerne = 0;
                
            }
        }
    }

    public static void resetAllVariable(){
        DetectionCoupPince5min.detectionConsecutif=0;
        DetectionCoupPince5min.counterPtbeforeBerne = 0;
        DetectionCoupPince5min.counterPtConsecutif = 0;
        DetectionCoupPince5min.fileDest = null;
        DetectionCoupPince5min.rfidSuralData.clear();
        DetectionCoupPince5min.indexStart=0;
        DetectionCoupPince5min.indexEnd = 0;
        lineCounter = 0;
        DetectionCoupPince5min.detection.clear();
        DetectionCoupPince5min.ListeTempPaire.clear();
    }
    
    public static int getlistnoncompletsize(){return DetectionCoupPince5min.ListDetectionNonComplet.size();}
    
    private static void resetVaraible(){
        DetectionCoupPince5min.counterPtbeforeBerne = 0;
        DetectionCoupPince5min.counterPtConsecutif = 0;
        DetectionCoupPince5min.detectionConsecutif=0;
        DetectionCoupPince5min.indexStart=0;
        DetectionCoupPince5min.indexEnd = 0;
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

        DetectionCoupPince5min.anode.add(a1);
        DetectionCoupPince5min.anode.add(a2);
        DetectionCoupPince5min.anode.add(a3);
        DetectionCoupPince5min.anode.add(a4);
        DetectionCoupPince5min.anode.add(a5);
        DetectionCoupPince5min.anode.add(a6);
        DetectionCoupPince5min.anode.add(a7);
        DetectionCoupPince5min.anode.add(a8);
        
    }

    private static void write_csv(ArrayList<String[]> myArrayList){

        try{
            if(!DetectionCoupPince5min.fileDest.exists()){
                FileWriter writer = new FileWriter(DetectionCoupPince5min.fileDest,StandardCharsets.UTF_8); //true for append mode
                writer.write("anode_number;");  
                writer.write("location_name;");  
                writer.write("groupe;");   
                writer.write("scope_time;");  
                writer.write("timestamp;");    
                writer.write("line_number;");
                writer.write("\n");
                writer.close();
            }
            FileWriter writer = new FileWriter(DetectionCoupPince5min.fileDest,StandardCharsets.UTF_8, true);
            for(int i=0;i<myArrayList.size();i++){
                if(Integer.valueOf(myArrayList.get(i)[3].replace("sec", "")) < DetectionCoupPince5min.maxScopeTime){
                    for(int j=0; j<myArrayList.get(0).length;j++){
                            writer.write(myArrayList.get(i)[j]);
                            writer.write(";");
                        }
                        writer.write("\n");
                        DetectionCoupPince5min.lineCounter++;
                }
            }
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private static void Overwrite_csv(ArrayList<String[]> myArrayList){

        try{
            
            FileWriter writer = new FileWriter(DetectionCoupPince5min.fileDest,StandardCharsets.UTF_8); //true for append mode
            writer.write("anode_number;");  
            writer.write("location_name;");  
            writer.write("groupe;");   
            writer.write("scope_time;");  
            writer.write("timestamp;");    
            writer.write("line_number;");
            writer.write("\n");
            
            for(int i=0;i<myArrayList.size();i++){
                if(Integer.valueOf(myArrayList.get(i)[3].replace("sec", "")) < DetectionCoupPince5min.maxScopeTime){
                    for(int j=0; j<myArrayList.get(0).length;j++){
                        //int s = Integer.valueOf(myArrayList.get(i)[3].replace("sec", ""));
                        
                            writer.write(myArrayList.get(i)[j]);
                            writer.write(";");
                        }
                    writer.write("\n");
                    DetectionCoupPince5min.lineCounter++;
                }
                
            }
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
