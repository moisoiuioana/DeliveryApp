import client.Client;
import client.ClientInterface;
import client.ClientWindow;

/**
 *
 * @author Ioana Moisoiu
 * Class contains main method which launches the client side of the application.
 *
 */

public class ClientApplication {

    public ClientInterface initialise(){
        Client client = new Client();
        return client;
    }

    public void launchGUI(ClientInterface clientInterface){
        ClientWindow clientWindow = new ClientWindow(clientInterface);
    }

    public static void main(String[] args){
        ClientApplication clientApplication = new ClientApplication();
        ClientInterface clientInterface = clientApplication.initialise();
        clientApplication.launchGUI(clientInterface);
    }
}



