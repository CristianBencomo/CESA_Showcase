import obd
import time
import pyrebase
from datetime import datetime

print ('Program is starting...')

#setting up database connection
apiKey = "ENTER API KEY"
config = {
    "apiKey": apiKey,
    "authDomain": "testfirebaseapp-18cbd.firebaseapp.com",
    "databaseURL": "https://testfirebaseapp-18cbd-default-rtdb.firebaseio.com/",
    "storageBucket": "testfirebaseapp-18cbd.appspot.com"
}

#connect to firebase and obd
firebase = pyrebase.initialize_app(config)
database = firebase.database()
connection = obd.OBD() 

# declare obd data to pull
speed = obd.commands.SPEED 
rpm = obd.commands.RPM
throttle = obd.commands.THROTTLE_POS
cool_tmp = obd.commands.COOLANT_TEMP
fuel = obd.commands.FUEL_LEVEL
amb_tmp = obd.commands.AMBIANT_AIR_TEMP
distance = obd.commands.DISTANCE_SINCE_DTC_CLEAR

# while forever loop
def loop():
	while(True):
        #Read obd responses
		res_speed = str(connection.query(speed)) # send the command, and parse the response
		res_rpm = str(connection.query(rpm))
		res_throttle = str(connection.query(throttle))
		res_cooltemp = str(connection.query(cool_tmp))
		res_fuel = str(connection.query(fuel))
		res_ambtmp = str(connection.query(amb_tmp))
		res_distance = str(connection.query(distance))

		#Send data to firebase
		currentTime = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
		data = {"time" : currentTime, "speed" : res_speed, "rpm" : res_rpm, 
        "throttle" : res_throttle, "coolant temp" : res_cooltemp, 
        "ambient temp" : res_ambtmp, "fuel" : res_fuel, "distance" : res_distance}

		print("The time is : ", currentTime)
		print("Sending data to firebase database...\n")
		database.child(deviceID).child("01-setTest").set(data)
		database.child(deviceID).child("01-pushTest").push(data)
		time.sleep(1)

#main function
if __name__ == '__main__':
	deviceID = "RPI_01"
	print ('Before loop')
	loop()


