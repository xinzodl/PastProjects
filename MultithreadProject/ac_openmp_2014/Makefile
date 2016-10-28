CC = gcc
CFLAGS= -Wall -g -fopenmp
PRGS = nbody
OBJS = video.o
LIBS = -lm -lX11 -L/usr/X11R6/lib

all: $(PRGS)

%.o: %.c $(HEADERS)
	$(CC) $(CFLAGS) -c $*.c $(INCLUDE) -o $@
$(PRGS): $(OBJS)
$(PRGS): % : %.o
	$(CC) $(CFLAGS) -o $@ $< $(OBJS) $(LDFLAGS) $(LIBS)

clean:
	-rm -f *.o  *~ $(PRGS)
