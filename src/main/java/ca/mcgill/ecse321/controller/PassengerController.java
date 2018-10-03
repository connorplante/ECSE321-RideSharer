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
@RequestMapping("/Passenger")
public class PassengerController {

    public PassengerController(){}

    @RequestMapping("/confirmBook")
    public PassengerTrip confirmBook(@RequestParam(value="tripID")int tripID,@RequestParam(value="username")
    String username,@RequestParam(value="pointA")String pointA,@RequestParam(value="pointB")String pointB) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Trip trip = (Trip) session.load(Trip.class, tripID);
        Passenger passenger = (Passenger) session.byNaturalId( User.class ).using( "username", username ).load();
        PassengerTrip passengerTrip = new PassengerTrip(passenger, trip);

        String sql = "SELECT LegID, Start, End, Price, NumSeats, FK_TripID FROM Legs WHERE FK_TripID = :stock";
        SQLQuery query = session.createSQLQuery(sql);

        query.setParameter("stock", tripID);
        List<Object[]> legs = query.list();
        ArrayList<Leg> allLegs = new ArrayList<Leg>();


        for (Object[] leg : legs) {
            Leg leg1 = new Leg((Integer)leg[0],(String)leg[1],(String)leg[2], (Integer)leg[3], (Integer)leg[4], trip);
            allLegs.add(leg1);
        }

        int startId = 0;
        int endId = 0;
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

        for(int i = 0; i < allLegs.size(); i++) {
            if (allLegs.get(i).getLegID() >= startId && allLegs.get(i).getLegID() <= endId) {
                passengerLegs.add(allLegs.get(i));
                totalPricePassenger = totalPricePassenger + allLegs.get(i).getPrice();
            }
        }

        passengerTrip.setPrice(totalPricePassenger);
        session.save(passengerTrip);
        session.getTransaction().commit();
        session.close();

        Session session1 = HibernateUtil.getSession();

        for (Leg pl : passengerLegs) {
            session1.beginTransaction();
            String string ="UPDATE Legs SET NumSeats= :seats WHERE LegID = :id";
            SQLQuery query1 = session1.createSQLQuery(string);
            query1.setParameter("seats", (pl.getNumSeats()-1));
            query1.setParameter("id", pl.getLegID());
            query1.executeUpdate();
            session1.getTransaction().commit();
        }

        session1.close();

        return passengerTrip;
    }
}

