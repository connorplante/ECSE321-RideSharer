/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse321.model;
import java.util.*;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;


// line 33 "../../../../model.ump"
@Entity
@Table(name = "Requests")
public class Request
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Autounique Attributes
  @Id
  @Column(name = "RequestID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int requestID;

  @Column(name = "Status")
  private boolean status;

  @Column(name = "Start")
  private String start;

  @Column(name = "End")
  private String end;

  //Car Associations
  @OneToOne
  @JoinColumn(name = "FK_PassengerID")
  private Passenger passenger;

  @OneToOne
  @JoinColumn(name = "FK_DriverID")
  private Driver driver;

  @OneToOne
  @JoinColumn(name = "FK_TripID")
  private Trip trip;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Request(){
  }
  
  public Request(Passenger aPassenger, Driver aDriver, Trip aTrip, String aStart, String aEnd)
  {
    status = true;
    boolean didAddDriver = setDriver(aDriver);
    if (!didAddDriver)
    {
      throw new RuntimeException("Unable to create car due to driver");
    }
    boolean didAddPassenger = setPassenger(aPassenger);
    if (!didAddPassenger)
    {
      throw new RuntimeException("Unable to create car due to passenger");
    }
    boolean didAddTrip = setTrip(aTrip);
    if (!didAddTrip)
    {
      throw new RuntimeException("Unable to create car due to driver");
    }
    start = aStart;
    end = aEnd;
  }

  //------------------------
  // INTERFACE
  //------------------------
  

  public boolean setStatus(boolean aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setStart(String aStart)
  {
    boolean wasSet = false;
    start = aStart;
    wasSet = true;
    return wasSet;
  }

  public boolean setEnd(String aEnd)
  {
    boolean wasSet = false;
    end = aEnd;
    wasSet = true;
    return wasSet;
  }

  public int getRequestID(){
      return requestID;
  }
  
  
  public boolean getStatus()
  {
    return status;
  }

  public String getStart()
  {
    return start;
  }

  public String getEnd()
  {
    return end;
  }

  /* Code from template association_GetOne */
  public Driver getDriver()
  {
    return driver;
  }

  /* Code from template association_GetOne */
  public Passenger getPassenger()
  {
    return passenger;
  }

  /* Code from template association_GetOne */
  public Trip getTrip()
  {
    return trip;
  }

  public boolean setDriver(Driver aDriver){
        boolean isSet = false;

      if(aDriver == null){
          return isSet;
      }else{
          driver = aDriver;
          isSet = true;
      }
      return isSet;
  }

  public boolean setPassenger(Passenger aPassenger){
    boolean isSet = false;

  if(aPassenger == null){
      return isSet;
  }else{
      passenger = aPassenger;
      isSet = true;
  }
  return isSet;
}

public boolean setTrip(Trip aTrip){
    boolean isSet = false;

  if(aTrip == null){
      return isSet;
  }else{
      trip = aTrip;
      isSet = true;
  }
  return isSet;
}

  public String toString()
  {
    return super.toString() + "["+
            "requestID" + ":" + getRequestID()+ "," +
            "status" + ":" + getStatus()+ "," +
            "tripID" + ":" + this.getTrip().getTripID()+ "," +
            "driver username" + ":" + this.getDriver().getUsername()+ "," +
            "driver ID" + ":" + this.getDriver().getUserID() + "," +
            "passenger username" + ":" + this.getPassenger().getUsername() + 
            "," + "passenger ID" + ":" + this.getPassenger().getUserID() + "]";
  }
  
}