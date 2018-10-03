/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse321.model;

import org.hibernate.Session;
import org.hibernate.annotations.NaturalId;

import ca.mcgill.ecse321.HibernateUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

// line 56 "../../../../model.ump"
@Entity
@Table(name="Legs")
public class Leg
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Leg Attributes
  @Column(name="Start")
  private String start;
  @Column(name="End")
  private String end;
  @Column(name="Price")
  private double price;
  @Column(name="NumSeats")
  private int numSeats;

  //Autounique Attributes
  @Id
  @Column(name="LegID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int legID;

  //Leg Associations
  @ManyToOne
  @JoinColumn(name="FK_TripID")
  private Trip trip;

  //------------------------
  // CONSTRUCTOR
  //------------------------
public Leg(int LegID, String aStart, String aEnd, double aPrice, int aNumSeats, Trip aTrip){
  start = aStart;
    end = aEnd;
    price = aPrice;
    numSeats = aNumSeats;
    legID = LegID;
    trip = aTrip;

};

  public Leg(String aStart, String aEnd, double aPrice, int aNumSeats, Trip aTrip)
  {
    start = aStart;
    end = aEnd;
    price = aPrice;
    numSeats = aNumSeats;
    trip = aTrip;
  }
  private static int getNumNextLegID() {

    Session session = HibernateUtil.getSession();
    int count = 1 + ((Long)session.createQuery("SELECT count(LegID) FROM Leg").uniqueResult()).intValue();
    session.close();

    return count;
  }
  //------------------------
  // INTERFACE
  //------------------------

  public boolean setID(int id){
    boolean wasSet = false;
    legID = id;
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

  public boolean setPrice(double aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
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

  public String getStart()
  {
    return start;
  }

  public String getEnd()
  {
    return end;
  }

  public double getPrice()
  {
    return price;
  }

  public int getNumSeats()
  {
    return numSeats;
  }

  public int getLegID()
  {
    return legID;
  }
  /* Code from template association_GetOne */
  public Trip getTrip()
  {
    return trip;
  }
  /* Code from template association_SetOneToMandatoryMany */
  public boolean setTrip(Trip aTrip)
  {
    boolean wasSet = false;
    //Must provide trip to leg
    if (aTrip == null)
    {
      return wasSet;
    }

    if (trip != null && trip.numberOfLegs() <= Trip.minimumNumberOfLegs())
    {
      return wasSet;
    }

    Trip existingTrip = trip;
    trip = aTrip;
    if (existingTrip != null && !existingTrip.equals(aTrip))
    {
      boolean didRemove = existingTrip.removeLeg(this);
      if (!didRemove)
      {
        trip = existingTrip;
        return wasSet;
      }
    }
    trip.addLeg(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Trip placeholderTrip = trip;
    this.trip = null;
    if(placeholderTrip != null)
    {
      placeholderTrip.removeLeg(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "legID" + ":" + getLegID()+ "," +
            "start" + ":" + getStart()+ "," +
            "end" + ":" + getEnd()+ "," +
            "price" + ":" + getPrice()+ "," +
            "numSeats" + ":" + getNumSeats()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "trip = "+(getTrip()!=null?Integer.toHexString(System.identityHashCode(getTrip())):"null");
  }
}