/* Trabalho realizado pelo grupo SO-007:
Diogo Freaza nº56969
Francisco Resendes nº57162
Guilherme Dias nº57163
*/
#include "main.h"
#include "memory.h"


/**
 * Edit's the correspondent operation in data->result by substituting it
*/
void editOpInResults (struct operation* op, struct main_data* data){
    int boolean = 0;
    for (int i = 0; i < MAX_RESULTS && boolean == 0; i++){
        if(data->results[i].id == op->id){
            data->results[i] = *op;
            boolean = 1;
        }
    }
}