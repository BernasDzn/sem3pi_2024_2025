void setup() {
  Serial.begin(9600);
}

void loop() {
  while(Serial.available()!=1){
  }
  char character = Serial.read();
  switch(character){
    case 'A':
      doFirstCase();
      break;
    case 'B':
      doSecondCase();
      break;
    default:
      break;
  }
  delay(5);
}

void doFirstCase(){
  for(int i = 0; i<100; i++){
    Serial.print("A");
  }
  Serial.println("");
  return;
}

void doSecondCase(){
  for(int i = 0; i<100; i++){
    Serial.print("B");
  }
  Serial.println("");
  return;
}