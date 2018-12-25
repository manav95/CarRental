/**
 * Store customer information
 * 
 * @author Manav Dutta 
 * @version 12/25/2018
 */
public class Customer
{
    private final String firstName;
    private final String middleName;
    private final String lastName;
    /**
     * Constructor for objects of class Customer
     */
    public Customer(String firstName, String middleName, String lastName)
    {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public String getMiddleName() {
        return this.middleName;
    }
    
    public String getLastName() {
        return this.lastName;
    }
}
