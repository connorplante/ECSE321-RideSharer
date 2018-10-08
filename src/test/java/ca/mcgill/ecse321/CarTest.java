package ca.mcgill.ecse321;
import org.junit.Test;

import ca.mcgill.ecse321.controller.CarController;
import ca.mcgill.ecse321.model.*;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Rule;

public class CarTest {

    @Rule
    public final SessionFactoryRule sf = new SessionFactoryRule();

    public CarController tCarController;

    @Before
    public void before() {
        tCarController = new CarController();
        tCarController.changeSession(sf.getSession());
    }

    @Test
    public void returnsCar() {

        Session session = sf.getSession();
        
        sf.beginTransaction();

        String make = "tMake";
        String model = "tModel";
        int year = 2000;
        int numSeats = 0;
        String licencePlate = "tLicencePlate";
        Driver tDriver = new Driver("tDriver", "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);

        Car tCar = new Car();
        tCar.setMake(make);
        tCar.setModel(model);
        tCar.setYear(year);
        tCar.setNumSeats(numSeats);
        tCar.setLicencePlate(licencePlate);
        tCar.setDriver(tDriver);

        int tCarID = tCar.getCarID();

        session.save(tCar);

        sf.commit();

        Car returnedCar = tCarController.getCarByID(tCarID);

        assertNotNull(returnedCar);
        assertEquals(tCarID, returnedCar.getCarID());
        assertEquals(make, returnedCar.getMake());
        assertEquals(model, returnedCar.getModel());
        assertEquals(year, returnedCar.getYear());
        assertEquals(licencePlate, returnedCar.getLicencePlate());
        assertEquals(tDriver.getUsername(), returnedCar.getDriver().getUsername());
    
    }

    @Test
    public void createCarTest() {

        String make = "tMake";
        String model = "tModel";
        int year = 2000;
        int numSeats = 0;
        String licencePlate = "tLicencePlate";
        Driver driver = new Driver("tDriver", "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);

        String s = tCarController.createCar(make, model, year, numSeats, licencePlate, driver.getUsername());
        String[] t = s.split("carID:|,make");
        int carID = Integer.parseInt(t[0].substring(t[0].length() - 1));
        Car reCar = tCarController.getCarByID(carID);

        assertNotNull(reCar);
        //assertEquals(s, reCar.toString());
        assertEquals(carID, reCar.getCarID());
        assertEquals(make, reCar.getMake());
        assertEquals(model, reCar.getModel());
        assertEquals(year, reCar.getYear());
        assertEquals(numSeats, reCar.getNumSeats());
        assertEquals(licencePlate, reCar.getLicencePlate());
        assertEquals(driver.getUserID(), reCar.getDriver().getUserID());

    }

    @Test
    public void updateCarTest() {

        Session session = sf.getSession();

        sf.beginTransaction();

        String make = "tMake1";
        String model = "tModel1";
        int year = 2000;
        int numSeats = 0;
        String licencePlate = "tLicencePlate1";
        Driver driver = new Driver("tDriver", "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);

        Car tCar = new Car();
        tCar.setMake(make);
        tCar.setModel(model);
        tCar.setYear(year);
        tCar.setNumSeats(numSeats);
        tCar.setLicencePlate(licencePlate);
        tCar.setDriver(driver);

        int carID = tCar.getCarID();

        session.save(tCar);

        sf.commit();

        String newMake = "tMake2";
        String newModel = "tModel2";
        int newYear = 2001;
        int newNumSeats = 1;
        String newLicencePlate = "tLicencePlate2";

        String result = tCarController.updateCar(carID, newMake, newModel, newYear, newNumSeats, newLicencePlate);
        Car reCar = tCarController.getCarByID(carID);

        assertEquals(result, reCar.toString());
        assertEquals(newMake, reCar.getMake());
        assertEquals(newModel, reCar.getModel());
        assertEquals(newYear, reCar.getYear());
        assertEquals(newNumSeats, reCar.getNumSeats());
        assertEquals(newLicencePlate, reCar.getLicencePlate());

    }

    @Test
    public void removeCarTest() {

        Session session = sf.getSession();

        sf.beginTransaction();

        String make = "tMake";
        String model = "tModel";
        int year = 2000;
        int numSeats = 0;
        String licencePlate = "tLicencePlate";
        Driver driver = new Driver("tDriver", "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);

        Car tCar = new Car();
        tCar.setMake(make);
        tCar.setModel(model);
        tCar.setYear(year);
        tCar.setNumSeats(numSeats);
        tCar.setLicencePlate(licencePlate);
        tCar.setDriver(driver);
        tCar.setStatus(true);
        int carID = tCar.getCarID();

        session.save(tCar);

        sf.commit();

        String result = tCarController.removeCar(carID);
        Car removed = tCarController.getCarByID(carID);

        assertFalse(removed.getStatus());
        assertEquals(result, removed.toString());
    }
}