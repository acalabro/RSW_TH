#include "heltec.h"
#include "images.h"
#define BAND    868E6

unsigned int counter = 0;
String rssi = "RSSI --";
String packSize = "--";
String packet ;
String sentString="";

void logo()
{
  Heltec.display->clear();
  Heltec.display->drawXbm(0,5,logo_width,logo_height,logo_bits);
  Heltec.display->display();
}

void setup()
{
  Heltec.begin(true, true, true, true, BAND);
  Heltec.display->init();
  delay(500);
}

void loop()
{
  Heltec.display->clear();
  Heltec.display->flipScreenVertically();  
  Heltec.display->setTextAlignment(TEXT_ALIGN_LEFT);
  Heltec.display->setFont(ArialMT_Plain_10);
  Heltec.display->drawString(0, 5, "##LORA Transmitter##");
  
  Heltec.display->drawString(0, 20, "Packet sent: ");
  Heltec.display->drawString(60,20, String(counter));
  Heltec.display->drawString(0, 30, "Sending string: ");
  Heltec.display->drawString(0,40,sentString);
  Heltec.display->display();
 
  Serial.begin(9600);
  sentString = Serial.readString();

  if (sentString.length() > 0) {
    /*
   * LoRa.setTxPower(txPower,RFOUT_pin);
   * txPower -- 0 ~ 20
   * RFOUT_pin could be RF_PACONFIG_PASELECT_PABOOST or RF_PACONFIG_PASELECT_RFO
   *   - RF_PACONFIG_PASELECT_PABOOST -- LoRa single output via PABOOST, maximum output 20dBm
   *   - RF_PACONFIG_PASELECT_RFO     -- LoRa single output via RFO_HF / RFO_LF, maximum output 14dBm
  */
  // send packet
    LoRa.beginPacket();
    LoRa.setTxPower(14,RF_PACONFIG_PASELECT_PABOOST);
    LoRa.print(sentString);
    LoRa.endPacket();
  
    counter++;
    digitalWrite(LED, HIGH);   // turn the LED on (HIGH is the voltage level)
    delay(100);                       // wait for a second
    digitalWrite(LED, LOW);    // turn the LED off by making the voltage LOW
    delay(100);                       // wait for a second
  }

}
