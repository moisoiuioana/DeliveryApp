package common;

import java.io.Serializable;

/**
 *
 * @author Ioana Moisoiu
 * Class models standard messages the can be send and received by either client or server objects.
 *
 */

public class Message implements Serializable{

    private String request;
    private Object object;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Message(String request, Object object){
        setRequest(request);
        setObject(object);
    }

    public Message(String request){
        setRequest(request);
    }
}
