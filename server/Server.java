package server;

import common.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Ioana Moisoiu
 * Class contains an implementation of the ServerInterface which interacts with the users and stores all the actions of users, staff
 * and drones.
 *
 */

public class Server implements ServerInterface {

    private List<User> users = new ArrayList<>();
    private List<Dish> dishes =new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Supplier> suppliers = new ArrayList<>();
    private List<Drone> drones = new ArrayList<>();
    private List<Staff> staffList = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private List<Postcode> postcodes = new ArrayList<>();
    private boolean dishEnable;
    private boolean ingredientEnable;
    private List<UpdateListener> updateListener = new ArrayList<>();
    private Comms comms =  new Comms(true);

    public Server(){
        comms.getServer().setServerInterface(this);
        DishRestock.setServerInterface(this);
        IngredientRestock.setServerInterface(this);
    }

    @Override
    public void loadConfiguration(String filename) throws FileNotFoundException {
        Configuration configuration = new Configuration(this, filename);
        configuration.configure(filename);
    }

    @Override
    public void setRestockingIngredientsEnabled(boolean enabled) { ingredientEnable = enabled; }

    @Override
    public void setRestockingDishesEnabled(boolean enabled) { dishEnable = enabled; }

    @Override
    public void setStock(Dish dish, Number stock) { dish.setStock(stock); }

    @Override
    public void setStock(Ingredient ingredient, Number stock) { ingredient.setStock(stock); }

    @Override
    public List<Dish> getDishes() { return dishes; }

    @Override
    public Dish addDish(String name, String description, Number price, Number restockThreshold, Number restockAmount) {
        Dish dish = new Dish(name, description, price, restockThreshold, restockAmount);
        dishes.add(dish);
        if(dish.getStock()==null){
            dish.setStock(0);
        }
        return dish;
    }

    @Override
    public void removeDish(Dish dish) throws UnableToDeleteException {
        dishes.remove(dish);
        notifyUpdate();
    }

    @Override
    public void addIngredientToDish(Dish dish, Ingredient ingredient, Number quantity) { dish.getRecipe().put(ingredient, quantity); }

    @Override
    public void removeIngredientFromDish(Dish dish, Ingredient ingredient) { dish.getRecipe().remove(ingredient); }

    @Override
    public void setRecipe(Dish dish, Map<Ingredient, Number> recipe) { dish.setRecipe((HashMap)recipe); }

    @Override
    public void setRestockLevels(Dish dish, Number restockThreshold, Number restockAmount) {
        dish.setRestockingThreshold(restockThreshold);
        dish.setRestockingAmount(restockAmount);
    }

    @Override
    public Number getRestockThreshold(Dish dish) { return dish.getRestockingThreshold(); }

    @Override
    public Number getRestockAmount(Dish dish) {
        return dish.getRestockingAmount();
    }

    @Override
    public Map<Ingredient, Number> getRecipe(Dish dish) { return dish.getRecipe(); }

    @Override
    public Map<Dish, Number> getDishStockLevels() {
        Map<Dish, Number> dishStockLevels = new HashMap<>();
        for(Dish dish: dishes){
            dishStockLevels.put(dish, dish.getStock());
        }
        return dishStockLevels;
    }

    @Override
    public List<Ingredient> getIngredients() { return ingredients; }

    @Override
    public Ingredient addIngredient(String name, String unit, Supplier supplier, Number restockThreshold, Number restockAmount) {
        Ingredient ingredient = new Ingredient(name, unit, supplier, restockThreshold, restockAmount);
        ingredients.add(ingredient);
        if(ingredient.getStock() == null){
            ingredient.setStock(0);
        }
        return ingredient;
    }

    @Override
    public void removeIngredient(Ingredient ingredient) throws UnableToDeleteException { ingredients.remove(ingredient); }

    @Override
    public void setRestockLevels(Ingredient ingredient, Number restockThreshold, Number restockAmount) {
        ingredient.setRestockingThreshold(restockThreshold);
        ingredient.setRestockingAmount(restockAmount);
    }

    @Override
    public Number getRestockThreshold(Ingredient ingredient) {
        return ingredient.getRestockingThreshold();
    }

    @Override
    public Number getRestockAmount(Ingredient ingredient) {
        return ingredient.getRestockingAmount();
    }

    @Override
    public Map<Ingredient, Number> getIngredientStockLevels() {
        Map<Ingredient, Number> ingredientStockLevels = new HashMap<>();
        for(Ingredient ingredient: ingredients){
            ingredientStockLevels.put(ingredient, ingredient.getStock());
        }
        return ingredientStockLevels;
    }

    @Override
    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    @Override
    public Supplier addSupplier(String name, Number distance) {
        Supplier supplier = new Supplier(name, distance);
        suppliers.add(supplier);
        return supplier;
    }

    @Override
    public void removeSupplier(Supplier supplier) throws UnableToDeleteException { suppliers.remove(supplier); }

    @Override
    public Number getSupplierDistance(Supplier supplier) {
        return supplier.getDistance();
    }

    @Override
    public List<Drone> getDrones() {
        return drones;
    }

    @Override
    public Drone addDrone(Number speed) {
        Drone drone = new Drone(speed);
        drones.add(drone);
        return drone;
    }

    @Override
    public void removeDrone(Drone drone) throws UnableToDeleteException { drones.remove(drone); }

    @Override
    public Number getDroneSpeed(Drone drone) {
        return drone.getSpeed();
    }

    @Override
    public String getDroneStatus(Drone drone) {
        return drone.getStatus();
    }

    @Override
    public List<Staff> getStaff() {
        return staffList;
    }

    @Override
    public Staff addStaff(String name) {
        Staff staffMember = new Staff(name);
        staffList.add(staffMember);
        return staffMember;
    }

    @Override
    public void removeStaff(Staff staff) throws UnableToDeleteException {
        staffList.remove(staff);
    }

    @Override
    public String getStaffStatus(Staff staff) {
        return staff.getStatus();
    }

    @Override
    public List<Order> getOrders() { return orders; }

    @Override
    public void removeOrder(Order order) throws UnableToDeleteException { orders.remove(order); }

    @Override
    public Number getOrderDistance(Order order) {
        return order.getDistance();
    }

    @Override
    public boolean isOrderComplete(Order order) {
        return order.isOrderComplete();
    }

    @Override
    public String getOrderStatus(Order order) {
        return order.getStatus();
    }

    @Override
    public Number getOrderCost(Order order) {
        return order.calculateCost();
    }

    @Override
    public List<Postcode> getPostcodes() {
        Postcode postcode = new Postcode();
        postcodes.addAll(postcode.getPostcodes());
        return postcodes;
    }

    @Override
    public void addPostcode(String code, Number distance) { postcodes.add(new Postcode(code, distance)); }

    @Override
    public void removePostcode(Postcode postcode) throws UnableToDeleteException { postcodes.remove(postcode); }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void removeUser(User user) throws UnableToDeleteException { users.remove(user); }

    @Override
    public void addUpdateListener(UpdateListener listener) {
        this.updateListener.add(listener);
    }

    @Override
    public void notifyUpdate() {
        this.updateListener.forEach(listener -> listener.updated(new UpdateEvent()));
    }

    public synchronized CopyOnWriteArrayList<Dish> checkDishes(){
        CopyOnWriteArrayList<Dish> dishList = new CopyOnWriteArrayList<>();
        dishList.addAll(getDishes());
        return dishList;
    }

    public synchronized CopyOnWriteArrayList<Ingredient> checkIngredients(){
        CopyOnWriteArrayList<Ingredient> ingredientList = new CopyOnWriteArrayList<>();
        ingredientList.addAll(getIngredients());
        return ingredientList;
    }

    public synchronized Ingredient restockIngredient(){
        CopyOnWriteArrayList<Ingredient> ingredients = checkIngredients();
        for(Ingredient ingredient: ingredients){
            if(ingredient.getStock().intValue()<ingredient.getRestockingThreshold().intValue()){
                return ingredient;
            }
        }
        return null;
    }

    public synchronized Dish restockDish(){
        CopyOnWriteArrayList<Dish> dishes = checkDishes();
        for(Dish dish: dishes){
            if(dish.getStock().intValue()<dish.getRestockingThreshold().intValue()){
                return dish;
            }
        }
        return null;
    }

    public synchronized CopyOnWriteArrayList<Order> checkOrders(){
        CopyOnWriteArrayList<Order> orderList = new CopyOnWriteArrayList<>();
        orderList.addAll(getOrders());
        return orderList;
    }

}
