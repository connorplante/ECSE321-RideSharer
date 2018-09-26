/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4236.7840197ce modeling language!*/


import java.sql.Date;
import java.sql.Time;
import java.util.*;

// line 42 "model.ump"
// line 96 "model.ump"
public class Trip
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Trip Attributes
  private Date date;
  private Time time;
  private enum status;

  //Trip Associations
  private Driver driver;
  private Car car;
  private List<Leg> legs;
  private List<PassengerTrip> passengerTrips;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Trip(Date aDate, Time aTime, enum aStatus, Driver aDriver, Car aCar)
  {
    date = aDate;
    time = aTime;
    status = aStatus;
    boolean didAddDriver = setDriver(aDriver);
    if (!didAddDriver)
    {
      throw new RuntimeException("Unable to create trip due to driver");
    }
    boolean didAddCar = setCar(aCar);
    if (!didAddCar)
    {
      throw new RuntimeException("Unable to create trip due to car");
    }
    legs = new ArrayList<Leg>();
    passengerTrips = new ArrayList<PassengerTrip>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setTime(Time aTime)
  {
    boolean wasSet = false;
    time = aTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setStatus(enum aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public Date getDate()
  {
    return date;
  }

  public Time getTime()
  {
    return time;
  }

  public enum getStatus()
  {
    return status;
  }
  /* Code from template association_GetOne */
  public Driver getDriver()
  {
    return driver;
  }
  /* Code from template association_GetOne */
  public Car getCar()
  {
    return car;
  }
  /* Code from template association_GetMany */
  public Leg getLeg(int index)
  {
    Leg aLeg = legs.get(index);
    return aLeg;
  }

  public List<Leg> getLegs()
  {
    List<Leg> newLegs = Collections.unmodifiableList(legs);
    return newLegs;
  }

  public int numberOfLegs()
  {
    int number = legs.size();
    return number;
  }

  public boolean hasLegs()
  {
    boolean has = legs.size() > 0;
    return has;
  }

  public int indexOfLeg(Leg aLeg)
  {
    int index = legs.indexOf(aLeg);
    return index;
  }
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
  /* Code from template association_SetOneToMany */
  public boolean setDriver(Driver aDriver)
  {
    boolean wasSet = false;
    if (aDriver == null)
    {
      return wasSet;
    }

    Driver existingDriver = driver;
    driver = aDriver;
    if (existingDriver != null && !existingDriver.equals(aDriver))
    {
      existingDriver.removeTrip(this);
    }
    driver.addTrip(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCar(Car aCar)
  {
    boolean wasSet = false;
    if (aCar == null)
    {
      return wasSet;
    }

    Car existingCar = car;
    car = aCar;
    if (existingCar != null && !existingCar.equals(aCar))
    {
      existingCar.removeTrip(this);
    }
    car.addTrip(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfLegsValid()
  {
    boolean isValid = numberOfLegs() >= minimumNumberOfLegs();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLegs()
  {
    return 1;
  }
  /* Code from template association_AddMandatoryManyToOne */
  public Leg addLeg(String aStart, String aEnd, double aPrice, int aNumSeats)
  {
    Leg aNewLeg = new Leg(aStart, aEnd, aPrice, aNumSeats, this);
    return aNewLeg;
  }

  public boolean addLeg(Leg aLeg)
  {
    boolean wasAdded = false;
    if (legs.contains(aLeg)) { return false; }
    Trip existingTrip = aLeg.getTrip();
    boolean isNewTrip = existingTrip != null && !this.equals(existingTrip);

    if (isNewTrip && existingTrip.numberOfLegs() <= minimumNumberOfLegs())
    {
      return wasAdded;
    }
    if (isNewTrip)
    {
      aLeg.setTrip(this);
    }
    else
    {
      legs.add(aLeg);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeLeg(Leg aLeg)
  {
    boolean wasRemoved = false;
    //Unable to remove aLeg, as it must always have a trip
    if (this.equals(aLeg.getTrip()))
    {
      return wasRemoved;
    }

    //trip already at minimum (1)
    if (numberOfLegs() <= minimumNumberOfLegs())
    {
      return wasRemoved;
    }

    legs.remove(aLeg);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLegAt(Leg aLeg, int index)
  {  
    boolean wasAdded = false;
    if(addLeg(aLeg))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLegs()) { index = numberOfLegs() - 1; }
      legs.remove(aLeg);
      legs.add(index, aLeg);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLegAt(Leg aLeg, int index)
  {
    boolean wasAdded = false;
    if(legs.contains(aLeg))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLegs()) { index = numberOfLegs() - 1; }
      legs.remove(aLeg);
      legs.add(index, aLeg);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addLegAt(aLeg, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPassengerTrips()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public PassengerTrip addPassengerTrip(Passenger aPassenger)
  {
    return new PassengerTrip(aPassenger, this);
  }

  public boolean addPassengerTrip(PassengerTrip aPassengerTrip)
  {
    boolean wasAdded = false;
    if (passengerTrips.contains(aPassengerTrip)) { return false; }
    Trip existingTrip = aPassengerTrip.getTrip();
    boolean isNewTrip = existingTrip != null && !this.equals(existingTrip);
    if (isNewTrip)
    {
      aPassengerTrip.setTrip(this);
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
    //Unable to remove aPassengerTrip, as it must always have a trip
    if (!this.equals(aPassengerTrip.getTrip()))
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
    Driver placeholderDriver = driver;
    this.driver = null;
    if(placeholderDriver != null)
    {
      placeholderDriver.removeTrip(this);
    }
    Car placeholderCar = car;
    this.car = null;
    if(placeholderCar != null)
    {
      placeholderCar.removeTrip(this);
    }
    for(int i=legs.size(); i > 0; i--)
    {
      Leg aLeg = legs.get(i - 1);
      aLeg.delete();
    }
    for(int i=passengerTrips.size(); i > 0; i--)
    {
      PassengerTrip aPassengerTrip = passengerTrips.get(i - 1);
      aPassengerTrip.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "time" + "=" + (getTime() != null ? !getTime().equals(this)  ? getTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "driver = "+(getDriver()!=null?Integer.toHexString(System.identityHashCode(getDriver())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "car = "+(getCar()!=null?Integer.toHexString(System.identityHashCode(getCar())):"null");
  }
}