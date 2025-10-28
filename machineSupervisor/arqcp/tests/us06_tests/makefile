INCLUDES = -I../
SOURCES = test.c ../../main/us06/dequeue_value.s
OBJFILES = test.o ../../main/us06/dequeue_value.o
EXEC = test_program

LIBS = ../unity.o callfunc.o

.SUFFIXES: .c .o .s

.c.o:
		gcc -g -Wall -Wextra -fanalyzer $(INCLUDES) -c $< -o $@

.s.o:
		as -g -o $@ $<

${EXEC}: ${OBJFILES} $(LIBS)
		gcc ${OBJFILES} $(LIBS) -o ${EXEC} -g

test.o: test.c ../../main/us06/dequeue_value.h

run: ${EXEC}
		./${EXEC}

clean:
		rm -f ${OBJFILES} ${EXEC}