#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <X11/Xlib.h> // Every Xlib program must include this
#include <assert.h>   // I include this to test return values the lazy way
#include <unistd.h>   // So we got the profile for 10 seconds
#define NIL (0)       // A name for the void pointer

#define NBODY_IMAGE_WIDTH 1024
#define NBODY_IMAGE_HEIGHT 860


void allocate_grid(int ncols, int nrows);
void cleanup_grid(int ncols, int nrows);
void init_grid(int ncols, int nrows);
void setupWindow(int ncols, int nrows);
void myDraw(int ncols, int nrows);
