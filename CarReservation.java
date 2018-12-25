import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
/**
 * Class that represents information about car booking
 * 
 * @author Manav Dutta
 * @version 12/25/2018
 */
public class CarReservation
{
    private LocalDate startDate;
    private LocalDate endDate;
    private int reservationId;
    private int carId;
    private Customer customer;
    /**
     * Constructor for objects of class CarReservation
     */
    public CarReservation(LocalDate startDate, int numDays, int reservationId, int carId, Customer cust)
    {
       if (startDate != null) {//support minimal objects used to check in set
          this.startDate = startDate;
          this.endDate = startDate.plusDays(numDays - 1);
       }
       this.reservationId = reservationId;
       this.customer = cust;
       this.carId = carId;
    }

    public LocalDate getStartDate() {
       return this.startDate;
    }
    
    public LocalDate getEndDate() {
       return this.endDate;
    }
    
    public void setStartDate(LocalDate startDate) {
       this.startDate = startDate;
    }
    
    public void setEndDate(LocalDate endDate) {
       this.endDate = endDate;
    }
    
    public int getCarId() {
       return this.carId;
    }
    
    public int getNumDays() {
       return (int)ChronoUnit.DAYS.between(this.startDate, this.endDate);
    }
    
    
    public void setCarId(int carId) {
       this.carId = carId;
    }
    
    public int getReservationId() {
       return this.reservationId;
    }
    
    public Customer getCustomer() {
       return this.customer;
    }
    
    public int hashCode() {
       return this.reservationId;
    }
    
    public boolean equals(Object obj) {
       return this.reservationId == ((CarReservation)obj).reservationId;
    }
}
