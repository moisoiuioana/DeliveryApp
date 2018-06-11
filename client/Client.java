package client;
import common.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Ioana Moisoiu
 * Class contains an implementation of the ClientInterface which allows different actions to be made by a user.
 *
 */
public class Client implements ClientInterface {

    private Map<User, Map<Dish,Number>> userBaskets = new ConcurrentHashMap<>();
    private List<UpdateListener> updateListener = new ArrayList<>();
    private Comms comms = new Comms(false);

    public Client(){
        comms.getClientConnection().setClientInterface(this);
    }

    @Override
    public User register(String username, String password, String address, Postcode postcode) {
        User newUser = new User(username, password, address, postcode);
        comms.getClientConnection().sendMessage(new Message("add user", newUser));
        return newUser;
    }

    @Override
    public User login(String username, String password) {
        comms.getClientConnection().sendMessage(new Message("get users"));
        Message message = (Message) comms.getClientConnection().receiveMessage();
        List<User> userList = (List<User>) message.getObject();
        for (User user: userList){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    @Override
    public List<Postcode> getPostcodes() {
        comms.getClientConnection().sendMessage(new Message("get postcodes"));
        Message message = (Message)comms.getClientConnection().receiveMessage();
        return (List<Postcode>)message.getObject();
    }

    @Override
    public List<Dish> getDishes() {
        comms.getClientConnection().sendMessage(new Message("get dishes"));
        Message message = (Message)comms.getClientConnection().receiveMessage();
        return (List<Dish>)message.getObject();
    }

    @Override
    public String getDishDescription(Dish dish) {
        comms.getClientConnection().sendMessage(new Message("get dish description", dish));
        Message message = (Message)comms.getClientConnection().receiveMessage();
        return ((String)message.getObject());
    }

    @Override
    public Number getDishPrice(Dish dish) {
        comms.getClientConnection().sendMessage(new Message("get dish price", dish));
        Message message = (Message)comms.getClientConnection().receiveMessage();
        return ((Number)message.getObject());
    }

    @Override
    public Map<Dish, Number> getBasket(User user) {
        if(userBaskets.containsKey(user)){
            return userBaskets.get(user);
        }
        return null;
    }

    @Override
    public Number getBasketCost(User user) {
        Number totalCost = 0;
        if(userBaskets.containsKey(user)){
            for(Dish dish: userBaskets.get(user).keySet()){
                Number amount = userBaskets.get(user).get(dish);
                totalCost = totalCost.intValue() + dish.getPrice().intValue()*amount.intValue();
            }
        }
        return totalCost;
    }

    @Override
    public void addDishToBasket(User user, Dish dish, Number quantity) {
        if(userBaskets.containsKey(user)){
            userBaskets.get(user).put(dish,quantity);
        }else{
            Map<Dish, Number> map = new HashMap<>();
            map.put(dish, quantity);
            userBaskets.put(user, map);
        }
    }

    @Override
    public void updateDishInBasket(User user, Dish dish, Number quantity) {
        if(quantity.equals(0)){
            userBaskets.get(user).remove(dish);
        }else{
            userBaskets.get(user).replace(dish,quantity);
        }
    }

    @Override
    public Order checkoutBasket(User user) {
        Order order = new Order(user, userBaskets.get(user));
        comms.getClientConnection().sendMessage(new Message("add order", order));
        clearBasket(user);
        return order;
    }

    @Override
    public void clearBasket(User user) { userBaskets.get(user).clear(); }

    @Override
    public List<Order> getOrders(User user) {
        comms.getClientConnection().sendMessage(new Message("get orders", user));
        Message message = (Message)comms.getClientConnection().receiveMessage();
        return (List<Order>)message.getObject();
    }

    @Override
    public boolean isOrderComplete(Order order) {
        comms.getClientConnection().sendMessage(new Message("get order", order));
        Message message = (Message)comms.getClientConnection().receiveMessage();
        return ((Order)message.getObject()).isOrderComplete();
    }

    @Override
    public String getOrderStatus(Order order) {
        comms.getClientConnection().sendMessage(new Message("get order", order));
        Message message = (Message)comms.getClientConnection().receiveMessage();
        return ((Order)message.getObject()).getStatus();
    }

    @Override
    public Number getOrderCost(Order order) {
        comms.getClientConnection().sendMessage(new Message("calculate order cost", order));
        Message message = (Message)comms.getClientConnection().receiveMessage();
        return (Number)message.getObject();
    }

    @Override
    public void cancelOrder(Order order) {
        comms.getClientConnection().sendMessage(new Message("cancel order", order));
        order.cancelOrder();
    }

    @Override
    public void addUpdateListener(UpdateListener listener) {
        this.updateListener.add(listener);
    }

    @Override
    public void notifyUpdate() {
        this.updateListener.forEach(listener -> listener.updated(new UpdateEvent()));
    }
}
