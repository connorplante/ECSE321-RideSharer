/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4236.7840197ce modeling language!*/



// line 50 "model.ump"
// line 102 "model.ump"
public class Leg
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Leg Attributes
  private String start;
  private String end;
  private double price;
  private int numSeats;

  //Leg Associations
  private Trip trip;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Leg(String aStart, String aEnd, double aPrice, int aNumSeats, Trip aTrip)
  {
    start = aStart;
    end = aEnd;
    price = aPrice;
    numSeats = aNumSeats;
    boolean didAddTrip = setTrip(aTrip);
    if (!didAddTrip)
    {
      throw new RuntimeException("Unable to create leg due to trip");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

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
            "start" + ":" + getStart()+ "," +
            "end" + ":" + getEnd()+ "," +
            "price" + ":" + getPrice()+ "," +
            "numSeats" + ":" + getNumSeats()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "trip = "+(getTrip()!=null?Integer.toHexString(System.identityHashCode(getTrip())):"null");
  }
}