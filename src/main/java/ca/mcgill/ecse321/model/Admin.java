/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4236.7840197ce modeling language!*/

package ca.mcgill.ecse321.model;

// line 28 "../../../../../../../ump/tmp206467/model.ump"
// line 87 "../../../../../../../ump/tmp206467/model.ump"
public class Admin extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Admin(String aUsername, String aPassword, String aFirstName, String aLastName, String aEmail, String aPhone, boolean aStatus, double aRating, int aNumRides)
  {
    super(aUsername, aPassword, aFirstName, aLastName, aEmail, aPhone, aStatus, aRating, aNumRides);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}