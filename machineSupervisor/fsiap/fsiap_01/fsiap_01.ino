#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>


#define DHTPIN 0
//int sensorPin = 0;

#define DHTTYPE DHT11

int firstReadingT = 0;
int firstReadingH = 0;

int ledTpin = 14;
int ledHpin = 15;

  char* t;
  char* h;

DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(9600);
  dht.begin();
  pinMode(ledTpin, OUTPUT);
  pinMode(ledHpin, OUTPUT);
  digitalWrite(ledTpin, HIGH);
  digitalWrite(ledHpin, HIGH);
  delay(5000);
  digitalWrite(ledTpin, LOW);
  digitalWrite(ledHpin, LOW);
  t = "OFF";
  t = "OFF";
  firstReadingT = dht.readTemperature();
  firstReadingH = dht.readHumidity();
  Serial.println("Temperature;Unit;Initial;Unit;Difference;Unit;Status;Humidity;Unit;Initial;Unit;Difference;Unit;Status;Time");
}

void loop() {
  int curReadingT = dht.readTemperature();
  int curReadingH = dht.readHumidity();
  
  //Serial.println("=======================================");
  
  //Serial.println("Initial temperature: " + String(firstReadingT) + " ºC");
  //Serial.println("Current temperature: " + String(curReadingT) + " ºC");
  //Serial.println("Temperature difference: " + String(curReadingT-firstReadingT) + " ºC");

  //Serial.println("Initial humidity: " + String(firstReadingH) + "%");
  //Serial.println("Current humidity: " + String(curReadingH) + "%");
  //Serial.println("Humidity difference (%): " + String(((((float)curReadingH/firstReadingH)*100)-100)) + "%");

  if(curReadingT > firstReadingT+5){
    // Serial.println("Temperature LED on.");
    t = "ON";
    digitalWrite(ledTpin, HIGH);
  }
  else{
    // Serial.println("Temperature LED off.");
    t = "OFF";
    digitalWrite(ledTpin, LOW);
  }

  if(curReadingH > (firstReadingH+(firstReadingH*0.05))){
    // Serial.println("Humidity LED on.");
    h = "ON";
    digitalWrite(ledHpin, HIGH);
  }
  else{
    // Serial.println("Humidity LED off.");
    h = "OFF";
    digitalWrite(ledHpin, LOW);
  }

  Serial.print(String(curReadingT) + ";C;" + String(firstReadingT) + ";C;" + String(curReadingT-firstReadingT) + ";C;"
  + t + ";" + String(curReadingH) + ";%;" + String(firstReadingH) + ";%;" + String(((((float)curReadingH/firstReadingH)*100)-100)) + "%;"+ h +";");

  delay(1000);
}