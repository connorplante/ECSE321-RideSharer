/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse321.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

// line 28 "../../../../model.ump"
@Entity(name="Admin")
@DiscriminatorValue(value = "3")
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
    super(aUsername, aPassword, aFirstName, aLastName, aEmail, aPhone, aStatus, aRating, aNumRides, 3);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}
