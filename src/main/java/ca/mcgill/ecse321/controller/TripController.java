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

@RestController
@RequestMapping("/Trip")
public class TripController {


    public TripController(){}

    @RequestMapping("/createTrip")
    public Trip createTrip(@RequestParam(value="start")String start, @RequestParam(value="end")String end, 
    @RequestParam(value="date")Date date, @RequestParam(value="time")int time,
    @RequestParam(value="username") String username, @RequestParam(value="carID")int carID, int numSeats, @RequestParam List<String> stops, @RequestParam List<Double> prices) {
       
        Time time1 = null;
        
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
       
      
        Driver driver = (Driver) session.byNaturalId( User.class ).using( "username", username ).load();
        
        
        //access Car object 
        Car car = (Car) session.load(Car.class, carID);
       

        
           
            Trip trip = new Trip(start, end, date, time1, Status.Scheduled, driver, car);
            
            session.save(trip);
            session.getTransaction().commit();
            session.close();
            
           
            int counter = 0;
            int sizePriceList = prices.size();

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
           
        

        return trip;


    }
    



    @RequestMapping("/cancelTrip")
    public Trip cancelTrip(@RequestParam(value="tripID")int tripID){
        Session session = HibernateUtil.getSession();
            session.beginTransaction();

        Trip trip = (Trip) session.load(Trip.class, tripID);
        
                trip.setTripStatus(Status.Cancelled);
            
            session.saveOrUpdate(trip);
            session.getTransaction().commit();
            session.close();
        return trip;
    }




    public Leg createLeg(String pointA, String pointB, Double price, int numSeats, Trip trip){

        
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Leg leg = new Leg(pointA, pointB, price, numSeats, trip);
        
            session.save(leg);
            session.getTransaction().commit();
            session.close();
        
        
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
}