package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ioana Moisoiu
 * Class models a postcode object which represents the address of a client.
 *
 */

public class Postcode extends Model implements Serializable{

    private List<Postcode> postcodes = new ArrayList<>();
    private Number distance;

    public Postcode(){
        createPostcodes();
    }

    public Postcode(String name, Number distance) {
        super(name);
        this.distance = distance;
        postcodes.add(this);
    }

    @Override
    public String getName() {
        return name;
    }

    public void createPostcodes(){
        Postcode postcode1 = new Postcode("SO16 0AA",2);
        postcodes.add(postcode1);
        Postcode postcode2 = new Postcode("SO16 0AB",3);
        postcodes.add(postcode2);
        Postcode postcode3 = new Postcode("SO16 0AC",4);
        postcodes.add(postcode3);
        Postcode postcode4 = new Postcode("SO16 0AD",5);
        postcodes.add(postcode4);
        Postcode postcode5 = new Postcode("SO16 0AE",6);
        postcodes.add(postcode5);
    }

    public Number getDistance(){ return distance; }

    public List<Postcode> getPostcodes(){ return postcodes; }
}
