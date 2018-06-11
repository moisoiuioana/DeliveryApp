package common;

import server.Server;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Ioana Moisoiu
 * Class contains a thread that checks if the stock of dishes is below the threshold and if so it is restocked by the staff by cooking
 * the restocking amount of the certain dish.
 *
 */

public class DishRestock extends Thread {

    private static Server serverInterface;
    private Staff staff;

    public DishRestock(Staff staff){ this.staff = staff; }

    public static void setServerInterface (Server server) { serverInterface = server; }

    public Dish getRestockingDish() { return serverInterface.restockDish(); }

    @Override
    public void run() {
        ReentrantLock lock = new ReentrantLock();
        while(true) {
            lock.lock();
            try{
                Dish dish = getRestockingDish();
                if (dish != null) {
                    for (Ingredient ingredient : dish.getRecipe().keySet()) {
                        if (ingredient.getStock().intValue() - ((dish.getRecipe().get(ingredient)).intValue() * dish.getRestockingAmount().intValue()) >= 0) {
                            ingredient.setStock(ingredient.getStock().intValue() - ((dish.getRecipe().get(ingredient)).intValue() * dish.getRestockingAmount().intValue()));
                        } else {
                            staff.setStatus("Waiting for ingredients");
                        }
                    }
                    staff.setStatus("Cooking");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dish.setStock(dish.getStock().intValue() + dish.getRestockingAmount().intValue());
                }
           }finally {
                staff.setStatus("Idle");
                lock.unlock();
            }
        }
    }
}