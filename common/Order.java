package common;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Ioana Moisoiu
 * Class models an order objects which contains a single order placed by a client.
 *
 */

public class Order extends Model implements Serializable{

    private String status;
    private Map<Dish, Number> order = new ConcurrentHashMap<>();
    private User user;
    private String id = UUID.randomUUID().toString();
    public Order(User user, Map<Dish,Number> order){
        super();
        setStatus("Order was placed");
        this.user = user;
        this.order = order;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    public String getId() { return id; }

    public Map<Dish,Number> getOrder() { return  order; }

    public Number getDistance() { return  user.getPostcode().getDistance(); }

    public void addAllDishes(Map<Dish, Number> map){ order.putAll(map); }

    public Number calculateCost(){
        Number cost = 0;
        for(Dish dish: order.keySet()){
            Number amount = order.get(dish);
            cost= cost.intValue() + dish.getPrice().intValue() * amount.intValue();
        }
        return cost;
    }

    public void cancelOrder(){
        order.clear();
        setStatus("Cancelled");
    }

    public void setStatus(String status){ this.status = status;}

    public String getStatus(){ return this.status; }

    public boolean isOrderComplete(){
        if(status.equals("Complete")){
            return true;
        }
        return false;
    }
}
