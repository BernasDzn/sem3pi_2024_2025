CC = gcc
CFLAGS = -Wall -Wextra
SRC = interface.c machine.c 
OBJ = $(SRC:.c=.o) ../main/us01/extract_data.s ../main/us02/get_number_binary.s ../main/us04/format_command.s ../main/us05/enqueue_value.s ../main/us06/dequeue_value.s ../main/us07/get_n_elements.s ../main/us08/move_n_to_array.s ../main/us09/sort_array.s ../main/us10/median.s
EXEC = machineController

all: $(EXEC)

$(EXEC): $(OBJ)
	$(CC) $(OBJ) -o $@ 

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

%.o: %.s
	$(CC) -c $< -o $@

run:
	./$(EXEC)

.PHONY: clean