package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ioana Moisoiu
 * Class models a supplier object from where ingredients can be restocked.
 *
 */

public class Supplier extends Model implements Serializable{

    private Number distance;
    private List<Ingredient> ingredients = new ArrayList<>();

    public Supplier(String name, Number distance){
        super(name);
        this.distance = distance;
    }
    @Override
    public String getName() { return name; }

    public Number getDistance() { return distance; }

    public void addIngredient( Ingredient ingredient){ ingredients.add(ingredient); }

}
