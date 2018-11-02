import org.junit.Test;

import ca.mcgill.ecse321.controller.UserController;
import ca.mcgill.ecse321.model.*;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Rule;

public class UserTest {

    public UserController tUserController;

    @Before
    public void before() {
        tUserController = new UserController();
    }

    // @Test
    // public void returnsUser() {

    //     //  Arrange
    //     Session session = sf.getSession();

    //     sf.beginTransaction();

    //     String username = "tUsername";
    //     String email = "test@test.com";
    //     String firstName = "tFirstName";
    //     String lastName = "tLastName";

    //     User tUser = new User();
    //     tUser.setUsername(username);
    //     tUser.setEmail(email);
    //     tUser.setFirstName(firstName);
    //     tUser.setLastName(lastName);

    //     session.save(tUser);

    //     sf.commit();

    //     //  Act
    //     User returnedUser = tUserController.getUserByUsername(username);

    //     //  Assert
    //     assertNotNull(returnedUser);
    //     assertEquals(1, returnedUser.getUserID());
    //     assertEquals(username, returnedUser.getUsername());
    //     assertEquals(email, returnedUser.getEmail());
    //     assertEquals(firstName, returnedUser.getFirstName());
    //     assertEquals(lastName, returnedUser.getLastName());
    // }

    @Test
    public void createDriverTest() {
        //  Arrange 
        String username = "tUsername2";
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
        assertEquals(1, reUser.getUserID());
        assertEquals(username, reUser.getUsername());
        assertEquals(email, reUser.getEmail());
        assertEquals(firstName, reUser.getFirstName());
        assertEquals(lastName, reUser.getLastName());
        assertEquals(0, reUser.getRole());
    }
}
//     @Test
//     public void createPassengerTest() throws Exception {
//         //  Arrange 
//         String username = "tUsername";
//         String email = "test@test.com";
//         String firstName = "tFirstName";
//         String lastName = "tLastName";
//         String password = "p";
//         String phone = "1";


//         //  Act
//         tUserController.createPassenger(username, password, firstName, lastName, email, phone);
//         User reUser = tUserController.getUserByUsername(username);
        
//         //  Assert
//         assertNotNull(reUser);
//         assertEquals(1, reUser.getUserID());
//         assertEquals(username, reUser.getUsername());
//         assertEquals(email, reUser.getEmail());
//         assertEquals(firstName, reUser.getFirstName());
//         assertEquals(lastName, reUser.getLastName());
//         assertEquals(1, reUser.getRole());
//     }

//     @Test
//     public void createAdminTest() {
//         //  Arrange 
//         String username = "tUsername";
//         String email = "test@test.com";
//         String firstName = "tFirstName";
//         String lastName = "tLastName";
//         String password = "p";
//         String phone = "1";


//         //  Act
//         tUserController.createAdmin(username, password, firstName, lastName, email, phone);
//         User reUser = tUserController.getUserByUsername(username);
        
//         //  Assert
//         assertNotNull(reUser);
//         assertEquals(1, reUser.getUserID());
//         assertEquals(username, reUser.getUsername());
//         assertEquals(email, reUser.getEmail());
//         assertEquals(firstName, reUser.getFirstName());
//         assertEquals(lastName, reUser.getLastName());
//         assertEquals(3, reUser.getRole());
//     }

//     @Test
//     public void resetPasswordTest() {
//         //  Arrange
//         Session session = sf.getSession();

//         sf.beginTransaction();

//         String username = "tUsername";
//         String currentP = "cp";
//         String email = "test@test.com";
//         String firstName = "tFirstName";
//         String lastName = "tLastName";

//         User tUser = new User();
//         tUser.setUsername(username);
//         tUser.setPassword(currentP);
//         tUser.setEmail(email);
//         tUser.setFirstName(firstName);
//         tUser.setLastName(lastName);

//         session.save(tUser);

//         sf.commit();

//         String newP = "np";

//         //  Act
//         boolean flag = tUserController.resetPassword(username, currentP, newP);
//         User reUser = tUserController.getUserByUsername(username);

//         //  Assert
//         assertTrue(flag);
//         assertEquals(newP, reUser.getPassword());
//     }

//     @Test
//     public void removeUserTest() {
//         //  Arrange
//         Session session = sf.getSession();

//         sf.beginTransaction();

//         String username = "tUsername";
//         String currentP = "cp";
//         String email = "test@test.com";
//         String firstName = "tFirstName";
//         String lastName = "tLastName";

//         User tUser = new User();
//         tUser.setUsername(username);
//         tUser.setPassword(currentP);
//         tUser.setEmail(email);
//         tUser.setFirstName(firstName);
//         tUser.setLastName(lastName);
//         tUser.setStatus(true);

//         session.save(tUser);

//         sf.commit();

//         //  Act
//         String result = tUserController.removeUser(username);
//         User removed = tUserController.getUserByUsername(username);

//         //  Assert
//         assertFalse(removed.getStatus());
//         assertEquals(removed.toString(), result);
//     }

//     @Test
//     public void updateRatingTest() {
//         //  Arrange
//         Session session = sf.getSession();

//         sf.beginTransaction();

//         String username = "tUsername";
//         String currentP = "cp";
//         String email = "test@test.com";
//         String firstName = "tFirstName";
//         String lastName = "tLastName";

//         User tUser = new User();
//         tUser.setUsername(username);
//         tUser.setPassword(currentP);
//         tUser.setEmail(email);
//         tUser.setFirstName(firstName);
//         tUser.setLastName(lastName);
//         tUser.setStatus(true);

//         session.save(tUser);

//         sf.commit();

//         //  Act
//         boolean flag = tUserController.updateRating(username, 4);
//         User reUser = tUserController.getUserByUsername(username);

//         //  Assert
//         assertTrue(flag);
//         assertEquals(4.0, reUser.getRating(), 0);
//     }

//     @Test 
//     public void updateUserInfoTest() {
//         //  Arrange
//         Session session = sf.getSession();

//         sf.beginTransaction();

//         String username = "tUsername";
//         String email = "test@test.com";
//         String firstName = "tFirstName";
//         String lastName = "tLastName";

//         User tUser = new User();
//         tUser.setUsername(username);
//         tUser.setEmail(email);
//         tUser.setFirstName(firstName);
//         tUser.setLastName(lastName);
//         tUser.setStatus(true);

//         session.save(tUser);

//         sf.commit();

//         String newFirst = "f";
//         String newLast = "l";
//         String newEmail = "e";
//         String newPhone = "p";

//         //  Act
//         String result = tUserController.updateUserInfo(username, newFirst, newLast, newEmail, newPhone);
//         User reUser = tUserController.getUserByUsername(username);

//         //  Assert
//         assertEquals(reUser.toString(), result);
//         assertEquals(newFirst, reUser.getFirstName());
//         assertEquals(newLast, reUser.getLastName());
//         assertEquals(newEmail, reUser.getEmail());
//         assertEquals(newPhone, reUser.getPhone());
//     }

// }/         assertEquals(newFirst, reUser.getFirstName());
//         assertEquals(newLast, reUser.getLastName());
//         assertEquals(newEmail, reUser.getEmail());
//         assertEquals(newPhone, reUser.getPhone());
//     }

// }