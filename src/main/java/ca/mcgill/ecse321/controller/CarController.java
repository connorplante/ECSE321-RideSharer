package ca.mcgill.ecse321.controller;

import ca.mcgill.ecse321.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.model.Car;
import ca.mcgill.ecse321.model.Driver;
import ca.mcgill.ecse321.model.User;
import org.hibernate.Hibernate;
import org.hibernate.Transaction;

@RestController
@RequestMapping("/Car")
public class CarController {
    
    Session session = HibernateUtil.getSession();

    /**
     * Method to create a car and add to database
     * Returns the String representation of the car created
     * Use the url /Car/createCar
     * @param make
     * @param model
     * @param year
     * @param numSeats
     * @param licencePlate
     * @param driver
     * @return Car car
     */
    @RequestMapping("/createCar")
    public String createCar(@RequestParam(value="make") String make, @RequestParam(value="model") String model,
    @RequestParam(value="year") int year, @RequestParam(name="numSeats") int numSeats, @RequestParam(name="licencePlate")
    String licencePlate, @RequestParam(value="username") String driverUsername){
       
        // Get session and begin transaction
        Session session = this.session;
        session.beginTransaction();

        // If the driver exists in the database, get the driver
        Driver driver;
        try{
            driver = (Driver) session.byNaturalId(User.class).using("username", driverUsername).load();
        }catch(Exception i){
            return "Driver does not exist!";
        }

        
        // The association between driver and car is handled by the datadase, so these are reset to avoid null pointers
        try{
            driver.getCars();
        }catch(NullPointerException n){
            driver.initArrayLists();
        }

        // Try to make a new car and put it into the database, catch error if it does not work
        Car car;
        try{
            car = new Car(make, model, year, numSeats, licencePlate, driver);
            driver.addCar(car);
            session.save(car);
            session.getTransaction().commit();
        }catch(Exception e){
            return "Could not create car!";
        }
        
        return car.toString();
    }

    /**
     * Method to updates the fields of a car object
     * Returns the string representation of the car to be updated with its new values
     * The only field in Car that cannot be updated is the Driver, as a Car belongs to a Driver
     * Use the url /Car/updateCar
     * @param car
     * @param make
     * @param model
     * @param year
     * @param numSeats
     * @param licencePlate
     * @return Car car
     */
    @RequestMapping("/updateCar")
    public String updateCar(@RequestParam(value="carID") int carID, @RequestParam(value="make") String make, @RequestParam(value="model") String model,
    @RequestParam(value="year") int year, @RequestParam(name="numSeats") int numSeats, @RequestParam(name="licencePlate")
    String licencePlate){

        // Get the session and start the transaction
        Session session = this.session;
        session.beginTransaction();

        // Try to get the car from the database, catch the error if it does not exist
        Car car;
        try{
            car = (Car) session.load(Car.class, carID);
        }catch(Exception e){
            return "Car does not exist!";
        }

        // Since these associations are handled by the database, reset them to avoid null pointers
        Driver driver = car.getDriver();
        driver.initArrayLists();
        
        // Update the car's fields with the new information
        car.setMake(make);
        car.setModel(model);
        car.setYear(year);
        car.setNumSeats(numSeats);
        car.setLicencePlate(licencePlate);

        // Try to save the new information to the database, catch the error if not
        try{
            session.getTransaction().commit();
        }catch(Exception e){
            return "Cannot make these changes!";
        }

        return car.toString();
    }

     /**
     * Method to remove a car by updating the status to inactive
     * Returns the string representation of the Car removed with its status set to false
     * Use the url /Car/removeCar
     * @param carID
     * @return Car car
     */
    @RequestMapping("/removeCar")
    public String removeCar(@RequestParam(value="carID") int carID){
        
        // Get the session and begin the transaction
        Session session = this.session;
        session.beginTransaction();

        // Try to load the car from the database, catch the error if it does not exist
        Car car;
        try{
            car = (Car) session.load(Car.class, carID);
        }catch(Exception e){
            return "Car does not exist!";
        }

        // Set the car's status to inactive
        car.setStatus(false);

        // Commit changes to database
        session.getTransaction().commit();

        return car.toString();
    }

    /**
     * Changes the session to the one passed
     * 
     * @param change
     */
    public void changeSession(Session change) {
        this.session = change;
    }

     /**
      * Returns the Car with the ID passed
      *
      * @param id
      * @return
      */
    public Car getCarByID(int id){
        return (Car) session.load(Car.class, id);
    }

}
