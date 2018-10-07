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

        sf.commit();

        //  Act
        User returnedUser = tUserController.getUserByBusername(username);

        //  Assert
        assertNotNull(returnedUser);
        assertEquals(1, returnedUser.getUserID());
        assertEquals(username, returnedUser.getUsername());
        assertEquals(email, returnedUser.getEmail());
        assertEquals(firstName, returnedUser.getFirstName());
        assertEquals(lastName, returnedUser.getLastName());
    }

    @Test
    public void createDriverTest() {
        //  Arrange 
        String username = "tUsername";
        String email = "test@test.com";
        String firstName = "tFirstName";
        String lastName = "tLastName";
        String password = "p";
        String phone = "1";


        //  Act
        tUserController.createDriver(username, password, firstName, lastName, email, phone);
        User reUser = tUserController.getUserByBusername(username);
        
        //  Assert
        assertNotNull(reUser);
        assertEquals(1, reUser.getUserID());
        assertEquals(username, reUser.getUsername());
        assertEquals(email, reUser.getEmail());
        assertEquals(firstName, reUser.getFirstName());
        assertEquals(lastName, reUser.getLastName());
        assertEquals(2, reUser.getRole());
    }

    @Test
    public void createPassengerTest() throws Exception {
        //  Arrange 
        String username = "tUsername";
        String email = "test@test.com";
        String firstName = "tFirstName";
        String lastName = "tLastName";
        String password = "p";
        String phone = "1";


        //  Act
        tUserController.createPassenger(username, password, firstName, lastName, email, phone);
        User reUser = tUserController.getUserByBusername(username);
        
        //  Assert
        assertNotNull(reUser);
        assertEquals(1, reUser.getUserID());
        assertEquals(username, reUser.getUsername());
        assertEquals(email, reUser.getEmail());
        assertEquals(firstName, reUser.getFirstName());
        assertEquals(lastName, reUser.getLastName());
        assertEquals(1, reUser.getRole());
    }

    @Test
    public void createAdminTest() {
        //  Arrange 
        String username = "tUsername";
        String email = "test@test.com";
        String firstName = "tFirstName";
        String lastName = "tLastName";
        String password = "p";
        String phone = "1";


        //  Act
        tUserController.createAdmin(username, password, firstName, lastName, email, phone);
        User reUser = tUserController.getUserByBusername(username);
        
        //  Assert
        assertNotNull(reUser);
        assertEquals(1, reUser.getUserID());
        assertEquals(username, reUser.getUsername());
        assertEquals(email, reUser.getEmail());
        assertEquals(firstName, reUser.getFirstName());
        assertEquals(lastName, reUser.getLastName());
        assertEquals(3, reUser.getRole());
    }

}