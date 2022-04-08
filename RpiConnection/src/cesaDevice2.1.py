import smbus			#import SMBus module of I2Ct
from gps import *
import obd
import time
import pyrebase
from pyfcm import FCMNotification #new
from datetime import datetime
import RPi.GPIO as GPIO

print ('Program is starting...')

#setting up database connection
config = {
    "apiKey": "IU4kftDPotea1YxDNDDi0f7FtUmwwglHyfk5Ol91",
    "authDomain": "testfirebaseapp-18cbd.firebaseapp.com",
    "databaseURL": "https://testfirebaseapp-18cbd-default-rtdb.firebaseio.com/",
    "storageBucket": "testfirebaseapp-18cbd.appspot.com"
}

#Getting FCM key
push_service = FCMNotification(api_key="AAAASHtg8jo:APA91bF1iuoDe0X1fp7hf_3R7hO3AiuZQzbJvKZSdbZY4ENgMegYHZ-yizLIcR51t5daA6NbWnrVIGbd_kc6zlcMVb1JIJVX73bv6EKdhP5DT3KTfwfr0YoI8SYeVL8l8eB_8RQWxrwV") 
FCMdb = database.child(deviceID)..child("DeviceInfo").child("FCM").get()
FCM = FCMdb.val()

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

#Notification variables
fuelLow = 0


#some MPU6050 Registers and their Address
PWR_MGMT_1   = 0x6B
SMPLRT_DIV   = 0x19
CONFIG       = 0x1A
GYRO_CONFIG  = 0x1B
INT_ENABLE   = 0x38
ACCEL_XOUT_H = 0x3B
ACCEL_YOUT_H = 0x3D
ACCEL_ZOUT_H = 0x3F
GYRO_XOUT_H  = 0x43
GYRO_YOUT_H  = 0x45
GYRO_ZOUT_H  = 0

def MPU_Init():
	print ("test int")
	#write to sample rate register
	bus.write_byte_data(Device_Address, SMPLRT_DIV, 7)

	#Write to power management register
	bus.write_byte_data(Device_Address, PWR_MGMT_1, 1)

	#Write to Configuration register
	bus.write_byte_data(Device_Address, CONFIG, 0)

	#Write to Gyro configuration register
	bus.write_byte_data(Device_Address, GYRO_CONFIG, 24)

	#Write to interrupt enable register
	bus.write_byte_data(Device_Address, INT_ENABLE, 1)

def read_raw_data(addr):
	#Accelero and Gyro value are 16-bit
	high = bus.read_byte_data(Device_Address, addr)
	low = bus.read_byte_data(Device_Address, addr+1)

	#concatenate higher and lower value
	value = ((high << 8) | low)

	#to get signed value from mpu6050
	if(value > 32768):
		value = value - 65536
	return value

gpsd = gps(mode=WATCH_ENABLE|WATCH_NEWSTYLE)

bus = smbus.SMBus(1) 	# or bus = smbus.SMBus(0) for older version boards
Device_Address = 0x68   # MPU6050 device address

MPU_Init()


# while forever loop
def loop():
	while(True):
		#Read Accelerometer raw value
		acc_x = read_raw_data(ACCEL_XOUT_H)
		acc_y = read_raw_data(ACCEL_YOUT_H)
		acc_z = read_raw_data(ACCEL_ZOUT_H)
		gyro_x = read_raw_data(GYRO_XOUT_H)
		gyro_y = read_raw_data(GYRO_YOUT_H)
		gyro_z = read_raw_data(GYRO_ZOUT_H)

		#Compute accelerometer values
		Ax = acc_x/16384.0
		Ay = acc_y/16384.0
		Az = acc_z/16384.0
		Gx = gyro_x/131.0
		Gy = gyro_y/131.0
		Gz = gyro_z/131.0
        
        #Read obd responses
		res_speed = str(connection.query(speed)) # send the command, and parse the response
		res_rpm = str(connection.query(rpm))
		res_throttle = str(connection.query(throttle))
		res_cooltemp = str(connection.query(cool_tmp))
		res_fuel = str(connection.query(fuel))
		res_ambtmp = str(connection.query(amb_tmp))
		res_distance = str(connection.query(distance))

		#Read GPS data
		nx = gpsd.next()
		if nx['class'] == 'TPV':
			latitude = getattr(nx,'lat', "Unknown")
			longitude = getattr(nx,'lon', "Unknown")
			lat_str = str(latitude)
			lon_str = str(longitude)


		#Notification and alerts code

        #Gyro
        fGx = float(Gx)
        fGy = float(Gy)
        fGz = float(Gz)
        fGthreshold = 1.5 #change this to modify all the gyro thresholds

		if(fGx > fGthreshold or fGy > fGthreshold or fGz > fGthreshold):
			registration_id = FCM
			message_title = "Shock Alert"
			message_body = "Higher than normal G forces detected!"
			result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)

		#fuel
		fFuel = float(res_fuel)
		fLowFuel = 20.0 
		tResetFuel = 25.0
		if(fuelLow and fFuel > tResetFuel ):
			fuelLow = 0

		if(!fuelLow and fFuel < fLowFuel):
			fuelLow = 1
			registration_id = FCM
			message_title = "Low Fuel"
			message_body = "Your fuel level is under 20% please refuel your car"
			result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)



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
		print("Closing Program")

#test comment


