import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;

public class DetectionCoupPince {
    private int nbrPtConsecutif = 5;
    private int detectionConsecutif=0;
    private double insertitude = 0.5;
    private int nbrePtbeforeBerne = 10;
    private double minDistance = 7.393;
    private double maxDistance = 15.82;
    private File fileDest;
    private ArrayList<double[]> anode = new ArrayList<>();
    private ArrayList<RfidSural> rfidSuralData;
    private ArrayList<RfidSural> dataCoupPince = new ArrayList<>();
    private int indexStart=0;
    private int indexEnd = 0;
    private double meanDistanceDetect = 0;
    private List<Double> listPointConse = new ArrayList<>();
    private ArrayList<String[]> detection = new ArrayList<>();
    Util utilities = new Util();

    public DetectionCoupPince(ArrayList<RfidSural> rd, File fileDest){
        System.out.println("******DETECTION COUP PINCE *******");
        this.rfidSuralData = rd;
        this.fileDest = fileDest;
        System.out.println("input date : "+rfidSuralData.size());

        putAnodeInArray();
        process();
        write_csv(detection);
    }

    private void process(){

        int ptcons = 0;
        int ptberne = 0;
        for(int i=0; i<rfidSuralData.size();i++){
            //distance dans le range des anodes
            if(rfidSuralData.get(i).distance >= minDistance && rfidSuralData.get(i).distance<=maxDistance)
            {
                //distance consécutif
                //this.indexStart = i;
                if(rfidSuralData.get(i).index==30918){
                    System.out.println("jump in");
                }
                ptcons = pointConsecutif(i);
                //si nbrecount > au nbreconsecutif voulu regarder pour la berne a croute
                if(ptcons!=0){
                    ptberne = pointBerne(ptcons);
                }
                if(ptberne!=0){
                    meanDistanceDetection();
                    i = this.indexStart;
                    for(int a=0;a<anode.size();a++){
                        if(rfidSuralData.get(i).distance > (anode.get(a)[2] - 0.5305) && rfidSuralData.get(i).distance <= (anode.get(a)[2] + 0.5305) )
                        {
                            String[] st = new String[6];
                            st[0] = String.valueOf(anode.get(a)[0])+"-"+anode.get(a)[1];
                            st[1] = String.valueOf(rfidSuralData.get(i).mse);
                            st[2] = String.valueOf(rfidSuralData.get(i).Groupe);
                            st[3] = String.valueOf(this.detectionConsecutif*5)+"sec";
                            st[4] = String.valueOf(utilities.convertDateTimeToString(rfidSuralData.get(i).timestamp));
                            st[5] = String.valueOf(rfidSuralData.get(i).index);
                            detection.add(st);
                            i = this.indexEnd;  //aller directement au dernier index du point de berne a croute detecter.
                            
                        }
                    }
                }
                ptcons = 0;
                ptberne = 0;
                this.detectionConsecutif = 0;
                this.meanDistanceDetect = 0;
                this.indexStart = 0;
                   
            }
        }
    }

    private int pointBerne(int index){
        int berneCount = this.nbrePtbeforeBerne;
        for(int i=index; i<rfidSuralData.size(); i++){
            berneCount--;
            if(rfidSuralData.get(i).distance > 3.8 && rfidSuralData.get(i).distance < 5.2 && berneCount>=0){
                this.indexEnd = i;
                return i;
            }
            if(berneCount<0){
                return 0;
            }
        }
        return 0;
    }

    private int pointConsecutif(int index){
        double tempDistance=0;
        double moinsIncert = 0;
        double plusIncert = 0;
        int count=0;
        tempDistance = rfidSuralData.get(index).distance;
        for(int n = index; n < rfidSuralData.size(); n++){
            moinsIncert = rfidSuralData.get(n).distance - insertitude;
            plusIncert = rfidSuralData.get(n).distance + insertitude;
            if(tempDistance >= moinsIncert  && tempDistance <= plusIncert){

                listPointConse.add(rfidSuralData.get(n).distance);
                count++;
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

    private void meanDistanceDetection(){
        for(int i=0; i<listPointConse.size();i++)
        {
            this.meanDistanceDetect += this.listPointConse.get(i);
        }
        this.meanDistanceDetect = this.meanDistanceDetect/this.listPointConse.size();
        this.listPointConse.clear();
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
            FileWriter writer = new FileWriter(this.fileDest,StandardCharsets.UTF_8); //true for append mode
            writer.write("anode_number;");  
            writer.write("location_name;");  
            writer.write("groupe;");   
            writer.write("scope_time;");  
            writer.write("timestamp;");    
            writer.write("line_number;");
            writer.write("\n");
            
            for(int i=0;i<myArrayList.size();i++){
                for(int j=0; j<myArrayList.get(0).length;j++){
                    writer.write(myArrayList.get(i)[j]);
                    writer.write(";");
                }
                writer.write("\n");
            }
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
