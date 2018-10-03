package ca.mcgill.ecse321.controller;

import ca.mcgill.ecse321.HibernateUtil;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.model.*;

import java.sql.Date;
import java.sql.Time;

import java.util.List;


import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.model.*;
import ca.mcgill.ecse321.model.Trip.Status;

@RestController
@RequestMapping("/Trip")
public class TripController {


    public TripController(){

    }
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

}

