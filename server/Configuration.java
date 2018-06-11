package server;

import common.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ioana Moisoiu
 * Class takes a file of a certain type containing different parameters of certain type objects. It creates these objects with these
 * parameters and loads them into the server.
 *
 */

public class Configuration {

    private Server server;
    private String filename;

    public Configuration(Server server, String filename){
        this.server = server;
        this.filename = filename;
    }

    public void configure(String filename){
        try{
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while(line!=null){
                if(line.contains("SUPPLIER")){
                    String[] param = line.split(":");
                    String supplierName = param[1];
                    Number distance = Integer.parseInt(param[2]);
                    server.addSupplier(supplierName, distance);
                }
                if(line.contains("INGREDIENT")){
                    String[] param = line.split(":");
                    String ingredientName = param[1];
                    String unit = param[2];
                    String supplierName = param[3];
                    Supplier supplier = null;
                    for(Supplier item: server.getSuppliers()){
                        if(item.getName().equals(supplierName)){
                            supplier = item;
                        }
                    }
                    Number restockThreshold = Integer.parseInt(param[4]);
                    Number restockAmount = Integer.parseInt(param[5]);
                    if(supplier!=null) {
                        server.addIngredient(ingredientName, unit, supplier, restockThreshold, restockAmount);
                    }

                }
                if(line.contains("DISH")){
                    String[] param = line.split((":"));
                    String dishName = param[1];
                    String dishdescripton = param[2];
                    Number dishPrice = Integer.parseInt(param[3]);
                    Number dishRestockThreshold = Integer.parseInt(param[4]);
                    Number dishRestockAmount = Integer.parseInt(param[5]);
                    server.addDish(dishName, dishdescripton, dishPrice, dishRestockThreshold, dishRestockAmount);
                    String recipe[] = param[6].split(",");
                    Map<Ingredient, Number>items = new HashMap<>();
                    for(int i=0; i<recipe.length; i++){
                        String ingredient[] = recipe[i].split(" \\* ");
                        String ingredientName = ingredient[1];
                        Ingredient thisIngredient= null;
                        Number amount = Integer.parseInt(ingredient[0]);
                        for(Ingredient item: server.getIngredients()){
                            if(item.getName().equals(ingredientName)){
                                thisIngredient = item;
                            }
                        }
                        if(thisIngredient!=null) {
                            items.put(thisIngredient, amount);
                        }
                    }
                    Dish dish=null;
                    for(Dish item: server.getDishes()){
                        if((item.getName()).equals(dishName)){
                            dish=item;
                        }
                    }
                    if(dish!=null) {
                        server.setRecipe(dish, items);
                    }
                }
                if(line.contains("POSTCODE")){
                    String[] param = line.split(":");
                    String postcodeName = param[1];
                    Number postcodeDistance = Integer.parseInt(param[2]);
                    server.addPostcode(postcodeName, postcodeDistance);
                }
                if(line.contains("USER")){
                    String[] param = line.split(":");
                    String username = param[1];
                    String password = param[2];
                    String address = param[3];
                    Postcode postcode = null;
                    for(Postcode item: server.getPostcodes()){
                        if(item.getName().equals(param[4])){
                            postcode = item;
                        }
                    }
                    if(postcode!=null){
                        User user = new User(username, password, address, postcode);
                        server.getUsers().add(user);
                    }
                }
                if(line.contains("ORDER")){
                    String[] param = line.split(":");
                    String username = param[1];
                    User user = null;
                    for(User item: server.getUsers()){
                        if(item.getUsername().equals(username)){
                            user = item;
                        }
                    }
                    String[] order = param[2].split(",");
                    Map<Dish,Number> orders = new HashMap<>();
                    for(int i=0; i<order.length; i++){
                        Number quantity = Integer.parseInt(order[i].split(" \\* ")[0]);
                        String dishName = order[i].split(" \\* ")[1];
                        Dish dish = null;
                        for(Dish item: server.getDishes()){
                            if(item.getName().equals(dishName)){
                                dish=item;
                            }
                        }
                        if(dish!=null){
                            orders.put(dish,quantity);
                        }
                    }
                    boolean existentUser = false;
                    for(Order item: server.getOrders()){
                        if(item.getName().equals(username)){
                            item.addAllDishes(orders);
                            existentUser = true;
                        }
                    }
                    if(existentUser == false){
                        server.getOrders().add(new Order(user, orders));
                    }
                }
                if(line.contains("STOCK")){
                    String[] param = line.split(":");
                    String itemName = param[1];
                    Number quantity = Integer.parseInt(param[2]);
                    Dish dish = null;
                    Ingredient ingredient = null;
                    for(Dish item: server.getDishes()){
                        if(item.getName().equals(itemName)){
                            dish = item;
                        }
                    }
                    for(Ingredient item: server.getIngredients()){
                        if(item.getName().equals(itemName)){
                            ingredient = item;
                        }
                    }
                    if(dish!=null) {
                        server.setStock(dish, quantity);
                    }else if(ingredient!=null){
                        server.setStock(ingredient, quantity);
                    }
                }
                if(line.contains("STAFF")){
                    server.addStaff(line.split(":")[1]);
                }
                if(line.contains("DRONE")){
                    server.addDrone(Integer.parseInt(line.split(":")[1]));
                }

                line = bufferedReader.readLine();
            }

        }catch(FileNotFoundException e) {
            System.out.println("Cannot open file");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
