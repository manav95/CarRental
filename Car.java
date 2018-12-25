/**
 * This class is designed to handle instances of the car class.
 * 
 * @author Manav Dutta
 * @version 12/25/2018
 */
public class Car
{
    private final int carId; //unique identifier for car in rental system
    private final String type;
    private final String model;
    private final String make;
    private final int makeYear; //unique year for the car
    /**
     * Constructor for objects of class Car
     */
    public Car(int id, String type, String model, String make, int year)
    {
        this.carId = id;
        this.type = type;
        this.model = model;
        this.make = make;
        this.makeYear = year;
    }
    
    public int getCarId() {
        return this.carId;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getModel() {
        return this.model;
    }
    
    public String getMake() {
        return this.make;
    }
    
    public int getMakeYear() {
        return this.makeYear;
    }
    
    public int hashCode() {
        return this.carId;
    }
    
    public boolean equals(Object obj) {
       return this.carId == ((Car)obj).carId;
    }
}
