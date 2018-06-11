import server.Server;
import server.ServerInterface;
import server.ServerWindow;

/**
 *
 * @author Ioana Moisoiu
 * Class contains the main method which launches the server side of the application.
 *
 */

public class ServerApplication {

    public ServerInterface initialise(){
        Server server = new Server();
        return server;
    }

    public void launchGUI(ServerInterface serverInterface){
        ServerWindow serverWindow = new ServerWindow(serverInterface);
    }

    public static void main(String[] args){
        ServerApplication serverApplication = new ServerApplication();
        ServerInterface serverInterface = serverApplication.initialise();
        serverApplication.launchGUI(serverInterface);
    }
}
