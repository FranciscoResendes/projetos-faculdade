/* 
    Diogo Freaza 56969
    Francisco Resendes 57162
    Pedro Barata 54483 
*/
#include <pthread.h>
#include "threads-private.h"

void start_read(pthread_mutex_t m, pthread_cond_t c, int write_n, int write_w){
    pthread_mutex_lock(&m);
    while(write_n > 0 || write_w > 0){
        pthread_cond_wait(&c, &m);
    }
    pthread_mutex_unlock(&m);
}

void end_read(pthread_mutex_t m, pthread_cond_t c, int read_n){
    pthread_mutex_lock(&m);
    read_n--;
    if(read_n == 0){
        pthread_cond_broadcast(&c);
    }
    pthread_mutex_unlock(&m);
}

void start_write(pthread_mutex_t m, pthread_cond_t c, int write_n, int write_w, int read_n){
    pthread_mutex_lock(&m);
    write_w++;
    while(write_n > 0 || read_n > 0){
        pthread_cond_wait(&c, &m);
    }
    write_w--;
    write_n++;
    pthread_mutex_unlock(&m);
}

void end_write(pthread_mutex_t m, pthread_cond_t c, int write_n){
    pthread_mutex_lock(&m);
    write_n--;
    pthread_cond_broadcast(&c);
    pthread_mutex_unlock(&m);
}