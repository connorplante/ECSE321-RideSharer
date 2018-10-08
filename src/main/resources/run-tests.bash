#/bin/bash

echo "This is a script to run the RiderSharer app from Group16"
sleep 2s
echo "Please ensure before continuing that you have a user in you mysql database called huser with password hpassword"
echo "Do you have this user in your mysql database? y or n"
read answer

if [ ${answer} == "n" ]; 
    then 
        echo "Please create that profile before running this script again"
        exit; 
fi

while [[ ${answer} != "y" ]]; do
    echo "Do you have this user in your mysql database? y or n"
    read ${answer}
done

echo "Have you run dbo.sql? y or n"
read answer2

if [ ${answer2} == "n" ]; 
    then 
        echo "Please run dbo.sql before running this script"
        exit; 
fi

while [[ ${answer2} != "y" ]]; do
    echo "Have you run dbo.sql? y or n"
    read ${answer2}
done

echo "Is the application running using the command mvn spring-boot:run? y or n"
read answer3

if [ ${answer3} == "n" ]; 
    then 
        echo "Please have the applicaiton running before running this script"
        exit; 
fi

while [[ ${answer3} != "y" ]]; do
    echo "Is the application running using the command mvn spring-boot:run? y or n"
    read ${answer3}
done


echo "The following runs sample inputs for all of the methods contained within our ride-sharing system"
echo "The script allows one to see the common functionality of the app, and follows the typical progression of how a user would use this applicaition"
sleep 3s
echo "The following details will help with the understanding of our system:"
echo "Trips posses a Status attribute - in the database, the following values represent Status:"
echo -e "0 -> Scheduled \n1 -> Cancelled \n2 -> Completed"
sleep 2s
echo "Cars and users also have a Status attribute - in the database, the following values represent Status:"
echo -e "0 -> Inactive \n1 -> Active"
sleep 2s

echo "Now creating passengers, the tabs will all remain open after the commands are run"
echo "The create methods for all users will display all of the user's information"
sleep 1.5s

open "http://localhost:8080/User/createPassenger?username=P_USERNAME&password=P_PASSWORD1&firstName=FIRSTNAME1&lastName=LASTNAME1&email=EMAIL1&phoneNumber=PHONENUMBER1" 
echo "Passenger 1 has userID: 1"
sleep 3s

open "http://localhost:8080/User/createPassenger?username=P_USERNAME2&password=P_PASSWORD2&firstName=FIRSTNAME2&lastName=LASTNAME2&email=EMAIL2&phoneNumber=PHONENUMBER2"
echo "Passenger 2 has userID: 2"
sleep 3s

open "http://localhost:8080/User/createPassenger?username=P_USERNAME3&password=P_PASSWORD3&firstName=FIRSTNAME3&lastName=LASTNAME3&email=EMAIL3&phoneNumber=PHONENUMBER3"
echo "Passenger 3 has userID: 3"
sleep 3s

open "http://localhost:8080/User/createPassenger?username=P_USERNAME4&password=P_PASSWORD4&firstName=FIRSTNAME4&lastName=LASTNAME4&email=EMAIL4&phoneNumber=PHONENUMBER4"
echo "Passenger 4 has userID: 4"
sleep 3s

open "http://localhost:8080/User/createPassenger?username=P_USERNAME5&password=P_PASSWORD5&firstName=FIRSTNAME5&lastName=LASTNAME5&email=EMAIL5&phoneNumber=PHONENUMBER5"
echo "Passenger 5 has userID: 5"
sleep 3s

echo "Now creating drivers"
sleep 0.5s

open "http://localhost:8080/User/createDriver?username=D_USERNAME1&password=D_PASSWORD1&firstName=FIRSTNAME1&lastName=LASTNAME1&email=EMAIL1&phoneNumber=PHONENUMBER1"
echo "Driver 1 has userID: 6"
sleep 3s

open "http://localhost:8080/User/createDriver?username=D_USERNAME2&password=D_PASSWORD2&firstName=FIRSTNAME2&lastName=LASTNAME2&email=EMAIL2&phoneNumber=PHONENUMBER2"
echo "Driver 2 has userID: 7"
sleep 3s

open "http://localhost:8080/User/createDriver?username=D_USERNAME3&password=D_PASSWORD3&firstName=FIRSTNAME3&lastName=LASTNAME3&email=EMAIL3&phoneNumber=PHONENUMBER3"
echo "Driver 3 has userID: 8"
sleep 3s

echo "Now creating admin"
sleep 0.5s

open "http://localhost:8080/User/createAdmin?username=A_USERNAME3&password=A_PASSWORD3&firstName=FIRSTNAME3&lastName=LASTNAME3&email=EMAIL3&phoneNumber=PHONENUMBER3"
echo "Admin 1 has userID: 9"
sleep 3s

echo "Now updating passenger1 to have FIRSTNAME6"
sleep 0.5s

open "http://localhost:8080/User/updateUserInfo?username=P_USERNAME&firstName=FIRSTNAME6&lastName=LASTNAME1&email=EMAIL1&phoneNumber=PHONENUMBER1" 
echo "Passenger 1 now has first name: FIRSTNAME6"
sleep 3s

echo "Now updating admin1 to have PHONENUMBER4 and EMAIL4"
sleep 0.5s

open "http://localhost:8080/User/updateUserInfo?username=A_USERNAME3&password=A_PASSWORD3&firstName=FIRSTNAME3&lastName=LASTNAME3&email=EMAIL4&phoneNumber=PHONENUMBER4"
echo "Admin 1 now has PHONENUMBER4 and EMAIL4"
sleep 3s

echo "Now resetting the password for Passenger 5"
sleep 0.5s

open "http://localhost:8080/User/resetPassword?username=P_USERNAME5&currentPassword=P_PASSWORD5&newPassword=P_USERNAME6"
echo "Passenger 5 now has password: P_PASSWORD6"
sleep 3s

echo "Now creating cars"
sleep 0.5s

open "http://localhost:8080/Car/createCar?make=MAKE&model=MODEL&year=2010&numSeats=4&licencePlate=LICENCEPLATE&username=D_USERNAME1"
echo "Driver 1 now has car with carID: 1"
sleep 3s

open "http://localhost:8080/Car/createCar?make=MAKE&model=MODEL&year=2010&numSeats=4&licencePlate=LICENCEPLATE&username=D_USERNAME1"
echo "Driver 1 now has car with carID: 2"
sleep 3s

open "http://localhost:8080/Car/createCar?make=MAKE&model=MODEL&year=2010&numSeats=4&licencePlate=LICENCEPLATE&username=D_USERNAME2"
echo "Driver 2 now has car with carID: 3"
sleep 3s

open "http://localhost:8080/Car/createCar?make=MAKE&model=MODEL&year=2010&numSeats=4&licencePlate=LICENCEPLATE&username=D_USERNAME3"
echo "Driver 3 now has car with carID: 4"
sleep 3s

echo "Now updating car"

open "http://localhost:8080/Car/updateCar?carID=2&make=MAKE2&model=MODEL&year=2010&numSeats=4&licencePlate=LICENCEPLATE"
echo "Car with ID 2 now has model: MODEL2"
sleep 3s

echo "Now creating trips"
sleep 0.5s

open "http://localhost:8080/Trip/createTrip?start=Ajax&end=Montreal&date=2018-10-30&time=9&username=D_USERNAME1&carID=1&numSeats=4&stops=Ajax&stops=Kingston&stops=Ottawa&stops=Montreal&prices=15&prices=20&prices=40"
echo "Driver 1 creates a trip with car 1 with tripID: 1"
sleep 3s

open "http://localhost:8080/Trip/createTrip?start=Burlington&end=Ottawa&date=2018-10-15&&time=9&username=D_USERNAME2&carID=3&numSeats=2&stops=Burlington&stops=Ottawa&prices=20"
echo "Driver 2 creates a trip with car 2 with tripID: 2"
sleep 3s

open "http://localhost:8080/Trip/createTrip?start=Toronto&end=Ottawa&date=2018-11-30&&time=9&username=D_USERNAME3&carID=4&numSeats=6&stops=Toronto&stops=Belleville&stops=Ottawa&prices=20&prices=12"
echo "Driver 3 creates a trip with car 4 with tripID: 3"
sleep 3s

open "http://localhost:8080/Trip/createTrip?start=Montreal&end=QuebecCity&date=2018-12-01&time=9&username=D_USERNAME1&carID=2&numSeats=4&stops=Montreal&stops=Sherbrooke&stops=QuebecCity&prices=10&prices=5"
echo "Driver 1 creates a trip with car 2 with tripID: 4"
sleep 3s

echo "Now finding trip by start and end destination, and then booking them for a passenger"
echo "This returns a list of tripIDs that correspond to the search"
sleep 1s

echo "Passenger 1 looks for a trip stating in Kingston and ending in Ottawa"
open "http://localhost:8080/Trip/findTrip?start=Kingston&end=Ottawa"
sleep 3s

open "http://localhost:8080/Passenger/confirmBook?tripID=1&username=P_USERNAME1&pointA=Kingston&pointB=Montreal"
echo "Passenger 1 is now booked on the legs for Kingston to Ottawa and Ottawa to Montreal of trip with tripID: 1"
sleep 3s

echo "Passenger 2 looks for a trip starting in Ajax and ending in Ottawa"
open "http://localhost:8080/Trip/findTrip?start=Ajax&end=Ottawa"
sleep 3s

open "http://localhost:8080/Passenger/confirmBook?tripID=1&username=P_USERNAME2&pointA=Ajax&pointB=Ottawa"
echo "Passenger 2 is now booked on the legs for Ajax to Kingston and Kingston to Ottawa on trip with tripID: 1"
sleep 3s

echo "Passenger 3 looks for trip stating in Burlington and ending in Ottawa"
open "http://localhost:8080/Trip/findTrip?start=Burlington&end=Ottawa"
sleep 3s

open "http://localhost:8080/Passenger/confirmBook?tripID=2&username=P_USERNAME3&pointA=Burlington&pointB=Ottawa"
echo "Passenger 3 is now booked on the leg for Burlington to Ottawa of trip with tripID: 2"
sleep 3s

echo "Passenger 4 looks for a trip starting in Toronto and ending in Ottawa"
open "http://localhost:8080/Trip/findTrip?start=Toronto&end=Ottawa"
sleep 3s

open "http://localhost:8080/Passenger/confirmBook?tripID=3&username=P_USERNAME4&pointA=Toronto&pointB=Ottawa"
echo "Passenger 4 is now booked on the legs for Toronto to Belleville and Belleville to Ottawa of trip with tripID: 3"
sleep 3s

echo "Passenger 5 looks for a trip starting in Toronto and ending in Belleville"
open "http://localhost:8080/Trip/findTrip?start=Toronto&end=Belleville"
sleep 3s

open "http://localhost:8080/Passenger/confirmBook?tripID=3&username=P_USERNAME5&pointA=Toronto&pointB=Belleville"
echo "Passenger 5 is now booked on the leg from Toronto to Belleville on the trip with tripID: 3"
sleep 3s

echo "Passenger 1 looks for a trip starting in Montreal and ending in Sherbrooke"
open "http://localhost:8080/Trip/findTrip?start=Montreal&end=Sherbrooke"
sleep 3s

echo "Passenger 1 looks if trip with tripID: 4 is less than their maximum price of 15$"
open "http://localhost:8080/Trip/filterPrice?tripID=4&start=Montreal&end=Sherbrooke&price=15"
sleep 3s

open "http://localhost:8080/Passenger/confirmBook?tripID=3&username=P_USERNAME1&pointA=Montreal&pointB=Sherbrooke"
echo "Passenger 1 is now booked on the leg from Montreal to Sherbrooke for the trip with tripID: 4"
sleep 3s

echo "Now cancelling a trip"
sleep 1s

open "http://localhost:8080/Trip/cancelTrip?tripID=4"
echo "The trip with tripID: 4 now has status: Cancelled"
sleep 3s

echo "Now completing a trip"
sleep 1s

open "http://localhost:8080/Trip/completeTrip?tripID=2"
echo "The trip with tripID: 2 now has status: Completed"
sleep 3s

echo "Now rating passengers"

open "http://localhost:8080/User/updateRating?username=P_USERNAME1&rating=3"
echo "The new rating by Driver 1 is computed for Passenger 1"
sleep 3s

open "http://localhost:8080/User/updateRating?username=P_USERNAME2&rating=4"
echo "The new rating by Driver 1 is computed for Passenger 2"
sleep 3s

open "http://localhost:8080/User/updateRating?username=P_USERNAME3&rating=4"
echo "The new rating by Driver 2 is computed for Passenger 3"
sleep 3s

open "http://localhost:8080/User/updateRating?username=P_USERNAME4&rating=2"
echo "The new rating by Driver 3 is computed for Passenger 4"
sleep 3s

open "http://localhost:8080/User/updateRating?username=P_USERNAME5&rating=5"
echo "The new rating by Driver 3 is computed for Passenger 5"
sleep 3s

echo "Now rating drivers"

open "http://localhost:8080/User/updateRating?username=D_USERNAME1&rating=5"
echo "The new rating by Passenger 1 is computed for Driver 1"
sleep 3s

open "http://localhost:8080/User/updateRating?username=D_USERNAME1&rating=4"
echo "The new rating by Passenger 2 is computed for Driver 1"
sleep 3s

open "http://localhost:8080/User/updateRating?username=D_USERNAME2&rating=3"
echo "The new rating by Passenger 3 is computed for Driver 2"
sleep 3s

open "http://localhost:8080/User/updateRating?username=D_USERNAME3&rating=2"
echo "The new rating by Passenger 5 is computed for Driver 3"
sleep 3s

echo "Now removing cars"
sleep 1s

open "http://localhost:8080/Car/removeCar?carID=1"
echo "Car with carId:1 is removed (status is set to inactive)"
sleep 3s

echo "Now removing users"
sleep 1s

open "http://localhost:8080/User/removeUser?username=P_USERNAME3"
echo "Passenger 3 is removed (status set to inactive)"
sleep 3s

open "http://localhost:8080/User/removeUser?username=D_USERNAME2"
echo "Driver 2 is removed (status set to inactive)"
