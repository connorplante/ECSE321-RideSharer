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
        //map from ID numbers to objects 
        Time time1 = null;
        
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
       
        //access Driver object 
        Driver driver = (Driver) session.byNaturalId( User.class ).using( "username", username ).load();
        System.out.println("username is" + driver.getUsername());
        
        //access Car object 
        Car car = (Car) session.load(Car.class, carID);
        System.out.println("username is" + car.getMake());

        System.out.println("Stop4");
           
            Trip trip = new Trip(start, end, date, time1, Status.Scheduled, driver, car);
            System.out.println("Stop4.5");
            //System.out.println(trip.getTripID());
            session.save(trip);
            session.getTransaction().commit();
            session.close();
            
            System.out.println("Stop5");
            int counter = 0;
            int sizePriceList = prices.size();

            for (String stop: stops){
                if ( counter < sizePriceList){
                double price = prices.get(counter);
                String pointA = stops.get(counter);
                String pointB = stops.get(counter+1);
                Leg leg = createLeg(pointA, pointB, price, numSeats, trip);
                counter++; 
                }
            }
            System.out.println("Stop8");
           
            
           
            System.out.println("Stop9");
        return trip;


    }
    //DONT NEED THIS METHOD ANYMORE 
    // @RequestMapping("/updateTrip")
    // public Trip updateTrip(@RequestParam(value="tripID")int tripID, @RequestParam(value="start")String start, @RequestParam(value="end")String end, 
    // @RequestParam(value="date")Date date, @RequestParam(value="time")Time time, @RequestParam(value="carID")int carID){

    //     //map trip ID to an object 
    //     if (start.length() != 0){
    //         trip.setStart(start);  
    //     }
    //     if (end.length() != 0){
    //         trip.setEnd(start);  
    //     }
    //     if (date != null){
    //         trip.setDate(date);
    //     }
    //     if (time != null){
    //         trip.setTime(time);
    //     }
    //     if (car != null){
    //         trip.setCar(car);
    //     }
    //         Session session = HibernateUtil.getSession();
    //         session.beginTransaction();
    //         session.saveOrUpdate(trip);
    //         session.getTransaction().commit();
    //         session.close();
    //     return trip;
       
    // }



    @RequestMapping("/cancelTrip")
    public Trip cancelTrip(@RequestParam(value="tripID")int tripID){
        Session session = HibernateUtil.getSession();
            session.beginTransaction();

        Trip trip = (Trip) session.load(Trip.class, tripID);
        //map tripID to an object 
                trip.setTripStatus(Status.Cancelled);
            
            session.saveOrUpdate(trip);
            session.getTransaction().commit();
            session.close();
        return trip;
    }




    public Leg createLeg(String pointA, String pointB, Double price, int numSeats, Trip trip){

        //find trip from trip ID
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Leg leg = new Leg(pointA, pointB, price, numSeats, trip);
        System.out.println("Making a new leg!!");
            session.save(leg);
            session.getTransaction().commit();
            session.close();
        
        
        return leg;
    

    }

}

