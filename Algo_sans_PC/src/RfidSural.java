import java.time.LocalDateTime;

public class RfidSural {
    protected String readerId;
    protected String tagId;
    protected String distance;
    protected String mse;
    protected String Groupe;
    //protected String NightGroup;
    protected int index;
    protected LocalDateTime timestamp;
    Util utilities = new Util();

    public RfidSural(String rowcsv){
        String[] line = null;
        line = rowcsv.split(";");
        this.readerId = line[0];
        this.tagId = line[1];
        this.distance = line[2].replace(',', '.');
        this.timestamp = utilities.convertStringToDateTime(line[3]);
        
    }
    public void removeAllPC(){

    }

    @Override
    public String toString(){
        return this.readerId + " " + this.tagId + " " + this.distance + " " + this.timestamp;
    }
    
}
