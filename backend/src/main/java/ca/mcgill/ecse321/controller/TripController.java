package ca.mcgill.ecse321.controller;

import ca.mcgill.ecse321.HibernateUtil;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.model.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.model.Trip.Status;
import ca.mcgill.ecse321.controller.InvalidInputException;

@RestController
@RequestMapping("/Trip")
@SuppressWarnings( {"deprecation", "rawtypes", "unchecked"} )
public class TripController {

    Session session = HibernateUtil.getSession();

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
    public String createTrip(@RequestParam(value="start")String start, @RequestParam(value="end")String end, 
    @RequestParam(value="date")Date date, @RequestParam(value="time")int time,
    @RequestParam(value="username") String username, @RequestParam(value="carID")int carID, int numSeats, @RequestParam List<String> stops, @RequestParam List<Integer> prices) throws InvalidInputException{
        
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
       
        Driver driver = null;
        
        //Access Driver object from database 
        try{
        driver = (Driver) session.byNaturalId( User.class ).using( "username", username ).load();
        }catch(Exception e){
            error += "driver";
        }
        
        Car car = null;

        //Access Car object from database 
        try{
        car = (Car) session.load(Car.class, carID);
        }catch(Exception e){
            error += "car";
        }
           
        Trip trip = null;
        //Create a Trip Object 
        try{
        trip = new Trip(start, end, date, time, Status.Scheduled, driver, car);
        }catch(Exception e){
            error += "trip";
        }

        String finalTrip = trip.toString();
            
        session.saveOrUpdate(trip);
        session.getTransaction().commit();
        session.close();

        if(error.length() > 0){
            return error;
        }
            
        //Initialize a counter to be used in a for loop
        int counter = 0;

        //Defined sizePriceList as the size of the list of prices taken in as an argument 
        int sizePriceList = prices.size();

        //List trough all stops inputted by user and create a leg corresponding to the start, end, price, numSeats, and trip
            for (String stop: stops){
                if ( counter < sizePriceList){
                int price = prices.get(counter);
                String pointA = stops.get(counter);
                String pointB = stops.get(counter+1);
                Leg leg = createLeg(pointA, pointB, price, numSeats, trip);
                trip.addLeg(leg);
                counter++; 
                }
            }  
    
        //Return the created trip
        return finalTrip;
    }
	//Method to return a list of upcoming trips associated with a driver 
	@RequestMapping("/scheduledTripsOfDriver")
     public List<Integer> scheduledTripsOfDriver(@RequestParam(value="username")String username){

            Session session = HibernateUtil.getSession();
            session.beginTransaction();


            Driver driver = (Driver) session.byNaturalId( User.class ).using( "username", username ).load();
        
            
            String string1 = "SELECT UserID FROM Users WHERE username= :username";
            SQLQuery query1 = session.createSQLQuery(string1);

            query1.setParameter("username", username);
            

            int userID = (Integer)query1.uniqueResult();

            String string3 = "SELECT CarID FROM Cars WHERE FK_UserID= :userID LIMIT 1";
            SQLQuery query3 = session.createSQLQuery(string3);

            query3.setParameter("userID", userID);

            //  int carID = (Integer)query3.uniqueResult();

           // Car car = (Car) session.byNaturalId(Car.class).using ("CarID", carID).load();
          
            String string4 = "SELECT TripID FROM Trips where FK_UserID = :userID and Status= 0";
            //String string2 = "SELECT TripID, Date, Time, Start, End, Status FROM Trips where FK_UserID= :userID and Status= 0";
            SQLQuery query2 = session.createSQLQuery(string4);
            query2.setParameter("userID", userID);

            List<Integer> scheduledTripsObjects = query2.list();


            // for (Object[] trip: scheduledTripsObjects){
            //     System.out.println(trip);
            // }

            return scheduledTripsObjects;

     }
	
	
	
    /**
     * Method to cancel a Trip 
     * Use the url /Trip/cancelTrip
     * @param tripID
     * @return Trip trip
     */
	
	    @RequestMapping("/updateTime")
    public String updateTrip(@RequestParam(value="time")int time, @RequestParam(value="tripID")int tripID) throws InvalidInputException {
       
        Session session = this.session;
        session.beginTransaction();
    
        //Access trip object from db
        Trip trip = (Trip) session.load(Trip.class, tripID);

        String string ="UPDATE Trips SET Time= :time WHERE TripID = :id";
        SQLQuery query1 = session.createSQLQuery(string);
        
        //Set status of trip to "1" represeting 'Cancelled'
        query1.setParameter("time", (time));
        query1.setParameter("id", tripID);
        query1.executeUpdate();
        session.getTransaction().commit();
       
        return trip.toString();
    }
    
    @RequestMapping("/updateDate")
    public String updateDate(@RequestParam(value="date")Date date, @RequestParam(value="tripID")int tripID) throws InvalidInputException {
       
        Session session = this.session;
        session.beginTransaction();
    
        //Access trip object from db
        Trip trip = (Trip) session.load(Trip.class, tripID);

        String string ="UPDATE Trips SET Date= :date WHERE TripID = :id";
        SQLQuery query1 = session.createSQLQuery(string);
        
        //Set status of trip to "1" represeting 'Cancelled'
        query1.setParameter("date", (date));
        query1.setParameter("id", tripID);
        query1.executeUpdate();
        session.getTransaction().commit();
       
        return trip.toString();
    
    }

    @RequestMapping("/updateStart")
    public String updateStart(@RequestParam(value="oldStart")String oldStart, @RequestParam(value="newStart")String newStart,@RequestParam(value="tripID")int tripID) throws InvalidInputException {
       
        Session session = this.session;
        session.beginTransaction();
    
        //Access trip object from db
        Trip trip = (Trip) session.load(Trip.class, tripID);

        String string ="UPDATE Trips SET Start= :newStart WHERE TripID = :id";
        SQLQuery query1 = session.createSQLQuery(string);
        
        //Set status of trip to "1" represeting 'Cancelled'
        query1.setParameter("newStart", (newStart));
        query1.setParameter("id", tripID);
        query1.executeUpdate();
        session.getTransaction().commit();
        
        session.beginTransaction();

        String string2 ="UPDATE Legs SET Start = :newStart WHERE FK_TripID = :id AND Start = :oldStart";
        SQLQuery query2 = session.createSQLQuery(string2);
        query2.setParameter("newStart", (newStart));
        query2.setParameter("oldStart", (oldStart));
        query2.setParameter("id", tripID);
        query2.executeUpdate();
        session.getTransaction().commit();
        
        return trip.toString();
    
    }

    @RequestMapping("/updateEnd")
    public String updateEnd(@RequestParam(value="oldEnd")String oldEnd, @RequestParam(value="newEnd")String newEnd,@RequestParam(value="tripID")int tripID) throws InvalidInputException {
       
        Session session = this.session;
        session.beginTransaction();
    
        //Access trip object from db
        Trip trip = (Trip) session.load(Trip.class, tripID);

        String string ="UPDATE Trips SET End= :newEnd WHERE TripID = :id";
        SQLQuery query1 = session.createSQLQuery(string);
        
        //Set status of trip to "1" represeting 'Cancelled'
        query1.setParameter("newEnd", (newEnd));
        query1.setParameter("id", tripID);
        query1.executeUpdate();
        session.getTransaction().commit();
        
        session.beginTransaction();

        String string2 ="UPDATE Legs SET End = :newEnd WHERE FK_TripID = :id AND End = :oldEnd";
        SQLQuery query2 = session.createSQLQuery(string2);
        query2.setParameter("newEnd", (newEnd));
        query2.setParameter("oldEnd", (oldEnd));
        query2.setParameter("id", tripID);
        query2.executeUpdate();
        session.getTransaction().commit();
        
        return trip.toString();
    
    }

@RequestMapping("/updateRoute")
    public String updateRoute(@RequestParam(value = "tripID") int tripID, @RequestParam List<String> stops, @RequestParam List<Integer> prices) throws InvalidInputException{

        Session session = this.session;
        
    
        //Access trip object from db
        Trip trip = (Trip) session.load(Trip.class, tripID);


        String sql = "SELECT LegID, Start, End, Price, NumSeats, FK_TripID FROM Legs WHERE FK_TripID = :id";
        SQLQuery query = session.createSQLQuery(sql);
        query.setParameter("id", tripID);
        
        //Populate a list with results from the query
        List<Object[]> queryLegs = query.list();
        
        //Initialize a Leg list 
        ArrayList<Leg> oldTripLegs = new ArrayList<Leg>();
        ArrayList<Leg> newTripLegs = new ArrayList<Leg>();

        int counter = 0;

        //Defined sizePriceList as the size of the list of prices taken in as an argument 
        int sizePriceList = prices.size();

        for (Object[] leg : queryLegs) {
            Leg leg1 = new Leg((Integer)leg[0],(String)leg[1],(String)leg[2], (Integer)leg[3], (Integer)leg[4], trip);
            oldTripLegs.add(leg1);
        }

        //List trough all stops inputted by user and create a leg corresponding to the start, end, price, numSeats, and trip
            for (String stop: stops){
                if ( counter < sizePriceList){
                int price = prices.get(counter);
                String pointA = stops.get(counter);
                String pointB = stops.get(counter+1);
                Leg leg = new Leg(pointA, pointB, price, 4, trip);
                newTripLegs.add(leg);
                counter++; 
                }
            }
            
            //check if first leg changed and then add
        if(!newTripLegs.get(0).getStart().equals(oldTripLegs.get(0).getStart())){
        session.beginTransaction();
        
        String string ="UPDATE Trips SET Start= :newStart WHERE TripID = :id";
        SQLQuery query1 = session.createSQLQuery(string);
        
        query1.setParameter("newStart", (newTripLegs.get(0).getStart()));
        query1.setParameter("id", tripID);
        query1.executeUpdate();
        session.getTransaction().commit();

        createLeg(newTripLegs.get(0).getStart(), oldTripLegs.get(0).getStart(), prices.get(0), 4, trip);
       
        }

        //check if last leg changed and then add
        if(!newTripLegs.get(newTripLegs.size() - 1).getEnd().equals(oldTripLegs.get(oldTripLegs.size() - 1).getEnd())){
        
        session.beginTransaction();
        String string ="UPDATE Trips SET End= :newEnd WHERE TripID = :id";
        SQLQuery query1 = session.createSQLQuery(string);
        
        query1.setParameter("newEnd", (newTripLegs.get(newTripLegs.size()-1).getEnd()));
        query1.setParameter("id", tripID);
        query1.executeUpdate();
        session.getTransaction().commit();

        createLeg(oldTripLegs.get(oldTripLegs.size() - 1).getEnd(), newTripLegs.get(newTripLegs.size()-1).getEnd(), prices.get(prices.size()-1), 4, trip);

        }
        //route is not the first or last leg 
       
        for(int i = 0; i < newTripLegs.size(); i++){
            if(!oldTripLegs.get(i).getEnd().equals(newTripLegs.get(i).getEnd())){
                session.beginTransaction();
                String string ="UPDATE Legs SET End= :newEnd, Price = :price WHERE FK_TripID = :id AND Start= :newStart";
                SQLQuery query1 = session.createSQLQuery(string);
                
                query1.setParameter("newEnd", newTripLegs.get(i).getEnd());
                query1.setParameter("price", prices.get(i));
                query1.setParameter("newStart", oldTripLegs.get(i).getStart());
                query1.setParameter("id", tripID);
                query1.executeUpdate();
                session.getTransaction().commit();

                createLeg(newTripLegs.get(i).getEnd(), oldTripLegs.get(i).getEnd(),prices.get(i + 1),4,trip);
                break;
            }
            
        }

    return trip.toString();

    }
	
	
    @RequestMapping("/cancelTrip")
    public Boolean cancelTrip(@RequestParam(value="tripID")int tripID){
        
        //Begin Session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        Boolean ret = false;
    
        //Access trip object from db
        Trip trip = getTripByID(tripID);
        
        //create query
        String string ="UPDATE Trips SET Status= :status WHERE TripID = :id";
        SQLQuery query1 = session.createSQLQuery(string);
        
        //Set status of trip to "1" represeting 'Cancelled'
        query1.setParameter("status", (1));
        query1.setParameter("id", tripID);
        query1.executeUpdate();
        
        //check valid tripID 
        if(tripID != 0){
            ret = true;
        }
        else{
            ret = false;
        }
        //close session
        session.getTransaction().commit();
        session.close();

        return ret;
    }
 
  /**
     * Method to complete a Trip 
     * Use the url /Trip/completeTrip
     * @param tripID
     * @return Trip trip
     */
    @RequestMapping("/completeTrip")
    public Boolean completeTrip(@RequestParam(value="tripID")int tripID, @RequestParam(value="username") String username){
    
        //Begin Session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        Boolean ret = false;

         //Access trip and driver object from db
         Trip trip = getTripByID(tripID);
         
         Driver driver = (Driver) session.byNaturalId( User.class ).using( "username", username ).load();
        //create query
        String string ="UPDATE Trips SET Status= :status WHERE TripID = :id";
        SQLQuery query1 = session.createSQLQuery(string);
        //Set status of trip to "2" represeting 'Completed'
        query1.setParameter("status", (2));
        query1.setParameter("id", tripID);
        query1.executeUpdate();
        
         //check valid trip ID
         if(tripID != 0){
            ret = true;
        }
        else{
            ret = false;
        }
        //close session
        session.getTransaction().commit();
        session.close();

        //Update the number of rides for a Driver upon booking the trip 
        session = HibernateUtil.getSession();
        session.beginTransaction();
        String string1 = "UPDATE Users SET numRides= :rides WHERE UserID = :id";
        SQLQuery query2 = session.createSQLQuery(string1);
        query2.setParameter("rides", driver.getNumRides()+1);
        query2.setParameter("id", driver.getUserID());
        query2.executeUpdate();
        session.getTransaction().commit();
        session.close();
	    
        return ret;

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

    public Leg createLeg(String pointA, String pointB, int price, int numSeats, Trip trip){
        
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
    public List<Integer> findTrip(  @RequestParam(value="start") String start, 
                                    @RequestParam(value="end") String end,
                                    @RequestParam(value="price", required = false) Integer price,
                                    @RequestParam(value="make", required = false) String make,
                                    @RequestParam(value="model", required = false) String model,
                                    @RequestParam(value="year", required = false) Integer year,
                                    @RequestParam(value="lowDate", required = false) java.sql.Date lowDate,
                                    @RequestParam(value="highDate", required = false) java.sql.Date highDate)
        {
        // Initialize return list
        List<Integer> tripIDs = new ArrayList<Integer>();

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
                tripIDs.add((Integer)startLeg[5]);
            }
        }

        // Close session
        session.getTransaction().commit();
        session.close();

        List<Integer> foundFailCriteria = new ArrayList<Integer>();

        // filter by price
        if (price != null) {
            for (Integer foundTripID : tripIDs) {
                if (!filterPrice(foundTripID, start, end, price)) {
                    foundFailCriteria.add(foundTripID);
                } 
            }
            tripIDs.removeAll(foundFailCriteria);
        }

        // filter by make
        foundFailCriteria.clear();
        if (make != null) {
            for (Integer foundTripID : tripIDs) {
                if (!filterMake(foundTripID, start, end, make)) {
                    foundFailCriteria.add(foundTripID);
                } 
            }
            tripIDs.removeAll(foundFailCriteria);
        }

        // filter by model
        foundFailCriteria.clear();
        if (model != null) {
            for (Integer foundTripID : tripIDs) {
                if (!filterModel(foundTripID, start, end, model)) {
                    foundFailCriteria.add(foundTripID);
                } 
            }
            tripIDs.removeAll(foundFailCriteria);
        }

        // filter by year
        foundFailCriteria.clear();
        if (model != null) {
            for (Integer foundTripID : tripIDs) {
                if (!filterYear(foundTripID, start, end, year)) {
                    foundFailCriteria.add(foundTripID);
                } 
            }
            tripIDs.removeAll(foundFailCriteria);
        }

        // filter by date
        foundFailCriteria.clear();
        if (lowDate != null && highDate != null) {
            for (Integer foundTripID : tripIDs) {
                if (!filterDate(foundTripID, start, end, lowDate, highDate)) {
                    foundFailCriteria.add(foundTripID);
                } 
            }
            tripIDs.removeAll(foundFailCriteria);
        }

        return tripIDs;
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

        // Close the session
        session.getTransaction().commit();
        session.close();

        // iterate through tripLegs to add prices
        // assumes valid tripId, start, end
        for (Object[] tripLeg : tripLegs) {
            if (((String)tripLeg[1]).equals(start)) {
                effectivePrice += (int)tripLeg[3];
            } else if (effectivePrice != 0) {
                effectivePrice += (int)tripLeg[3];
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

        // Close the session
        session.getTransaction().commit();
        session.close();

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
            session.close();
            return false;
        }
        int carId = cars.get(0); // holds carId of thr given trip

        // Make query on Car, gets Make of Car
        SQLQuery queryGetModel = session.createSQLQuery(queryModel);
        queryGetModel.setParameter("carId", carId);
        List<String> models = queryGetModel.list();
        if (models.size() != 1) {
            System.out.println("Oops! Something went wrong");
            session.close();
            return false;
        }
        String carModel = models.get(0);

        // Close the session
        session.getTransaction().commit();
        session.close();

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
            session.close();
            return false;
        }
        int carId = cars.get(0); // holds carId of thr given trip

        // Make query on Car, gets Make of Car
        SQLQuery queryGetYear = session.createSQLQuery(queryYear);
        queryGetYear.setParameter("carId", carId);
        List<Integer> years = queryGetYear.list();
        if (years.size() != 1) {
            System.out.println("Oops! Something went wrong");
            session.close();
            return false;
        }
        int carYear = years.get(0);

        // Close the session
        session.getTransaction().commit();
        session.close();

        // Check if make of car matches make given by user
        fitsCriteria = (year <= carYear);

        return fitsCriteria;
    }

    @RequestMapping("/filterDate")
    public boolean filterDate(@RequestParam(value="tripId") int tripId, @RequestParam(value="start") String start, @RequestParam(value="end") String end, 
    @RequestParam(value="lowDate") java.sql.Date lowDate, @RequestParam(value="highDate") java.sql.Date highDate) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        boolean fitsCriteria = false;

        // Get start and end Date corresponding to Trip ID
        String queryDate = "SELECT Date FROM Trips WHERE TripID = :tripId";

        // Begin Session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        // Make query on legs, gets CarId
        SQLQuery queryGetDate = session.createSQLQuery(queryDate);
        queryGetDate.setParameter("tripId", tripId);
        List<java.sql.Date> dates = queryGetDate.list();
        if (dates.size() != 1) {
            System.out.println("Oops! Something went wrong");
            session.close();
            return false;
        }
        java.sql.Date tripDate = dates.get(0); // holds date of the given trip

        // Close the session
        session.getTransaction().commit();
        session.close();

        String strTripDate = tripDate.toString();
        String strLowDate = lowDate.toString();

        // Check if tripDate is in between lowDate and HighDate
        fitsCriteria = ((tripDate.compareTo(lowDate) >= 0 && tripDate.compareTo(highDate) <= 0) || strTripDate.substring(0, 10).equals(strLowDate));

        return fitsCriteria;
    }

    @RequestMapping("/showPassengersForTrip")
    public List<Integer> showPassengersForTrip(@RequestParam(value="TripID") int TripID) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        
        String string = "SELECT FK_UserID from PassengerTrips where FK_TripID=:TripID";
        SQLQuery queryFindPassengers = session.createSQLQuery(string);
        queryFindPassengers.setParameter("TripID", TripID);

        List<Integer> passengers = queryFindPassengers.list(); 

       session.getTransaction().commit();
       session.close();
    
       return passengers;
    }

    @RequestMapping("/showDriverForTrip")
    public List<Integer> showDriverForTrip(@RequestParam(value="TripID") int TripID) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

       String string = "SELECT FK_UserID from Trips where TripID=:TripID";
       SQLQuery queryFindDriver = session.createSQLQuery(string);
       queryFindDriver.setParameter("TripID", TripID);

       List<Integer> driver = queryFindDriver.list(); 

       session.getTransaction().commit();
       session.close();

       return driver;
    }
	@RequestMapping("/completedTrips")
    public ArrayList<ArrayList<String>> completedTrips(@RequestParam(value="username") String username){

        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        User driver = (User) session.byNaturalId( User.class ).using( "username", username ).load();
        int id = driver.getUserID();

        session.getTransaction().commit();
        session.close();

        Session session2 = HibernateUtil.getSession();
        session2.beginTransaction();

        String string = "SELECT * from Trips where FK_UserID=:id AND Status=2";
        
        SQLQuery queryFindDriver = session2.createSQLQuery(string);
        queryFindDriver.setParameter("id", id);
        List<Object[]> trips = queryFindDriver.list();

        session2.getTransaction().commit();
        session2.close();

        ArrayList<Integer> tripIDs = new ArrayList<Integer>();

        for(Object[] o : trips){
            String a = o[0].toString();
            Integer i = Integer.parseInt(a);
            tripIDs.add(i);
        }

        ArrayList<String> legStrings = new ArrayList<String>();

        int counter = 0;

        for(Integer i : tripIDs){
            legStrings.add(counter, getLegs(i));
            counter++;
        }

        ArrayList<String> dateStrings = new ArrayList<String>();
        counter = 0;

        for(Object[] o : trips){
            String a = o[1].toString();
            dateStrings.add(counter, a.substring(0, 10));
            counter++;
        }

        ArrayList<String> timeStrings = new ArrayList<String>();
        counter = 0;

        for(Object[] o : trips){
            String a = o[2].toString();
            timeStrings.add(counter, a);
            counter++;
        }

        ArrayList<Integer> numSeats = new ArrayList<Integer>();
        counter = 0;

        for(Integer w : tripIDs){
            int p = getNumSeats(w);
            numSeats.add(counter, p);
            counter++;
        }

        ArrayList<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();

        for(int k = 0; k < tripIDs.size(); k++){
            ArrayList<String> inner = new ArrayList<String>();

            inner.add(0, tripIDs.get(k).toString());
            inner.add(1, legStrings.get(k));
            inner.add(2, dateStrings.get(k));
            inner.add(3, timeStrings.get(k));
            inner.add(4, numSeats.get(k).toString());

            outer.add(k, inner);
        }
        return outer;
    }

    @RequestMapping("/tripsToBeCompleted")
    public ArrayList<ArrayList<String>> tripsCanBeCompleted(@RequestParam(value="username") String username){

        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        User driver = (User) session.byNaturalId( User.class ).using( "username", username ).load();
        int id = driver.getUserID();

        session.getTransaction().commit();
        session.close();

        Session session2 = HibernateUtil.getSession();
        session2.beginTransaction();

        String string = "SELECT * from Trips where FK_UserID=:id AND Status=0";
        
        SQLQuery queryFindDriver = session2.createSQLQuery(string);
        queryFindDriver.setParameter("id", id);
        List<Object[]> trips = queryFindDriver.list();

        session2.getTransaction().commit();
        session2.close();

        ArrayList<Integer> tripIDs = new ArrayList<Integer>();

        for(Object[] o : trips){
            String a = o[0].toString();
            Integer i = Integer.parseInt(a);
            tripIDs.add(i);
        }

        ArrayList<String> legStrings = new ArrayList<String>();

        int counter = 0;

        for(Integer i : tripIDs){
            legStrings.add(counter, getLegs(i));
            counter++;
        }

        ArrayList<String> dateStrings = new ArrayList<String>();
        counter = 0;

        for(Object[] o : trips){
            String a = o[1].toString();
            dateStrings.add(counter, a.substring(0, 10));
            counter++;
        }

        ArrayList<String> timeStrings = new ArrayList<String>();
        counter = 0;

        for(Object[] o : trips){
            String a = o[2].toString();
            timeStrings.add(counter, a);
            counter++;
        }

        ArrayList<Integer> numSeats = new ArrayList<Integer>();
        counter = 0;

        for(Integer w : tripIDs){
            int p = getNumSeats(w);
            numSeats.add(counter, p);
            counter++;
        }

        ArrayList<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();

        for(int k = 0; k < tripIDs.size(); k++){
            ArrayList<String> inner = new ArrayList<String>();

            inner.add(0, tripIDs.get(k).toString());
            inner.add(1, legStrings.get(k));
            inner.add(2, dateStrings.get(k));
            inner.add(3, timeStrings.get(k));
            inner.add(4, numSeats.get(k).toString());

            outer.add(k, inner);
        }


        return outer;
    }

    public int getNumSeats(int i){
        Session session2 = HibernateUtil.getSession();
        session2.beginTransaction();

        String string = "Select NumSeats from Legs where FK_TripID=:id";
        
        SQLQuery queryFindDriver = session2.createSQLQuery(string);
        queryFindDriver.setParameter("id", i);
        List<Integer> legs = queryFindDriver.list();

        session2.getTransaction().commit();
        session2.close();

        int lowest = Integer.MAX_VALUE;

        for(int j = 0; j < legs.size(); j++){
            if(legs.get(j) < lowest){
                lowest = legs.get(j);
            }
        }
        return lowest;
    }

    public String getLegs(int i){
        Session session2 = HibernateUtil.getSession();
        session2.beginTransaction();

        String string = "Select Start, End from Legs where FK_TripID=:id";
        
        SQLQuery queryFindDriver = session2.createSQLQuery(string);
        queryFindDriver.setParameter("id", i);
        List<Object[]> legs = queryFindDriver.list();

        session2.getTransaction().commit();
        session2.close();

        String s = "";

        for(int j = 0; j < legs.size(); j++){
            if(j == legs.size() - 1){
                s += legs.get(j)[1].toString();
            }else if(j == 0){
                s += legs.get(j)[0].toString() + ", " + legs.get(j)[1].toString() + ", ";
            }
            else{
                s += legs.get(j)[1].toString() + ", ";
            }
        }
        return s;
    }

    public Trip getTripByID(int tripID) {
        return (Trip) session.load(Trip.class, tripID);
    }

    public void changeSession(Session change) {
        this.session = change;
    }

    @RequestMapping("/tripInfo")
    public ArrayList<ArrayList<String>> getTripInfo(@RequestParam("tripIds") ArrayList<Integer> tripIds, @RequestParam("start") String start, 
    @RequestParam("end") String end) {
        // date      0
        // price     1
        // numSeats  2
        // status    3
        // stop1     4
        // stop2     5
        // .
        // .
        // .

        ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();

        for (Integer tripId : tripIds) {
            Trip trip = getTripByID(tripId);
            ArrayList<String> ret = new ArrayList<String>();
            String dateStr = trip.getDate().toString();

            // add date to return list
            ret.add(0, dateStr);

            int effectivePrice = 0;
            int effectiveNumSeats = 0;

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

            // Close the session
            session.getTransaction().commit();
            session.close();

            // iterate through tripLegs to add prices
            // assumes valid tripId, start, end
            for (Object[] tripLeg : tripLegs) {
                if (((String)tripLeg[1]).equals(start)) {
                    effectivePrice += (int)tripLeg[3];
                    effectiveNumSeats = (int)tripLeg[4];
                } else if (effectivePrice != 0) {
                    effectivePrice += (int)tripLeg[3];

                    if (effectiveNumSeats > (int)tripLeg[4]) {
                        effectiveNumSeats = (int)tripLeg[4];
                    }
                } 
                if (((String)tripLeg[2]).equals(end)) {
                    break;
                }
            }

            // add price to return list
            String priceStr = String.valueOf(effectivePrice);
            ret.add(1, priceStr);

            // add numSeats to return list
            String numSeatsStr = String.valueOf(effectiveNumSeats);
            ret.add(2, numSeatsStr);

            // add status to reutrn list
            String statusStr = String.valueOf(trip.getTripStatus());
            ret.add(3, statusStr);

            // add stops to end of list
            ret.add((String) tripLegs.get(0)[1]);

            for (Object[] tripLeg : tripLegs) {
                ret.add((String)tripLeg[2]);
            }

            retList.add(ret);
        }
        return retList;
    }
}
