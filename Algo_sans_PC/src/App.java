import java.io.*;
//import java.util.ArrayList;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class App {
    public static void main(String[] args) throws Exception {
        File fileOperation = new File("O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/opération_mse/pour_mes_test_2023_02_21.csv");
        File fileEquipement = new File("O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/Attribue/Liste antene et balise UWB-csv.csv");
        File fileRfidSural = new File("O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/données_csv/RFID_SURAL_2023_02_07.csv");
        /* 
        ReadFile rf = new ReadFile(fileEquipement, fileOperation,fileRfidSural);

        System.out.println(rf.getMSEOperationInfo().toString());
        System.out.println("------------------------------");
        System.out.println(rf.getAntenneLocation().get(1)[0].toString());
        System.out.println("------------------------------");
        */

        //string to time
        String str = "23:55:00";
        LocalTime time = LocalTime.parse(str);
        //time = time.minusMinutes(20) ;
        System.out.println(time);
        //prend le date en lisant la date du fichier et obtenir un datetime avec

        //datetime to string
        LocalDateTime  myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        System.out.println("After formatting: " + formattedDate);

        //string to datetime
        //LocalDateTime dateTime = LocalDateTime.parse(dateString);

        LocalDateTime dateTime2 = LocalDateTime.parse("2017-09-25 23:55:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        dateTime2.getHour();
        System.out.println("string to datetime " + dateTime2);
        
        if (time.getHour()==dateTime2.getHour() && time.getMinute()==dateTime2.getMinute() ){
            System.out.println(true);
        }



    }

    
}
