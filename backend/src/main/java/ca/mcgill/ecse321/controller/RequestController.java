package ca.mcgill.ecse321.controller;

import ca.mcgill.ecse321.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.omg.CORBA.UserException;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.model.Car;
import ca.mcgill.ecse321.model.Driver;
import ca.mcgill.ecse321.model.User;
import ca.mcgill.ecse321.model.Passenger;
import org.hibernate.Hibernate;
import org.hibernate.Transaction;
import org.hibernate.SQLQuery;
import ca.mcgill.ecse321.model.Request;
import ca.mcgill.ecse321.model.Trip;

import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

@RestController
@RequestMapping("/Request")
public class RequestController {
    
    Session session = HibernateUtil.getSession();

    @RequestMapping("/createRequest")
    public Request CreateRequest(@RequestParam(value="passengerName") String passengerName, @RequestParam(value="driverName")
    String driverName, @RequestParam(value="tripID") int tripID){
        
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Passenger passenger = (Passenger) session.byNaturalId( User.class ).using( "username", passengerName ).load();
        Driver driver = (Driver) session.byNaturalId( User.class ).using( "username", driverName ).load();
        String email = driver.getEmail();
        Trip trip = (Trip) session.load(Trip.class, tripID);

        Request request = new Request(passenger, driver, trip);

        session.saveOrUpdate(request);
        session.getTransaction().commit();
        session.close();

        String host = "smtp.gmail.com";  
       String wmail = "t00.ridesharer@gmail.com";//change accordingly  
       String pw = "qydaqzkmmqnxgqjh";//change accordingly
       String to = email;//change accordingly 
       Properties props = new Properties();
       props.setProperty("mail.transport.protocol", "smtp");
       props.setProperty("mail.host", "smtp.gmail.com");
       props.put("mail.smtp.auth", "true");
       props.put("mail.smtp.port", "465");
       props.put("mail.debug", "true");
       props.put("mail.smtp.socketFactory.port", "465");
       props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
       props.put("mail.smtp.socketFactory.fallback", "false"); 
       
       javax.mail.Session session2 = javax.mail.Session.getDefaultInstance(props, new javax.mail.Authenticator() {  
      
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(wmail,pw);
             }
        });
        
        //Compose the message
        try {
            MimeMessage message = new MimeMessage(session2);
            message.setFrom(new InternetAddress("RideSharer t00 <t00.ridesharer@gmail.com>"));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("Trip Requested!");
            message.setText("Hi! \n\nA passenger has requested to be on your trip! \n\nGo to the confirm book tab " + 
            "under manage trips to check the request!\n\n" +
            "The t00 Team");
            
            //send the message
            Transport.send(message);
        } catch (MessagingException e) {e.printStackTrace();} 
        
        return request;

    }

    @RequestMapping("/showRequestsToDriver")
    public List<Object[]> showAllCarsForUsername(@RequestParam(value="username") String username){
        
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        
        Driver driver = (Driver) session.byNaturalId( Driver.class ).using( "username", username ).load();

        int id = driver.getUserID();

        String string ="SELECT * FROM Requests WHERE FK_DriverID= :id AND status=1";

        SQLQuery queryFindRequests = session.createSQLQuery(string);
        queryFindRequests.setParameter("id", id);
        List<Object[]> requests = queryFindRequests.list();

        session.getTransaction().commit();
        session.close();

        return requests;
    }

    @RequestMapping("/setStatus")
    public Request setStatus(@RequestParam(value="requestID") int requestID){
       
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Request request = (Request) session.load(Request.class, requestID);

        request.setStatus(false);

        session.saveOrUpdate(request);
        session.getTransaction().commit();
        session.close();

        return request;
    }

}