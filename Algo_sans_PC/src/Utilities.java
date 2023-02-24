import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

abstract class Utilities {
    public static String MseOperationFileDate;

    public static LocalDateTime convertMseTimeToDateTime(String str ){
        
        String opDatetime = MseOperationFileDate+" "+str;

        LocalDateTime dt = LocalDateTime.parse(opDatetime,
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        return dt;
    }

    public LocalDateTime convertStringToDateTime(String str){
        LocalDateTime dt = LocalDateTime.parse(str,
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm"));
        return dt;
    }
    
    public String convertDateTimeToString(LocalDateTime dt){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = dt.format(myFormatObj);
        return formattedDate;
    }

    public boolean isInShiftNightRange(LocalDateTime inputDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime){
        LocalDateTime opStart = LocalDateTime.parse(this.MseOperationFileDate+" "+"20:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")).minusDays(1).minusMinutes(20);
        LocalDateTime opEnd = LocalDateTime.parse(this.MseOperationFileDate+" "+"08:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        //startDateTime = startDateTime.minusDays(1);
        if(startDateTime.getHour()>=20 && startDateTime.getHour()<=23){
            startDateTime = startDateTime.minusDays(1);
        }
        else if(startDateTime.getHour()>=0 && startDateTime.getHour()<=8){
            startDateTime = startDateTime;
        }

        if(endDateTime.getHour()>=20 && endDateTime.getHour()<=23){
            endDateTime = endDateTime.minusDays(1);
        }
        else if(endDateTime.getHour()>=0 && endDateTime.getHour()<=8){
            endDateTime = endDateTime;
        }
        
        
        
        /*
        if(opStart.getDayOfWeek()==inputDateTime.getDayOfWeek() && (inputDateTime.getHour()>0 && inputDateTime.getHour()<8)){
            startDateTime = startDateTime.plusDays(1);
        }*/

        if(inputDateTime.compareTo(opStart)>=0 && inputDateTime.compareTo(opEnd)<=0){
            if(inputDateTime.compareTo(startDateTime.minusMinutes(20))>=0 && inputDateTime.compareTo(endDateTime)<=0)
            {
                return true;
            }
        }
        
        return false;
    }

    public boolean isInShiftDayRange(LocalDateTime inputDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime){
        LocalDateTime opStart = LocalDateTime.parse(this.MseOperationFileDate+" "+"08:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")).minusDays(1).minusMinutes(20);
        LocalDateTime opEnd = LocalDateTime.parse(this.MseOperationFileDate+" "+"20:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")).minusDays(1);

        if(inputDateTime.compareTo(opStart)>=0 && inputDateTime.compareTo(opEnd)<=0){
            if(inputDateTime.compareTo(startDateTime.minusDays(1).minusMinutes(20))>=0 && inputDateTime.compareTo(endDateTime.minusDays(1))<=0)
            {
                return true;
            }
            
        }

        return false;
    }
}

class Util extends Utilities{

}
