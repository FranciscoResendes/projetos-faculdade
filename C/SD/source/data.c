/* 
    Diogo Freaza 56969
    Francisco Resendes 57162
    Pedro Barata 54483 
*/

#include <stdlib.h>
#include <string.h>


#include "data.h"

// struct data_t {
// 	int datasize; /* Tamanho do bloco de dados */
// 	void *data;   /* Conteúdo arbitrário */
// };

/* Função que cria um novo elemento de dados data_t e que inicializa 
 * os dados de acordo com os argumentos recebidos, sem necessidade de
 * reservar memória para os dados.	
 * Retorna a nova estrutura ou NULL em caso de erro.
 */
struct data_t *data_create(int size, void *data){ 
    if(size <= 0 || data == NULL){
       return NULL;
    }

    struct data_t *d = (struct data_t*)  malloc(sizeof(struct data_t));
    d->datasize = size;//nao é alocado memoria para aqui por causa da linha 13
    d->data = data;

    return d;
} 

/* Função que elimina um bloco de dados, apontado pelo parâmetro data,
 * libertando toda a memória por ele ocupada.
 * Retorna 0 (OK) ou -1 em caso de erro.
 */
int data_destroy(struct data_t *data){
    
    if (data == NULL || data->data == NULL ){//será necessário verificar se datasize <= 0?
        return -1;
    }
    free(data->data);//Será necessário já que é POD?
    //free(&(data->datasize)); //Aparentemente há um free a mais destes 3
    free(data);
    return 0;
}

/* Função que duplica uma estrutura data_t, reservando a memória
 * necessária para a nova estrutura.
 * Retorna a nova estrutura ou NULL em caso de erro.
 */
struct data_t *data_dup(struct data_t *data){
    if(data == NULL || data->datasize <= 0 || data->data == NULL){
        return NULL;
    }

    struct data_t *d = (struct data_t*)  malloc(sizeof(struct data_t));

    //struct data_t *d = data_create(data->datasize, data->data);

    d->data = malloc(data->datasize);
    int size = sizeof(data);
    memcpy(d, data, size);
    memcpy(d->data, data->data, data->datasize);
    return d;
}

/* Função que substitui o conteúdo de um elemento de dados data_t.
 * Deve assegurar que liberta o espaço ocupado pelo conteúdo antigo.
 * Retorna 0 (OK) ou -1 em caso de erro.
 */
int data_replace(struct data_t *data, int new_size, void *new_data){
    if(data == NULL || data->datasize <= 0 || data->data == NULL){
        return -1;
    }

    //free(data->data);
    data->datasize = new_size;
    //data->data = malloc(new_size);
    data->data = new_data;
    return 0;
}


