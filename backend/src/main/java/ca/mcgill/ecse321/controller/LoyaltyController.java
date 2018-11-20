package ca.mcgill.ecse321.controller;

import ca.mcgill.ecse321.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import ca.mcgill.ecse321.model.Car;
import ca.mcgill.ecse321.model.Driver;
import ca.mcgill.ecse321.model.User;
import org.hibernate.Hibernate;
import org.hibernate.Transaction;
import org.hibernate.SQLQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/Loyalty")
public class LoyaltyController{

    Session session = HibernateUtil.getSession();
   
    public LoyaltyController(){}

     /**
     * Method to return all passengers, in order of decreasing numRides 
     * "loyal" interpreted as the most completed trips 
     * Use the url /Loyalty/loyalPassengers
     * @return ArrayList<ArrayList<String>> of sorted passengers based on numRides 
     */
    @RequestMapping("/loyalPassengers")
    public ArrayList<ArrayList<String>> mostLoyalPassengers() throws InvalidInputException {
        
        String error = "";
        ArrayList<String> errorStringList = new ArrayList<String>();

        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        String mostLoyalPassengers = "select UserID, Username, numRides from Users where Role = 1 and Status = 1 order by numRides desc";
        SQLQuery query1 = session.createSQLQuery(mostLoyalPassengers);

        List<Object[]> objectLoyalPassengers = query1.list(); //return list of query

        ArrayList<ArrayList<String>> outerLoyalPassengerList = new ArrayList<ArrayList<String>>(); //return list of method 
        
        for (Object[] passenger: objectLoyalPassengers){ //add individual passenger lists to return list with needed information
            ArrayList<String> innerLoyalPassenger = new ArrayList<String>();
            for (int i = 0; i < 3; i++){
                innerLoyalPassenger.add(passenger[i].toString());   
            }
            outerLoyalPassengerList.add(innerLoyalPassenger);
        }
        session.close();

        if(outerLoyalPassengerList.size() == 0){
            error = error + "No passengers have any completed trips in the system";
            errorStringList.add(error);
        }
        //if the error message is not empty, return the error in the outer array list 
        if(error != ""){
            outerLoyalPassengerList.add(errorStringList);
            return outerLoyalPassengerList;
        }
    
        return outerLoyalPassengerList;
    }
    /**
     * Method to return all drivers, in order of decreasing numRides 
     * "loyal" interpreted as the most completed trips 
     * Use the url /Loyalty/loyalDrivers
     * @return ArrayList<ArrayList<String>> of sorted drivers based on numRides 
     */
    @RequestMapping("/loyalDrivers")
    public ArrayList<ArrayList<String>> mostLoyalDrivers() throws InvalidInputException {
       
        //initialize error strings 
        String error = "";
        ArrayList<String> errorStringList = new ArrayList<String>();
        
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        String mostLoyalDrivers = "select UserID, Username, numRides from Users where Role = 2 and Status = 1 order by numRides desc";
        SQLQuery query1 = session.createSQLQuery(mostLoyalDrivers);
        
        List<Object[]> objectLoyalDrivers = query1.list(); //return list of query

        ArrayList<ArrayList<String>> outerLoyalDriversList = new ArrayList<ArrayList<String>>(); //return list of method 

        for (Object[] driver: objectLoyalDrivers){ //add individual driver lists to return list with needed information
            ArrayList<String> innerLoyalDriver = new ArrayList<String>();
            for (int i = 0; i < 3; i++){
                innerLoyalDriver.add(driver[i].toString());   
            }
            outerLoyalDriversList.add(innerLoyalDriver);
        }
        session.close();

        if(outerLoyalDriversList.size() == 0){
            error = error + "No drivers have any completed trips in the system";
            errorStringList.add(error);
        }
        //if the error message is not empty, return the error in the outer array list 
        if(error != ""){
            outerLoyalDriversList.add(errorStringList);
            return outerLoyalDriversList;
        }

        return outerLoyalDriversList;
    }

     /**
     * Method to return most loyal drivers, in order of decreasing numRides, based on a certain time frame 
     * "loyal" interpreted as the most completed trips 
     * Use the url /Loyalty/loyalDriversTimeFrame
     * @return ArrayList<ArrayList<String>> of sorted drivers based on numRides, based on a certain time frame 
     */
    @RequestMapping("/loyalDriversTimeFrame")
    public ArrayList<ArrayList<String>> mostLoyalDriversTimeFrame(@RequestParam(value="startDate") String start, @RequestParam(value="endDate") String end) throws InvalidInputException{
        
        //initialize error strings 
        String error = "";
        ArrayList<String> errorStringList = new ArrayList<String>();

        start = start.replace("-", "");
        end = end.replace("-", "");

        //initialize return list 
        ArrayList<ArrayList<String>> outerLoyalDriversTimeList = new ArrayList<ArrayList<String>>();
        
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        
        if(Integer.parseInt(start) > Integer.parseInt(end)){

            error = error + "The end date must come after the start date. ";
            errorStringList.add(error);
            outerLoyalDriversTimeList.add(errorStringList);

        }
        //if the dates inputted were not valid
        if(error != ""){
            return outerLoyalDriversTimeList;
        }

        String mostLoyalDriversTimeFrame = "SELECT FK_UserID, COUNT(FK_UserID) AS 'totalNumTrips' FROM Trips WHERE Date >= :START AND Date <= :END AND Status = 2 GROUP BY FK_UserID ORDER BY COUNT(FK_UserID) DESC";
        SQLQuery query2 = session.createSQLQuery(mostLoyalDriversTimeFrame);
        query2.setParameter("START", start);
        query2.setParameter("END", end);
        

        List<Object[]> objectLoyalDriversTime = query2.list();

        //if there were not results for the inputted dates return error message 
        if(objectLoyalDriversTime.size() == 0){
            error = error + "There were no drivers with completed trips in this time frame";
            errorStringList.add(error);
            outerLoyalDriversTimeList.add(errorStringList);
            return outerLoyalDriversTimeList;
        }

        ArrayList<String> loyalDriversTimeTotal = new ArrayList<String>();
        ArrayList<String> loyalDriversTimeIDs = new ArrayList<String>();

        //fill in array lists from query 
        for (Object[] driver: objectLoyalDriversTime){
            loyalDriversTimeIDs.add(driver[0].toString());
            loyalDriversTimeTotal.add(driver[1].toString());
        }

        int counter = 0;
        //add to the outer array list an array list of each driver including its numRides for this time frame 
        for (String user_ID : loyalDriversTimeIDs){
            
            ArrayList<String> innerDriverTime = new ArrayList<String>();

            String loyalDriverQuery = "SELECT UserID, Username, FirstName, LastName, Rating FROM Users WHERE UserID =:userID";
            SQLQuery query = session.createSQLQuery(loyalDriverQuery);
            query.setParameter("userID", user_ID);

            List<Object[]> objectLoyalDriver = query.list();

            for(Object[] infoDriver: objectLoyalDriver){
                for(int i = 0; i < 5; i++){
                    innerDriverTime.add(infoDriver[i].toString());
                }
                innerDriverTime.add(loyalDriversTimeTotal.get(counter));
                outerLoyalDriversTimeList.add(innerDriverTime);
                counter++;
            }

        }
        session.close();

        return outerLoyalDriversTimeList;

    }
    /**
     * Method to return most loyal passengers, in order of decreasing numRides, based on a certain time frame 
     * "loyal" interpreted as the most completed trips 
     * Use the url /Loyalty/loyalPassengersTimeFrame
     * @return ArrayList<ArrayList<String>> of sorted passengers based on numRides, based on a certain time frame 
     */
    @RequestMapping("/loyalPassengersTimeFrame")
    public ArrayList<ArrayList<String>> mostLoyalPassengersTimeFrame(@RequestParam(value="startDate") String start, @RequestParam(value="endDate") String end) throws InvalidInputException{

        //initialize error strings 
        String error = "";
        ArrayList<String> errorStringList = new ArrayList<String>();

        start = start.replace("-", "");
        end = end.replace("-", "");
        

         //initialize return list 
        ArrayList<ArrayList<String>> outerLoyalPassengersTimeList = new ArrayList<ArrayList<String>>();
        
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        if(Integer.parseInt(start) > Integer.parseInt(end)){

            error = error + "The end date must come after the start date. ";
            errorStringList.add(error);
            outerLoyalPassengersTimeList.add(errorStringList);

        }
        //if the dates inputted were not valid
        if(error != ""){
            return outerLoyalPassengersTimeList;
        }

        String mostLoyalPassengersTimeFrame = "select FK_UserID, COUNT(FK_UserID) as 'totalNumTrips' from PassengerTrips GROUP BY FK_UserID ORDER BY COUNT(FK_UserID) DESC";
        SQLQuery query2 = session.createSQLQuery(mostLoyalPassengersTimeFrame);
        
        List<Object[]> objectLoyalPassengersTime = query2.list();

        ArrayList<String> loyalPassengersTimeTotal = new ArrayList<String>();
        ArrayList<String> loyalPassengersTimeIDs = new ArrayList<String>();

        //fill in array lists from query 
        for (Object[] passenger: objectLoyalPassengersTime){
            loyalPassengersTimeIDs.add(passenger[0].toString());
            loyalPassengersTimeTotal.add(passenger[1].toString());
        }
        int passengerCounter = 0;
        for (String passengerID: loyalPassengersTimeIDs){
            
            ArrayList<String> passengerTripIDs = new ArrayList<String>();

            String loyalPassengerTripsQuery = "SELECT FK_TripID, Price FROM PassengerTrips WHERE FK_UserID =:userID";
            SQLQuery query3 = session.createSQLQuery(loyalPassengerTripsQuery);
            query3.setParameter("userID", passengerID);

            List<Object[]> objectPassengerTripIDs = query3.list();

            //fill in array with query of trip ids 
            for (Object[] trips: objectPassengerTripIDs){
                passengerTripIDs.add(trips[0].toString());

            }

            for(String tripID: passengerTripIDs){
                boolean isValid = false;

                ArrayList<String> tripDate = new ArrayList<String>();
                //trip needs to be completed 
                String validTripQuery = "SELECT Date, Time from Trips WHERE TripID =:TRIPID AND Status = 2";
                SQLQuery query4 = session.createSQLQuery(validTripQuery);
                query4.setParameter("TRIPID", tripID); 

                List<Object[]> objectDate = query4.list();

                //if the trip was NOT completed then go to next trip and minus one from total trips taken
                if(objectDate.size() == 0){
                    int currentTotal = Integer.parseInt(loyalPassengersTimeTotal.get(passengerCounter));
                    int newTotalInt = currentTotal - 1;
                    String newTotal = Integer.toString(newTotalInt);
                    loyalPassengersTimeTotal.set(passengerCounter, newTotal);
                    continue;
                }
                
                for(Object[] trip: objectDate){
                    tripDate.add(trip[0].toString());
                }

                String date = tripDate.get(0);
                date = date.substring(0, 10);
                date = date.replace("-", "");

                if(Integer.parseInt(date) >= Integer.parseInt(start) && Integer.parseInt(date) <= Integer.parseInt(end)){
                    isValid = true;

                }else{
                    int currentTotal = Integer.parseInt(loyalPassengersTimeTotal.get(passengerCounter));
                    int newTotalInt = currentTotal - 1;
                    String newTotal = Integer.toString(newTotalInt);
                    loyalPassengersTimeTotal.set(passengerCounter, newTotal);
                }
            }

            passengerCounter++;
        }


        int counter = 0;
        for (String user_ID : loyalPassengersTimeIDs){
            
            ArrayList<String> innerPassengerTime = new ArrayList<String>();

            String loyalPassengerQuery = "SELECT UserID, Username, FirstName, LastName, Rating FROM Users WHERE UserID =:userID";
            SQLQuery query = session.createSQLQuery(loyalPassengerQuery);
            query.setParameter("userID", user_ID);

            List<Object[]> objectLoyalPassenger = query.list();

            for(Object[] infoPassenger: objectLoyalPassenger){
                for(int i = 0; i < 5; i++){
                    innerPassengerTime.add(infoPassenger[i].toString());
                }
                if(Integer.parseInt(loyalPassengersTimeTotal.get(counter)) > 0){
                    innerPassengerTime.add(loyalPassengersTimeTotal.get(counter));
                    outerLoyalPassengersTimeList.add(innerPassengerTime);
                    
                }
                counter++;
            }

        }

        //if there were not results for the inputted dates return error message 
        if(outerLoyalPassengersTimeList.size() == 0){
            error = error + "There were no passengers with completed trips in this time frame.";
            errorStringList.add(error);
            outerLoyalPassengersTimeList.add(errorStringList);
            return outerLoyalPassengersTimeList;
        }

        session.close();
        return outerLoyalPassengersTimeList;
    }

}
