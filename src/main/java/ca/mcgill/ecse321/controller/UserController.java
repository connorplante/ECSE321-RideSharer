package ca.mcgill.ecse321.controller;

import ca.mcgill.ecse321.*;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.model.*;

@RestController
@RequestMapping("/User")
public class UserController {

    @RequestMapping("/createPassenger")
    public User createPassenger (@RequestParam(value="username") String username,@RequestParam(value="password") String password,
    @RequestParam(value="firstName") String firstName, @RequestParam(value="lastName") String lastName,@RequestParam(value="email") String email,
    @RequestParam(value="phoneNumber") String phoneNumber) {



        System.out.println("stop 1");
        User passenger =  new User(username, password, firstName, lastName, email, phoneNumber, true, 0, 0);
        System.out.println("stop 2");
        Session session = HibernateUtil.getSession();
        System.out.println("stop 3");
        session.beginTransaction();
        System.out.println("stop 4");
        session.saveOrUpdate(passenger);
        System.out.println("stop 5");
        session.getTransaction().commit();
        System.out.println("stop 6");
        session.close();

        System.out.println("Processed");

        return passenger;


    }

}


