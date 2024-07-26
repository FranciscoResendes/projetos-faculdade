#include "memory.h"
#include "main.h"

/**
  * Checks if the circular_buffer is full, 1 if yes, 0 if not
 */
int isCircularFull(struct circular_buffer* buffer, int buffer_size) {
    return ((buffer->ptrs->in + 1) % buffer_size) == buffer->ptrs->out;
}

/**
 * Checks if the circular_buffer is empty, 1 if empty, 0 if not
 */
int isCircularEmpty(struct circular_buffer* buffer) {
    return buffer->ptrs->in == buffer->ptrs->out;
}