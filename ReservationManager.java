import java.util.*;
import java.time.LocalDate;
/**
 * ReservationManager handles various reservations for a variety of cars. It is an enum to 
 * guarantee ReservationManager is initialized once and can't be initialized via reflection.
 * 
 * @author Manav Dutta
 * @version 12/25/2018
 */
public enum ReservationManager
{
    INSTANCE;
    private static volatile Map<String, Set<Car>> typeMap = new HashMap<>(); //type to car id mapping
    private static volatile Map<Integer, Set<CarReservation>> bookingMap = new HashMap<>(); //car to car reservations mapping
    private static volatile int reservationCounter = 1; //helps us guarantee uniqueness as an incrementing count
    private static volatile int carCounter = 1; //helps us guarantee uniqueness as an incrementing count for new cars
    
    //add a new car to the system
    public synchronized Response<Integer> addCar(String type, String model, String make, int year) {
          Car newCar = new Car(carCounter, type, model, make, year);
          Set<Car> carSet = null; //check if type has a corresponding set
          if (typeMap.containsKey(type)) { 
             carSet = typeMap.get(type);
          }
          else {
             carSet = new HashSet<>();
          }
          carSet.add(newCar);
          typeMap.put(type, carSet);
          carCounter++;
          return new Response<Integer>(true, carCounter-1, "Added car successfully");
    }
    
    //remove a car given its id to the system
    public synchronized Response<Car> removeCar(int carId) {
          Car newCar = new Car(carId, "", "", "", -1);
          for (String type: typeMap.keySet()) {
              Set<Car> typeCars = typeMap.get(type);
              if (typeCars.contains(newCar)) {
                 typeCars.remove(newCar);
                 return new Response<Car>(true, newCar, "Car removed successfully.");
              }
          }
          return new Response<Car>(false, newCar, "Car not found.");
    }
    
    //let customer reserve a car given type, start date, and number of days required
    public synchronized Response<CarReservation> reserveCar(String type, LocalDate startDate, int numDays, String first, String middle, String last) {
        if (startDate == null || numDays == 0) {
           return new Response<CarReservation>(false, null, "No valid date input specified.");
        }
        Set<Car> cars = typeMap.get(type);
        if (cars == null || cars.size() == 0) {
           return new Response<CarReservation>(false, null, "No cars found to match specified type.");
        }
        for (Car car: cars) {
            if (checkCarAvailability(car, startDate, numDays)) {
                Customer cust = new Customer(first, middle, last);
                CarReservation reservation = new CarReservation(startDate, numDays, car.getCarId(), reservationCounter, cust);
                Set<CarReservation> bookings = bookingMap.get(car.getCarId());
                if (bookings == null || bookings.size() == 0) {
                    bookings = new HashSet<>();
                }
                bookings.add(reservation);
                bookingMap.put(car.getCarId(), bookings);
                reservationCounter++;
                return new Response<CarReservation>(true, reservation, "Car has been reserved.");
            }
        }
        return new Response<CarReservation>(false, null, "No cars of this type found for specified rental period.");
    }
    
    private boolean checkCarAvailability(Car car, LocalDate startDate, int numDays) {
          LocalDate endDate = startDate.plusDays(numDays - 1);
          Set<CarReservation> bookings = bookingMap.get(car.getCarId());
          if (bookings == null) {
              return true;
          }
          for (CarReservation booking: bookings) { //check for overlapping reservation dates
              if (!(startDate.isAfter(booking.getEndDate()) || endDate.isBefore(booking.getStartDate()))) {
                 return false;  
              }
          }
          return true;
    }
    
    //let customer remove their reservation
    public synchronized Response<String> removeReservation(int resId, int carId) {
         Set<CarReservation> bookings = bookingMap.get(carId);
         if (bookings != null) {
            boolean result = bookings.remove(new CarReservation(null, 0, resId, carId, null));
            if (result) {
               return new Response<String>(true, null, "Removed specified reservation id.");
            }
         }
         return new Response<String>(false, null, "No matching reservation found.");
    }
    
    //modify reservation type
    public synchronized Response<CarReservation> modifyReservationType(int resId, int carId, String newType) {
               Set<Car> cars = typeMap.get(newType);
               if (cars == null || cars.size() == 0) { //check if cars exist for new type
                   return new Response<CarReservation>(false, null, "No cars found to match specified type.");
               }
               CarReservation res = returnReservation(resId, carId);
               if (res != null) {
                  for (Car car: cars) { //go through cars
                      if (checkCarAvailability(car, res.getStartDate(), res.getNumDays())) {
                          res.setCarId(car.getCarId());    
                          bookingMap.get(carId).remove(res);
                          Set<CarReservation> newBookings = bookingMap.get(car.getCarId());
                          if (newBookings == null || newBookings.size() == 0) {
                              newBookings = new HashSet<>();
                            }
                          newBookings.add(res);
                          bookingMap.put(car.getCarId(), newBookings);
                          return new Response<CarReservation>(true, res, "Modified reservation.");
                      }
                  }
                  return new Response<CarReservation>(false, null, "No cars free in specified type.");
                }
            else {
                return new Response<CarReservation>(false, null, "No matching reservation found.");
            }
    }
    
    //modify reservation dates
    public synchronized Response<CarReservation> modifyReservationDates(int resId, int carId, LocalDate newDate, int numDays) {
         CarReservation res = returnReservation(resId, carId);
         if (res != null) {
               bookingMap.get(carId).remove(res);
               res.setStartDate(newDate);
               res.setEndDate(newDate.plusDays(numDays-1));
               bookingMap.get(carId).add(res);
               return new Response<CarReservation>(true, res, "Modifying reservation.");
         }
         return new Response<CarReservation>(false, null, "No matching reservation found.");
    }
    
    //test method to check if car is in type inventory, only need car id
    boolean checkCarExistence(String type, int carId) {
        Set<Car> carSet = typeMap.get(type);
        return carSet != null && carSet.contains(new Car(carId, "", "", "", 0));
    }
    
    CarReservation returnReservation(int resId, int carId) {
         Set<CarReservation> bookings = bookingMap.get(carId);
         if (bookings != null) {
            for (CarReservation booking: bookings) {
                   if (booking.getReservationId() == resId) {
                      return booking;
                   }
            }
         }
        return null;
    }
}
