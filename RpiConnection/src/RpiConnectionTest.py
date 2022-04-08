import time
import pyrebase
from datetime import datetime

config = {
    "apiKey": "IU4kftDPotea1YxDNDDi0f7FtUmwwglHyfk5Ol91",
    "authDomain": "testfirebaseapp-18cbd.firebaseapp.com",
    "databaseURL": "https://testfirebaseapp-18cbd-default-rtdb.firebaseio.com/",
    "storageBucket": "testfirebaseapp-18cbd.appspot.com"
}

firebase = pyrebase.initialize_app(config)
database = firebase.database()
 
print("Send Data to Firebase Using Raspberry Pi")
print("—————————————-")
print()
 
while True:
    currentTime = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    
    print("The current date and time is :", currentTime)
    print()
    
    data = {
    "time": currentTime
    }
    database.child("TestTime").child("1-set").set(data)
    database.child("TestTime").child("2-push").push(data)
    
    time.sleep(10)