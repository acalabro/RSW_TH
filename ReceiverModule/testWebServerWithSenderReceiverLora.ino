#include <WiFi.h>
#include <WiFiClient.h>
#include <WebServer.h>
#include <ESPmDNS.h>
#include "heltec.h"
#define BAND    868E6  //you can set band here directly,e.g. 868E6,915E6

unsigned int counter = 0;
String rssi = "RSSI --";
String packSize = "--";
String packet ;
String packetBlock="";


const char* ssid = "Emergency_mobile";
const char* password = "6363636363";

WebServer server(80);

const int led = 13;

void handleRoot() {
  digitalWrite(led, 1);
  //server.send(200, "text/html", "Last message received has a length of: " + packSize + 
  //+" bytes<br />and position will be available at:<br />" +
  //+ "<a href=\"http://maps.google.com/maps?&z=17&mrt=yp&t=k&q=" + packet + "\">this link</a>");


//  server.send(200,"text/html",
//  "<meta http-equiv=\"refresh\" content=\"5; URL=/\"><html><head><title>Lora Receiver</title></head><body style=\"background-color:#97cddf;\"><h1>Messages</h1><p>Last message received has a length of: " +
//   packSize + " bytes<br />and position will be available at:<br />" +
//   "<a href=\"http://maps.google.com/maps?&z=17&mrt=yp&t=k&q=" + packet + "\">this link</a></p></body></html>");


  server.send(200,"text/html","<meta http-equiv=\"refresh\" content=\"5; URL=/\"><html><head><title>Lora Receiver</title></head><body style=\"background-color:#97cddf;\"><font face=\"Arial\" color=\"#FF7A59\"> <h1>Messages</h1></font><font face=\"Arial\" color=\"#2d3e50\"><p>" + packetBlock +
      "</p></font></body></html>");

  Serial.println(packetBlock);
  digitalWrite(led, 0);
}

void handleNotFound() {
  digitalWrite(led, 1);
  String message = "File Not Found\n\n";
  message += "URI: ";
  message += server.uri();
  message += "\nMethod: ";
  message += (server.method() == HTTP_GET) ? "GET" : "POST";
  message += "\nArguments: ";
  message += server.args();
  message += "\n";
  for (uint8_t i = 0; i < server.args(); i++) {
    message += " " + server.argName(i) + ": " + server.arg(i) + "\n";
  }
  server.send(404, "text/plain", message);
  digitalWrite(led, 0);
}

void setDisplay(void){
  Heltec.display->clear();
  Heltec.display->setTextAlignment(TEXT_ALIGN_LEFT);
  Heltec.display->setFont(ArialMT_Plain_10);
  Heltec.display->drawString(0 , 15 , "Received "+ packSize + " bytes");
  Heltec.display->drawStringMaxWidth(0 , 26 , 128, packet);
  Heltec.display->drawString(0, 0, rssi);  
  Heltec.display->display();
}

void cbk(int packetSize) {
  packet ="";
  packSize = String(packetSize,DEC);
  for (int i = 0; i < packetSize; i++) { packet += (char) LoRa.read(); }
  rssi = "RSSI " + String(LoRa.packetRssi(), DEC) ;
  setDisplay();
  packetBlock = packetBlock + "<br />" + packet;
}

void setupLora(void) {
   //WIFI Kit series V1 not support Vext control
  Heltec.begin(true /*DisplayEnable Enable*/, true /*Heltec.Heltec.Heltec.LoRa Disable*/, true /*Serial Enable*/, true /*PABOOST Enable*/, BAND /*long BAND*/);
 
  Heltec.display->init();
  Heltec.display->flipScreenVertically();  
  Heltec.display->setFont(ArialMT_Plain_10);
  delay(1500);
  Heltec.display->clear();
  Heltec.display->drawString(0, 0, "Heltec.LoRa Initial success!");
  Heltec.display->drawString(0, 10, "Wait for incoming data...");
  Heltec.display->display();
  delay(1000);
  //LoRa.onReceive(cbk);
  LoRa.receive();
}

void setup(void) {

  setupLora();
  
  pinMode(led, OUTPUT);
  digitalWrite(led, 0);
  Serial.begin(9600);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.println("");
    //WIFI Kit series V1 not support Vext control
  Serial.println("Heltec.LoRa init succeeded.");

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  if (MDNS.begin("esp32")) {
    Serial.println("MDNS responder started");
  }

  server.on("/", handleRoot);

  server.on("/inline", []() {
    server.send(200, "text/plain", "this works as well");
  });

  server.onNotFound(handleNotFound);

  server.begin();
  Serial.println("HTTP server started");
}

void loop(void) {
  server.handleClient();
  messagesLoop();
  
}

void messagesLoop(void) {
  int packetSize = LoRa.parsePacket();
  if (packetSize) { cbk(packetSize);  }
  delay(10);
}
