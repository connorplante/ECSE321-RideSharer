// package ca.mcgill.ecse321;
// import org.junit.Test;

// import ca.mcgill.ecse321.controller.CarController;
// import ca.mcgill.ecse321.model.*;

// import static org.junit.Assert.*;

// import org.hibernate.Session;
// import org.junit.Before;
// import org.junit.Rule;

// public class CarTest {

//     @Rule
//     public final SessionFactoryRule sf = new SessionFactoryRule();

//     public CarController tCarController;

//     @Before
//     public void before() {
//         tCarController = new CarController();
//         tCarController.changeSession(sf.getSession());
//     }

//     @Test
//     public void returnsCar() {

//         Session session = sf.getSession();
        
//         sf.beginTransaction();

//         String make = "tMake";
//         String model = "tModel";
//         int year = 2000;
//         int numSeats = 0;
//         String licencePlate = "tLicencePlate";
//         Driver tDriver = new Driver("tDriver", "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);

//         session.save(tDriver);

//         Car tCar = new Car();
//         tCar.setMake(make);
//         tCar.setModel(model);
//         tCar.setYear(year);
//         tCar.setNumSeats(numSeats);
//         tCar.setLicencePlate(licencePlate);
//         tCar.setDriver(tDriver);

//         session.save(tCar);

//         Car returnedCar = tCarController.getCarByID(tCar.getCarID());

//         assertNotNull(returnedCar);
//         assertEquals(make, returnedCar.getMake());
//         assertEquals(model, returnedCar.getModel());
//         assertEquals(year, returnedCar.getYear());
//         assertEquals(licencePlate, returnedCar.getLicencePlate());
//         assertEquals(tDriver.getUsername(), returnedCar.getDriver().getUsername());

//         session.getTransaction().commit();
    
//     }

//     @Test
//     public void createCarTest() {

//         Session session = sf.getSession();

//         sf.beginTransaction();
        
//         String make = "tMake";
//         String model = "tModel";
//         int year = 2000;
//         int numSeats = 0;
//         String licencePlate = "tLicencePlate";
//         Driver driver = new Driver("tDriver", "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);

//         session.save(driver);

//         session.getTransaction().commit();

//         String s = tCarController.createCar(make, model, year, numSeats, licencePlate, driver.getUsername());
//         int carID = 1;
//         Car reCar = tCarController.getCarByID(carID);

//         assertNotNull(reCar);
//         assertEquals(carID, reCar.getCarID());
//         assertEquals(make, reCar.getMake());
//         assertEquals(model, reCar.getModel());
//         assertEquals(year, reCar.getYear());
//         assertEquals(numSeats, reCar.getNumSeats());
//         assertEquals(licencePlate, reCar.getLicencePlate());
//         assertEquals(driver.getUserID(), reCar.getDriver().getUserID());

//     }

//     @Test
//     public void updateCarTest() {

//         Session session = sf.getSession();

//         sf.beginTransaction();

//         String make = "tMake1";
//         String model = "tModel1";
//         int year = 2000;
//         int numSeats = 0;
//         String licencePlate = "tLicencePlate1";
//         Driver driver = new Driver("tDriver", "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);

//         session.save(driver);

//         Car tCar = new Car(make, model, year, numSeats, licencePlate, driver);

//         int carID = tCar.getCarID();

//         session.save(tCar);

//         session.getTransaction().commit();

//         String newMake = "tMake2";
//         String newModel = "tModel2";
//         int newYear = 2001;
//         int newNumSeats = 1;
//         String newLicencePlate = "tLicencePlate2";

//         String result = tCarController.updateCar(tCar.getCarID(), newMake, newModel, newYear, newNumSeats, newLicencePlate);
//         Car reCar = tCarController.getCarByID(tCar.getCarID());

//         assertEquals(newMake, reCar.getMake());
//         assertEquals(newModel, reCar.getModel());
//         assertEquals(newYear, reCar.getYear());
//         assertEquals(newNumSeats, reCar.getNumSeats());
//         assertEquals(newLicencePlate, reCar.getLicencePlate());

//     }

//     @Test
//     public void removeCarTest() {

//         Session session = sf.getSession();

//         sf.beginTransaction();

//         String make = "tMake";
//         String model = "tModel";
//         int year = 2000;
//         int numSeats = 0;
//         String licencePlate = "tLicencePlate";
//         Driver driver = new Driver("tDriver", "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);

//         session.save(driver);

//         Car tCar = new Car();
//         tCar.setMake(make);
//         tCar.setModel(model);
//         tCar.setYear(year);
//         tCar.setNumSeats(numSeats);
//         tCar.setLicencePlate(licencePlate);
//         tCar.setDriver(driver);
//         tCar.setStatus(true);
//         //int carID = tCar.getCarID();

//         session.save(tCar);

//         session.getTransaction().commit();

//         String result = tCarController.removeCar(tCar.getCarID());
//         Car removed = tCarController.getCarByID(tCar.getCarID());

//         assertFalse(removed.getStatus());
//         assertEquals(result, removed.toString());
//     }
// }