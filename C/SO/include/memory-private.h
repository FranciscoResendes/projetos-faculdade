#include "main.h"
#include "memory.h"


/** Ficheiro com as funções auxiliares*/

 /**
  * Checks if the circular_buffer is full, 1 if yes, 0 if not
 */
int isCircularFull(struct circular_buffer* buffer, int buffer_size);

/**
 * Checks if the circular_buffer is empty, 1 if empty, 0 if not
*/
int isCircularEmpty(struct circular_buffer* buffer);

/**
 * Writes operation to the data's histoy/log 
*/
void writeOperationToDatasLog(struct main_data* data, struct operation* op);