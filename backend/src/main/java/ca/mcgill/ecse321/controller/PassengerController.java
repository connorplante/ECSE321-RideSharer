package ca.mcgill.ecse321.controller;

import ca.mcgill.ecse321.HibernateUtil;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.model.*;

import java.util.ArrayList;
import java.util.List;


import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/Passenger")
@SuppressWarnings( {"deprecation", "rawtypes", "unchecked"} )
public class PassengerController {

    Session session = HibernateUtil.getSession();

    public PassengerController(){}


    /**
     * Method to create a PassengerTrip, for the user this implies booking a trip
     * Use the url /Passenger/confirmBook
     * @param tripID
     * @param username
     * @param pointA
     * @param pointB
     * @return PassengerTrip passengerTrip
     */

    @RequestMapping("/confirmBook")
    public String confirmBook(@RequestParam(value="tripID")int tripID,@RequestParam(value="username")
    String username,@RequestParam(value="pointA")String pointA,@RequestParam(value="pointB")String pointB) throws InvalidInputException{
        
        String error = "";

        //Begin session
        Session session = this.session;
        session.beginTransaction();

       //Load specified trip and passenger in SQL tables into new objects using tripID and username
        Trip trip = (Trip) session.load(Trip.class, tripID);
        Passenger passenger = (Passenger) session.byNaturalId( User.class ).using( "username", username ).load();
        
        //Create the new PassengerTrip
        PassengerTrip passengerTrip = new PassengerTrip(passenger, trip);

        //Initialize SQL query to obtain Legs associated with specified trip
        String sql = "SELECT LegID, Start, End, Price, NumSeats, FK_TripID FROM Legs WHERE FK_TripID = :stock";
        SQLQuery query = session.createSQLQuery(sql);
        query.setParameter("stock", tripID);
        
        //Populate a list with results from the query
        List<Object[]> legs = query.list();
        
        //Initialize a Leg list 
        ArrayList<Leg> allLegs = new ArrayList<Leg>();

        //Populate an array allLegs with Leg objects from query
        for (Object[] leg : legs) {
            Leg leg1 = new Leg((Integer)leg[0],(String)leg[1],(String)leg[2], (Integer)leg[3], (Integer)leg[4], trip);
            allLegs.add(leg1);
        }

        int startId = 0;
        int endId = 0;

        //Filter allLegs to only the required legs based on the pointA and pointB requested by user
        for(int i =0 ; i< allLegs.size(); i++) {
            if(allLegs.get(i).getStart().contentEquals(pointA)) {
                startId = allLegs.get(i).getLegID();
            }
            if(allLegs.get(i).getEnd().contentEquals(pointB)) {
                endId = allLegs.get(i).getLegID();
            }
        }

        double totalPricePassenger = 0;
        ArrayList<Leg> passengerLegs = new ArrayList<Leg>();

        //Add the required legs to an array passengerLegs, total the price
        for(int i = 0; i < allLegs.size(); i++) {
            if (allLegs.get(i).getLegID() >= startId && allLegs.get(i).getLegID() <= endId) {
                if(allLegs.get(i).getNumSeats() == 0) {
                    error = "There are no available seats for this trip";
                }
                passengerLegs.add(allLegs.get(i));
                totalPricePassenger = totalPricePassenger + allLegs.get(i).getPrice();
            }
        }
        if (error.length() > 0) {
            throw new InvalidInputException(error.trim());
        }
        //Set the price of the of the PassengerTrip to calculated total
        passengerTrip.setPrice(totalPricePassenger);
        
        //Save and close session
        session.save(passengerTrip);
        session.getTransaction().commit();

        //Open new session
        session = this.session;

        //Generate query to update number of seats available on each leg of trip
        for (Leg pl : passengerLegs) {
            session.beginTransaction();
            String string ="UPDATE Legs SET NumSeats= :seats WHERE LegID = :id";
            SQLQuery query1 = session.createSQLQuery(string);
            query1.setParameter("seats", (pl.getNumSeats()-1));
            query1.setParameter("id", pl.getLegID());
            query1.executeUpdate();
            session.getTransaction().commit();
        }

        //Open new session, update the number of rides for a Passenger upon booking the trip
        session = this.session;
        session.beginTransaction();
        String string1 = "UPDATE Users SET numRides= :rides WHERE UserID = :id";
        SQLQuery query2 = session.createSQLQuery(string1);
        query2.setParameter("rides", passenger.getNumRides()+1);
        query2.setParameter("id", passenger.getUserID());
        query2.executeUpdate();
        session.getTransaction().commit();

        return passengerTrip.toString();
    }
}
