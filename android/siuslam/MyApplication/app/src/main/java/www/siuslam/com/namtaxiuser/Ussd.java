package www.siuslam.com.namtaxiuser;

/**
 * Created by Gervasius.Ishuuwa on 10/4/2015.
 */
public class Ussd {
    private String code;
    private String cell;
    private String network;
    private String location;
    public Ussd()
    {

    }
    public Ussd(String code,String cell,String network,String location)
    {
        this.setCode(code);
        this.setCell(cell);
        this.setNetwork(network);
        this.setLocation(location);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
