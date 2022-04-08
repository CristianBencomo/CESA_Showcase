import smbus			#import SMBus module of I2Ct
import math
import serial
import obd
import time
import string
import pyrebase
import pynmea2
from datetime import datetime

print ('Program is setting up...')

#setting up database connection
config = {
	"apiKey": "IU4kftDPotea1YxDNDDi0f7FtUmwwglHyfk5Ol91",
	"authDomain": "testfirebaseapp-18cbd.firebaseapp.com",
	"databaseURL": "https://testfirebaseapp-18cbd-default-rtdb.firebaseio.com/",
	"storageBucket": "testfirebaseapp-18cbd.appspot.com"
}

#Notification variables
fuelLow = 0
shock = 0
coolantHot = 0

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

#gyroscope initialization
#Gyroscope
power_mgmt_1 = 0x6b
power_mgmt_2 = 0x6c

def read_byte(reg):
	return bus.read_byte_data(address, reg)

def read_word(reg):
	h = bus.read_byte_data(address, reg)
	l = bus.read_byte_data(address, reg+1)
	value = (h << 8) + l
	return value

def read_word_2c(reg):
	val = read_word(reg)
	if (val >= 0x8000):
		return -((65535 - val) + 1)
	else:
		return val

def dist(a,b):
	return math.sqrt((a*a)+(b*b))

def get_y_rotation(x,y,z):
	radians = math.atan2(x, dist(y,z))
	return -math.degrees(radians)

def get_x_rotation(x,y,z):
	radians = math.atan2(y, dist(x,z))
	return math.degrees(radians)

bus = smbus.SMBus(1) # bus = smbus.SMBus(0) fuer Revision 1
address = 0x68       # via i2cdetect

# Aktivieren, um das Modul ansprechen zu koennen
bus.write_byte_data(address, power_mgmt_1, 0)

# while forever loop
def loop():
	while(True):
		#GYRO
		gyroscope_xout = read_word_2c(0x43)
		gyroscope_yout = read_word_2c(0x45)
		gyroscope_zout = read_word_2c(0x47)
	
		accelerometer_xout = read_word_2c(0x3b)
		accelerometer_yout = read_word_2c(0x3d)
		accelerometer_zout = read_word_2c(0x3f)
	
		accelerometer_xout_scaled = accelerometer_xout / 16384.0
		accelerometer_yout_scaled = accelerometer_yout / 16384.0
		accelerometer_zout_scaled = accelerometer_zout / 16384.0
	
		#firebase gyro variables
		Ax = str(accelerometer_xout_scaled)
		Ay = str(accelerometer_yout_scaled)
		Az = str(accelerometer_zout_scaled)
		Gx = str(gyroscope_xout / 131)
		Gy = str(gyroscope_xout / 131)
		Gz = str(gyroscope_xout / 131)

		#GPS
		port="/dev/ttyAMA0"
		ser=serial.Serial(port, baudrate=9600, timeout=0.5)
		dataout = pynmea2.NMEAStreamReader()
		newdata=ser.readline()  
		
		if newdata[0:6] == "$GPRMC":
			newmsg=pynmea2.parse(newdata)
			lat=newmsg.latitude
			lng=newmsg.longitude
			gps = "Latitude=" + str(lat) + "and Longitude=" + str(lng)
			print(gps)

		#firebase GPS variables
		lon_str = str(lng)
		lat_str = str(lat)
		
		#Read obd responses
		res_speed = str(connection.query(speed)) # send the command, and parse the response
		res_rpm = str(connection.query(rpm))
		res_throttle = str(connection.query(throttle))
		res_cooltemp = str(connection.query(cool_tmp))
		res_fuel = str(connection.query(fuel))
		res_ambtmp = str(connection.query(amb_tmp))
		res_distance = str(connection.query(distance))

		#Notifications
		#Gyro
		fGx = float(Gx)
		fGy = float(Gy)
		fGz = float(Gz)
		fGthreshold = 2.0 #change this to modify all the gyro thresholds
		fGreset = 0.5

		if(fGx > fGthreshold or fGy > fGthreshold or fGz > fGthreshold) and shock != 1:
			registration_id = FCM
			message_title = "Shock Alert"
			message_body = "Higher than normal G forces detected!"
			result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)
			print("Setting alert in database...\n")
			alert = {message_title : message_body}
			database.child(deviceID).child("alerts").push(alert)
			shock = 1

		if(fGx < fGreset or fGy < fGreset or fGz < fGreset) and shock == 1:
			shock = 0
			database.child(deviceID).child("alerts").child("Shock Alert").delete()
			

		#fuel
		fFuel = float(res_fuel)
		fLowFuel = 20.0 # threshold to send notification
		tResetFuel = 25.0 #treshhold to reset flag
		if(fuelLow == 1 and fFuel > tResetFuel ):
			fuelLow = 0
			database.child(deviceID).child("alerts").child("Low Fuel").delete()

		if(fuelLow != 1 and fFuel < fLowFuel):
			fuelLow = 1
			registration_id = FCM
			message_title = "Low Fuel"
			message_body = "Your fuel level is under 20% please refuel your car"
			result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)
			print("Setting alert in database...\n")
			alert = {message_title : message_body}
			database.child(deviceID).child("alerts").push(alert)

		fCoolant = float(res_cooltemp)
		fHighTemp = 110.0
		fResetTemp = 103.0

		#coolant temp
		if(coolantHot != 1 and fCoolant > fHighTemp):
			coolantHot = 1
			registration_id = FCM
			message_title = "High Temperature"
			message_body = "Your coolant is too hot"
			result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)
			print("Setting alert in database...\n")
			alert = {message_title : message_body}
			database.child(deviceID).child("alerts").push(alert)

		if(coolantHot == 1 and fCoolant < fResetTemp):
			coolantHot = 0
			database.child(deviceID).child("alerts").child("High Temperature").delete()

		#engine codes
		#Testing purposes since test car does not currently have a check engine on
		code = "P0420"
		print("Setting Engine code in database...\n")
		codes = {"code" : code}
		database.child(deviceID).child("alerts").push(codes)

		#Send data to firebase
		currentTime = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
		data = {"time" : currentTime, "Acc x" : Ax, "Acc Y" : Ay, "Acc Z" : Az, "Gx" : Gx, "Gy" : Gy, "Gz" : Gz,
		"speed" : res_speed, "rpm" : res_rpm, "throttle" : res_throttle, "coolant temp" : res_cooltemp, 
		"ambient temp" : res_ambtmp, "fuel" : res_fuel, "distance" : res_distance, "longitude" : lon_str, "latitude" : lat_str}

		print("The time is : ", currentTime)
		print("Sending data to firebase database...\n")
		database.child(deviceID).child("01-setTest").set(data)
		database.child(deviceID).child("01-pushTest").push(data)
		time.sleep(1)

#while True:
if __name__ == '__main__':
	deviceID = "RPI_01"
	print ('Program is starting...')
	try:
		loop()
	except KeyboardInterrupt:
		GPIO.cleanup()
		print("Closing Program...")

#test comment


