package common;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Ioana Moisoiu
 * Class models a dish object which can be ordered by clients.
 *
 */

public class Dish extends Model implements Serializable{

    private String description;
    private Number price;
    private Map<Ingredient, Number> recipe = new ConcurrentHashMap<>();
    private Number restockingThreshold;
    private Number restockingAmount;
    private Number stock;

    public Dish(String name, String description, Number price, Number restockingThreshold, Number restockingAmount){
        super(name);
        setDescription(description);
        setPrice(price);
        setRestockingThreshold(restockingThreshold);
        setRestockingAmount(restockingAmount);
    }


    @Override
    public String getName() { return name; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.notifyUpdate();
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) { this.price = price; }

    public Map<Ingredient, Number> getRecipe() { return recipe; }

    public void setRecipe(Map<Ingredient, Number> recipe) {
        this.recipe = recipe;
    }

    public synchronized Number getRestockingThreshold() { return restockingThreshold; }

    public void setRestockingThreshold(Number restockingThreshold) { this.restockingThreshold = restockingThreshold; }

    public synchronized Number getRestockingAmount() { return restockingAmount; }

    public void setRestockingAmount(Number restockingAmount) { this.restockingAmount = restockingAmount; }

    public synchronized void setStock(Number stock){ this.stock = stock; }

    public synchronized Number getStock() { return stock; }

}
