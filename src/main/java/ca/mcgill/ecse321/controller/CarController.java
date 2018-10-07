package ca.mcgill.ecse321.controller;

import ca.mcgill.ecse321.HibernateUtil;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.model.Car;
import ca.mcgill.ecse321.model.Driver;
import ca.mcgill.ecse321.model.User;


@RestController
@RequestMapping("/Car")
public class CarController {

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
    public static String createCar(@RequestParam(value="make") String make, @RequestParam(value="model") String model,
    @RequestParam(value="year") int year, @RequestParam(name="numSeats") int numSeats, @RequestParam(name="licencePlate")
    String licencePlate, @RequestParam(value="username") String driverUsername){
       
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Driver driver;
        try{
            driver = (Driver) session.byNaturalId(User.class).using("username", driverUsername).load();
        }catch(Exception i){
            session.close();
            return "Driver does not exist!";
        }

        
        //the association between driver and car is handled by the datadase, so these are reset
        try{
            driver.getCars();
        }catch(NullPointerException n){
            driver.initArrayLists();
        }

        Car car;

        try{
            car = new Car(make, model, year, numSeats, licencePlate, driver);
            driver.addCar(car);
            session.save(car);
            session.getTransaction().commit();
        }catch(Exception e){
            session.close();
            return "Could not create car!";
        }

        session.close();
        
        return car.toString();
    }

    /**
     * Method to updates the fields of a car object
     * Returns the string representation of the car to be updated with its new values
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
    public static String updateCar(@RequestParam(value="carID") int carID, @RequestParam(value="make") String make, @RequestParam(value="model") String model,
    @RequestParam(value="year") int year, @RequestParam(name="numSeats") int numSeats, @RequestParam(name="licencePlate")
    String licencePlate){

        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Car car;
        try{
            car = (Car) session.load(Car.class, carID);
        }catch(Exception e){
            session.close();
            return "Car does not exist!";
        }

        // Since these associations are handled by the database, reset them
        Driver driver = car.getDriver();
        driver.initArrayLists();
        
        car.setMake(make);
        car.setModel(model);
        car.setNumSeats(year);
        car.setNumSeats(numSeats);
        car.setLicencePlate(licencePlate);

        try{
            session.getTransaction().commit();
        }catch(Exception e){
            session.close();
            return "Cannot make these changes!";
        }

        session.close();

        return car.toString();
    }

     /**
     * Method to remove a car by updating the status to inactive
     * Returns the car removed with its status set to false
     * Use the url /Car/removeCar
     * @param carID
     * @return Car car
     */
    @RequestMapping("/removeCar")
    public static String removeCar(@RequestParam(value="carID") int carID){
        
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Car car;
        
        try{
            car = (Car) session.load(Car.class, carID);
        }catch(Exception e){
            session.close();
            return "Car does not exist!";
        }

        car.setStatus(false);

        session.getTransaction().commit();
        session.close();

        return car.toString() + " removed";
    }

}
