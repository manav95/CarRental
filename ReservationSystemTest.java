import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.time.LocalDate;
/**
 * Test classes for reservation system
 *
 * @author  Manav Dutta
 * @version 12/25/2018
 */
public class ReservationSystemTest
{
    private ReservationManager manager;
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
       manager = ReservationManager.INSTANCE;
       manager.addCar("Sedan", "Honda", "Accord", 2016);
       manager.addCar("SUV", "Honda", "Wrangler", 2017);
       manager.addCar("SUV", "Honda", "Wrangler", 2015);
       manager.addCar("Truck", "Ford", "Grand Cherokee", 2017);
       manager.addCar("Truck", "Ford", "Grand Cherokee", 2015);
    }
    
    @Test
    public void runTests() {
        addCar();
        removeCar();
        reserveCar();
        removeReservation();
        modifyReservationType();
        modifyReservationDates();
    }
    
    
    public void addCar() {
       int carId = manager.addCar("Sedan", "Honda", "Accord", 2017).getData();
       assertTrue(manager.checkCarExistence("Sedan", carId));
    }
    
    
    public void removeCar() {
       int carId = manager.addCar("Jeep", "Dodge", "Cherokee", 2013).getData();
       assertEquals("Car removed successfully.", manager.removeCar(carId).getMessage());
       assertFalse(manager.checkCarExistence("Jeep", carId));
       assertEquals("Car not found.", manager.removeCar(carId).getMessage());
    }
    
    
    public void reserveCar() {
       String first = "test";
       String middle = "testMid";
       String end = "testEnd";
       
       assertTrue(manager.reserveCar("Sedan", LocalDate.of(2018, 8, 5), 5, first, middle, end).getSuccess());
       assertEquals("No valid date input specified.", manager.reserveCar("SUV", null, 5, first, middle, end).getMessage());
       assertEquals("No cars found to match specified type.", manager.reserveCar("DeLorian", LocalDate.of(2019, 4,4), 5, first, middle, end).getMessage());
       assertTrue(manager.reserveCar("Sedan", LocalDate.of(2018, 8, 6), 2, first, middle, end).getSuccess());
       assertEquals("No cars of this type found for specified rental period.", manager.reserveCar("Sedan", LocalDate.of(2018, 8, 5), 5, first, middle, end).getMessage());
    }
    
    
    public void removeReservation() {
       String first = "test";
       String middle = "testMid";
       String end = "testEnd";
       
       CarReservation res = manager.reserveCar("SUV", LocalDate.of(2018, 10, 6), 4, first, middle, end).getData();
       assertEquals("Removed specified reservation id.", manager.removeReservation(res.getReservationId(), res.getCarId()).getMessage());
       assertEquals("No matching reservation found.", manager.removeReservation(res.getReservationId(), res.getCarId()).getMessage());
    }
    
    
    public void modifyReservationType() {
        String first = "test";
        String middle = "testMid";
        String end = "testEnd";
        CarReservation res = manager.reserveCar("SUV", LocalDate.of(2019, 10, 6), 4, first, middle, end).getData();
        Response<CarReservation> response = manager.modifyReservationType(res.getReservationId(), res.getCarId(), "Truck");
        assertEquals("Modified reservation.",  response.getMessage());
    }
    
    
    public void modifyReservationDates() {
        String first = "test";
        String middle = "testMid";
        String end = "testEnd";
        
        CarReservation res = manager.reserveCar("Truck", LocalDate.of(2018, 1, 1), 4, first, middle, end).getData();
        Response<CarReservation> modResp = manager.modifyReservationDates(res.getReservationId(), res.getCarId(), LocalDate.of(2018, 2, 2), 5);
        assertEquals("Modifying reservation.", modResp.getMessage());
    }
    
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
}
