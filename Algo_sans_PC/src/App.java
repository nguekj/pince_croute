import java.io.*;

public class App {
    public static void main(String[] args) throws Exception {
        File fileOperation = new File("O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/opération_mse/pour_mes_test_2023_02_21.csv");
        File fileEquipement = new File("O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/Attribue/Liste antene et balise UWB-csv.csv");
        File fileRfidSural = new File("O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/données_csv/RFID_SURAL_2023_02_07.csv");
        File fileDest = new File("O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/opération_write/MSE_RFID_SURAL_2023_02_07.csv");
        
        Analyse analyse = new Analyse(fileOperation,fileEquipement,fileRfidSural,fileDest);
        
        /*
        //string to datetime wiht file date
        String[] fileDate = fileRfidSural.getName().split("RFID_SURAL_")[1].split(".csv");
        String str = "23:55:00";
        String rfidDatetime = fileDate[0].replace('_','-')+" "+str;
        LocalDateTime dt = LocalDateTime.parse(rfidDatetime,
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        //LocalDateTime dt = LocalDateTime.parse(rfidDatetime);
        //time = time.minusMinutes(20) ;
        System.out.println(dt);
        //prend le date en lisant la date du fichier et obtenir un datetime avec

        // string to datetime
        LocalDateTime dateTime2 = LocalDateTime.parse("2023-02-07 23:54:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        System.out.println("string to datetime " + dateTime2);
        //dateTime2 = dateTime2.plusMinutes(5);
        if (dt.compareTo(dateTime2)==0){
            System.out.println(true);
        }
        
        //datetime to string
        //LocalDateTime  myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateTime2.format(myFormatObj);
        System.out.println("datetime tostring: " + formattedDate);
        */

    }

    
}
