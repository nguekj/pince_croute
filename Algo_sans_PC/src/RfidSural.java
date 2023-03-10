import java.time.LocalDateTime;

public class RfidSural {
    protected String readerId;
    protected String tagId;
    protected double distance;
    protected String mse;
    protected String Groupe;
    //protected String NightGroup;
    protected int index;
    protected LocalDateTime timestamp;
    protected int counterPtConsecutif;
    protected int counterPtBerne;
    Util utilities = new Util();

    protected enum DetectionEnCours{
        ENCOURSDEBERNE,
        ENCOURSDEPTCONSECUTIF,
        NONE
    }
    protected DetectionEnCours detectionEnCours = DetectionEnCours.NONE;
    
    public RfidSural(String rowcsv){
        String[] line = null;
        line = rowcsv.split(";");
        this.readerId = line[0];
        this.tagId = line[1];
        this.distance = Double.valueOf(line[2].replace(',', '.'));
        this.timestamp = utilities.convertStringToDateTime(line[3]);
    }

    public RfidSural(){}
     
    public void setEnumDetectionNonComplet(DetectionEnCours n){
        detectionEnCours = n;
    }
    public DetectionEnCours getEnumDetectionNonComplet(){return this.detectionEnCours;}

    @Override
    public String toString(){
        return this.readerId + " " + this.tagId + " " + this.distance + " " + this.timestamp + " " +detectionEnCours+ " " +counterPtConsecutif+ " " +counterPtBerne;
    }
    
}
