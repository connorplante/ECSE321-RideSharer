package ca.mcgill.ecse321.controller;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SQLQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.HibernateUtil;
import ca.mcgill.ecse321.model.Admin;
import ca.mcgill.ecse321.model.Driver;
import ca.mcgill.ecse321.model.Passenger;
import ca.mcgill.ecse321.model.User;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/User")
public class UserController {

    Session session = HibernateUtil.getSession();

    /**
     * Method to create a user of type Passenger
     * Use the url /User/createPassenger
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param email
     * @param phoneNumber
     * @return Passenger passenger
     */
    @RequestMapping("/createPassenger")
    public Passenger createPassenger (@RequestParam(value="username") String username,@RequestParam(value="password") String password,
    @RequestParam(value="firstName") String firstName, @RequestParam(value="lastName") String lastName,@RequestParam(value="email") String email,
    @RequestParam(value="phoneNumber") String phoneNumber) throws Exception {

        Passenger passenger =  new Passenger(username, password, firstName, lastName, email, phoneNumber, true, 0, 0);
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(passenger);
            tx.commit();
        } catch (javax.persistence.PersistenceException e) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return null;
        }

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
            message.setSubject("Welcome!");
            message.setText("Hi! \n\nThank you for creating a Passenger profile! \n\nYour account has been successfully activated!\n\n" +
            "The t00 Team");
            
            //send the message
            Transport.send(message);
        } catch (MessagingException e) {e.printStackTrace();}  

        return passenger;
    }

    /** 
     * Method to create a user of type Driver
     * Use the url /User/createDriver
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param email
     * @param phoneNumber
     * @return Driver driver
     */
    @RequestMapping("/createDriver")
    public Driver createDriver (@RequestParam(value="username") String username,@RequestParam(value="password") String password,
    @RequestParam(value="firstName") String firstName, @RequestParam(value="lastName") String lastName,@RequestParam(value="email") String email,
    @RequestParam(value="phoneNumber") String phoneNumber) {

       Driver driver =  new Driver(username, password, firstName, lastName, email, phoneNumber, true, 0, 0);
       Session session = HibernateUtil.getSession();
       session.beginTransaction();
       session.saveOrUpdate(driver);
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
            message.setSubject("Welcome!");
            message.setText("Hi! \n\nThank you for creating a Driver profile! \n\nYour account has been successfully activated!\n\n" +
            "The t00 Team");
            
            //send the message
            Transport.send(message);
        } catch (MessagingException e) {e.printStackTrace();}  

       return driver;
    }

    /** 
     * Method to create a user of type Driver
     * Use the url /User/createDriver
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param email
     * @param phoneNumber
     * @return Admin admin
     */
    @RequestMapping("/createAdmin")
    public Admin createAdmin (@RequestParam(value="username") String username,@RequestParam(value="password") String password,
    @RequestParam(value="firstName") String firstName, @RequestParam(value="lastName") String lastName,@RequestParam(value="email") String email,
    @RequestParam(value="phoneNumber") String phoneNumber) {

        Admin admin =  new Admin(username, password, firstName, lastName, email, phoneNumber, true, 0, 0);
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.saveOrUpdate(admin);
        session.getTransaction().commit();
        session.close();

        return admin;
    }

    /**
     * Resets a users password
     * returns true if reset was successful
     * returns false if reset failed
     * Works for User, Driver, Passenger, Admin
     * @param username
     * @param currentPassword
     * @param newPassword
     * @return Boolean 
     */
    @RequestMapping("/resetPassword")
    public Boolean resetPassword (@RequestParam(value="username") String username, @RequestParam(value="currentPassword") String currentPassword,
    @RequestParam(value="newPassword") String newPassword) {

        Session session = HibernateUtil.getSession();
        Boolean ret;
        session.beginTransaction();

        User user; 

        //Find user by username in database
        try{
            user = getUserByUsername(username);
        }catch(Exception e){
            session.close();
            return false;
        }

        //Update password to the new one if entered current password is correct
        if (user.getPassword().equals(currentPassword)) {
            user.setPassword(newPassword);
            session.saveOrUpdate(user);
            ret = true;
        } else {
            ret = false;
        }

        //Save and close session
        session.getTransaction().commit();
        session.close();

        return ret;
    }

    /**
     * Resets a users password
     * returns true if reset was successful
     * returns false if reset failed
     * Works for User, Driver, Passenger, Admin
     * @param username
     * @param currentPassword
     * @param newPassword
     * @return Boolean 
     */
    @RequestMapping("/logIn")
    public Boolean logIn(@RequestParam(value="username") String username, @RequestParam(value="password") String password) {

        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        User user; 

        //Find user by username in database
        try{
            user = getUserByUsername(username);
        }catch(Exception e){
            session.close();
            return new Boolean(false);
        }

        //Update password to the new one if entered current password is correct
        if (user.getPassword().equals(password)) {
            session.getTransaction().commit();
            session.close();
            return new Boolean(true);
        } else {
            session.getTransaction().commit();
            session.close();
            return new Boolean(false);
        }
    }

    /**
     * Updates a user's information (not including password or username)
     * Returns the user's String representation with their new information
     * 
     * @param username
     * @param firstName
     * @param lastName
     * @param email
     * @param phoneNumber
     * @return String
     */
    @RequestMapping("/updateUserInfo")
    public ArrayList<String> updateUserInfo (@RequestParam(value="username") String username, @RequestParam(value="firstName") String firstName, 
    @RequestParam(value="lastName") String lastName, @RequestParam(value="email") String email, 
    @RequestParam(value="phoneNumber") String phoneNumber){
        
        Session session = HibernateUtil.getSession();

        //Begin transaction
        session.beginTransaction();

        User user = null;

        //find user by username in database
        try{
            user = getUserByUsername(username);
            //update fields of the user information
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPhone(phoneNumber);
        }catch(Exception e){
            session.getTransaction().rollback();
            session.close();
        }

        session.saveOrUpdate(user);
        session.getTransaction().commit();
        session.close();

        ArrayList<String> returning = new ArrayList<String>();
        returning.add(0, username);

        return returning;
    } 

    /**
     * Removes a user by changing their status
     * Returns the user's String representation with their new status
     * 
     * @param username
     * @return String
     */
    @RequestMapping("/removeUser")
    public String removeUser (@RequestParam(value="username") String username) {
        
        //Begin session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        User user;

        //Find user by username in database
        try{
            user = getUserByUsername(username);
        }catch(Exception e){
            session.getTransaction().rollback();
            session.close();
            return "User does not exist!";
        }
        //Change user status to remove their profile
        user.setStatus(false);
        session.saveOrUpdate(user);
        session.getTransaction().commit();
        session.close();
        return user.toString();
    } 

    /**
     * Updates user rating
     * Returns true if update was successful
     * Returns false it failed
     * 
     * @param username
     * @param rating
     * @return Boolean
     */
    @RequestMapping("/updateRating")
    public Boolean updateRating (@RequestParam(value="username") String username, @RequestParam(value="rating") int rating) { //username or passenger object

        Session session = HibernateUtil.getSession();
        Boolean ret;
        session.beginTransaction();
        User user;
        
        //Find user by username in database
        try {
            user = getUserByUsername(username);
        } catch(Exception e) {
            session.getTransaction().rollback();
            session.close();
            return false;
        }

        //only drivers and passengers have ratings, so if user is admin return false
        if(user.getRole() == 3){
            return false;
        }

        if (rating <= 5 && rating >= 0) {

            double pastAvgRating = user.getRating();
            double newAvgRating;
            int numRides = user.getNumRides();

            //if it is the user's first ride, set this rating to global rating
            if(pastAvgRating == 0 || numRides == 0) {
                user.setRating(rating);
                user.setNumRides(++numRides);
                newAvgRating = rating;
                ret = true;
            } else { //update the average rating considering past ratings and number of rides
                newAvgRating = (pastAvgRating*numRides + rating)/(++numRides);
                user.setRating(newAvgRating);
                user.setNumRides(numRides);
                try { 
                    session.saveOrUpdate(user);
                } catch(Exception e) {
                    session.getTransaction().rollback();
                    session.close();
                    return false;
                }
                ret = true;
                System.out.println(newAvgRating);
            }   
        } else {
            ret = false;
        }

        //save and close the session
        try {
            session.getTransaction().commit();
        } catch(Exception e) {
            session.getTransaction().rollback();
            session.close();
            return false;
        }
        session.close();
        return ret;
    }

    @RequestMapping("/showPassengersForTrip")
    public String[] showPassengersForTrip(@RequestParam(value="TripID") int TripID) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        
        String string = "SELECT FK_UserID from PassengerTrips where FK_TripID=:TripID";
        SQLQuery queryFindPassengers = session.createSQLQuery(string);
        queryFindPassengers.setParameter("TripID", TripID);

        List<Integer> passengers = queryFindPassengers.list(); 

       session.getTransaction().commit();
       session.close();

       List<String> stringPassengers = new ArrayList<String>(passengers.size());
       for(Integer UserID : passengers) {
            String username = getUsernameByID(UserID);
            stringPassengers.add(username);
       }
       String[] returnPassengers = stringPassengers.toArray(new String[0]);
    
       return returnPassengers;
    }

    @RequestMapping("/showDriverForTrip")
    public String[] showDriverForTrip(@RequestParam(value="TripID") int TripID) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

       String string = "SELECT FK_UserID from Trips where TripID=:TripID";
       SQLQuery queryFindDriver = session.createSQLQuery(string);
       queryFindDriver.setParameter("TripID", TripID);

       List<Integer> driver = queryFindDriver.list(); 
       
       session.getTransaction().commit();
       session.close();

       List<String> stringDriver = new ArrayList<String>(driver.size());
       for(Integer UserID : driver) {
            String username = getUsernameByID(UserID);
            stringDriver.add(username);
       }
       String[] returnDriver = stringDriver.toArray(new String[0]);
       
       return returnDriver;
    }

    public String getUsernameByID(@RequestParam(value="UserID") int UserID) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        String string = "SELECT Username FROM Users WHERE UserID=:UserID";
        SQLQuery queryFindUsername = session.createSQLQuery(string);
        queryFindUsername.setParameter("UserID", UserID);

        List<String> username = queryFindUsername.list();

        session.getTransaction().commit();
        session.close();

        String returnUser = username.toString();
        return returnUser;
    }
    
    public User getUserByUsername(String username) {
        return (User) session.byNaturalId( User.class ).using( "username", username ).load();
    }

    public void changeSession(Session change) {
        this.session = change;
    }

    @RequestMapping("/displayProfileInfo")
    public ArrayList<String> displayProfileInfo(@RequestParam(value="username") String username){

        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        User user = (User) session.byNaturalId( User.class ).using( "username", username ).load();

        String a = user.getFirstName();
        String b = user.getLastName();
        String c = user.getEmail();
        String d = user.getPhone();

        session.getTransaction().commit();
        session.close();

        ArrayList<String> returning = new ArrayList<String>();
        returning.add(0, a);
        returning.add(1, b);
        returning.add(2, c);
        returning.add(3, d);

        return returning;

    }
}
