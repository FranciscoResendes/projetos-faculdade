#ifndef _THREADS_PRIVATE_H
#define _THREADS_PRIVATE_H

#include <pthread.h>

void start_read(pthread_mutex_t m, pthread_cond_t c, int write_n, int write_w);

void end_read(pthread_mutex_t m, pthread_cond_t c, int read_n);

void start_write(pthread_mutex_t m, pthread_cond_t c, int write_n, int write_w, int read_n);


void end_write(pthread_mutex_t m, pthread_cond_t c, int write_n);
#endif
