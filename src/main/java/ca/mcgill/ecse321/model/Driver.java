/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4236.7840197ce modeling language!*/

package ca.mcgill.ecse321.model;
import java.util.*;
import java.sql.Date;
import java.sql.Time;

// line 22 "../../../../../../../ump/tmp206467/model.ump"
// line 81 "../../../../../../../ump/tmp206467/model.ump"
public class Driver extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Driver Associations
  private List<Trip> trips;
  private List<Car> cars;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Driver(String aUsername, String aPassword, String aFirstName, String aLastName, String aEmail, String aPhone, boolean aStatus, double aRating, int aNumRides)
  {
    super(aUsername, aPassword, aFirstName, aLastName, aEmail, aPhone, aStatus, aRating, aNumRides);
    trips = new ArrayList<Trip>();
    cars = new ArrayList<Car>();
  }

  //------------------------
  // INTERFACE
  //------------------------
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
  /* Code from template association_GetMany */
  public Car getCar(int index)
  {
    Car aCar = cars.get(index);
    return aCar;
  }

  public List<Car> getCars()
  {
    List<Car> newCars = Collections.unmodifiableList(cars);
    return newCars;
  }

  public int numberOfCars()
  {
    int number = cars.size();
    return number;
  }

  public boolean hasCars()
  {
    boolean has = cars.size() > 0;
    return has;
  }

  public int indexOfCar(Car aCar)
  {
    int index = cars.indexOf(aCar);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTrips()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Trip addTrip(Date aDate, Time aTime, String aStart, String aEnd, Trip.Status aTripStatus, Car aCar)
  {
    return new Trip(aDate, aTime, aStart, aEnd, aTripStatus, this, aCar);
  }

  public boolean addTrip(Trip aTrip)
  {
    boolean wasAdded = false;
    if (trips.contains(aTrip)) { return false; }
    Driver existingDriver = aTrip.getDriver();
    boolean isNewDriver = existingDriver != null && !this.equals(existingDriver);
    if (isNewDriver)
    {
      aTrip.setDriver(this);
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
    //Unable to remove aTrip, as it must always have a driver
    if (!this.equals(aTrip.getDriver()))
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
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfCarsValid()
  {
    boolean isValid = numberOfCars() >= minimumNumberOfCars();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCars()
  {
    return 1;
  }
  /* Code from template association_AddMandatoryManyToOne */
  public Car addCar(String aMake, String aModel, int aYear, int aNumSeats, String aLicencePlate)
  {
    Car aNewCar = new Car(aMake, aModel, aYear, aNumSeats, aLicencePlate, this);
    return aNewCar;
  }

  public boolean addCar(Car aCar)
  {
    boolean wasAdded = false;
    if (cars.contains(aCar)) { return false; }
    Driver existingDriver = aCar.getDriver();
    boolean isNewDriver = existingDriver != null && !this.equals(existingDriver);

    if (isNewDriver && existingDriver.numberOfCars() <= minimumNumberOfCars())
    {
      return wasAdded;
    }
    if (isNewDriver)
    {
      aCar.setDriver(this);
    }
    else
    {
      cars.add(aCar);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCar(Car aCar)
  {
    boolean wasRemoved = false;
    //Unable to remove aCar, as it must always have a driver
    if (this.equals(aCar.getDriver()))
    {
      return wasRemoved;
    }

    //driver already at minimum (1)
    if (numberOfCars() <= minimumNumberOfCars())
    {
      return wasRemoved;
    }

    cars.remove(aCar);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCarAt(Car aCar, int index)
  {  
    boolean wasAdded = false;
    if(addCar(aCar))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCars()) { index = numberOfCars() - 1; }
      cars.remove(aCar);
      cars.add(index, aCar);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCarAt(Car aCar, int index)
  {
    boolean wasAdded = false;
    if(cars.contains(aCar))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCars()) { index = numberOfCars() - 1; }
      cars.remove(aCar);
      cars.add(index, aCar);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCarAt(aCar, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=trips.size(); i > 0; i--)
    {
      Trip aTrip = trips.get(i - 1);
      aTrip.delete();
    }
    for(int i=cars.size(); i > 0; i--)
    {
      Car aCar = cars.get(i - 1);
      aCar.delete();
    }
    super.delete();
  }

}