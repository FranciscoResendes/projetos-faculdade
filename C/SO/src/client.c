/* Trabalho realizado pelo grupo SO-007:
Diogo Freaza nº56969
Francisco Resendes nº57162
Guilherme Dias nº57163
*/

#include <stdio.h>

#include "main.h"
#include "memory.h"
#include "private.h"


 
/* Função que lê uma operação do buffer de memória partilhada entre a main
* e clientes que seja direcionada a client_id. Antes de tentar ler a operação, deve
* verificar se data->terminate tem valor 1. Em caso afirmativo, retorna imediatamente da função.
*/
void client_get_operation(struct operation* op, int client_id, struct comm_buffers* buffers, struct main_data* data){
    if (*data->terminate != 1){
        read_main_client_buffer(buffers->main_client,client_id,data->buffers_size, op); //(lê para dentro de op o valor respetivo)
    }
}


/* Função que processa uma operação, alterando o seu campo receiving_client para o id
* passado como argumento, alterando o estado da mesma para 'C' (client), e 
* incrementando o contador de operações. Atualiza também a operação na estrutura data.
*/
void client_process_operation(struct operation* op, int client_id, struct main_data* data, int* counter){
    op->receiving_client = client_id;
    op->status = 'C';
    ++*counter;
    int boolean = 0;
    printf("Cliente recebeu pedido!\n");
    editOpInResults(op, data);
}

/* Função que escreve uma operação no buffer de memória partilhada entre
* clientes e intermediários.
*/
void client_send_operation(struct operation* op, struct comm_buffers* buffers, struct main_data* data){
    write_client_interm_buffer(buffers->client_interm,data->buffers_size,op);
}

/* Função principal de um Cliente. Deve executar um ciclo infinito onde em 
* cada iteração lê uma operação da main e se data->terminate ainda 
* for igual a 0, processa-a. Operações com id igual a -1 são ignoradas
* (op inválida) e se data->terminate for igual a 1 é porque foi dada ordem
* de terminação do programa, portanto deve-se fazer return do número de 
* operações processadas. Para efetuar estes passos, pode usar os outros
* métodos auxiliares definidos em client.h.
*/
int execute_client(int client_id, struct comm_buffers* buffers, struct main_data* data){
    struct operation* op = &(struct operation){-1}; // Talvez esta operação necessite de ser revista (compiund literals)
    int i = 0;
    while (1){    
        if(*data->terminate == 1){
            return *data->client_stats;
        }
        client_get_operation(op, client_id, buffers, data);
        if (op->id != -1 && *data->terminate != 1){
            client_process_operation(op, client_id, data, data->client_stats);           
            client_send_operation(op,buffers,data);
        }
        i++;
    }
}