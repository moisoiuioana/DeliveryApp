package common;

/**
 *
 * @author Ioana Moisoiu
 * Class models a staff object which restocks the dishes and cooks orders that are being placed.
 *
 */

public class Staff extends Model {

    private String status;

    public Staff(String name) {
        super(name);
        setStatus("Idle");
        DishRestock dishRestock = new DishRestock(this);
        dishRestock.start();
    }
    @Override
    public String getName() {
        return name;
    }

    public synchronized String getStatus() {
        return status;
    }

    public synchronized void setStatus(String status) {
        this.status = status;
    }
}