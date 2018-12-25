/**
 * Response class for reservations interactions with the manager
 * 
 * @author Manav Dutta
 * @version 12/25/2018
 */
public class Response<T>
{
    private boolean success;
    private T data;
    private String message;

    /**
     * Constructor for objects of class Response
     */
    public Response(boolean s, T data, String msg)
    {
        this.success = s;
        this.data = data;
        this.message = msg;
    }

    public boolean getSuccess() {
        return this.success;
    }
    
    public T getData() {
        return this.data;
    }
    
    public String getMessage() {
        return this.message;
    }
}
