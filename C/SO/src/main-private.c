#include <string.h>
#include <stdio.h>

#include "memory.h"
#include "main.h"

/**
 * Writes operation to the data's histoy/log (the results variable)
*/
void writeOperationToDatasLog(struct main_data* data, struct operation* op){
    int done = 0;
    int i = 0;
    while (done == 0 && i < MAX_RESULTS){
        //"o que está lá dentro" é igual a um objeto do mesmo tipo cheio de 0's?
        if (memcmp(&data->results[i], &(struct operation){0}, sizeof(struct operation)) == 0){ //if empty (see how data->results is created in main)
            data->results[i] = *op;
            done = 1;
        }
        i++;
    }
}

/**
 * Print's the operation's status
*/
void printOperationStatus(struct operation* op){
    printf("Pedido %d com estado %c requisitado pelo cliente %d à empresa %d", op->id, op->status, op->requesting_client, op->requested_enterp);
    if (op->receiving_client != -1){
        printf(", foi recebido pelo cliente %d",op->receiving_client);
        if (op->receiving_interm != -1){
            printf(", processado pelo intermediário %d", op->receiving_interm);
            if (op->receiving_enterp != -1){
               printf(", e tratado pela empresa %d!",op->receiving_enterp);
            }
        }
    }
    printf("\n");
}