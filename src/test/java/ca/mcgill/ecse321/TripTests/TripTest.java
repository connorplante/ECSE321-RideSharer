package ca.mcgill.ecse321.TripTests;
import org.junit.Test;

import ca.mcgill.ecse321.SessionFactoryRule;
import ca.mcgill.ecse321.controller.*;
import ca.mcgill.ecse321.model.*;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Rule;

public class TripTest {

    @Rule 
    public final SessionFactoryRule sf = new SessionFactoryRule();

    public UserController tUserController;
    public TripController tTripController;

    @Before
    public void before() {
        tTripController = new TripController();
        tTripController.changeSession(sf.getSession());
    }

    @Test
    public void createTripTest() throws InvalidInputException {
        //  Arrange 
        Session session = sf.getSession();
        sf.beginTransaction();

        String username = "tDriver";

        Driver tDriver = new Driver(username, "tPassword", "tFirstName", "tLastName", "tEmail", "tPhone", true, 0, 0);
    
        session.save(tDriver);

        Car tCar = new Car("tMake", "tModel", 2000, 6, "tPlate", tDriver);

        session.save(tCar);

        sf.commit();

        String start = "tStart";
        String end = "tEnd";
        Date date = new Date(0);
        int time = 1;
        int carID = 1;
        int numSeats = 4;
        List<String> stops = new ArrayList<String>();
        stops.add(start);
        stops.add("tStop");
        stops.add(end);
        List<Double> prices = new ArrayList<Double>();
        prices.add(14.0);
        prices.add(19.0);

        //  Act
        String result = tTripController.createTrip(start, end, date, time, username, carID, numSeats, stops, prices);
        Trip tTrip = tTripController.getTripByID(1);

        //  Assert
        assertNotNull(tTrip);
        assertEquals(result, tTrip.toString());
    }
}
