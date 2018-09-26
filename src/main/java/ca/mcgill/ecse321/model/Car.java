/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4236.7840197ce modeling language!*/


import java.util.*;
import java.sql.Date;
import java.sql.Time;

// line 31 "model.ump"
// line 90 "model.ump"
public class Car
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Car Attributes
  private String make;
  private String model;
  private int year;
  private int numSeats;
  private String licencePlate;

  //Car Associations
  private Driver driver;
  private List<Trip> trips;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Car(String aMake, String aModel, int aYear, int aNumSeats, String aLicencePlate, Driver aDriver)
  {
    make = aMake;
    model = aModel;
    year = aYear;
    numSeats = aNumSeats;
    licencePlate = aLicencePlate;
    boolean didAddDriver = setDriver(aDriver);
    if (!didAddDriver)
    {
      throw new RuntimeException("Unable to create car due to driver");
    }
    trips = new ArrayList<Trip>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setMake(String aMake)
  {
    boolean wasSet = false;
    make = aMake;
    wasSet = true;
    return wasSet;
  }

  public boolean setModel(String aModel)
  {
    boolean wasSet = false;
    model = aModel;
    wasSet = true;
    return wasSet;
  }

  public boolean setYear(int aYear)
  {
    boolean wasSet = false;
    year = aYear;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumSeats(int aNumSeats)
  {
    boolean wasSet = false;
    numSeats = aNumSeats;
    wasSet = true;
    return wasSet;
  }

  public boolean setLicencePlate(String aLicencePlate)
  {
    boolean wasSet = false;
    licencePlate = aLicencePlate;
    wasSet = true;
    return wasSet;
  }

  public String getMake()
  {
    return make;
  }

  public String getModel()
  {
    return model;
  }

  public int getYear()
  {
    return year;
  }

  public int getNumSeats()
  {
    return numSeats;
  }

  public String getLicencePlate()
  {
    return licencePlate;
  }
  /* Code from template association_GetOne */
  public Driver getDriver()
  {
    return driver;
  }
  /* Code from template association_GetMany */
  public Trip getTrip(int index)
  {
    Trip aTrip = trips.get(index);
    return aTrip;
  }

  public List<Trip> getTrips()
  {
    List<Trip> newTrips = Collections.unmodifiableList(trips);
    return newTrips;
  }

  public int numberOfTrips()
  {
    int number = trips.size();
    return number;
  }

  public boolean hasTrips()
  {
    boolean has = trips.size() > 0;
    return has;
  }

  public int indexOfTrip(Trip aTrip)
  {
    int index = trips.indexOf(aTrip);
    return index;
  }
  /* Code from template association_SetOneToMandatoryMany */
  public boolean setDriver(Driver aDriver)
  {
    boolean wasSet = false;
    //Must provide driver to car
    if (aDriver == null)
    {
      return wasSet;
    }

    if (driver != null && driver.numberOfCars() <= Driver.minimumNumberOfCars())
    {
      return wasSet;
    }

    Driver existingDriver = driver;
    driver = aDriver;
    if (existingDriver != null && !existingDriver.equals(aDriver))
    {
      boolean didRemove = existingDriver.removeCar(this);
      if (!didRemove)
      {
        driver = existingDriver;
        return wasSet;
      }
    }
    driver.addCar(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTrips()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Trip addTrip(Date aDate, Time aTime, Trip.Status aTripStatus, Driver aDriver)
  {
    return new Trip(aDate, aTime, aTripStatus, aDriver, this);
  }

  public boolean addTrip(Trip aTrip)
  {
    boolean wasAdded = false;
    if (trips.contains(aTrip)) { return false; }
    Car existingCar = aTrip.getCar();
    boolean isNewCar = existingCar != null && !this.equals(existingCar);
    if (isNewCar)
    {
      aTrip.setCar(this);
    }
    else
    {
      trips.add(aTrip);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTrip(Trip aTrip)
  {
    boolean wasRemoved = false;
    //Unable to remove aTrip, as it must always have a car
    if (!this.equals(aTrip.getCar()))
    {
      trips.remove(aTrip);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addTripAt(Trip aTrip, int index)
  {  
    boolean wasAdded = false;
    if(addTrip(aTrip))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTrips()) { index = numberOfTrips() - 1; }
      trips.remove(aTrip);
      trips.add(index, aTrip);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTripAt(Trip aTrip, int index)
  {
    boolean wasAdded = false;
    if(trips.contains(aTrip))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTrips()) { index = numberOfTrips() - 1; }
      trips.remove(aTrip);
      trips.add(index, aTrip);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTripAt(aTrip, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Driver placeholderDriver = driver;
    this.driver = null;
    if(placeholderDriver != null)
    {
      placeholderDriver.removeCar(this);
    }
    for(int i=trips.size(); i > 0; i--)
    {
      Trip aTrip = trips.get(i - 1);
      aTrip.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "make" + ":" + getMake()+ "," +
            "model" + ":" + getModel()+ "," +
            "year" + ":" + getYear()+ "," +
            "numSeats" + ":" + getNumSeats()+ "," +
            "licencePlate" + ":" + getLicencePlate()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "driver = "+(getDriver()!=null?Integer.toHexString(System.identityHashCode(getDriver())):"null");
  }
}