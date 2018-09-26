/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4236.7840197ce modeling language!*/



/**
 * Unable to update umple code due to error at [59,7]
 * Unable to update umple code due to error at [58,7]
 */
// line 4 "model.ump"
// line 69 "model.ump"
public class User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //User Attributes
  private String username;
  private String password;
  private String email;
  private String phone;
  private bool status;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public User(String aUsername, String aPassword, String aEmail, String aPhone, bool aStatus)
  {
    username = aUsername;
    password = aPassword;
    email = aEmail;
    phone = aPhone;
    status = aStatus;
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

  public boolean setStatus(bool aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
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

  public String getEmail()
  {
    return email;
  }

  public String getPhone()
  {
    return phone;
  }

  public bool getStatus()
  {
    return status;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "username" + ":" + getUsername()+ "," +
            "password" + ":" + getPassword()+ "," +
            "email" + ":" + getEmail()+ "," +
            "phone" + ":" + getPhone()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 11 model.ump
  double rating
    int numRides
  
}