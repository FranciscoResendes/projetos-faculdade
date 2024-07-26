/* Trabalho realizado pelo grupo SO-007:
Diogo Freaza nº56969
Francisco Resendes nº57162
Guilherme Dias nº57163
*/

#include<stdio.h>

#include "memory.h"
#include "main.h"
#include "private.h"


/* Função que lê uma operação do buffer de memória partilhada entre clientes e intermediários.
* Antes de tentar ler a operação, deve verificar se data->terminate tem valor 1.
* Em caso afirmativo, retorna imediatamente da função.
*/
void intermediary_receive_operation(struct operation* op, struct comm_buffers* buffers, struct main_data* data){
    if (*data->terminate != 1){
        read_client_interm_buffer(buffers->client_interm, data->buffers_size, op);
    }
}


/* Função que processa uma operação, alterando o seu campo receiving_intermediary para o id
* passado como argumento, alterando o estado da mesma para 'I' (intermediary), e 
* incrementando o contador de operações. Atualiza também a operação na estrutura data.
*/
void intermediary_process_operation(struct operation* op, int interm_id, struct main_data* data, int* counter){
    op->receiving_interm = interm_id;
    op->status = 'I';
    ++*counter;
    printf("Intermediário recebeu pedido!\n");
    editOpInResults(op, data);
}


/* Função que escreve uma operação no buffer de memória partilhada entre
* intermediários e empresas.
*/
void intermediary_send_answer(struct operation* op, struct comm_buffers* buffers, struct main_data* data){
    write_interm_enterp_buffer(buffers->interm_enterp,data->buffers_size,op);
}


/* Função principal de um Intermediário. Deve executar um ciclo infinito onde em 
* cada iteração lê uma operação dos clientes e se a mesma tiver id 
* diferente de -1 e se data->terminate ainda for igual a 0, processa-a e
* envia a mesma para as empresas. Operações com id igual a -1 são 
* ignoradas (op inválida) e se data->terminate for igual a 1 é porque foi 
* dada ordem de terminação do programa, portanto deve-se fazer return do
* número de operações processadas. Para efetuar estes passos, pode usar os
* outros métodos auxiliares definidos em intermediary.h.
*/
int execute_intermediary(int interm_id, struct comm_buffers* buffers, struct main_data* data){
    struct operation* op = &(struct operation){-1}; // Talvez esta operação necessite de ser revista (compiund literals)
    while (*data->terminate == 0){
        intermediary_receive_operation(op, buffers, data);
        if (op->id != -1 && *data->terminate != 1){
            intermediary_process_operation(op, interm_id,data,data->intermediary_stats);
            intermediary_send_answer(op, buffers, data);
        }
    }
    return *data->intermediary_stats;
}
