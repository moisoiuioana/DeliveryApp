package common;

import client.Client;
import server.ServerInterface;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ioana Moisoiu
 * Class contains an implementation of the communications between Client and Server.
 *
 */

public class Comms {

    private boolean isServer;
    private Server server;
    private ClientConnection connection;
    private int port;

    public Comms(boolean isServer){
        setServer(isServer);
        if(isServer()){
            server = new Server();
            setPort(server.portNumber);
        }else{
            try {
                connection = new ClientConnection(new Socket("localhost", 30000));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Server getServer(){ return server; }

    public ClientConnection getClientConnection(){ return connection; }

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean server) {
        isServer = server;
    }

    public void setPort(int portNumber){ this.port = portNumber; }

    public class ClientConnection extends Thread{

        private Socket connection;
        private ObjectOutputStream outputStream;
        private ObjectInputStream inputStream;
        private boolean isConnection;
        private Client clientInterface;

        public ClientConnection(Socket connection){
            setConnection(connection);
            try {
                outputStream = new ObjectOutputStream(getConnection().getOutputStream());
                outputStream.flush();
                setOutputStream(outputStream);
                inputStream = new ObjectInputStream(getConnection().getInputStream());
                setInputStream(inputStream);
            }catch (IOException e){
                e.printStackTrace();
            }
            startConnection();
            this.start();
        }

        public void setClientInterface(Client clientInterface) { this.clientInterface = clientInterface; }

        public Socket getConnection() { return connection; }

        public void setConnection(Socket connection) { this.connection = connection; }

        public ObjectOutputStream getOutputStream() { return outputStream; }

        public void setOutputStream(ObjectOutputStream outputStream) { this.outputStream = outputStream; }

        public ObjectInputStream getInputStream() { return inputStream; }

        public void setInputStream(ObjectInputStream inputStream) { this.inputStream = inputStream; }

        private void startConnection(){ isConnection = true; }

        private void stopConnection() { isConnection = false; }

        private boolean getConnectionState() { return  isConnection; }

        public void sendMessage(Object message) {
            try {
                getOutputStream().writeObject(message);
                getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Object receiveMessage() {
            try {
                return getInputStream().readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void closeConnection(){
            try {
                getOutputStream().close();
                getInputStream().close();
                getConnection().close();
                stopConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public class Server extends Thread{

        public static final int portNumber = 30000;
        private ServerSocket serverSocket;
        private boolean running;
        private ObjectOutputStream outputStream;
        private ObjectInputStream inputStream;
        private ServerInterface serverInterface;

        public Server(){
            activateServer();
            this.start();
        }

        public ServerInterface getServerInterface() { return serverInterface; }

        public void setServerInterface(ServerInterface serverInterface) { this.serverInterface = serverInterface; }

        private void activateServer(){ running = true; }

        private void stopServer(){ running = false; }

        private boolean getServerState(){ return running; }

        public void setOutputStream(ObjectOutputStream outputStream){ this.outputStream = outputStream; }

        public void setInputStream(ObjectInputStream inputStream) { this.inputStream = inputStream; }

        public ObjectOutputStream getOutputStream() { return outputStream; }

        public ObjectInputStream getInputStream() { return inputStream; }

        public void sendMessage(Object message) {
            try {
                getOutputStream().writeObject(message);
                getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Object receiveMessage() {
            try {
                return getInputStream().readObject();
            } catch (ClassNotFoundException | IOException e) {
               // e.printStackTrace();
            }
            return null;
        }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(portNumber);
                while(getServerState()){
                    Socket socket = serverSocket.accept();
                    setOutputStream(new ObjectOutputStream(socket.getOutputStream()));
                    getOutputStream().flush();
                    setInputStream(new ObjectInputStream(socket.getInputStream()));
                    while(true) {
                        synchronized (socket) {
                            Message message = (Message) receiveMessage();
                            if (message.getRequest().equals("get dishes")) {
                                sendMessage(new Message("dishes", serverInterface.getDishes()));
                            }
                            if(message.getRequest().equals("get postcodes")){
                                sendMessage(new Message("postcodes", serverInterface.getPostcodes()));
                            }
                            if(message.getRequest().equals("get users")){
                                sendMessage(new Message("users", serverInterface.getUsers()));
                            }
                            if(message.getRequest().equals("add user")){
                                serverInterface.getUsers().add((User)message.getObject());
                            }
                            if(message.getRequest().equals("get dish description")){
                                Dish dish = (Dish)message.getObject();
                                for(Dish item: serverInterface.getDishes()){
                                    if(item.getName().equals(dish.getName())){
                                        sendMessage(new Message("dish", item.getDescription()));
                                    }
                                }
                            }
                            if(message.getRequest().equals("get dish price")){
                                Dish dish = (Dish)message.getObject();
                                for(Dish item: serverInterface.getDishes()){
                                    if(item.getName().equals(dish.getName())){
                                        sendMessage(new Message("dish", item.getPrice()));
                                    }
                                }
                            }
                            if(message.getRequest().equals("add order")){
                                serverInterface.getOrders().add((Order)message.getObject());
                            }
                            if(message.getRequest().equals("get orders")){
                                User user = (User) message.getObject();
                                List<Order> orderList= new ArrayList<>();
                                for(Order order: serverInterface.getOrders()){
                                    if(order.getName().equals(user.getName())){
                                        orderList.add(order);
                                    }
                                }
                                sendMessage(new Message("orders", orderList));
                            }
                            if(message.getRequest().equals("get order")){
                                Order order = (Order)message.getObject();
                                for(Order item: serverInterface.getOrders()){
                                    if(item.getId().equals(order.getId())){
                                        sendMessage(new Message("order ",item));
                                    }
                                }
                            }
                            if(message.getRequest().equals("calculate order cost")){
                                Order order = (Order)message.getObject();
                                for(Order item: serverInterface.getOrders()){
                                    if(item.getId().equals(order.getId())){
                                        sendMessage(new Message("order ",serverInterface.getOrderCost(item)));
                                    }
                                }
                            }
                            if(message.getRequest().equals("cancel order")){
                                Order order = (Order)message.getObject();
                                for(Order item: serverInterface.getOrders()){
                                    if(item.getId().equals(order.getId())){
                                        item.cancelOrder();
                                    }
                                }
                            }
                        }
                    }
                }
            }catch(IOException e){
               e.printStackTrace();
            }finally {
                close();
            }
        }

        public void close(){
            stopServer();
            try{
                serverSocket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}