/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse321.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;

import org.hibernate.Session;
import org.hibernate.annotations.NaturalId;

import ca.mcgill.ecse321.HibernateUtil;

// line 3 "../../../../model.ump"
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Role", discriminatorType = DiscriminatorType.INTEGER)
@Table(name = "Users")
public class User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //User Attributes
  @NaturalId
  @Column(name = "Username")
  private String username;
  @Column(name = "Password")
  private String password;
  @Column(name = "FirstName")
  private String firstName;
  @Column(name = "LastName")
  private String lastName;
  @Column(name = "Email")
  private String email;
  @Column(name = "PhoneNumber")
  private String phone;
  @Column(name = "Status")
  private boolean status;
  @Column(name = "Rating")
  private double rating;
  @Column(name = "numRides")
  private int numRides;
  @Transient
  private int role;

  //Autounique Attributes
  @Id
  @Column(name = "UserID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int userID;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public User(){}

  public User(String aUsername, String aPassword, String aFirstName, String aLastName, String aEmail, String aPhone, boolean aStatus, double aRating, int aNumRides, int aRole)
  {
    username = aUsername;
    password = aPassword;
    firstName = aFirstName;
    lastName = aLastName;
    email = aEmail;
    phone = aPhone;
    status = aStatus;
    rating = aRating;
    numRides = aNumRides;
    role = aRole;
  }

  public User(String aUsername, String aPassword, String aFirstName, String aLastName, String aEmail, String aPhone, boolean aStatus, double aRating, int aNumRides){
    username = aUsername;
    password = aPassword;
    firstName = aFirstName;
    lastName = aLastName;
    email = aEmail;
    phone = aPhone;
    status = aStatus;
    rating = aRating;
    numRides = aNumRides;
    role = 4;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setUsername(String aUsername)
  {
    boolean wasSet = false;
    username = aUsername;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setFirstName(String aFirstName)
  {
    boolean wasSet = false;
    firstName = aFirstName;
    wasSet = true;
    return wasSet;
  }

  public boolean setLastName(String aLastName)
  {
    boolean wasSet = false;
    lastName = aLastName;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setPhone(String aPhone)
  {
    boolean wasSet = false;
    phone = aPhone;
    wasSet = true;
    return wasSet;
  }

  public boolean setStatus(boolean aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setRating(double aRating)
  {
    boolean wasSet = false;
    rating = aRating;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumRides(int aNumRides)
  {
    boolean wasSet = false;
    numRides = aNumRides;
    wasSet = true;
    return wasSet;
  }

  public boolean setRole(int roleNum)
  {
    boolean wasSet = false;
    role = roleNum;
    wasSet = true;
    return wasSet;
  }

  public String getUsername()
  {
    return username;
  }

  public String getPassword()
  {
    return password;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPhone()
  {
    return phone;
  }

  public boolean getStatus()
  {
    return status;
  }

  public double getRating()
  {
    return rating;
  }

  public int getNumRides()
  {
    return numRides;
  }

  public int getUserID()
  {
    return userID;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isStatus()
  {
    return status;
  }

  public int getRole(){
    return role;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "userID" + ":" + getUserID()+ "," +
            "username" + ":" + getUsername()+ "," +
            "password" + ":" + getPassword()+ "," +
            "firstName" + ":" + getFirstName()+ "," +
            "lastName" + ":" + getLastName()+ "," +
            "email" + ":" + getEmail()+ "," +
            "phone" + ":" + getPhone()+ "," +
            "status" + ":" + getStatus()+ "," +
            "rating" + ":" + getRating()+ "," +
            "numRides" + ":" + getNumRides()+ "]";
  }
}