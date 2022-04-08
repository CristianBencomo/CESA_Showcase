import smbus			#import SMBus module of I2Ct
import obd
import time
import pyrebase
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

		#Send data to firebase
		currentTime = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
		data = {"time" : currentTime, "Acc x" : Ax, "Acc Y" : Ay, "Acc Z" : Az, "Gx" : Gx, "Gy" : Gy, "Gz" : Gz,
        "speed" : res_speed, "rpm" : res_rpm, "throttle" : res_throttle, "coolant temp" : res_cooltemp, 
        "ambient temp" : res_ambtmp, "fuel" : res_fuel, "distance" : res_distance}

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

#test comment


