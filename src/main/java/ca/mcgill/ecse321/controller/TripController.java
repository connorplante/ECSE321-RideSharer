package ca.mcgill.ecse321.controller;

import ca.mcgill.ecse321.HibernateUtil;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.model.*;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.model.*;
import ca.mcgill.ecse321.model.Trip.Status;
import ca.mcgill.ecse321.controller.InvalidInputException;

@RestController
@RequestMapping("/Trip")
public class TripController {

    public TripController(){}

    /**
     * Method to create a Trip 
     * Use the url /Trip/createTrip
     * @param start
     * @param end
     * @param date
     * @param time
     * @param email
     * @param username 
     * @param carID
     * @param numSeats
     * @param stops 
     * @param prices 
     * @return Trip trip
     */
    
    @RequestMapping("/createTrip")
    public Trip createTrip(@RequestParam(value="start")String start, @RequestParam(value="end")String end, 
    @RequestParam(value="date")Date date, @RequestParam(value="time")int time,
    @RequestParam(value="username") String username, @RequestParam(value="carID")int carID, int numSeats, @RequestParam List<String> stops, @RequestParam List<Double> prices) throws InvalidInputException{

        String error = "";
        
        //Check if input values of start and end are equal to eachother 
        if (start.equals(end)){
            error = "This end point of a trip cannot be the same as the start point";
        }
        //If the error string contains an error, throw the error and end the method
        if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}

        //Begin Session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
       
        //Access Driver object from database 
        Driver driver = (Driver) session.byNaturalId( User.class ).using( "username", username ).load();
        
        //Access Car object from database 
        Car car = (Car) session.load(Car.class, carID);
           
        //Create a Trip Object 
        Trip trip = new Trip(start, end, date, time, Status.Scheduled, driver, car);
            
        session.save(trip);
        session.getTransaction().commit();
        session.close();
            
        //Initialize a counter to be used in a for loop
        int counter = 0;

        //Defined sizePriceList as the size of the list of prices taken in as an argument 
        int sizePriceList = prices.size();

        //List trough all stops inputted by user and create a leg corresponding to the start, end, price, numSeats, and trip
            for (String stop: stops){
                if ( counter < sizePriceList){
                double price = prices.get(counter);
                String pointA = stops.get(counter);
                String pointB = stops.get(counter+1);
                Leg leg = createLeg(pointA, pointB, price, numSeats, trip);
                trip.addLeg(leg);
                counter++; 
                }
            }  
        
        //Return the created trip
        return trip;
    }
    /**
     * Method to cancel a Trip 
     * Use the url /Trip/cancelTrip
     * @param tripID
     * @return Trip trip
     */
    @RequestMapping("/cancelTrip")
    public Trip cancelTrip(@RequestParam(value="tripID")int tripID){
        
        //Begin Session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
    
        //Access trip object from db
        Trip trip = (Trip) session.load(Trip.class, tripID);
        
        //create query
        String string ="UPDATE Trips SET Status= :status WHERE TripID = :id";
        SQLQuery query1 = session.createSQLQuery(string);
        
        //Set status of trip to "1" represeting 'Cancelled'
        query1.setParameter("status", (1));
        query1.setParameter("id", tripID);
        query1.executeUpdate();
        
        //close session
        session.getTransaction().commit();
        session.close();

        return trip;

    }
 
  /**
     * Method to complete a Trip 
     * Use the url /Trip/completeTrip
     * @param tripID
     * @return Trip trip
     */
    @RequestMapping("/completeTrip")
    public Trip completeTrip(@RequestParam(value="tripID")int tripID){
    
        //Begin Session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

         //Access trip object from db
         Trip trip = (Trip) session.load(Trip.class, tripID);
    
        //create query
        String string ="UPDATE Trips SET Status= :status WHERE TripID = :id";
        SQLQuery query1 = session.createSQLQuery(string);
        //Set status of trip to "2" represeting 'Completed'
        query1.setParameter("status", (2));
        query1.setParameter("id", tripID);
        query1.executeUpdate();
        
        //close session
        session.getTransaction().commit();
        session.close();

        return trip;

    }
/**
     * Method to Create a Leg
     * Helper function used in the createTrip method 
     * @param pointA 
     * @param pointB 
     * @param price 
     * @param numSeats 
     * @param trip
     * @return Leg leg
     */

    public Leg createLeg(String pointA, String pointB, Double price, int numSeats, Trip trip){
        
        //Begin Session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        //Create new Leg object 
        Leg leg = new Leg(pointA, pointB, price, numSeats, trip);
        
        //Save to database and close session
        session.save(leg);
        session.getTransaction().commit();
        session.close();
        
        //Return Leg 
        return leg;
    }

    /**
     * Method that takes in a start and end and returns a list of trip IDs
     * @param start
     * @param end
     * @return List of Trip IDs
     */
    @RequestMapping("/findTrip")
    public List<Integer> findTrip(@RequestParam(value="start") String start, @RequestParam(value="end") String end) {
        // Initialize return list
        List<Integer> foundTripIDs = new ArrayList<Integer>();

        // Initialize queries that will be made to find trip with given start and end
        String queryStart = "SELECT LegID, Start, End, Price, NumSeats, FK_TripID FROM Legs WHERE Start = :start";
        String queryEndAndFK_TripID = "SELECT LegID, Start, End, Price, NumSeats, FK_TripID FROM Legs WHERE End = :end AND FK_tripID = :sameTrip AND LegID >= :futureLeg";
        String queryFK_TripID = "SELECT LegID, Start, End, Price, NumSeats, FK_TripID FROM Legs WHERE FK_TripID = :fk_tripid";

        // Begin Session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        // Make first query
        SQLQuery queryFindLegs = session.createSQLQuery(queryStart);
        queryFindLegs.setParameter("start", start);

        // startLegs will contain a list of the row information
        List<Object[]> startLegs = queryFindLegs.list();

        // Iterate through each leg, the information is stored in the list like this:
        // leg[0] | leg[1] | leg[2] | leg[3] | leg[4]   | leg[5] 
        // LegID  | Start  | End    | Price  | NumSeats | FK_TripID
        for (Object[] startLeg : startLegs) {
            queryFindLegs = session.createSQLQuery(queryEndAndFK_TripID);
            queryFindLegs.setParameter("end", end).setParameter("sameTrip", startLeg[5]).setParameter("futureLeg", startLeg[0]);
            List<Object[]> endLegs = queryFindLegs.list();
            
            // If this list is not null, then a suitable end leg was found
            if (!endLegs.isEmpty()) {
                foundTripIDs.add((Integer)startLeg[5]);
            }
        }

        session.getTransaction().commit();
        session.close();

        return foundTripIDs;
    }

    /**
     * method to check if a Trip (given tripID, start, end) satisfies the criteria (price)
     * @param tripId
     * @param start
     * @param end
     * @param price
     * @return Boolean 
     */
    @RequestMapping("/filterPrice")
    public boolean filterPrice(@RequestParam(value="tripId") int tripId, @RequestParam(value="start") String start, @RequestParam(value="end") String end, 
    @RequestParam(value="price") int price) {
        boolean fitsCriteria = false;
        int effectivePrice = 0;

        // Get legs corresponding to Trip ID
        String queryLegs = "SELECT LegID, Start, End, Price, NumSeats, FK_TripID FROM Legs WHERE FK_TripID = :tripId";

        // Begin Session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        // Make query on legs
        SQLQuery queryGetLegs = session.createSQLQuery(queryLegs);
        queryGetLegs.setParameter("tripId", tripId);
        
        // tripLegs contains the legs of the trips
        // Legs are stored as such in the array:
        // leg[0] | leg[1] | leg[2] | leg[3] | leg[4]   | leg[5] 
        // LegID  | Start  | End    | Price  | NumSeats | FK_TripID
        List<Object[]> tripLegs = queryGetLegs.list();

        // iterate through tripLegs to add prices
        // assumes valid tripId, start, end
        for (Object[] tripLeg : tripLegs) {
            if (((String)tripLeg[1]).equals(start)) {
                effectivePrice += (Integer)tripLeg[3];
            } else if (effectivePrice != 0) {
                effectivePrice += (Integer)tripLeg[3];
            } 
            if (((String)tripLeg[2]).equals(end)) {
                break;
            }
        }

        fitsCriteria = price >= effectivePrice;
        return fitsCriteria;
    }

    /**
     * method to check if a Trip (given tripID, start, end) satisfies the criteria (make)
     * @param tripId
     * @param start
     * @param end
     * @param make
     * @return Boolean 
     */
    @RequestMapping("/filterMake")
    public boolean filterMake(@RequestParam(value="tripId") int tripId, @RequestParam(value="start") String start, @RequestParam(value="end") String end, 
    @RequestParam(value="make") String make) {
        boolean fitsCriteria = false;

        // Get legs corresponding to Trip ID
        String queryTrip = "SELECT FK_CarID FROM Trips WHERE TripID = :tripId";
        String queryMake = "SELECT Make FROM Cars WHERE CarID = :carId";

        // Begin Session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        // Make query on legs, gets CarId
        SQLQuery queryGetCarId = session.createSQLQuery(queryTrip);
        queryGetCarId.setParameter("tripId", tripId);
        List<Integer> cars = queryGetCarId.list();
        if (cars.size() != 1) {
            System.out.println("Oops! Something went wrong");
            return false;
        }
        int carId = cars.get(0); // holds carId of thr given trip

        // Make query on Car, gets Make of Car
        SQLQuery queryGetMake = session.createSQLQuery(queryMake);
        queryGetMake.setParameter("carId", carId);
        List<String> makes = queryGetMake.list();
        if (makes.size() != 1) {
            System.out.println("Oops! Something went wrong");
            return false;
        }
        String carMake = makes.get(0);

        // Check if make of car matches make given by user
        fitsCriteria = make.equalsIgnoreCase(carMake);

        return fitsCriteria;
    }

    /**
     * method to check if a Trip (given tripID, start, end) satisfies the criteria (model)
     * @param tripId
     * @param start
     * @param end
     * @param model
     * @return Boolean 
     */
    @RequestMapping("/filterModel")
    public boolean filterModel(@RequestParam(value="tripId") int tripId, @RequestParam(value="start") String start, @RequestParam(value="end") String end, 
    @RequestParam(value="model") String model) {
        boolean fitsCriteria = false;

        // Get legs corresponding to Trip ID
        String queryTrip = "SELECT FK_CarID FROM Trips WHERE TripID = :tripId";
        String queryModel = "SELECT Model FROM Cars WHERE CarID = :carId";

        // Begin Session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        // Make query on legs, gets CarId
        SQLQuery queryGetCarId = session.createSQLQuery(queryTrip);
        queryGetCarId.setParameter("tripId", tripId);
        List<Integer> cars = queryGetCarId.list();
        if (cars.size() != 1) {
            System.out.println("Oops! Something went wrong");
            return false;
        }
        int carId = cars.get(0); // holds carId of thr given trip

        // Make query on Car, gets Make of Car
        SQLQuery queryGetModel = session.createSQLQuery(queryModel);
        queryGetModel.setParameter("carId", carId);
        List<String> models = queryGetModel.list();
        if (models.size() != 1) {
            System.out.println("Oops! Something went wrong");
            return false;
        }
        String carModel = models.get(0);

        System.out.println(carModel);

        // Check if make of car matches make given by user
        fitsCriteria = model.equalsIgnoreCase(carModel);

        return fitsCriteria;
    }

    /**
     * method to check if a Trip (given tripID, start, end) satisfies the criteria (model)
     * @param tripId
     * @param start
     * @param end
     * @param model
     * @return Boolean 
     */
    @RequestMapping("/filterYear")
    public boolean filterYear(@RequestParam(value="tripId") int tripId, @RequestParam(value="start") String start, @RequestParam(value="end") String end, 
    @RequestParam(value="year") int year) {
        boolean fitsCriteria = false;

        // Get legs corresponding to Trip ID
        String queryTrip = "SELECT FK_CarID FROM Trips WHERE TripID = :tripId";
        String queryYear = "SELECT Year FROM Cars WHERE CarID = :carId";

        // Begin Session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        // Make query on legs, gets CarId
        SQLQuery queryGetCarId = session.createSQLQuery(queryTrip);
        queryGetCarId.setParameter("tripId", tripId);
        List<Integer> cars = queryGetCarId.list();
        if (cars.size() != 1) {
            System.out.println("Oops! Something went wrong");
            return false;
        }
        int carId = cars.get(0); // holds carId of thr given trip

        // Make query on Car, gets Make of Car
        SQLQuery queryGetYear = session.createSQLQuery(queryYear);
        queryGetYear.setParameter("carId", carId);
        List<Integer> years = queryGetYear.list();
        if (years.size() != 1) {
            System.out.println("Oops! Something went wrong");
            return false;
        }
        int carYear = years.get(0);

        // Check if make of car matches make given by user
        fitsCriteria = (year <= carYear);

        return fitsCriteria;
    }
}