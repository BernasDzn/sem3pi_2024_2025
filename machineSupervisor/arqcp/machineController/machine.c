#include "machine.h"
#include <stdlib.h>
#include <string.h>

Machine* create_machine(short id, char *name, int median_window, int min_temperature, int max_temperature, char *temperature_unit, int min_humidity, int max_humidity, char *humidity_unit, size_t buffer_length){
    Machine* newMachine = (Machine*)malloc(sizeof(Machine));
    newMachine->id = id;
    newMachine->name = name;
    newMachine->median_window = median_window;
    newMachine->min_temperature = min_temperature;
    newMachine->max_temperature = max_temperature;
    newMachine->temperature_unit = temperature_unit;
    newMachine->min_humidity = min_humidity;
    newMachine->max_humidity = max_humidity;
    newMachine->humidity_unit = humidity_unit;
    newMachine->buffer_length = buffer_length;
    newMachine->temperatureBuffer = (circular_buffer*)malloc(sizeof(circular_buffer));
    newMachine->temperatureBuffer->buffer = (int*)malloc(buffer_length * sizeof(int));
    newMachine->temperatureBuffer->head = 0;
    newMachine->temperatureBuffer->tail = 0;
    newMachine->humidityBuffer = (circular_buffer*)malloc(sizeof(circular_buffer));
    newMachine->humidityBuffer->buffer = (int*)malloc(buffer_length * sizeof(int));
    newMachine->humidityBuffer->head = 0;
    newMachine->humidityBuffer->tail = 0;
    newMachine->operation = (Operation*)malloc(sizeof(Operation));
    newMachine->status = OFF;
    newMachine->allOperations[0] = 0;
    newMachine->operationQuantity = 0;
    return newMachine;
}

// TODO: read machines from file and return success or error
