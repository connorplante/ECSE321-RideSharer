/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse321.model;

import javax.persistence.*;
import org.hibernate.Session;
import org.hibernate.annotations.NaturalId;

import ca.mcgill.ecse321.HibernateUtil;

// line 66 "../../../../model.ump"

@Entity
@Table(name = "PassengerTrips")
public class PassengerTrip
{

  //------------------------

  // STATIC VARIABLES
  //------------------------

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PassengerTrip Attributes
 

  //Autounique Attributes
  @Id
  @Column(name = "PassengerTripID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int passengerTripID;
  @ManyToOne
  @JoinColumn(name = "FK_TripID")
  private Trip trip;
  //PassengerTrip Associations
  @ManyToOne
  @JoinColumn(name = "FK_UserID")
  private Passenger passenger;
  @Column(name = "Price")
  private double price;

  //Helper Variables
  @Transient
  private boolean canSetPrice;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PassengerTrip(Passenger aPassenger, Trip aTrip)
  {
    canSetPrice = true;
    passenger= aPassenger;
    trip=aTrip;
  }

  private static int getNumNextTripID() {

    Session session = HibernateUtil.getSession();
    int count = 1 + ((Long)session.createQuery("SELECT count(PassengerTripID) FROM PassengerTrip").uniqueResult()).intValue();
    session.close();

    return count;
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template attribute_SetImmutable */
  public boolean setPrice(double aPrice)
  {
    boolean wasSet = false;
    if (!canSetPrice) { return false; }
    canSetPrice = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public double getPrice()
  {
    return price;
  }

  public int getPassengerTripID()
  {
    return passengerTripID;
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
  /* Code from template association_SetOneToMany */
  public boolean setPassenger(Passenger aPassenger)
  {
    boolean wasSet = false;
    if (aPassenger == null)
    {
      return wasSet;
    }

    Passenger existingPassenger = passenger;
    passenger = aPassenger;
    if (existingPassenger != null && !existingPassenger.equals(aPassenger))
    {
      existingPassenger.removePassengerTrip(this);
    }
    passenger.addPassengerTrip(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setTrip(Trip aTrip)
  {
    boolean wasSet = false;
    if (aTrip == null)
    {
      return wasSet;
    }

    Trip existingTrip = trip;
    trip = aTrip;
    if (existingTrip != null && !existingTrip.equals(aTrip))
    {
      existingTrip.removePassengerTrip(this);
    }
    trip.addPassengerTrip(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Passenger placeholderPassenger = passenger;
    this.passenger = null;
    if(placeholderPassenger != null)
    {
      placeholderPassenger.removePassengerTrip(this);
    }
    Trip placeholderTrip = trip;
    this.trip = null;
    if(placeholderTrip != null)
    {
      placeholderTrip.removePassengerTrip(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "passengerTripID" + ":" + getPassengerTripID()+ "," +
            "price" + ":" + getPrice()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "passenger = "+(getPassenger()!=null?Integer.toHexString(System.identityHashCode(getPassenger())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "trip = "+(getTrip()!=null?Integer.toHexString(System.identityHashCode(getTrip())):"null");
  }
}