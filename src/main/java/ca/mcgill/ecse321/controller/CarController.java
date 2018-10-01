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
    public static Car createCar(@RequestParam(value="make") String make, @RequestParam(value="model") String model,
    @RequestParam(value="year") int year, @RequestParam(name="numSeats") int numSeats, @RequestParam(name="licencePlate")
    String licencePlate, @RequestParam(value="userID") String driverUsername){
       
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Driver driver = (Driver) session.byNaturalId(User.class).using("username", driverUsername).load();

        try{
            driver.getCars();
        }catch(NullPointerException n){
            driver.initArrayLists();
        }

        Car car = new Car(make, model, year, numSeats, licencePlate, driver);
        driver.addCar(car);
        
        session.save(car);
        session.getTransaction().commit();
        session.close();
        
        return car;
    }

    /**
     * Method to updates the fields of a car object
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
    public static Car updateCar(@RequestParam(value="carID") int carID, @RequestParam(value="make") String make, @RequestParam(value="model") String model,
    @RequestParam(value="year") int year, @RequestParam(name="numSeats") int numSeats, @RequestParam(name="licencePlate")
    String licencePlate){

        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Car car = (Car) session.load(Car.class, carID);

        Driver driver = car.getDriver();
        driver.initArrayLists();
        

        car.setMake(make);
        car.setModel(model);
        car.setNumSeats(year);
        car.setNumSeats(numSeats);
        car.setLicencePlate(licencePlate);

        session.getTransaction().commit();
        session.close();

        return car;
    }

     /**
     * Method to remove a car by updating the status to inactive
     * Use the url /Car/removeCar
     * @param carID
     * @return Car car
     */
    @RequestMapping("/removeCar")
    public static Car removeCar(@RequestParam(value="carID") int carID){
        
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Car car = (Car) session.load(Car.class, carID);

        car.setStatus(false);

        session.getTransaction().commit();
        session.close();

        return car;
    }

}