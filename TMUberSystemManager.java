// Name: Shivam Patel 
// Student ID: 501168896

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;

/*
 * 
 * This class contains the main logic of the system.
 * 
 *  It keeps track of all users, drivers and service requests (RIDE or DELIVERY)
 * 
 */
public class TMUberSystemManager
{
  private Map<String,User> users; // holds all users in a map where key is user id and value is user object 
  private ArrayList<Driver> drivers; // holds all drivers in an ArrayList 
  private Queue<TMUberService> [] serviceRequests; // holds all service requests in an array of queues 

  public double totalRevenue; // Total revenues accumulated via rides and deliveries
  
  // Rates per city block
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  
  // Portion of a ride/delivery cost paid to the driver
  private static final double PAYRATE = 0.1;

  // These variables are used to generate user account and driver ids
  int userAccountId = 900;
  int driverId = 700;

  public TMUberSystemManager()
  {
    users = new HashMap<String,User>(); // intializing users 
    drivers = new ArrayList<Driver>(); // intializing drivers 
    serviceRequests = new LinkedList[4];  // intializing serviceRequests to length of 4(one for each zone)

    // iterating through service requests and initializing each queue into a linked list 
    for (int i = 0; i < serviceRequests.length; i ++) {
      serviceRequests[i] = new LinkedList<>();
    }

    totalRevenue = 0;
  }

  // General string variable used to store an error message when something is invalid 
  // (e.g. user does not exist, invalid address etc.)  
  // The methods below will set this errMsg string and then return false
  
  // Generate a new user account id
  private String generateUserAccountId()
  {
    return "" + userAccountId + users.size();
  }
  
  // Generate a new driver id
  private String generateDriverId()
  {
    return "" + driverId + drivers.size();
  }

  // Given user account id, find user in list of users
  public User getUser(String accountId)
  {
    return users.get(accountId);
  }
  
  // Check for duplicate user
  private boolean userExists(User user)
  {
    // Simple way
    // return users.contains(user);
    return users.containsValue(user);
  }
  
 // Check for duplicate driver
 private boolean driverExists(Driver driver)
 {
   // simple way
   // return drivers.contains(driver);
   return drivers.contains(driver);
 }
  
 
 // Given a user, check if user ride/delivery request already exists in service requests
 private boolean existingRequest(TMUberService req)
 {
   // Simple way return serviceRequests.contains(req);
   for (int i = 0; i < serviceRequests.length; i ++) {
    Queue<TMUberService> current_queue = serviceRequests[i];
    if (current_queue.contains(req)) {
      return true;
    } 
   }
   return false;
 } 
 
  
  // Calculate the cost of a ride or of a delivery based on distance 
  private double getDeliveryCost(int distance)
  {
    return distance * DELIVERYRATE;
  }

  private double getRideCost(int distance)
  {
    return distance * RIDERATE;
  }

  // Go through all drivers and see if one is available
  // Choose the first available driver
  private Driver getAvailableDriver()
  {
    for (int i = 0; i < drivers.size(); i++)
    {
      Driver driver = drivers.get(i);
      if (driver.getStatus() == Driver.Status.AVAILABLE)
        return driver;
    }
    return null;
  }

  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers()
  {
    ArrayList<User> u_list = new ArrayList<User>(users.values());
    Collections.sort(u_list,new IDComparator());
    for (int i = 0; i < u_list.size(); i ++) {
      System.out.println();
      u_list.get(i).printInfo();
    }
  }

  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers()
  {
    for (int i = 0; i < drivers.size(); i++)
    {
      System.out.println();
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      drivers.get(i).printInfo();
    }
  }
 
  // Print Information (printInfo()) about all current service requests
  public void listAllServiceRequests()
  {
    for (int i = 0; i < serviceRequests.length; i ++) {
      System.out.println("\nZONE " + i);
      System.out.println("======");
      int count = 1; // used to make sure requests are being numbered properly 
      for (TMUberService current_request: serviceRequests[i]) {
        System.out.print("\n" + count + ". ------------------------------------------------------------");
        current_request.printInfo();
        System.out.println();
        count ++;
      }
    }
  }

  // Add a new user to the system
  public void registerNewUser(String name, String address, double wallet)
  {
    // Check to ensure name is valid
    if (name == null || name.equals(""))
    {
      throw new IllegalInputException("Invalid User Name");
    }
    // Check to ensure that the wallet is valid 
    if (wallet < 0) {
      throw new IllegalInputException("Invalid Wallet Amount");
    }

    // Check to ensure address is valid 
    if (!CityMap.validAddress(address))
    {
      throw new IllegalInputException("Invalid User Address");
    }

    // Check for duplicate user
    String user_id = generateUserAccountId();
    User user = new User(user_id,name, address, wallet);

    // Check for existing user 
    if (userExists(user))
    {
      throw new UserExistsException("User Already Exists");
    }
    // Adding user in map with key value pair 
    users.put(user_id,user); 
  }

  // Add a new driver to the system
  public void registerNewDriver(String name, String carModel, String carLicencePlate,String address, int zone)
  {
    // Check to ensure name is valid
    if (name == null || name.equals(""))
    {
      throw new IllegalInputException("Invalid Driver Name");
    }
    // Check to ensure car models is valid
    if (carModel == null || carModel.equals(""))
    {
      throw new IllegalInputException("Invalid Car Model");
    }

    // Check to ensure car licence plate is valid
    if (carLicencePlate == null || carLicencePlate.equals(""))
    {
      throw new IllegalInputException("Invalid Car License Plate");
    }

    // Checks for valid address 
    if (!CityMap.validAddress(address)) { 
      throw new IllegalInputException("Invalid Driver Address");
    }

    // Check for duplicate driver. If not a duplicate, add the driver to the drivers list
    Driver driver = new Driver(generateDriverId(), name, carModel, carLicencePlate,address,zone);

    // Check for existing driver 
    if (driverExists(driver))
    {
      throw new DriverExistsException("Driver Already Exists in System");
    }

    drivers.add(driver);
  }

  // Request a ride. User wallet will be reduced when drop off happens
  public void requestRide(String accountId, String from, String to)
  {
    // Initial input checks 
    if (accountId.equals("") || accountId == null) {
      throw new IllegalInputException("Invalid Account Id");
    }

    // Check valid user account
    User user = getUser(accountId);
    // Checks if user is not found 
    if (user == null)
    {
      throw new UserAccountNotFoundException("User Account Not Found");
    }

    // Check for a valid from address 
    if (!CityMap.validAddress(from))
    {
      throw new IllegalInputException("Invalid From Address");
    }
    // Check for a valid to address 
    if (!CityMap.validAddress(to))
    {
      throw new IllegalInputException("Invalid To Address");
    }

    // Get the distance for this ride
    int distance = CityMap.getDistance(from, to);
    
    if (!(distance > 1))
    {
      throw new InsufficientTravelDistanceException("Insufficient Travel Distance");
    }

    double cost = getRideCost(distance);
    // Checks if user doesn't have enough money 
    if (user.getWallet() < cost)
    {
      throw new InsufficientFundsException("Insufficient Funds");
    }
    
    // Create the request
    TMUberRide req = new TMUberRide(from, to, user, distance, cost);
    
    // Check if existing ride request for user 
    if (existingRequest(req))
    {
      throw new ExistingRideRequestException("User Already Has Ride Request");
    }

    int city_zone = CityMap.getCityZone(from);
    serviceRequests[city_zone].add(req); // add request to correct queue in serviceRequests based on zone 
    user.addRide();
  }

  // Request a food delivery. User wallet will be reduced when drop off happens
  public void requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId)
  {
    // Initial Input Checks 
    if (accountId.equals("") || accountId == null) {
      throw new IllegalInputException("Invalid Account Id");
    }

    // Check for valid user account
    User user = getUser(accountId);
    // Check for user account not found 
    if (user == null)
    {
      throw new UserAccountNotFoundException("User Account Not Found");
    }

    // Check for valid from address
    if (!CityMap.validAddress(from))
    {
      throw new IllegalInputException("Invalid From Address");
    }
    // Check for valid to address
    if (!CityMap.validAddress(to))
    {
      throw new IllegalInputException("Invalid To Address");
    }

    // Check for valid restaurant name
    if (restaurant.equals("") || restaurant == null) {
      throw new IllegalInputException("Invalid Restaurant");
    }
    // Check for valid order Id
    if (foodOrderId.equals("") || foodOrderId == null) {
      throw new IllegalInputException("Invalid Food Order Id");
    }

    int distance = CityMap.getDistance(from, to);    
    if (distance <= 0)
    {
      throw new InsufficientTravelDistanceException("Insufficient Travel Distance");
    }
    // Check if user has enough money in wallet for this delivery
    double cost = getDeliveryCost(distance);
    if (user.getWallet() < cost)
    {
      throw new InsufficientFundsException("Insufficient Funds");
    }
    
    TMUberDelivery delivery = new TMUberDelivery(from, to, user, distance, cost, restaurant, foodOrderId); 
    // Check if user has existing delivery request with this restaurant and food order #
    if (existingRequest(delivery))
    {
      throw new ExistingDeliveryRequestException("User Already Has Delivery Request at Restaurant with this Food Order");
    }

    int city_zone = CityMap.getCityZone(from);
    serviceRequests[city_zone].add(delivery); // add request to appropriate queue in service requests based on zone
    user.addDelivery();
  }

  // Cancel an existing service request. 
  // parameter request is the index in the serviceRequests array list
  public void cancelServiceRequest(int request, int zone)
  {
    // Check if valid request #
    if (zone > 3 || zone < 0) {
      throw new IllegalInputException("Invalid Zone Number: " + zone);
    }

    Queue<TMUberService> q = serviceRequests[zone];

    // Check for valid request number 
    if (request < 1 || request > q.size())
    {
      throw new IllegalInputException("Invalid Request # " + request + " for Zone " + zone); 
    }

    Iterator<TMUberService> iterator = q.iterator();
    TMUberService service_to_cancel = null;

    int start = 1;
    // use iterator and iterate until service to remove is found based on request number, remove it using iterator.remove();
    while (iterator.hasNext() && start <= request) {
      TMUberService next_service = iterator.next();
      if (start == request) {
        service_to_cancel = next_service;
        iterator.remove();
        break;
      }
      start += 1;
    }

    // Checks if service request was not found 
    if (service_to_cancel == null) {
      throw new InvalidRequestNumberException("Could not find request number: " + request + " in Zone " + zone);
    }

    System.out.println("Succesfully cancelled request " + request + " in Zone " + zone);
  }

  //pickup method 
  public void pickup(String driverId) {
    // Invalid driver id check 
    if (driverId.equals("") || driverId == null) {
      throw new IllegalInputException("Invalid Driver Id");
    }

    Driver required_driver = null;
    for (int i = 0; i < drivers.size(); i++) {
      Driver current_driver = drivers.get(i);
      if (current_driver.getId().equals(driverId)) {
        required_driver = current_driver;
      }
    }

    // driver id not found after iterating through drivers 
    if (required_driver == null) {
      throw new DriverNotFoundException("Driver Not Found");
    }

    String current_address = required_driver.getAddress();
    int driver_zone = CityMap.getCityZone(current_address);

    // Checking if driver zone is valid 
    if (driver_zone >= serviceRequests.length || driver_zone < 0) {
      throw new InvalidZoneException("Out of Zone");
    }
    Queue<TMUberService> q = serviceRequests[driver_zone];

    // testing if queue is empty, no service requests found 
    if (q.isEmpty()) {
      throw new NoServiceRequestFoundException("No Service Request in Zone " + driver_zone);
    }

    // Check if that driver is already busy with another request 
    if (required_driver.getStatus() != Driver.Status.AVAILABLE) {
      throw new NoAvailableDriverException("This Driver Is In the Middle of Another Request");
    }

    TMUberService tmuservice = q.remove();
    required_driver.setService(tmuservice); // setting service found to driver 
    required_driver.setStatus(Driver.Status.DRIVING);
    required_driver.setAddress(tmuservice.getFrom());
    System.out.println("Driver " + required_driver.getId() + " Picking up in Zone: " + driver_zone);
  }
  
  // Drop off a ride or a delivery. This completes a service
  public void dropOff(String driverId)
  {
    Driver required_driver = null;
    for (int i = 0; i < drivers.size(); i ++) {
      Driver current = drivers.get(i);
      if (current.getId().equals(driverId)) {
        required_driver = current;
      }
    }

    // Checking for invalid driver id 
    if (driverId.equals("") || driverId == null) {
      throw new IllegalInputException("Invalid Driver Id");
    }

    // Checking if driver id is not found 
    if (required_driver == null) {
      throw new DriverNotFoundException("Driver Not Found");
    }

    TMUberService service = required_driver.getService();

    // Checking whether service if found or not 
    if (service == null) {
      throw new NoAvailableDriverException("No Active Service for Driver " + driverId);
    }

    // Total revenue, paying driver, deducing user wallet 
    totalRevenue += service.getCost();
    required_driver.pay(service.getCost() * PAYRATE);
    totalRevenue -= service.getCost() * PAYRATE;
    required_driver.setStatus(Driver.Status.AVAILABLE);
    required_driver.setAddress(service.getTo());
    User user = service.getUser();
    user.setAddress(service.getTo());
    user.payForService(service.getCost());

    // Set driver address to new address, get new zone 
    required_driver.setAddress(service.getTo());
    required_driver.setZone(CityMap.getCityZone(service.getTo()));
    required_driver.setService(null);
    System.out.println("Driver " + required_driver.getId() + " Dropping Off");
  }

  public void driveTo(String driverId, String address) {
    Driver required_d = null;
    for (int i = 0; i < drivers.size(); i ++) {
      if (drivers.get(i).getId().equals(driverId)) {
        required_d = drivers.get(i);
      }
    }
    // Checking for invalid driver id 
    if (driverId.equals("") || driverId == null) {
      throw new IllegalInputException("Invalid Driver Id");
    }

    // Checking if driver id not found 
    if (required_d == null) {
      throw new DriverNotFoundException("Driver with ID " + driverId + " not found");
    }

    // Checking if driver is not available
    if (required_d.getStatus() != Driver.Status.AVAILABLE) {
      throw new NoAvailableDriverException("Driver Is Not Avilable");
    }

    // Checking for invalid address  
    if (!CityMap.validAddress(address)){
      throw new IllegalInputException("Invalid Address");
    }
    
    // Setting driver address to new address, new zone 
    required_d.setAddress(address);
    required_d.setZone(CityMap.getCityZone(address));
    System.out.println("Driver " + required_d.getId() + " now in Zone " + CityMap.getCityZone(address));
    }


  // Sort users by name
  public void sortByUserName()
  {
    ArrayList<User> userList = new ArrayList<User>(users.values());
    Collections.sort(userList, new NameComparator());
    for (int i = 0; i < userList.size(); i ++) {
      System.out.println();
      userList.get(i).printInfo();
    }
  }
  
  // Comparator class for sortByUserName method 
  private class NameComparator implements Comparator<User>
  {
    public int compare(User a, User b)
    {
      return a.getName().compareTo(b.getName());
    }
  }

  // Comparator class for listAllUsers() method 
  private class IDComparator implements Comparator<User> {
    public int compare(User a, User b) {
      return Integer.compare(Integer.parseInt(a.getAccountId()),Integer.parseInt(b.getAccountId()));
    }
  }

  // Sort users by number amount in wallet
  public void sortByWallet()
  {
    ArrayList<User> userList = new ArrayList<User>(users.values());
    Collections.sort(userList, new UserWalletComparator());
    for (int i = 0; i < userList.size(); i ++) {
      System.out.println();
      userList.get(i).printInfo();
    }
  }
  
  // Comparator class for sortByWallet method 
  private class UserWalletComparator implements Comparator<User>
  {
    public int compare(User a, User b)
    {
      if (a.getWallet() > b.getWallet()) return 1;
      if (a.getWallet() < b.getWallet()) return -1; 
      return 0;
    }
  }

  // Sort trips (rides or deliveries) by distance
  // class TMUberService must implement Comparable
  public void sortByDistance()
  {
    ArrayList<TMUberService> requests = new ArrayList<>();

    for (Queue<TMUberService> queue: serviceRequests) {
      requests.addAll(queue);
    }
    Collections.sort(requests, new DistanceComparator());
    listAllServiceRequests();
  }

  // Comparator class for sortByDistance method 
  private class DistanceComparator implements Comparator<TMUberService> {
    public int compare(TMUberService a, TMUberService b) {
      return Integer.compare(a.getDistance(),b.getDistance());
    }
  }

  // Set users before loading users 
  public void setUsers(ArrayList<User> userList) {
    for (int i = 0; i < userList.size(); i ++) {
      User currentUser = userList.get(i);
      users.put(currentUser.getAccountId(),currentUser);
    }
  }

  // Set drivers before loading drivers 
  public void setDrivers(ArrayList<Driver> driverList) {
    this.drivers = driverList;
  }
}


// Exceptions 
class UserExistsException extends RuntimeException {
  public UserExistsException() {}
  public UserExistsException(String message) {
    super(message);
  }
}

class DriverExistsException extends RuntimeException {
  public DriverExistsException() {}
  public DriverExistsException(String message) {
    super(message);
  }
}

class UserAccountNotFoundException extends RuntimeException {
  public UserAccountNotFoundException() {}
  public UserAccountNotFoundException(String message) {
    super(message);
  }
}

class InsufficientTravelDistanceException extends RuntimeException {
  public InsufficientTravelDistanceException(){}
  public InsufficientTravelDistanceException(String message) {
    super(message);
  }
}

class InsufficientFundsException extends RuntimeException {
  public InsufficientFundsException(){}
  public InsufficientFundsException(String message) {
    super(message);
  }
}

class NoAvailableDriverException extends RuntimeException {
  public NoAvailableDriverException(){}
  public NoAvailableDriverException(String message) {
    super(message);
  }
}

class ExistingRideRequestException extends RuntimeException {
  public ExistingRideRequestException(){} 
  public ExistingRideRequestException(String message) {
    super(message);
  }
}

class ExistingDeliveryRequestException extends RuntimeException {
  public ExistingDeliveryRequestException(){}
  public ExistingDeliveryRequestException(String message) {
    super(message);
  }
}

class InvalidRequestNumberException extends RuntimeException {
  public InvalidRequestNumberException(){}
  public InvalidRequestNumberException(String message) {
    super(message);
  }
}

class DriverNotFoundException extends RuntimeException {
  public DriverNotFoundException(){}
  public DriverNotFoundException(String message) {
    super(message);
  }
}

class InvalidZoneException extends RuntimeException {
  public InvalidZoneException(){}
  public InvalidZoneException(String message) {
    super(message);
  }
}

class NoServiceRequestFoundException extends RuntimeException {
  public NoServiceRequestFoundException(){} 
  public NoServiceRequestFoundException(String message) {
    super(message);
  }
}

class IllegalInputException extends RuntimeException {
  public IllegalInputException(){} 
  public IllegalInputException(String message) {
    super(message);
  }
}



