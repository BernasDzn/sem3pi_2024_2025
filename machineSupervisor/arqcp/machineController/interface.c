#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "machine.h"

#include <unistd.h>
#include <fcntl.h>
#include <termios.h>

#include "../main/us01/extract_data.h"
#include "../main/us02/get_number_binary.h"
#include "../main/us04/format_command.h"
#include "../main/us05/enqueue_value.h"
#include "../main/us06/dequeue_value.h"
#include "../main/us08/move_n_to_array.h"
#include "../main/us07/get_n_elements.h"
#include "../main/us09/sort_array.h"
#include "../main/us10/median.h"

#define BUFFER_SIZE 1024

#define PORT "/dev/ttyUSB0"

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

typedef struct MachineList {
    Machine* data;
    struct MachineList* nextPtr;
} MachineList;

MachineList* add_machine(Machine* data) {
    MachineList* list = (MachineList*)malloc(sizeof(MachineList));
    list->data = data;
    list->nextPtr = NULL;
    return list;
}

void assign_operation(Machine* machine, short operation) {
    if(operation>0 && operation<32){
        machine->operation->op_number = operation;
        (machine->allOperations)[machine->operationQuantity] = operation;
        (machine->operationQuantity)++;
        machine->status = OP;
    }
    else{
        machine->operation->op_number = 0;
        machine->operation->op_name = NULL;
        machine->status = OFF;
    }
}

void remove_machine(MachineList** machines, int index, int * numberOfMachines) {
    MachineList* current = *machines;
    MachineList* previous = NULL;
    int i = 0;
    while (current != NULL) {
        if (i == index) {
            if (previous == NULL) {
                *machines = current->nextPtr;
            } else {
                previous->nextPtr = current->nextPtr;
            }
            free(current->data->name);
            free(current->data->temperature_unit);
            free(current->data->humidity_unit);
            free(current->data->temperatureBuffer->buffer);
            free(current->data->temperatureBuffer);
            free(current->data->humidityBuffer->buffer);
            free(current->data->humidityBuffer);
            free(current->data->operation->op_name);
            free(current->data->operation);
            free(current->data);
            free(current);
            (*numberOfMachines)--;
            return;
        }
        i++;
        previous = current;
        current = current->nextPtr;
    }

}

void print_machine_list(MachineList* machines) {
    MachineList* current = machines;
    while (current != NULL) {
        if (current->data != NULL) {
            printf("> %s\n", current->data->name);
        }
        current = current->nextPtr;
    }
}

Machine* get_machine_by_id(MachineList* machines, short id) {
    MachineList* current = machines;
    while (current != NULL && current->data != NULL) {
        if (current->data->id == id) {
            return current->data;
        }
        current = current->nextPtr;
    }
    return NULL;
}

int read_number(char* message) {
    int number;
    printf("%s", message);
    scanf("%d", &number);
    return number;
}

Machine* get_machine_by_index(MachineList** machines, int index) {
    int i = 0;
    MachineList* current = *machines;
    while (current != NULL) {
        if (i == index) {
            return current->data;
        }
        i++;
        current = current->nextPtr;
    }
    return NULL;
}

void clear_buffer(char * buffer) {
    for (int i = 0; i < BUFFER_SIZE; i++) {
        buffer[i] = '\0';
    }
}

void update_machine_status(Machine * machine, char * buffer){
    char temp_unit[20];
    int temp_value;
    extract_data(buffer,"TEMP",temp_unit,&temp_value);
    char hum_unit[20];
    int hum_value;
    extract_data(buffer,"HUM",hum_unit,&hum_value);
    printf("Temperature: %d %s\n", temp_value, temp_unit);
    printf("Humidity: %d %s\n", hum_value, hum_unit);
    machine->temperature = temp_value;
    machine->humidity = hum_value;
    machine->temperature_unit = strdup(temp_unit);
    machine->humidity_unit = strdup(hum_unit);
    enqueue_value(machine->temperatureBuffer->buffer, machine->buffer_length, &machine->temperatureBuffer->tail, &machine->temperatureBuffer->head, temp_value);
    enqueue_value(machine->humidityBuffer->buffer, machine->buffer_length, &machine->humidityBuffer->tail, &machine->humidityBuffer->head, hum_value);
    if(get_n_elements(machine->temperatureBuffer->buffer, machine->buffer_length, &machine->temperatureBuffer->tail, &machine->temperatureBuffer->head) >= machine->median_window){
        int temp_values[machine->median_window];
        int hum_values[machine->median_window];
        move_n_to_array(machine->temperatureBuffer->buffer, machine->buffer_length, &machine->temperatureBuffer->tail, &machine->temperatureBuffer->head, machine->median_window, temp_values);
        move_n_to_array(machine->humidityBuffer->buffer, machine->buffer_length, &machine->humidityBuffer->tail, &machine->humidityBuffer->head, machine->median_window, hum_values);
        sort_array(temp_values, machine->median_window, 1);
        sort_array(hum_values, machine->median_window, 1);
        int median_temp;
        int median_hum;
        median(temp_values, machine->median_window, &median_temp);
        median(hum_values, machine->median_window, &median_hum);
        if(temp_value > machine->max_temperature || temp_value < machine->min_temperature){
            printf("\n+---------------------------------+\n");
            printf("|  TEMPERATURE OUTSIDE THRESHOLD  |\n");
            printf("+---------------------------------+\n");
        }
        if(hum_value > machine->max_humidity || hum_value < machine->min_humidity){
            printf("\n+---------------------------------+\n");
            printf("|   HUMIDITY OUTSIDE THRESHOLD    |\n");
            printf("+---------------------------------+\n");
        }
    }
}

void run_file(char* path, MachineList* machines) {
    FILE* fptr = fopen(path, "r");
    if (fptr == NULL) {
        printf("Failed to find file: %s\n", path);
        return;
    }

    const char *port = PORT;
    int baud_rate = B9600;
    char buffer[BUFFER_SIZE];

    int fd = open_serial_port(port, baud_rate);

    sleep(2); 
    tcflush(fd,TCIOFLUSH);

    char line[200];
    fgets(line, 200, fptr);
    while (fgets(line, 200, fptr)) {
        clear_buffer(buffer);
        char* token = strtok(line, ";");
        int index = 0;
        Machine* thisMachine = get_machine_by_id(machines, atoi(token));
        if(thisMachine == NULL){
            printf("\nMachine with token %d not found.\n", atoi(token));
            return;
        }
        token = strtok(NULL, ";");
        while (token != NULL) {
            if (index == 0) {
                thisMachine->operation->op_number = atoi(token);
                (thisMachine->allOperations)[thisMachine->operationQuantity] =  atoi(token);
    			(thisMachine->operationQuantity)++;
                }
            if (index == 1) {
                thisMachine->operation->op_name = strdup(token);
                }
            if (index == 2) {
                thisMachine->operation->start_time = atoi(token);
                }
            token = strtok(NULL, ";");
            index++;
        }
        thisMachine->status = OP;
        printf("\nMachine %d is now in operation.\n", thisMachine->id);
        printf("Operation: %s\n", thisMachine->operation->op_name);

        format_command("OP", thisMachine->operation->op_number, buffer);

        buffer[strcspn(buffer, "\n")] = 0;

        if (write(fd, buffer, strlen(buffer)) < 0) {
            perror("Error writing to serial port");
            continue;
        }

        usleep(2000000);
        int n = read(fd, buffer, BUFFER_SIZE - 1);
        while(n<=0){
            read(fd, buffer, BUFFER_SIZE - 1);
            printf("No response from device\n");
        }

        buffer[n-2] = '\0';
        buffer[n-1] = '\0';

        update_machine_status(thisMachine, buffer);
        usleep(6000000);
    }
}

void show_machine_data(Machine* machine) {
    printf("ID: %d\n", machine->id);
    printf("Name: %s\n", machine->name);
    printf("Temperature: Min (%d) to Max (%d) %s\n", machine->min_temperature, machine->max_temperature, machine->temperature_unit);
    printf("Humidity: Min (%d) to Max (%d) %s\n", machine->min_humidity, machine->max_humidity, machine->humidity_unit);
    printf("Operation: %d\n", machine->operation->op_number);
    printf("Status: %s\n", machineStatusStrings[machine->status]);
}


void export_machine_operations(Machine* machine) {
	char* path = malloc(50 * sizeof(char));
    sprintf(path, "machine_%d_operations.csv", machine->id);
    FILE *fexp = fopen(path, "w");

    fprintf(fexp, "Machine %d", machine->id);
    for(int i=0; i <machine->operationQuantity; i++){
        fprintf(fexp, ";%d", machine->allOperations[i]);
	}
    if(machine->operationQuantity == 0){
        fprintf(fexp, ";NONE");
    }
    fprintf(fexp, "\n");
    fclose(fexp);
    printf("Successfully exported to %s\n", path);

}

void run_current_status(Machine* machine){
    const char *port = PORT;
    int baud_rate = B9600;
    char buffer[BUFFER_SIZE];

    int fd = open_serial_port(port, baud_rate);

    sleep(2); 
    tcflush(fd,TCIOFLUSH);

    format_command(machineStatusStrings[machine->status], machine->operation->op_number, buffer);

    buffer[strcspn(buffer, "\n")] = 0;

    if (write(fd, buffer, strlen(buffer)) < 0) {
        perror("Error writing to serial port");
        return;
    }

    usleep(2000000);
    int n = read(fd, buffer, BUFFER_SIZE - 1);
    while(n<=0){
        read(fd, buffer, BUFFER_SIZE - 1);
        printf("No response from device\n");
    }

    buffer[n-2] = '\0';
    buffer[n-1] = '\0';

    update_machine_status(machine, buffer);
    usleep(1000000);
}

void manageMachine(Machine* machine, MachineList** machines, int index, int * numberOfMachines) {
    printf("\nManaging machine: %s\n", machine->name);
    printf("1. Add operation\n");
    printf("2. Remove operation\n");
    printf("3. Display machine data\n");
    printf("4. Export machine operations\n");
    printf("5. Run machine status\n");
    printf("6. Remove machine\n");
    printf("7. Back\n");
    int option = read_number("Option: ");
    switch (option) {
        case 1:
            printf("\nAdding Operation\n");
            assign_operation(machine, read_number("Operation: "));
            break;
        case 2:
            printf("\nRemoving Operation\n");
            assign_operation(machine, 0);
            break;
        case 3:
            printf("\nMachine Data\n");
            show_machine_data(machine);
            break;
        case 4:
            printf("\nExport Machine Operations\n");
			export_machine_operations(machine);
            break;
        case 5:
            printf("\nRun Machine Status\n");
            run_current_status(machine);
            break;
        case 6:
            printf("\nRemoving Machine\n");
            remove_machine(machines, index, numberOfMachines);
            break;
        default:
            printf("\nGoing back\n");
            break;
    }
}


Machine* read_machine() {
    char * name = (char*)malloc(100 * sizeof(char));
    char * temperature_unit = (char*)malloc(100 * sizeof(char));
    char * humidity_unit = (char*)malloc(100 * sizeof(char));
    int median_window;
    int min_temperature, max_temperature;
    int min_humidity, max_humidity;
    short id;
    int buffer_length;
    printf("Create machine\n");
    id = read_number("Insert ID:");
    printf("Insert Name:");
    scanf(" %99[^\n]", name);
    printf("Insert Temperature Unit:");
    scanf(" %99[^\n]", temperature_unit);
    printf("Insert Humidity Unit:");
    scanf(" %99[^\n]", humidity_unit);
    median_window = read_number("Insert Median Window:");
    min_temperature = read_number("Insert Minimum Temperature:");
    max_temperature = read_number("Insert Maximum Temperature:");
    min_humidity = read_number("Insert Minimum Humidity:");
    max_humidity = read_number("Insert Maximum Humidity:");
    buffer_length = read_number("Insert Buffer Length:");
             
    return create_machine(id, name, median_window, 
                                            min_temperature, max_temperature, 
                                            temperature_unit, min_humidity, 
                                            max_humidity, humidity_unit, buffer_length);
}

int print_machine_list_index(MachineList** machines, int * numberOfMachines) {
    if(*numberOfMachines == 0 || *machines == NULL){
        printf("No machines registered.\n");
        return 0;
    }
    printf("\nSelect a machine:\n");
    int index = 1;
    int option = 0;
    MachineList* current = *machines;
    while (current != NULL) {
        if (current->data != NULL) {
            printf("%d > %s\n", index, current->data->name);
        }
        index++;
        current = current->nextPtr;
    }
    printf("%d > Quit\n", index);
    do{
        option = read_number("Option: ");
        if(option < 1 || option > index){
            printf("Invalid option. Please try again.\n");
        }   
    } while (option < 1 || option > index);
    
    if (option == index) {
        return 1;
    }

    Machine* selectedMachine = get_machine_by_index(machines, option - 1);
    manageMachine(selectedMachine, machines, option - 1, numberOfMachines);
    return 0;
}

void add_machine_to_list(Machine* machine, MachineList** machines, int* numberOfMachines) {
    (*numberOfMachines)++;

    if (*machines == NULL) {
        *machines = add_machine(machine);
    } else {
        MachineList* current = *machines;
        while (current->nextPtr != NULL) {
            current = current->nextPtr;
        }
        current->nextPtr = add_machine(machine);
    }
}

int read_machines_from_file(MachineList** machines, char* path, int* numberOfMachines) {
    printf("Reading machines ... \n");
    FILE* fptr = fopen(path, "r");
    if (fptr == NULL) {
        printf("Failed to find file: %s\n", path);
        return 0;
    }

    char line[200];

    fgets(line, 200, fptr);
    while (fgets(line, 200, fptr)) {
        char* token = strtok(line, ";");
        int index = 0;

        Machine* thisMachine = (Machine*)malloc(sizeof(Machine));
        while (token != NULL) {
            if (index == 0) thisMachine->id = atoi(token);
            if (index == 1) thisMachine->name = strdup(token);
            if (index == 2) thisMachine->min_temperature = atoi(token);
            if (index == 3) thisMachine->max_temperature = atoi(token);
            if (index == 4) thisMachine->temperature_unit = strdup(token);
            if (index == 5) thisMachine->min_humidity = atoi(token);
            if (index == 6) thisMachine->max_humidity = atoi(token);
            if (index == 7) thisMachine->humidity_unit = strdup(token);
            if (index == 8) thisMachine->buffer_length = atoi(token);
            if (index == 9) thisMachine->median_window = atoi(token);
            token = strtok(NULL, ";");
            index++;
        }

        thisMachine->temperatureBuffer = (circular_buffer*)malloc(sizeof(circular_buffer));
        thisMachine->temperatureBuffer->buffer = (int*)malloc(thisMachine->buffer_length * sizeof(int));
        thisMachine->temperatureBuffer->head = 0;
        thisMachine->temperatureBuffer->tail = 0;
        thisMachine->humidityBuffer = (circular_buffer*)malloc(sizeof(circular_buffer));
        thisMachine->humidityBuffer->buffer = (int*)malloc(thisMachine->buffer_length * sizeof(int));
        thisMachine->humidityBuffer->head = 0;
        thisMachine->humidityBuffer->tail = 0;
        thisMachine->operation = (Operation*)malloc(sizeof(Operation));
        thisMachine->operation->op_name = NULL;
        thisMachine->operation->op_number = 0;
        thisMachine->status = OFF;
    	thisMachine->allOperations[0]= 0;
    	thisMachine->operationQuantity=0;

        add_machine_to_list(thisMachine, machines, numberOfMachines);
    }

    fclose(fptr);
    printf("Done reading machines.\n");
    return 1;
}

char* get_input_file_path() {
    printf("File path: ");
    char* str = malloc(200 * sizeof(char));
    scanf(" %1299[^\n]", str);
    return str;
}

int manage_machines(MachineList** machines, int numberOfMachines) {
  	printf("\nMachine Supervisor:\n");
    printf("1. Add machine\n");
    printf("2. List machines\n");
    printf("3. Run file\n");
    printf("4. Quit\n");
    int option = read_number("Option: ");
    switch (option) {
        case 1:
            add_machine_to_list(read_machine(), machines, &numberOfMachines);
            break;
        case 2:
            print_machine_list_index(machines, &numberOfMachines);
            break;
        case 3:
            run_file(get_input_file_path(), *machines);
            break;
        default:
            return 1;
    }

    return 0;
}

int main() {
    int numberOfMachines = 0;

    MachineList* machines = NULL;

    int res = 0;
    while (res == 0) {
        char* path = get_input_file_path();
        res = read_machines_from_file(&machines, path, &numberOfMachines);
        free(path);
    }

    printf("%d machines registered in the system.\n", numberOfMachines);

    int op = 0;
    while(op == 0){
        op = manage_machines(&machines, numberOfMachines);
    }

    return 0;
}
