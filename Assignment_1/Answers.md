# Answers
This is the Markdown document where we will put our answers for the mini test.

## Question 1:
### A)
a)
```java
SensorManager mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
final List<Sensor> sensorList = mgr.getSensorList(Sensor.TYPE_ALL);

final ArrayList<String> sensorNames = new ArrayList<String>();
for (Sensor sensor : sensorList) {
      sensorNames.add(sensor.getName());
      System.out.println(sensor.getName());
}
```

b)
??

c)
??

### B)
??

## Question 2:
`OnResume()` is invoked when transitioning to *Activity Running*.

`OnDestroy()` is invoked when transitioning to *Activity shutdown*.

`OnStop()` is invoked when transitioning to *App process killed*.


## Question 3:
They should be defined in `res/values/strings.xml`
The advantage is that we can easily swap from one language to another. Say we want to change the App from English to German: We would initially only have a `res/values/strings.xml`. Now we create another file `res/values/de_strings.xml` where all the translated strings are stored. We can now simply use the `de_strings.xml` in the entire app instead of having to change each string value manually.

## Question 4:
From the android document:
>An Intent provides a facility for performing late runtime binding between the code in different applications. Its most significant use is in the launching of activities, where it can be thought of as the glue between activities. It is basically a passive data structure holding an abstract description of an action to be performed.
>
>--<cite>Google and Open Handset Alliance n.d. Android API Guide. https://developer.android.com/reference/android/content/Intent.html. Accessed May Oct 6, 2017.<cite>

*Explicit intents* are used to launch a new activity in an app and may send some data to the new activity

*Implicit intents* are used when we want to send data or switch to an inbuilt android activity. For that we must exactly specify which inbuilt activity we want to call. Examples for inbuilt activities are:
- Browser
- Camera
- Contact
- Dialpad

## Question 5:
a) false
b) true
c) true
d) false

## Question 6:
missing service entry:
```xml
<service  android:name=".LocationService."  android:exported="false"/>
```

missing location permission:
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```
missing send sms permission
```xml
<uses-permission android:name="android.permission.SEND_SMS"/>
```
