/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse321.model;

// line 26 "../../../../rideSharingModel.ump"
public class Admin extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Admin(String aUsername, String aPassword, String aEmail, String aPhone, boolean aStatus, double aRating, int aNumRides)
  {
    super(aUsername, aPassword, aEmail, aPhone, aStatus, aRating, aNumRides);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}