import org.junit.Test;

import ca.mcgill.ecse321.HibernateUtil;
import ca.mcgill.ecse321.controller.UserController;
import ca.mcgill.ecse321.model.*;

import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;

import org.springframework.test.context.jdbc.Sql;

public class UserTest {

    private UserController tUserController;

    @BeforeClass
    public static void init() {
        Session session = HibernateUtil.getSession();

        String username = "f1";
        String email = "test@test.com";
        String firstName = "tFirstName";
        String lastName = "tLastName";
        String password = "pw";

        User tUser = new User();
        tUser.setUsername(username);
        tUser.setEmail(email);
        tUser.setFirstName(firstName);
        tUser.setLastName(lastName);
        tUser.setPassword(password);

        session.beginTransaction();
        session.save(tUser);
        session.getTransaction().commit();

        User tUser2 = new User();
        tUser2.setUsername("remove");
        tUser2.setEmail(email);
        tUser2.setFirstName(firstName);
        tUser2.setLastName(lastName);
        tUser2.setPassword(password);

        session.beginTransaction();
        session.save(tUser2);
        session.getTransaction().commit();

        session.close();
    }

    @Before
    public void setUp() {
        tUserController = new UserController();
    }

    @Test
    public void createDriverTest() {
        //  Arrange 
        String username = "d1";
        String email = "test@test.com";
        String firstName = "tFirstName";
        String lastName = "tLastName";
        String password = "p";
        String phone = "1";

        //  Act
        tUserController.createDriver(username, password, firstName, lastName, email, phone);
        User reUser = tUserController.getUserByUsername(username);
        
        //  Assert
        assertNotNull(reUser);
        assertEquals(3, reUser.getUserID());
        assertEquals(username, reUser.getUsername());
        assertEquals(email, reUser.getEmail());
        assertEquals(firstName, reUser.getFirstName());
        assertEquals(lastName, reUser.getLastName());
    }

    @Test
    public void createAdminTest() {
        //  Arrange 
        String username = "a1";
        String email = "test@test.com";
        String firstName = "tFirstName";
        String lastName = "tLastName";
        String password = "p";
        String phone = "1";

        //  Act
        tUserController.createAdmin(username, password, firstName, lastName, email, phone);
        User reUser = tUserController.getUserByUsername(username);
        
        //  Assert
        assertNotNull(reUser);
        assertEquals(4, reUser.getUserID());
        assertEquals(username, reUser.getUsername());
        assertEquals(email, reUser.getEmail());
        assertEquals(firstName, reUser.getFirstName());
        assertEquals(lastName, reUser.getLastName());
    }

    @Test
    public void createPassengerTest() throws Exception {
        //  Arrange 
        String username = "p1";
        String email = "test@test.com";
        String firstName = "tFirstName";
        String lastName = "tLastName";
        String password = "p";
        String phone = "1";


        //  Act
        tUserController.createPassenger(username, password, firstName, lastName, email, phone);
        User reUser = tUserController.getUserByUsername(username);
        
        //  Assert
        assertNotNull(reUser);
        assertEquals(5, reUser.getUserID());
        assertEquals(username, reUser.getUsername());
        assertEquals(email, reUser.getEmail());
        assertEquals(firstName, reUser.getFirstName());
        assertEquals(lastName, reUser.getLastName());
    }

    @Test
    public void resetPasswordTest() {
        //  Arrange
        String username = "f1";
        String currentP = "pw";
        String newP = "np";

        //  Act
        ArrayList<Boolean> flag = tUserController.resetPassword(username, currentP, newP);
        User reUser = tUserController.getUserByUsername(username);

        //  Assert
        assertTrue(flag.get(0));
        assertEquals(newP, reUser.getPassword());
    }

    @Test
    public void removeUserTest() {
        String username = "remove";
        //  Act
        String result = tUserController.removeUser(username);
        User removed = tUserController.getUserByUsername(username);

        //  Assert
        assertFalse(removed.getStatus());
        assertEquals(removed.toString(), result);
    }

    @Test
    public void updateRatingTest() {
        String username = "f1";
        //  Act
        boolean flag = tUserController.updateRating(username, 4, 1);
        User reUser = tUserController.getUserByUsername(username);

        //  Assert
        assertTrue(flag);
        assertEquals(4.0, reUser.getRating(), 0);
    }

    @Test 
    public void updateUserInfoTest() {
        String username = "f1";
        String newFirst = "f";
        String newLast = "l";
        String newEmail = "e";
        String newPhone = "p";

        //  Act
        ArrayList<String> result = tUserController.updateUserInfo(username, newFirst, newLast, newEmail, newPhone);
        User reUser = tUserController.getUserByUsername(username);

        //  Assert
        assertEquals(username, result.get(0));
        assertEquals(newFirst, reUser.getFirstName());
        assertEquals(newLast, reUser.getLastName());
        assertEquals(newEmail, reUser.getEmail());
        assertEquals(newPhone, reUser.getPhone());
    }

    @Test
    public void returnsUser() {
        String username = "f1";
        String firstName = "f";
        String lastName = "l";
        String email = "e";
        //  Act
        User returnedUser = tUserController.getUserByUsername(username);

        //  Assert
        assertNotNull(returnedUser);
        assertEquals(1, returnedUser.getUserID());
        assertEquals(username, returnedUser.getUsername());
        assertEquals(email, returnedUser.getEmail());
        assertEquals(firstName, returnedUser.getFirstName());
        assertEquals(lastName, returnedUser.getLastName());
    }
}