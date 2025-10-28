#include <USB.h>
#include <USBHIDKeyboard.h>

USBHIDKeyboard Keyboard;

void setup() {
  USB.begin();
  Keyboard.begin();
  delay(1000);
}

void loop() {
  if (Keyboard.isKeyPressed()) {
    char key = Keyboard.readKey();

    // Verifica se a tecla Ctrl está pressionada
    if (Keyboard.isKeyPressed(KEY_LEFT_CTRL)) {
      Keyboard.press(KEY_LEFT_CTRL);
      Keyboard.press(key);
      Keyboard.release(key);
      Keyboard.release(KEY_LEFT_CTRL);
    } else {
      Keyboard.press(key);
      Keyboard.release(key);
    }
  }
}