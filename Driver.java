// Name: Shivam Patel 
// Student ID: 501168896

/*
 * 
 * This class simulates a car driver in a simple Uber-like app 
 */
public class Driver
{
  private String id;
  private String name;
  private String carModel;
  private String licensePlate;
  private double wallet;
  private String type;
  
  // New instance variables 
  private TMUberService service;
  private String address;
  private int zone;
  
  public static enum Status {AVAILABLE, DRIVING};
  private Status status;
    
  
  public Driver(String id, String name, String carModel, String licensePlate, String address,int zone)
  {
    this.id = id;
    this.name = name;
    this.carModel = carModel;
    this.licensePlate = licensePlate;
    this.status = Status.AVAILABLE;
    this.wallet = 0;
    this.type = "";
    this.address = address;
    this.zone = zone;
  }
  // Print Information about a driver
  public void printInfo()
  {
    System.out.printf("Id: %-3s Name: %-15s Car Model: %-15s License Plate: %-10s Wallet: %2.2f    Status: %3s    Address: %s    Zone: %d", 
        id, name, carModel, licensePlate, wallet, status, address, zone);
  }
  
  // Getters and Setters
  public String getType()
  {
    return type;
  }
  public void setType(String type)
  {
    this.type = type;
  }
  public String getId()
  {
    return id;
  }
  public void setId(String id)
  {
    this.id = id;
  }
  public String getName()
  {
    return name;
  }
  public void setName(String name)
  {
    this.name = name;
  }
  public String getCarModel()
  {
    return carModel;
  }
  public void setCarModel(String carModel)
  {
    this.carModel = carModel;
  }
  public String getLicensePlate()
  {
    return licensePlate;
  }
  public void setLicensePlate(String licensePlate)
  {
    this.licensePlate = licensePlate;
  }
  public Status getStatus()
  {
    return status;
  }
  public void setStatus(Status status)
  {
    this.status = status;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String other_address) {
    this.address = other_address;
  }
  
  public double getWallet()
  {
    return wallet;
  }
  public void setWallet(double wallet)
  {
    this.wallet = wallet;
  }

  public TMUberService getService() {
    return service;
  }

  public void setService(TMUberService other_service){
    this.service = other_service;
  }

  public void setZone(int other_zone) {
    this.zone = other_zone;
  }

  /*
   * Two drivers are equal if they have the same name and license plates.
   * This method is overriding the inherited method in superclass Object
   */
  public boolean equals(Object other)
  {
    Driver otherDriver = (Driver) other;
    return this.name.equals(otherDriver.name) && this.licensePlate.equals(otherDriver.licensePlate);
  }
  
  // A driver earns a fee for every ride or delivery
  public void pay(double fee)
  {
    wallet += fee;
  }
}
