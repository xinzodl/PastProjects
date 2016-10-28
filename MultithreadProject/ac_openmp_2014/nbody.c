#include <stdlib.h>
#include <stdio.h>
#include "nbody.h"
#include "video.h"
#include <time.h>
#include <math.h>
#include <string.h>
#include <sys/time.h>

double *x, *y; 
double *fx, *fy, *vx, *vy, *m;
int    nbodies;
int    width;
int    height;
int    nsteps;
double g;
double interval;
double min_dist;

int    **grid;

int init_bodies(char *file) {
     FILE *f; 
     int i;
 
     f = fopen(file,"r");
	
     fscanf(f,"%d %lf %lf %d %lf %d %d\n",&nbodies,&g,&interval,&nsteps,&min_dist,&width,&height);
     x  = malloc(sizeof(double) * nbodies);
     y  = malloc(sizeof(double) * nbodies);
     fx = malloc(sizeof(double) * nbodies);
     fy = malloc(sizeof(double) * nbodies);
     vx = malloc(sizeof(double) * nbodies);
     vy = malloc(sizeof(double) * nbodies);
     m  = malloc(sizeof(double) * nbodies);
     for (i = 0; i < nbodies; i++) {
		fscanf(f,"%lf %lf %lf %lf %lf\n",&x[i],&y[i],&vx[i],&vy[i],&m[i]);
     }
     fclose(f);
     printf ("NBODY PROBLEM____________________________________________________________________\n");
     printf ("nbodies: %d\tg var: %0.2lf\tinterval: %0.2lfsec\tnsteps: %d\tmin_dist: %0.2lfp\twidth %d\theight: %d\n", nbodies,g,interval,nsteps,min_dist,width,height);
     return 1;
}

int finalize_nbodies(char *file) {
     FILE *f;
     int i;
     
     f = fopen(file,"w");
     fprintf(f,"%d %lf %lf %d %lf %d %d\n",nbodies,g,interval, nsteps,min_dist,width,height);
     for (i = 0; i < nbodies; i++) {
                fprintf(f,"%lf %lf %lf %lf %lf\n",x[i],y[i],vx[i],vy[i],m[i]);
     }
     fclose(f);
     free(x);
     free(y);
     free(fx);
     free(fy);
     free(vx);
     free(vy);
     free(m);
     return 1;
}

double factorial(double x) {
 double fac = 1;
 int i;
 for (i=2; i<=x; i++) fac *= i;
 return fac;
}

double mypow(double x, double exp) {
 double result = 1;
 int i;

 result = 1;
 for (i=0; i<exp; i++) result *= x;
 return result;
}

double mysin(double alpha) {
      double result = 0;
      int i;

      for (i=0; i < MAX_ACCURACY;i++) {
            result += (double)(mypow(-1,i) / factorial((double)(2*i+1))) * (double)mypow(alpha, 2*i+1);
      }
      return result;
}

double mycos(double alpha) {
      double result = 0;
      int i;

      for (i=0; i < MAX_ACCURACY;i++) {
            result += (double)(mypow(-1,i) / factorial((double)(2*i))) * (double)mypow(alpha, 2*i);
      }
      return result;
}


int calc_nbodies() {
     double *ax, *ay;
     double fax, fay;
     double dist, f, alpha, aux;
     int i,j;     

     ax = malloc(sizeof(double) * nbodies);
     ay = malloc(sizeof(double) * nbodies);

     for (i = 0; i < nbodies; i++) {
	fx[i] = fy[i] = 0;
     }

     for (i = 0; i < nbodies; i++) {
       for (j = i + 1; j < nbodies; j++) {
          dist = sqrt((x[j] - x[i]) * (x[j] - x[i]) + (y[j] - y[i]) * (y[j] - y[i]));
          if (dist > min_dist ) {
               f = g * m[i] * m[j] / (dist * dist);
               if (f > MAX_F) f = MAX_F;
               aux  = (y[j]-y[i]) / (x[j]-x[i]);
               if (aux > 1)
			aux = aux-(((int)aux)/1);
               else if (aux < -1)
			aux = aux+(((int)aux)/-1);
	       alpha = atan(aux);
               
	       fax   = f * mycos(alpha);
	       fay   = f * mysin(alpha);
	       fx[i] += fax;
               fy[i] += fay;
               fx[j] -= fax; 
               fy[j] -= fay;
          }
       }
     }
     for (i = 0; i < nbodies; i++) {
          ax[i] = fx[i] / m[i];
          ay[i] = fy[i] / m[i];
          vx[i] = vx[i] + ax[i] * interval;
          vy[i] = vy[i] + ay[i] * interval;
          x[i] = x[i] + vx[i] * interval;
          y[i] = y[i] + vy[i] * interval;
          
          if (x[i] <= 0) {
             x[i] = 2;
             vx[i] = - vx[i];
          }
          if (x[i] >= width ) {
             x[i] = width - 2;
             vx[i] = - vx[i];
          }
          if (y[i] <= 0) {
             y[i] = 2;
             vy[i] = - vy[i];
          }
          if (y[i] >= height) {
             y[i] = height - 2;
             vy[i] = - vy[i];
          }
          //printf("body %d: x=%d, y=%d vx=%lf vy=%lf ax=%lf ay=%lf fx=%lf  fy=%lf\n", i, x[i], y[i],vx[i],vy[i],ax[i],ay[i],fx[i],fy[i]);
     }

     free(ax);
     free(ay);

     return 1;
}
 
int main (int argc, char ** argv) {
     int i,iter;
     int x11 = 0;
     struct timeval start, end;
     double usec = 0;
     

     iter=0;
     if (argc < 4) {
        printf ("Usage:\n");
	printf ("   %s <input_file> <output_file> <X11_0|1>\n", argv[0]);
	printf ("Where:\n");
	printf ("   <X11_0|1> can be 0 or 1. 0 means no X11, 1 means use X11.\n");
        return 0;
     }
     if (!strcmp(argv[3],"X11_1")) x11 = 1;
     init_bodies(argv[1]);
     if (x11) {
         allocate_grid(width, height);
         setupWindow(width, height);
     }
     for (iter = 0;iter < nsteps;iter++) {
         gettimeofday(&start, NULL); 
         calc_nbodies();
         gettimeofday(&end, NULL); 
         usec += end.tv_usec + 1000000*(end.tv_sec - start.tv_sec) - start.tv_usec;
         if (x11) {
             init_grid(width, height);
             for (i=0; i < nbodies; i++) {
                  grid[(int)x[i]][(int)y[i]]= m[i];
             }
             if(iter % REFRESH ==0 ) myDraw(width, height);
         }
     }
     if (x11) 
         cleanup_grid(width, height);
     finalize_nbodies(argv[2]);
     printf ("nbody takes: %0.2fsec    mean: %0.2fsec    accuracy: %d \n", (double)usec / 1000000, (double)usec / 1000000/nsteps,MAX_ACCURACY);
     return 1;
}
