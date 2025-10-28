INCLUDES = -I../
SOURCES = test.c ../../main/us10/median.s ../../main/us09/sort_array.s
OBJFILES = test.o ../../main/us10/median.o ../../main/us09/sort_array.o
EXEC = test

LIBS = ../unity.o callfunc.o

.SUFFIXES: .c .o .s

.c.o:
		gcc -g -Wall -Wextra -fanalyzer $(INCLUDES) -c $< -o $@

.s.o:
		as -g -o $@ $<

${EXEC}: ${OBJFILES} $(LIBS)
		gcc ${OBJFILES} $(LIBS) -o ${EXEC}

test.o: test.c ../../main/us10/median.h

run: ${EXEC}
		./${EXEC}

clean:
		rm -f ${OBJFILES} ${EXEC}