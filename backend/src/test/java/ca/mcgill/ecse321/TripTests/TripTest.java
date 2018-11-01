// package ca.mcgill.ecse321.TripTests;
// import org.junit.Test;

// import ca.mcgill.ecse321.SessionFactoryRule;
// import ca.mcgill.ecse321.controller.*;
// import ca.mcgill.ecse321.model.*;
// import ca.mcgill.ecse321.model.Trip.Status;

// import static org.junit.Assert.*;

// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.sql.Date;
// import java.util.List;

// import org.hibernate.SQLQuery;
// import org.hibernate.Session;
// import org.junit.Before;
// import org.junit.Rule;

// public class TripTest {

//     @Rule 
//     public final SessionFactoryRule sf = new SessionFactoryRule();

//     public UserController tUserController;
//     public TripController tTripController;

//     @Before
//     public void before() {
//         tTripController = new TripController();
//         tTripController.changeSession(sf.getSession());
//     }

//     @Test
//     public void createTripTest() throws InvalidInputException {
//         //  Arrange 
//         Session session = sf.getSession();
//         sf.beginTransaction();

//         String username = "tDriver";

//         Driver tDriver = new Driver(username, "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);
    
//         session.save(tDriver);

//         Car tCar = new Car("tMake", "tModel", 2000, 6, "tPlate", tDriver);

//         session.save(tCar);

//         sf.commit();

//         String start = "tStart";
//         String end = "tEnd";
//         Date date = new Date(0);
//         int time = 1;
//         int carID = 1;
//         int numSeats = 4;
//         List<String> stops = new ArrayList<String>();
//         stops.add(start);
//         stops.add("tStop");
//         stops.add(end);
//         List<Integer> prices = new ArrayList<Integer>();
//         prices.add(14);
//         prices.add(19);

//         //  Act
//         String result = tTripController.createTrip(start, end, date, time, username, carID, numSeats, stops, prices);
//         Trip tTrip = tTripController.getTripByID(1);

//         //  Assert
//         assertNotNull(tTrip);
//         assertEquals(result, tTrip.toString());
//     }

//     @Test
//     public void cancelTripTest() {
//         //  Arrange 
//         Session session = sf.getSession();
//         sf.beginTransaction();

//         String username = "tDriver";

//         Driver tDriver = new Driver(username, "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);
    
//         session.save(tDriver);

//         Car tCar = new Car("tMake", "tModel", 2000, 6, "tPlate", tDriver);

//         session.save(tCar);

//         String start = "tStart";
//         String end = "tEnd";
//         Date date = new Date(0);
//         int time = 1;
//         List<String> stops = new ArrayList<String>();
//         stops.add(start);
//         stops.add("tStop");
//         stops.add(end);
//         List<Double> prices = new ArrayList<Double>();
//         prices.add(14.0);
//         prices.add(19.0);
//         Trip tTrip = new Trip(start, end, date, time, Status.Scheduled, tDriver, tCar);

//         session.save(tTrip);

//         sf.commit();

//         //  Act
//         Boolean flag = tTripController.cancelTrip(1);
//         String queryTrip = "SELECT TripID FROM Trips WHERE Status = 1";
//         SQLQuery query = session.createSQLQuery(queryTrip);


//         //  Assert
//         assertTrue(flag);
//         assertEquals(1, query.list().get(0));
//     }

//     @Test
//     public void completeTripTest() {
//         //  Arrange 
//         Session session = sf.getSession();
//         sf.beginTransaction();

//         String username = "tDriver";

//         Driver tDriver = new Driver(username, "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);
    
//         session.save(tDriver);

//         Car tCar = new Car("tMake", "tModel", 2000, 6, "tPlate", tDriver);

//         session.save(tCar);

//         String start = "tStart";
//         String end = "tEnd";
//         Date date = new Date(0);
//         int time = 1;
//         List<String> stops = new ArrayList<String>();
//         stops.add(start);
//         stops.add("tStop");
//         stops.add(end);
//         List<Double> prices = new ArrayList<Double>();
//         prices.add(14.0);
//         prices.add(19.0);
//         Trip tTrip = new Trip(start, end, date, time, Status.Scheduled, tDriver, tCar);

//         session.save(tTrip);

//         sf.commit();

//         //  Act
//         Boolean flag = tTripController.completeTrip(1, username);
//         String queryTrip = "SELECT TripID FROM Trips WHERE Status = 2";
//         SQLQuery query = session.createSQLQuery(queryTrip);

//         //  Assert
//         assertTrue(flag);
//         assertEquals(1, query.list().get(0));
//     }

//     @Test 
//     public void createLegTest() {
//         //  Arrange
//         Session session = sf.getSession();
//         sf.beginTransaction();

//         String username = "tDriver";

//         Driver tDriver = new Driver(username, "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);
    
//         session.save(tDriver);

//         Car tCar = new Car("tMake", "tModel", 2000, 6, "tPlate", tDriver);

//         session.save(tCar);

//         String start = "tStart";
//         String end = "tEnd";
//         Date date = new Date(0);
//         int time = 1;
//         List<String> stops = new ArrayList<String>();
//         stops.add(start);
//         stops.add("tStop");
//         stops.add(end);
//         List<Double> prices = new ArrayList<Double>();
//         prices.add(14.0);
//         prices.add(19.0);
//         Trip tTrip = new Trip(start, end, date, time, Status.Scheduled, tDriver, tCar);

//         session.save(tTrip);

//         sf.commit();

//         //  Act 
//         Leg tLeg = tTripController.createLeg(start, end, 14, 6, tTrip);
//         Leg reLeg = (Leg) session.load(Leg.class, 1);

//         //  Assert
//         assertNotNull(reLeg);
//         assertEquals(tLeg.getPrice(), reLeg.getPrice(), 0);
//     }

//     @Test
//     public void findTripTest() throws InvalidInputException {
//         //  Arrange
//         Session session = sf.getSession();
//         sf.beginTransaction();

//         String username = "tDriver";

//         Driver tDriver = new Driver(username, "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);
    
//         session.save(tDriver);

//         Car tCar = new Car("tMake", "tModel", 2000, 6, "tPlate", tDriver);

//         session.save(tCar);

//         String start = "tStart";
//         String end = "tEnd";
//         Date date = new Date(0);
//         int time = 1;
//         List<String> stops = new ArrayList<String>();
//         stops.add(start);
//         stops.add("tStop");
//         stops.add(end);
//         List<Integer> prices = new ArrayList<Integer>();
//         prices.add(14);
//         prices.add(19);

//         sf.commit();

//         //  Act
//         tTripController.createTrip(start, end, date, time, username, 1, 4, stops, prices);
//         List<Integer> tripIDs = tTripController.findTrip(start, end, null, null, null, null, null, null);

//         //  Assert
//         assertNotNull(tripIDs);
//         assertEquals(1, tripIDs.size());
//         assertEquals(1, (int) tripIDs.get(0));
//     }

//     @Test
//     public void filterPriceTest() throws InvalidInputException {
//         //  Arrange
//         Session session = sf.getSession();
//         sf.beginTransaction();

//         String username = "tDriver";

//         Driver tDriver = new Driver(username, "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);
    
//         session.save(tDriver);

//         Car tCar = new Car("tMake", "tModel", 2000, 6, "tPlate", tDriver);

//         session.save(tCar);

//         String start = "tStart";
//         String end = "tEnd";
//         Date date = new Date(0);
//         int time = 1;
//         List<String> stops = new ArrayList<String>();
//         stops.add(start);
//         stops.add(end);
//         List<Integer> prices = new ArrayList<Integer>();
//         prices.add(14);

//         sf.commit();

//         //  Act
//         tTripController.createTrip(start, end, date, time, username, 1, 4, stops, prices);
//         boolean resultT = tTripController.filterPrice(1, start, end, 14);
//         boolean resultF = tTripController.filterPrice(1, start, end, 10);

//         //  Assert
//         assertTrue(resultT);
//         assertFalse(resultF);
//     }

//     @Test
//     public void filterMakeTest() throws InvalidInputException {
//         //  Arrange
//         Session session = sf.getSession();
//         sf.beginTransaction();

//         String username = "tDriver";

//         Driver tDriver = new Driver(username, "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);
    
//         session.save(tDriver);

//         String make = "tMake";

//         Car tCar = new Car(make, "tModel", 2000, 6, "tPlate", tDriver);

//         session.save(tCar);

//         String start = "tStart";
//         String end = "tEnd";
//         Date date = new Date(0);
//         int time = 1;
//         List<String> stops = new ArrayList<String>();
//         stops.add(start);
//         stops.add(end);
//         List<Integer> prices = new ArrayList<Integer>();
//         prices.add(14);

//         sf.commit();

//         //  Act
//         tTripController.createTrip(start, end, date, time, username, 1, 4, stops, prices);
//         boolean resultT = tTripController.filterMake(1, start, end, make);
//         boolean resultF = tTripController.filterMake(1, start, end, "b");

//         //  Assert
//         assertTrue(resultT);
//         assertFalse(resultF);
//     }

//     @Test
//     public void filterModelTest() throws InvalidInputException {
//         //  Arrange
//         Session session = sf.getSession();
//         sf.beginTransaction();

//         String username = "tDriver";

//         Driver tDriver = new Driver(username, "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);
    
//         session.save(tDriver);

//         String make = "tMake";
//         String model = "tModel";
//         Car tCar = new Car(make, model, 2000, 6, "tPlate", tDriver);

//         session.save(tCar);

//         String start = "tStart";
//         String end = "tEnd";
//         Date date = new Date(0);
//         int time = 1;
//         List<String> stops = new ArrayList<String>();
//         stops.add(start);
//         stops.add(end);
//         List<Integer> prices = new ArrayList<Integer>();
//         prices.add(14);

//         sf.commit();

//         //  Act
//         tTripController.createTrip(start, end, date, time, username, 1, 4, stops, prices);
//         boolean resultT = tTripController.filterModel(1, start, end, model);
//         boolean resultF = tTripController.filterModel(1, start, end, "b");

//         //  Assert
//         assertTrue(resultT);
//         assertFalse(resultF);
//     }

//     @Test
//     public void filterYearTest() throws InvalidInputException {
//         //  Arrange
//         Session session = sf.getSession();
//         sf.beginTransaction();

//         String username = "tDriver";

//         Driver tDriver = new Driver(username, "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);
    
//         session.save(tDriver);

//         String make = "tMake";
//         String model = "tModel";
//         int year = 2000;
//         Car tCar = new Car(make, model, year, 6, "tPlate", tDriver);

//         session.save(tCar);

//         String start = "tStart";
//         String end = "tEnd";
//         Date date = new Date(0);
//         int time = 1;
//         List<String> stops = new ArrayList<String>();
//         stops.add(start);
//         stops.add(end);
//         List<Integer> prices = new ArrayList<Integer>();
//         prices.add(14);

//         sf.commit();

//         //  Act
//         tTripController.createTrip(start, end, date, time, username, 1, 4, stops, prices);
//         boolean resultT = tTripController.filterYear(1, start, end, year);
//         boolean resultF = tTripController.filterYear(1, start, end, 2001);

//         //  Assert
//         assertTrue(resultT);
//         assertFalse(resultF);
//     }

//     @Test
//     public void filterDateTest() throws InvalidInputException {
//         //  Arrange
//         Session session = sf.getSession();
//         sf.beginTransaction();

//         String username = "tDriver";

//         Driver tDriver = new Driver(username, "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);
    
//         session.save(tDriver);

//         String make = "tMake";
//         String model = "tModel";
//         int year = 2000;
//         Car tCar = new Car(make, model, year, 6, "tPlate", tDriver);

//         session.save(tCar);

//         String start = "tStart";
//         String end = "tEnd";
//         Date date = new Date(2018, 5, 11);
//         int time = 1;
//         List<String> stops = new ArrayList<String>();
//         stops.add(start);
//         stops.add(end);
//         List<Integer> prices = new ArrayList<Integer>();
//         prices.add(14);

//         sf.commit();

//         //  Act
//         tTripController.createTrip(start, end, date, time, username, 1, 4, stops, prices);
//         boolean resultT = tTripController.filterDate(1, start, end, date, date);
//         boolean resultF = tTripController.filterDate(1, start, end, new Date(1999-8-13), new Date(1999-8-5));

//         //  Assert
//         assertTrue(resultT);
//         assertFalse(resultF);
//     }
// }
