package ca.mcgill.ecse321.controller;

import org.hibernate.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.HibernateUtil;
import ca.mcgill.ecse321.model.Admin;
import ca.mcgill.ecse321.model.Driver;
import ca.mcgill.ecse321.model.Passenger;
import ca.mcgill.ecse321.model.User;

@RestController
@RequestMapping("/User")
public class UserController {

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
    @RequestParam(value="phoneNumber") String phoneNumber) {

        Passenger passenger =  new Passenger(username, password, firstName, lastName, email, phoneNumber, true, 0, 0);
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.saveOrUpdate(passenger);
        session.getTransaction().commit();
        session.close();

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

        User user = (User) session.byNaturalId( User.class ).using( "username", username ).load();
        if (user.getPassword().equals(currentPassword)) {
            user.setPassword(newPassword);
            session.saveOrUpdate(user);
            ret = true;
        } else {
            ret = false;
        }

        session.getTransaction().commit();
        session.close();

        return ret;
    }
}