package common;

import server.Server;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Ioana Moisoiu
 * Class contains a thread which keeps checking if there are ingredients to be restocked or orders to be delivered by drones.
 *
 */

public class IngredientRestock extends Thread {

    private static Server serverInterface;
    private Drone drone;

    public  IngredientRestock(Drone drone){
        this.drone = drone;
        this.start();
    }

    public static void setServerInterface(Server server) {
        serverInterface = server;
    }

    public List<Order> getOrders() { return  serverInterface.checkOrders(); }

    @Override
    public void run(){
        ReentrantLock lock = new ReentrantLock();
        while (true){
            lock.lock();
            try {
                if (serverInterface.restockIngredient()!= null) {
                    drone.setStatus("Restocking "+ serverInterface.restockIngredient().getName());
                    try {
                        int time = serverInterface.restockIngredient().getSupplier().getDistance().intValue()*drone.getSpeed().intValue();
                        Thread.sleep(time*30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    serverInterface.setStock(serverInterface.restockIngredient(), serverInterface.restockIngredient().getStock().intValue() + serverInterface.restockIngredient().getRestockingAmount().intValue());
                }
                if (!getOrders().isEmpty()) {
                    for (Order order : getOrders()) {
                        if(!(order.isOrderComplete())) {
                            boolean canDeliver = true;
                            for (Dish dish : order.getOrder().keySet()) {
                                if(dish.getStock().intValue() - order.getOrder().get(dish).intValue() < 0){
                                    canDeliver = false;
                                    break;
                                }
                            }
                            if(canDeliver){
                                drone.setStatus("Delivering order");
                                System.out.println(order.getId());
                                try {
                                    int time = order.getDistance().intValue()*drone.getSpeed().intValue();
                                    Thread.sleep(time*30);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                for (Dish dish : order.getOrder().keySet()) {
                                    serverInterface.setStock(dish, dish.getStock().intValue() - order.getOrder().get(dish).intValue());

                                }
                                order.setStatus("Complete");
                            }
                        }
                    }
                }

            }finally {
                drone.setStatus("Idle");
                lock.unlock();
            }
        }
    }
}
