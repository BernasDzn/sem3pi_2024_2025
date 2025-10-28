#include <MQUnifiedsensor.h>
#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>

#define DHTPIN 0

#define DHTTYPE DHT11
#define         Board                   ("Raspberry Pi Pico W")
#define         Pin                     (A0)  //Analog input 3 of your arduino
#define         Type                    ("MQ-2") //MQ2
#define         Voltage_Resolution      (3.3)
#define         ADC_Bit_Resolution      (12) // For arduino UNO/MEGA/NANO
#define         RatioMQ2CleanAir        (9.83) //RS / R0 = 9.83 ppm

int firstReadingT = 0;
int firstReadingH = 0;
float firstReadingG = 0;

int ledTpin = 14; //ventilation fan
int ledHpin = 15; //exhaust fan

DHT dht(DHTPIN, DHTTYPE);

MQUnifiedsensor MQ2(Board, Voltage_Resolution, ADC_Bit_Resolution, Pin, Type);

void setup() {
  Serial.begin(9600);
  dht.begin();

  MQ2.setRegressionMethod(1); //_PPM =  a*ratio^b
  MQ2.setA(574.25); MQ2.setB(-2.222); // Configure the equation to to calculate LPG concentration
  MQ2.init();

  Serial.print("Calibrating please wait.");
  float calcR0 = 0;
  for(int i = 1; i<=10; i ++)
  {
    MQ2.update(); // Update data, the arduino will read the voltage from the analog pin
    calcR0 += MQ2.calibrate(RatioMQ2CleanAir);
    Serial.print(".");
  }
  MQ2.setR0(calcR0/10);
  Serial.println("  done!.");

  if(isinf(calcR0)) {Serial.println("Warning: Conection issue, R0 is infinite (Open circuit detected) please check your wiring and supply"); while(1);}
  if(calcR0 == 0){Serial.println("Warning: Conection issue found, R0 is zero (Analog pin shorts to ground) please check your wiring and supply"); while(1);}
  /*****************************  MQ CAlibration ********************************************/

  pinMode(ledTpin, OUTPUT);
  pinMode(ledHpin, OUTPUT);
  MQ2.update(); // Update data, the arduino will read the voltage from the analog pin
  firstReadingT = dht.readTemperature();
  firstReadingH = dht.readHumidity();
  firstReadingG = (MQ2.readSensor() * 100.0) / 971.88); //Sensor will read PPM concentration using the model, a and b values set previously or from the setup
  Serial.println("Temperature;Unit;Initial;Unit;Difference;Unit;Humidity;Unit;Initial;Unit;Difference;Unit;Gas;Unit;Initial;Unit;Difference;Unit;Status;Time");
}

void loop() {
  int curReadingT = dht.readTemperature();
  int curReadingH = dht.readHumidity();
  float curReadingG = (MQ2.readSensor() * 100.0) / 971.88);

  Serial.print(String(curReadingT) + ";C;" + String(firstReadingT) + ";C;" + String(curReadingT-firstReadingT) + ";C;"
  + String(curReadingH) + ";%;" + String(firstReadingH) + ";%;" + String(((((float)curReadingH/firstReadingH)*100)-100)) + "%;"
  + String(curReadingG) + ";%;" + String(firstReadingG) + ";%;" + String(((((float)curReadingG/firstReadingG)*100)-100)) + "%;"+ g +";");

  if(curReadingT > firstReadingT+5){
    digitalWrite(ledHpin, HIGH);
    delay(5000);
    digitalWrite(ledHpin, LOW);
    digitalWrite(ledTpin, HIGH);
    delay(5000);
    digitalWrite(ledTpin, LOW);
  }

  if(curReadingH > (firstReadingH+(firstReadingH*0.05))){
    digitalWrite(ledTpin, HIGH);
    delay(10000);
    digitalWrite(ledTpin, LOW);
    digitalWrite(ledHpin, HIGH);
    delay(10000);
    digitalWrite(ledHpin, LOW);
  }

  if(firstReadingG > (curReadingG+(curReadingG*0.02))){
    digitalWrite(ledTpin, HIGH);
    digitalWrite(ledHpin, HIGH);
    delay(10000);
    digitalWrite(ledTpin, LOW);
    digitalWrite(ledHpin, LOW);
  }
  delay(1000);
}