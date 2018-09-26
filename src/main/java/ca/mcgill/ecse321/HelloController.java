package ca.mcgill.ecse321;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.hibernate.Session;

@RestController
public class HelloController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/hello")
    public Hello hello(@RequestParam(value="name", defaultValue="World") String name) {
        Hello retHello = new Hello(counter.incrementAndGet(), String.format(template, name));

        /* Connect to the DB and begin an atomic operation */
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        /* Write the student to the DB by committing the transaction */
        session.saveOrUpdate(retHello);

        session.getTransaction().commit();

        session.close();

        //HibernateUtil.closeSession();

        System.out.println("close");

        return retHello;
    }
}
