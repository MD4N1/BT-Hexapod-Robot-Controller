/* Demo Pengendali Motor DC jarak jauh dengan Bluetooth Dongle dan USB Host Shield
 * Perangkat yang digunakan:
 * 1. Arduino Duemilanove 328p
 * 2. USB Host Shield, library dapat di download di https://github.com/felis/USB_Host_Shield_2.0
 * 3. USB Bluetooth Dongle (bebas)
 * 4. Smartphone Android minimal Ginger Bread
 * 5. Aplikasi BluetoothAndroidAndroid.apk, source code Arduino asli dll., dapat didownload di https://github.com/mharkus/BluetoothArduinoAndroid
*/
#include <Arduino.h>
#include <Usb.h>
#include <SPP.h>
#include <usbhub.h>

USB Usb;
BTD Btd(&Usb); // Buat Bluetooth Dongle
SPP SerialBT(&Btd, "Motor Controller","1234"); //Nama Devais Bluetooh Dongle dan PIN nya

char val[4];
char StatusMotor1 = 2;
char ArahMotor1 = 3;
char StatusMotor2 = 4;
char ArahMotor2= 5;
char index = 0;
unsigned long previousMillis = 0;
int interval= 20;

void setup()
{
  pinMode(StatusMotor1, OUTPUT);
  pinMode(ArahMotor1, OUTPUT);
  pinMode(StatusMotor2, OUTPUT);
  pinMode(ArahMotor2, OUTPUT);
  Serial.begin(57600);
  if (Usb.Init() == -1)
  {
    while (1); //halt
  }
}

void loop()
{
  Usb.Task();
  if (SerialBT.connected)
  {
     unsigned long currentMillis = millis();
     if ((unsigned long)(currentMillis - previousMillis) >= interval)
    {
    if(index < 3)
    {
      val[index] = SerialBT.read();
      SerialBT.print(val[index]);
      index++;
    }

    val[index] = '\0';

    SerialBT.println("");
    if(index == 3)
    {
      if(strcmp("m11", val) == 0)
      {
        digitalWrite(StatusMotor1, HIGH);
        Serial.print("m11");
      }
      else if(strcmp("m10", val) == 0)
      {
        digitalWrite(StatusMotor1, LOW);
        Serial.print("m10");
      }
      else if(strcmp("d10", val) == 0)
      {
        digitalWrite(ArahMotor1, LOW);
        Serial.print("d10");
      }
      else if(strcmp("d11", val) == 0)
      {
        digitalWrite(ArahMotor1, HIGH);
        Serial.print("d11");
      }


      else if(strcmp("m21", val) == 0)
      {
        digitalWrite(StatusMotor2, HIGH);
        Serial.print("m21");
      }
      else if(strcmp("m20", val) == 0)
      {
        digitalWrite(StatusMotor2, LOW);
        Serial.print("m20");
      }
      else if(strcmp("d20", val) == 0)
      {
        digitalWrite(ArahMotor2, LOW);
        Serial.print("d20");
      }
      else if(strcmp("d21", val) == 0)
      {
        digitalWrite(ArahMotor2, HIGH);
        Serial.print("d21");
      }

      else if(strcmp("s11", val) == 0)
      {
        //digitalWrite(StatusMotor1, HIGH);
        //digitalWrite(StatusMotor2, HIGH);
        Serial.print("s11");
      }
      else if(strcmp("s10", val) == 0)
      {
        //digitalWrite(StatusMotor1, LOW);
        //digitalWrite(StatusMotor2, LOW);
        Serial.print("s10");
      }
      else if(strcmp("s21", val) == 0)
      {
        //digitalWrite(ArahMotor1, LOW);
        //digitalWrite(ArahMotor2, LOW);
        Serial.print("s21");
      }
      else if(strcmp("s20", val) == 0)
      {
        //digitalWrite(ArahMotor1, HIGH);
        //digitalWrite(ArahMotor2, HIGH);
        Serial.print("s20");
      }
       else if(strcmp("t11", val) == 0)
      {
        //digitalWrite(ArahMotor1, HIGH);
        //digitalWrite(ArahMotor2, HIGH);
        Serial.print("t11");
      }
       else if(strcmp("t10", val) == 0)
      {
        //digitalWrite(ArahMotor1, HIGH);
        //digitalWrite(ArahMotor2, HIGH);
        Serial.print("t10");
      }
       else if(strcmp("t21", val) == 0)
      {
        //digitalWrite(ArahMotor1, HIGH);
        //digitalWrite(ArahMotor2, HIGH);
        Serial.print("t21");
      }
       else if(strcmp("t20", val) == 0)
      {
        //digitalWrite(ArahMotor1, HIGH);
        //digitalWrite(ArahMotor2, HIGH);
        Serial.print("t20");
      }
      index = 0;
      previousMillis = currentMillis;
     }
   }
  }
}
