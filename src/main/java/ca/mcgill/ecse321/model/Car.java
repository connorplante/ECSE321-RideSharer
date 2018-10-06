/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse321.model;
import java.util.*;
import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import org.hibernate.Session;

import ca.mcgill.ecse321.HibernateUtil;

// line 33 "../../../../model.ump"
@Entity
@Table(name = "Cars")
public class Car
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Car Attributes
  @Column(name = "Make")
  private String make;
  @Column(name = "Model")
  private String model;
  @Column(name = "Year")
  private int year;
  @Column(name = "numSeats")
  private int numSeats;
  @Column(name = "LicensePlate")
  private String licencePlate;
  @Column(name = "Status")
  private boolean status;

  //Autounique Attributes
  @Id
  @Column(name = "CarID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int carID;

  //Car Associations
  @ManyToOne
  @JoinColumn(name = "FK_UserID")
  private Driver driver;
  @Transient
  private List<Trip> trips;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Car(){

  }
  
  public Car(String aMake, String aModel, int aYear, int aNumSeats, String aLicencePlate, Driver aDriver)
  {
    make = aMake;
    model = aModel;
    year = aYear;
    numSeats = aNumSeats;
    licencePlate = aLicencePlate;
    status = true;
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

  public boolean setStatus(boolean aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
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
  
  public boolean getStatus()
  {
    return status;
  }

  public String getLicencePlate()
  {
    return licencePlate;
  }

  public int getCarID()
  {
    return carID;
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
  public Trip addTrip(String aStart, String aEnd, Date aDate, int aTime, Trip.Status aTripStatus, Driver aDriver)
  {
    return new Trip(aStart, aEnd, aDate, aTime, aTripStatus, aDriver, this);
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
            "carID" + ":" + getCarID()+ "," +
            "make" + ":" + getMake()+ "," +
            "model" + ":" + getModel()+ "," +
            "year" + ":" + getYear()+ "," +
            "numSeats" + ":" + getNumSeats()+ "," +
            "licencePlate" + ":" + getLicencePlate()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "driverUserName = "+ this.getDriver().getUsername() + "]";
  }
  
  public void initList(){
    trips = new ArrayList<Trip>();
  }
}
