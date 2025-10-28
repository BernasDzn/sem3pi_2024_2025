#ifndef MACHINE_H
#define MACHINE_H

#include <time.h>

// Circular buffer info
typedef struct
{
  int head;
  int tail;
  int* buffer;
} circular_buffer;

#define foreach_machineStatus(machineStatus) \
        machineStatus(ON)  \
        machineStatus(OP)  \
        machineStatus(OFF) \

#define GENERATE_ENUM(ENUM) ENUM,
#define GENERATE_STRING(STRING) #STRING,

enum machineStatus {
    foreach_machineStatus(GENERATE_ENUM)
};

static const char *machineStatusStrings[] = {
    foreach_machineStatus(GENERATE_STRING)
};

typedef struct{
  char * op_name;
  short op_number;
  int start_time;
} Operation;

typedef struct {
  size_t buffer_length;
  circular_buffer* temperatureBuffer;
  circular_buffer* humidityBuffer;
  char *name;
  char *temperature_unit;
  char *humidity_unit;
  int median_window;
  int min_temperature, max_temperature;
  int temperature;
  int min_humidity, max_humidity;
  int humidity;
  short id;
  Operation* operation;
  enum machineStatus status;
  short allOperations[100];
  int operationQuantity;
} Machine;

Machine* create_machine(short id, char *name, int median_window, int min_temperature, int max_temperature, char *temperature_unit, int min_humidity, int max_humidity, char *humidity_unit, size_t buffer_length);

#endif // MACHINE_H