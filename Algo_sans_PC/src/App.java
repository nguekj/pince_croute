import java.io.*;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App {
    public static void main(String[] args) throws Exception {
        String dirOnline = "O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/";
        String dirOffline = "C:/Users/nguekj/Hiver2023/";
        String dir = dirOnline;
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Enter wanted date yyyy_mm_dd: ");
        String filedate = myScanner.nextLine();

        myScanner.close();
        File fileOperation = new File(dir+"opération_mse/OPERATION_"+filedate+".csv");
        File fileEquipement = new File(dir+"Attribue/Liste antene et balise UWB-csv.csv");
        File fileRfidSural = new File(dir+"données_csv/RFID_SURAL_"+filedate+".csv");
        File fileTraite = new File(dir+"données_traitées/MSE_RFID_SURAL_"+filedate+".csv");
        File fileDectionAnode = new File(dir+"données_détection_anode/DETECTION_RFID_SURAL_"+filedate+".csv");
        
        Analyse analyse = new Analyse(fileOperation,fileEquipement,fileRfidSural,fileTraite);
        DetectionAnode da = new DetectionAnode();
        da.Detection(fileDectionAnode,fileTraite);
        
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
