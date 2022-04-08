import time
import pyrebase
from datetime import datetime
import RPi.GPIO as GPIO

apiKey = "ENTER API KEY"
config = {
    "apiKey": apiKey,
    "authDomain": "testfirebaseapp-18cbd.firebaseapp.com",
    "databaseURL": "https://testfirebaseapp-18cbd-default-rtdb.firebaseio.com/",
    "storageBucket": "testfirebaseapp-18cbd.appspot.com"
}

firebase = pyrebase.initialize_app(config)
database = firebase.database()

trigPin = 16
echoPin = 18
MAX_DISTANCE = 220
timeOut = MAX_DISTANCE*60
 

def pulseIn(pin,level,timeOut): # obtain pulse time of a pin under timeOut
    t0 = time.time()
    while(GPIO.input(pin) != level):
        if((time.time() - t0) > timeOut*0.000001):
            return 0;
    t0 = time.time()
    while(GPIO.input(pin) == level):
        if((time.time() - t0) > timeOut*0.000001):
            return 0;
    pulseTime = (time.time() - t0)*1000000
    return pulseTime


def getSonar():     # get the measurement results of ultrasonic module,with unit: cm
    GPIO.output(trigPin,GPIO.HIGH)      
    time.sleep(0.00001)
    GPIO.output(trigPin,GPIO.LOW)
    pingTime = pulseIn(echoPin,GPIO.HIGH,timeOut)
    distance = pingTime * 340.0 / 2.0 / 10000.0  
    return distance

def setup():
    GPIO.setmode(GPIO.BOARD)      
    GPIO.setup(trigPin, GPIO.OUT) 
    GPIO.setup(echoPin, GPIO.IN)  

def loop():
    while(True):
        currentTime = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        distance = getSonar()

        data = {
            "time" : currentTime,
            "distance" : distance
        }
         # get distance
        print ("The distance is : %.2f cm"%(distance))
        print ("The time is : ", currentTime)

        print("Seding data to firebase database...\n")
        database.child(deviceID).child("01-setTest").set(data)
        database.child(deviceID).child("01-pushTest").push(data)
        time.sleep(10)


if __name__ == '__main__':     
    deviceID = "RPI_01"
    print ('Program is starting...')
    setup()
    try:
        loop()
    except KeyboardInterrupt:  
        GPIO.cleanup()         