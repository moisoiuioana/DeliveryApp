package common;

import java.io.Serializable;

/**
 *
 * @author Ioana Moisoiu
 * Class models an ingredient object representing ingredients used to cook dishes.
 *
 */

public class Ingredient extends Model implements Serializable{

    private String unit;
    private Supplier supplier;
    private Number restockingThreshold;
    private Number restockingAmount;
    private Number stock;


    public Ingredient(String name, String unit, Supplier supplier, Number restockingThreshold, Number restockingAmount){
        super(name);
        this.unit =unit;
        this.supplier =supplier;
        this.restockingThreshold = restockingThreshold;
        this.restockingAmount = restockingAmount;
    }

    @Override
    public String getName() {
        return name;
    }

    public  String getUnit() {
        return unit;
    }

    public  void setUnit(String unit) {
        this.unit = unit;
    }

    public  Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public  synchronized Number getRestockingThreshold() { return restockingThreshold; }

    public   void setRestockingThreshold(Number restockingThreshold) { this.restockingThreshold = restockingThreshold; }

    public  synchronized Number getRestockingAmount() { return restockingAmount; }

    public  void setRestockingAmount(Number restockingAmount) { this.restockingAmount = restockingAmount; }

    public synchronized void setStock( Number stock ) { this.stock = stock; }

    public synchronized Number getStock (){ return stock; }
}
