import smbus
import math
import serial
import time
import string
import pynmea2
import pyrebase
from datetime import datetime

#setting up database connection
config = {
	"apiKey": "IU4kftDPotea1YxDNDDi0f7FtUmwwglHyfk5Ol91",
	"authDomain": "testfirebaseapp-18cbd.firebaseapp.com",
	"databaseURL": "https://testfirebaseapp-18cbd-default-rtdb.firebaseio.com/",
	"storageBucket": "testfirebaseapp-18cbd.appspot.com"
}

#connect to firebase and obd
firebase = pyrebase.initialize_app(config)
database = firebase.database()
deviceID = "RPI_01"

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

while(1):
	#Gyro
	print "Gyroscope"
	gyroscope_xout = read_word_2c(0x43)
	gyroscope_yout = read_word_2c(0x45)
	gyroscope_zout = read_word_2c(0x47)

	print "gyroscope_xout: ", ("%5d" % gyroscope_xout), " scaled: ", (gyroscope_xout / 131)
	print "gyroscope_yout: ", ("%5d" % gyroscope_yout), " scaled: ", (gyroscope_yout / 131)
	print "gyroscope_zout: ", ("%5d" % gyroscope_zout), " scaled: ", (gyroscope_zout / 131)

	print
	print "accelerometer"
	print "---------------------"

	accelerometer_xout = read_word_2c(0x3b)
	accelerometer_yout = read_word_2c(0x3d)
	accelerometer_zout = read_word_2c(0x3f)

	accelerometer_xout_scaled = accelerometer_xout / 16384.0
	accelerometer_yout_scaled = accelerometer_yout / 16384.0
	accelerometer_zout_scaled = accelerometer_zout / 16384.0

	print "accelerometer_xout: ", ("%6d" % accelerometer_xout), " scaled: ", accelerometer_xout_scaled
	print "accelerometer_yout: ", ("%6d" % accelerometer_yout), " scaled: ", accelerometer_yout_scaled
	print "accelerometer_zout: ", ("%6d" % accelerometer_zout), " scaled: ", accelerometer_zout_scaled

	print "X Rotation: " , get_x_rotation(accelerometer_xout_scaled, accelerometer_yout_scaled, accelerometer_zout_scaled)
	print "Y Rotation: " , get_y_rotation(accelerometer_xout_scaled, accelerometer_yout_scaled, accelerometer_zout_scaled)

	#firebase variables
	Ax = str(accelerometer_xout_scaled)
	Ay = str(accelerometer_yout_scaled)
	Az = str(accelerometer_zout_scaled)
	Gx = str(gyroscope_xout / 131)
	Gy = str(gyroscope_xout / 131)
	Gz = str(gyroscope_xout / 131)

	#GPS
	lon_str = ""
	lat_str = ""

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
		lon_str = str(lng)
		lat_str = str(lat)

	#firebase variables
	#lon_str = str(lng)
	#lat_str = str(lat)
	

	#Send data to firebase
	currentTime = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
	data = {"time" : currentTime, "Acc x" : Ax, "Acc Y" : Ay, "Acc Z" : Az, "Gx" : Gx, "Gy" : Gy, "Gz" : Gz,
	"longitude" : lon_str, "latitude" : lat_str}

	print("The time is : ", currentTime)
	print("Sending data to firebase database...\n")
	database.child(deviceID).child("01-setTest").set(data)
	database.child(deviceID).child("01-pushTest").push(data)
	time.sleep(1)