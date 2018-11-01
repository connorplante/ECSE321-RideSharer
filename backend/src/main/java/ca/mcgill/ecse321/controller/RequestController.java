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
import java.util.ArrayList;

import javax.mail.*;
import javax.mail.internet.*;

@RestController
@RequestMapping("/Request")
public class RequestController {
    
    Session session = HibernateUtil.getSession();

    @RequestMapping("/createRequest")
    public Request CreateRequest(@RequestParam(value="passengerName") String passengerName, @RequestParam(value="tripID") int tripID, @RequestParam(value="start") String start,
    @RequestParam(value="end") String end){
        
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Passenger passenger = (Passenger) session.byNaturalId( User.class ).using( "username", passengerName ).load();

        Driver driver = getDriver(tripID);

        String email = driver.getEmail();
        Trip trip = (Trip) session.load(Trip.class, tripID);

        Request request = new Request(passenger, driver, trip, start, end);

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
            message.setSubject("Trip Request!");
            message.setText("Hi! \n\nA passenger has requested to be on your trip!\n\nGo to the the confirm book tab under " + 
            "manage trips to view this request.\n\n" +
            "The t00 Team");
            
            //send the message
            Transport.send(message);
        } catch (MessagingException e) {e.printStackTrace();} 
        
        return request;

    }

    private Driver getDriver(int id){
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        
        String string = "SELECT FK_UserID FROM Trips WHERE TripID= :id";
        SQLQuery queryFindRequests = session.createSQLQuery(string);
        queryFindRequests.setParameter("id", id);
        List<Object[]> requests = queryFindRequests.list();

        session.getTransaction().commit();
        session.close();

        Integer driverID = Integer.parseInt(requests.get(0)[0].toString());

        Session session2 = HibernateUtil.getSession();
        session.beginTransaction();
        Driver driver = (Driver) session.load(User.class, driverID);
        session.getTransaction().commit();
        session.close();

        return driver;
    }

    @RequestMapping("/showRequestsToDriver")
    public ArrayList<ArrayList<String>> showAllRequestsForUsername(@RequestParam(value="username") String username){
        
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

        ArrayList arrayListOuter = new ArrayList<String>();

        for(int i = 0; i < requests.size(); i++){

            ArrayList arrayListInner = new ArrayList<String>();

            arrayListInner.add(0, requests.get(i)[6].toString());

            String s = requests.get(i)[4].toString();
            Integer p = Integer.parseInt(s);
            String passengerUsername = getUsername(p);

            arrayListInner.add(1, passengerUsername);

            String s2 = requests.get(i)[6].toString();
            Integer p2 = Integer.parseInt(s2);
            String date = getDate(p2);

            arrayListInner.add(2, date);
            
            String startEnd = "Start: " + requests.get(i)[2].toString() + "End: " + requests.get(i)[3];

            arrayListInner.add(3, startEnd);

            arrayListInner.add(4, requests.get(i)[0].toString());

            arrayListOuter.add(i, arrayListInner);
        }

        return arrayListOuter;
    }

    private String getUsername(int i){
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        User passenger = (User) session.load(User.class, i);
        String s = passenger.getUsername();

        session.getTransaction().commit();
        session.close();

        return s;
    }

    private String getDate(int i){
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Trip trip = (Trip) session.load(Trip.class, i);
        String s = trip.getDate().toString();

        session.getTransaction().commit();
        session.close();

        return s;
    }

    @RequestMapping("/acceptRequest")
    public ArrayList<String> setStatus(@RequestParam(value="requestID") int requestID){
       
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Request request = (Request) session.load(Request.class, requestID);
        String email = request.getPassenger().getEmail();

        request.setStatus(false);

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
             message.setSubject("Request Accepted!");
             message.setText("Hi! \n\nYour request for a trip has been accepted!\n\nBon voyage!\n\nThe t00 Team");
             
             //send the message
             Transport.send(message);
         } catch (MessagingException e) {e.printStackTrace();} 

         ArrayList<String> returning = new ArrayList<String>();
         returning.add(0, "hi");
         return returning;
    }

    @RequestMapping("/rejectRequest")
    public ArrayList<String> rejectRequest(@RequestParam(value="requestID") int requestID){

        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Request request = (Request) session.load(Request.class, requestID);
        String email  = request.getPassenger().getEmail();

        request.setStatus(false);

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
             message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));message.setSubject("Trip Request Update");
             message.setText("Hi! \n\nYour request for a trip has been denied by the driver.\n\nYou can browse more trips " + 
             "under the book trips tab in the app.\n\n" +
             "The t00 Team");
             //send the message
             Transport.send(message);
         } catch (MessagingException e) {e.printStackTrace();} 

        ArrayList<String> returning = new ArrayList<String>();
        returning.add(0, "hi");
        return returning;

    }

}
