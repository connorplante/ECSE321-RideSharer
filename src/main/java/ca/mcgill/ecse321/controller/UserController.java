package ca.mcgill.ecse321.controller;

import ca.mcgill.ecse321.HibernateUtil;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.model.Passenger;

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
     * @return an object of type Passenger
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
}


