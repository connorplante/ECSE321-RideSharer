package ca.mcgill.ecse321;
import org.junit.Test;

import ca.mcgill.ecse321.controller.UserController;
import ca.mcgill.ecse321.model.*;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Rule;

public class UserTest {

    @Rule 
    public final SessionFactoryRule sf = new SessionFactoryRule();

    public UserController tUserController;

    @Before
    public void before() {
        tUserController = new UserController();
        tUserController.changeSession(sf.getSession());
    }

    @Test
    public void returnsUser() {

        //  Arrange
        Session session = sf.getSession();

        sf.beginTransaction();

        String username = "tUsername";
        String email = "test@test.com";
        String firstName = "tFirstName";
        String lastName = "tLastName";

        User tUser = new User();
        tUser.setUsername(username);
        tUser.setEmail(email);
        tUser.setFirstName(firstName);
        tUser.setLastName(lastName);

        session.save(tUser);

        User tUser2 = new User();
        tUser2.setUsername("tUsername2");

        session.save(tUser2);

        sf.commit();

        //  Act
        User returnedUser = (User) session.byNaturalId(User.class).using("username", username).load();
        User returnedUser2 = (User) session.byNaturalId(User.class).using("username", "tUsername2").load();
        //  Assert
        assertNotNull(returnedUser);
        assertEquals(returnedUser.getUserID(), 1);
        assertEquals(returnedUser2.getUserID(), 2);
        assertEquals(returnedUser.getUsername(), username);
        assertEquals(returnedUser.getEmail(), email);
        assertEquals(returnedUser.getFirstName(), firstName);
        assertEquals(returnedUser.getLastName(), lastName);
    }

    @Test
    public void createDriverTest() {
        //  Arrange 
        Session session = sf.getSession();

        String username = "tUsername";
        String email = "test@test.com";
        String firstName = "tFirstName";
        String lastName = "tLastName";
        String password = "p";
        String phone = "1";

        Driver driver = tUserController.createDriver(username, password, firstName, lastName, email, phone);
        User reUser = (User) session.byNaturalId(User.class).using("username", username).load();;

        assertNotNull(reUser);

    }

}