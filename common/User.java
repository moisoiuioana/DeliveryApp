package common;

import java.io.Serializable;

/**
 *
 * @author Ioana Moisoiu
 * Class models a user object in which details about clients are stored.
 *
 */

public class User extends Model implements Serializable{

    private String username;
    private String password;
    private String address;
    private Postcode postcode;

    public User(String username, String password, String address, Postcode postcode) {
        super();
        this.username = username;
        this.password = password;
        this.address = address;
        this.postcode = postcode;;
    }

    @Override
    public String getName() {
        return username;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){ return password; }

    public String getAddress() { return address; }

    public Postcode getPostcode() { return postcode; }

}