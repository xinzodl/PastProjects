#include "video.h"

extern int **grid;


Display *dpy;
int blackColor;
int whiteColor;
Window w;
GC gc;
Pixmap buffer;
Colormap theColormap;
int numXGrayscale=512;
XColor Xgrayscale[512];
int IMAGE_WIDTH, IMAGE_HEIGHT;

void allocate_grid(int ncols, int nrows) {
    int i,j;

    grid =  malloc(sizeof(int *) * (ncols + 2));
    for (i=0; i<ncols+2;i++) {
        grid[i] = malloc( sizeof(int) * (nrows + 2));
        for (j=0;j<nrows+2;j++) {
            grid[i][j]=0;
        }
    }
    IMAGE_WIDTH = ncols;
    IMAGE_HEIGHT = nrows;
}
void cleanup_grid(int ncols, int nrows) {
    int i;
    for (i=0;i<ncols+2;i++) {
        free (grid[i]);
    }
    free (grid);
}
void init_grid(int ncols, int nrows){
    int i,j;
    for (i=0;i<=ncols;i++) {
        for (j=0;j<=nrows;j++) {
                grid[i][j]=0;
        }
    }
}


void setupWindow(int ncols, int nrows) {
      int i;

      // Open the display
      
      dpy = XOpenDisplay(NIL);
      assert(dpy);

      // Get some colors

      blackColor = BlackPixel(dpy, DefaultScreen(dpy));
      whiteColor = WhitePixel(dpy, DefaultScreen(dpy));

      // Create the window
      if (nrows>ncols) {
         IMAGE_WIDTH =  NBODY_IMAGE_WIDTH * ncols/nrows;
         IMAGE_HEIGHT = NBODY_IMAGE_HEIGHT;
      } else {
         IMAGE_HEIGHT = NBODY_IMAGE_HEIGHT * nrows/ncols;
         IMAGE_WIDTH = NBODY_IMAGE_WIDTH;
      }
      

      w = XCreateSimpleWindow(dpy, DefaultRootWindow(dpy), 0, 0, 
				     IMAGE_WIDTH, IMAGE_HEIGHT, 0, whiteColor,
				     whiteColor);
      buffer = XCreatePixmap(dpy,DefaultRootWindow(dpy),
          IMAGE_WIDTH,IMAGE_HEIGHT,DefaultDepth(dpy,
          DefaultScreen(dpy)));
          
      theColormap = XCreateColormap(dpy, DefaultRootWindow(dpy),
          DefaultVisual(dpy,DefaultScreen(dpy)), AllocNone);
          
      for (i=0;i<numXGrayscale;i++) {
          int color = (int)((double)i*35535.0/(double)numXGrayscale)+30000;
          Xgrayscale[i].red=color;
          Xgrayscale[i].green=color * i;
          Xgrayscale[i].blue=color * i * i;
          XAllocColor(dpy,theColormap,&(Xgrayscale[i]));
      }

      // We want to get MapNotify events

      XSelectInput(dpy, w, StructureNotifyMask);

      // "Map" the window (that is, make it appear on the screen)

      XMapWindow(dpy, w);

      // Create a "Graphics Context"

      gc = XCreateGC(dpy, w, 0, NIL);

      // Tell the GC we draw using the white color

      XSetForeground(dpy, gc, whiteColor);

      // Wait for the MapNotify event

      for(;;) {
	    XEvent e;
	    XNextEvent(dpy, &e);
	    if (e.type == MapNotify)
		  break;
      }

}

void myDraw(int ncols, int nrows) {

    int x1,y1; 
    int i,j; 
    XSetForeground(dpy, gc, whiteColor);
    XFillRectangle(dpy,buffer,gc,0,0,IMAGE_WIDTH,IMAGE_HEIGHT);
    int rect_width=(int)((double)IMAGE_WIDTH/(double)(ncols+1));
    int rect_height=(int)((double)IMAGE_HEIGHT/(double)(nrows+1));
    for (i=1;i<=ncols;i++) {
        x1 = (int)((double)(i-1)/(double)(ncols+1)*(double)IMAGE_WIDTH);
        for (j=1;j<=nrows;j++) {
            y1 = (int)((double)(j-1)/(double)(nrows+1)*(double)IMAGE_HEIGHT);
            if (grid[i][j]>0) {
                int masa = grid[i][j];
                if (masa>numXGrayscale-1) masa=numXGrayscale-1;
                XSetForeground(dpy, gc, Xgrayscale[masa].pixel);
            } else {
                XSetForeground(dpy, gc, whiteColor);
            }
            XFillRectangle(dpy,buffer,gc,x1,y1,rect_width,rect_height);
         }
     }
     
     XCopyArea(dpy, buffer, w, gc, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT,  0, 0);
     XFlush(dpy);
	  
}
