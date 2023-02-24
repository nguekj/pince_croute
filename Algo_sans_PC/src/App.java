import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App {
    public static void main(String[] args) throws Exception {
        String dir1 = "O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/";
        String dir2 = "C:/Users/nguekj/Hiver2023/";

        File fileOperation = new File(dir2+"opération_mse/OPERATION_2023_02_07.csv");
        File fileEquipement = new File(dir2+"Attribue/Liste antene et balise UWB-csv.csv");
        File fileRfidSural = new File(dir2+"données_csv/RFID_SURAL_2023_02_07.csv");
        File fileTraite = new File(dir2+"données_traitées/MSE_RFID_SURAL_2023_02_07.csv");
        File fileDectionAnode = new File("données_détection_anode/DETECTION_RFID_SURAL_2023_02_07.csv");

        Analyse analyse = new Analyse(fileOperation,fileEquipement,fileRfidSural,fileTraite);
        //DetectionAnode da = new DetectionAnode();
        //da.Detection(fileDectionAnode,fileTraite);
        //String[] fileDate = fileOperation.getName().split("OPERATION_")[1].split(".csv");
        //String opDate = fileDate[0].replace('_','-');
        //System.out.println(opDate);
        /*
        //string to datetime wiht file date
        String[] fileDate = fileRfidSural.getName().split("RFID_SURAL_")[1].split(".csv");
        String str = "00:00:00";
        String dt = "2023-02-06 08:00:00";//fileDate[0].replace('_','-')+" "+str;
        LocalDateTime rfidDatetime = LocalDateTime.parse(dt,
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        //LocalDateTime dt = LocalDateTime.parse(rfidDatetime);
        //time = time.minusMinutes(20) ;
  
        //prend le date en lisant la date du fichier et obtenir un datetime avec

        // string to datetime
        LocalDateTime start = LocalDateTime.parse("2023-02-07 21:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        LocalDateTime end = LocalDateTime.parse("2023-02-07 22:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
     

        if((rfidDatetime.compareTo(start.minusMinutes(20))>=0 && 
        rfidDatetime.compareTo(end.minusMinutes(20))<=0) ||
            (rfidDatetime.compareTo(start.minusDays(1).minusMinutes(20))>=0 &&
            rfidDatetime.compareTo(end.minusDays(1))<=0)){
            System.out.println(rfidDatetime);
        }
        */
        //jour("2023-02-06 09:00:00","2023-02-07 08:00:00","2023-02-07 20:00:00"); //jour est good
        //nuit("2023-02-07 01:00:00","2023-02-07 20:00:00","2023-02-07 21:00:00");
        // done work case : "2023-02-07 01:00:00","2023-02-07 02:00:00","2023-02-07 08:00:00"
        // done work case : "2023-02-07 01:00:00","2023-02-07 02:00:00","2023-02-07 08:00:00"
    }

    public static void nuit(String in, String st, String en){
        String mseDate = "2023-02-07";
        LocalDateTime opStart = LocalDateTime.parse("2023-02-07" +" "+"20:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")).minusDays(1).minusMinutes(20);
        LocalDateTime opEnd = LocalDateTime.parse("2023-02-07"+" "+"08:00:00",DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        LocalDateTime inputDateTime = LocalDateTime.parse(in, DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        LocalDateTime startDateTime = LocalDateTime.parse(st, DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));//.minusMinutes(20)
        LocalDateTime endDateTime = LocalDateTime.parse(en, DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        //startDateTime = startDateTime.minusDays(1);
        LocalDateTime nexDay = LocalDateTime.parse(mseDate+" "+"00:00:00",DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        System.out.println(inputDateTime);
        System.out.println(startDateTime);
        System.out.println(inputDateTime.getHour());
        if(startDateTime.getHour()>=20 && startDateTime.getHour()<=23){
            startDateTime = startDateTime.minusDays(1);
        }
        else if(startDateTime.getHour()>=0 && startDateTime.getHour()<=8){
            startDateTime = startDateTime;
        }
        else{
            System.out.println("error startdate");
        }

        if(endDateTime.getHour()>=20 && endDateTime.getHour()<=23){
            endDateTime = endDateTime.minusDays(1);
        }
        else if(endDateTime.getHour()>=0 && endDateTime.getHour()<=8){
            endDateTime = endDateTime;
        }
        else{
            System.out.println("error endDateTime");
        }

        if(inputDateTime.compareTo(opStart)>=0 && inputDateTime.compareTo(opEnd)<=0){
            if(inputDateTime.compareTo(startDateTime.minusMinutes(20))>=0 && inputDateTime.compareTo(endDateTime)<=0)
            {
                System.out.println(true);
            }
        }
        /*
        if(opStart.getDayOfWeek()==inputDateTime.getDayOfWeek() && (inputDateTime.getHour()>0 && inputDateTime.getHour()<8)){
            startDateTime = startDateTime.plusDays(1);
            System.out.println("add +1");
        }
        
        if(inputDateTime.compareTo(opStart)>=0 && inputDateTime.compareTo(opEnd)<=0){
            if(inputDateTime.compareTo(startDateTime)>=0 && inputDateTime.compareTo(endDateTime)<=0)
            {
                System.out.println(true);
            }
        }
        */
    }

    public static void jour(String in, String st, String en){
        LocalDateTime opStart = LocalDateTime.parse("2023-02-07" +" "+"08:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")).minusDays(1).minusMinutes(20);
        LocalDateTime opEnd = LocalDateTime.parse("2023-02-07"+" "+"20:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")).minusDays(1);
        LocalDateTime inputDateTime = LocalDateTime.parse(in, DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        LocalDateTime startDateTime = LocalDateTime.parse(st, DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")).minusMinutes(20);
        LocalDateTime endDateTime = LocalDateTime.parse(en, DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        
        if(inputDateTime.compareTo(opStart)>=0 && inputDateTime.compareTo(opEnd)<=0){
            if(inputDateTime.compareTo(startDateTime.minusDays(1))>=0 && inputDateTime.compareTo(endDateTime.minusDays(1))<=0)
            {
                System.out.println(true);
            }
        }
    }
    
}
