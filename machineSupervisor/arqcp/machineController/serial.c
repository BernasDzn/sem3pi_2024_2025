#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <termios.h>

#define BUFFER_SIZE 256

// Function to open and configure the serial port
int open_serial_port(const char *port, int baud_rate) {
    int fd = open(port, O_RDWR | O_NOCTTY | O_SYNC);
    if (fd < 0) {
        perror("Error opening serial port");
        exit(EXIT_FAILURE);
    }

    struct termios tty;
    memset(&tty, 0, sizeof(tty));
    if (tcgetattr(fd, &tty) != 0) {
        perror("Error getting terminal attributes");
        close(fd);
        exit(EXIT_FAILURE);
    }

    cfsetospeed(&tty, baud_rate);
    cfsetispeed(&tty, baud_rate);

    tty.c_cflag = (tty.c_cflag & ~CSIZE) | CS8;  // 8-bit chars
    tty.c_iflag &= ~IGNBRK;                     // disable break processing
    tty.c_lflag = 0;                            // no signaling chars, no echo, no canonical processing
    tty.c_oflag = 0;                            // no remapping, no delays
    tty.c_cc[VMIN]  = 1;                        // read blocks until 1 char is received
    tty.c_cc[VTIME] = 1;                        // 0.1 second timeout

    tty.c_iflag &= ~(IXON | IXOFF | IXANY);     // shut off xon/xoff ctrl
    tty.c_cflag |= (CLOCAL | CREAD);           // enable reading
    tty.c_cflag &= ~(PARENB | PARODD);         // no parity
    tty.c_cflag &= ~CSTOPB;                    // 1 stop bit
    tty.c_cflag &= ~CRTSCTS;                   // no hardware flow control

    if (tcsetattr(fd, TCSANOW, &tty) != 0) {
        perror("Error setting terminal attributes");
        close(fd);
        exit(EXIT_FAILURE);
    }

    return fd;
}

int main() {
    const char *port = "/dev/ttyACM0";  
    int baud_rate = B9600;
    char buffer[BUFFER_SIZE];

    int fd = open_serial_port(port, baud_rate);

    while (1) {
        printf("\nEnter command (ON, OP, OFF) 10101:\n");

        printf("Command: ");
        if (fgets(buffer, BUFFER_SIZE, stdin) == NULL) {
            printf("Error reading input\n");
            continue;
        }

        buffer[strcspn(buffer, "\n")] = 0;

        if (write(fd, buffer, strlen(buffer)) < 0) {
            perror("Error writing to serial port");
            continue;
        }

        usleep(100000);
        int n = read(fd, buffer, BUFFER_SIZE - 1);
        if (n > 0) {
            buffer[n] = '\0';
            printf("Device response: %s\n", buffer);
        } else {
            printf("No response from device\n");
        }
    }

    close(fd);
    return 0;
}