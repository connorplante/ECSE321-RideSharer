/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse321.model;
import java.util.*;

// line 17 "../../../../model.ump"
public class Passenger extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Passenger Associations
  private List<PassengerTrip> passengerTrips;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Passenger(String aUsername, String aPassword, String aFirstName, String aLastName, String aEmail, String aPhone, boolean aStatus, double aRating, int aNumRides)
  {
    super(aUsername, aPassword, aFirstName, aLastName, aEmail, aPhone, aStatus, aRating, aNumRides, 1);
    passengerTrips = new ArrayList<PassengerTrip>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public PassengerTrip getPassengerTrip(int index)
  {
    PassengerTrip aPassengerTrip = passengerTrips.get(index);
    return aPassengerTrip;
  }

  public List<PassengerTrip> getPassengerTrips()
  {
    List<PassengerTrip> newPassengerTrips = Collections.unmodifiableList(passengerTrips);
    return newPassengerTrips;
  }

  public int numberOfPassengerTrips()
  {
    int number = passengerTrips.size();
    return number;
  }

  public boolean hasPassengerTrips()
  {
    boolean has = passengerTrips.size() > 0;
    return has;
  }

  public int indexOfPassengerTrip(PassengerTrip aPassengerTrip)
  {
    int index = passengerTrips.indexOf(aPassengerTrip);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPassengerTrips()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public PassengerTrip addPassengerTrip(Trip aTrip)
  {
    return new PassengerTrip(this, aTrip);
  }

  public boolean addPassengerTrip(PassengerTrip aPassengerTrip)
  {
    boolean wasAdded = false;
    if (passengerTrips.contains(aPassengerTrip)) { return false; }
    Passenger existingPassenger = aPassengerTrip.getPassenger();
    boolean isNewPassenger = existingPassenger != null && !this.equals(existingPassenger);
    if (isNewPassenger)
    {
      aPassengerTrip.setPassenger(this);
    }
    else
    {
      passengerTrips.add(aPassengerTrip);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePassengerTrip(PassengerTrip aPassengerTrip)
  {
    boolean wasRemoved = false;
    //Unable to remove aPassengerTrip, as it must always have a passenger
    if (!this.equals(aPassengerTrip.getPassenger()))
    {
      passengerTrips.remove(aPassengerTrip);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPassengerTripAt(PassengerTrip aPassengerTrip, int index)
  {  
    boolean wasAdded = false;
    if(addPassengerTrip(aPassengerTrip))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPassengerTrips()) { index = numberOfPassengerTrips() - 1; }
      passengerTrips.remove(aPassengerTrip);
      passengerTrips.add(index, aPassengerTrip);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePassengerTripAt(PassengerTrip aPassengerTrip, int index)
  {
    boolean wasAdded = false;
    if(passengerTrips.contains(aPassengerTrip))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPassengerTrips()) { index = numberOfPassengerTrips() - 1; }
      passengerTrips.remove(aPassengerTrip);
      passengerTrips.add(index, aPassengerTrip);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPassengerTripAt(aPassengerTrip, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=passengerTrips.size(); i > 0; i--)
    {
      PassengerTrip aPassengerTrip = passengerTrips.get(i - 1);
      aPassengerTrip.delete();
    }
    super.delete();
  }

}