INCLUDES = -I../
SOURCES = test.c ../../main/us07/get_n_elements.s
OBJFILES = test.o ../../main/us07/get_n_elements.o
EXEC = test_program

LIBS = ../unity.o callfunc.o

.SUFFIXES: .c .o .s

.c.o:
		gcc -g -Wall -Wextra -fanalyzer $(INCLUDES) -c $< -o $@

.s.o:
		as -g -o $@ $<

${EXEC}: ${OBJFILES} $(LIBS)
		gcc ${OBJFILES} $(LIBS) -o ${EXEC}

test.o: test.c ../../main/us07/get_n_elements.h

run: ${EXEC}
		./${EXEC}

clean:
		rm -f ${OBJFILES} ${EXEC}