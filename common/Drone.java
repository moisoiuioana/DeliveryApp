package common;

import java.io.Serializable;

/**
 *
 * @author Ioana Moisoiu
 * Class models a drone object which can restock ingredients and deliver orders.
 *
 */

public class Drone extends Model implements Serializable{

    private Number speed;
    private String status;

    public Drone(Number speed) {
        setName("Drone");
        this.speed = speed;
        status = "Idle";
        IngredientRestock ingredientRestock = new IngredientRestock(this);
    }

    @Override
    public String getName() { return name; }

    public Number getSpeed() { return speed; }

    public void setSpeed(Number speed) { this.speed = speed; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

}
