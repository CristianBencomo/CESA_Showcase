import time
import pyrebase
from datetime import datetime
from pyfcm import FCMNotification #new

print("Program is starting")

apiKey = "ENTER API KEY"
config = {
    "apiKey": apiKey,
    "authDomain": "testfirebaseapp-18cbd.firebaseapp.com",
    "databaseURL": "https://testfirebaseapp-18cbd-default-rtdb.firebaseio.com/",
    "storageBucket": "testfirebaseapp-18cbd.appspot.com"
}

firebase = pyrebase.initialize_app(config)
database = firebase.database()
deviceID = "RPI_02"

push_service = FCMNotification(api_key="AAAASHtg8jo:APA91bF1iuoDe0X1fp7hf_3R7hO3AiuZQzbJvKZSdbZY4ENgMegYHZ-yizLIcR51t5daA6NbWnrVIGbd_kc6zlcMVb1JIJVX73bv6EKdhP5DT3KTfwfr0YoI8SYeVL8l8eB_8RQWxrwV") 
FCMdb = database.child(deviceID).child("DeviceInfo").child("FCM").get()
FCM = FCMdb.val()

print(FCM)

print(FCM)
registration_id = FCM
message_title = "Bump Alert"
message_body = "Your car has been bumped!"
result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)

print(result)