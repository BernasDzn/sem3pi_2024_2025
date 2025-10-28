SOURCES = machineController/serial.c
OBJFILES = machineController/serial.o
EXEC = machineController/serial_test

.SUFFIXES: .c .o

machineController/serial.o:
		gcc ${SOURCES} -g -Wall -Wextra -fanalyzer -c $< -o $@

${EXEC}: ${OBJFILES}
		gcc ${OBJFILES} -o ${EXEC} -g

run: ${EXEC}
		./${EXEC}

clean:
		rm -f ${OBJFILES} ${EXEC}