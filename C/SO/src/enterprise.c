/* Trabalho realizado pelo grupo SO-007:
Diogo Freaza nº56969
Francisco Resendes nº57162
Guilherme Dias nº57163
*/

#include<stdio.h>

#include "memory.h"
#include "main.h"
#include "private.h"

/* Função que lê uma operação do buffer de memória partilhada entre
* os intermediários e as empresas que seja direcionada à empresa enterprise_id.
* Antes de tentar ler a operação, o processo deve verificar se data->terminate
* tem valor 1. Em caso afirmativo, retorna imediatamente da função.
*/
void enterprise_receive_operation(struct operation* op, int enterp_id, struct comm_buffers* buffers, struct main_data* data){
    if (*data->terminate != 1){
        read_interm_enterp_buffer(buffers->interm_enterp,enterp_id,data->buffers_size,op);
    }
}

/* Função que processa uma operação, alterando o seu campo receiving_enterp para o id
* passado como argumento, alterando o estado da mesma para 'E' ou 'A' conforme o número
* máximo de operações já tiver sido atingido ou não, e incrementando o contador de operações.
* Atualiza também a operação na estrutura data.
*/
void enterprise_process_operation(struct operation* op, int enterp_id, struct main_data* data, int* counter){
    op->receiving_enterp = enterp_id;

    if(*counter < data->max_ops){
        op->status = 'E';
    }
    else{
        op->status = 'A';
    }
    printf("Empresa recebeu pedido!\n");
    ++*counter;
    editOpInResults(op, data);
}

/* Função principal de uma Empresa. Deve executar um ciclo infinito onde em 
* cada iteração lê uma operação e se e data->terminate ainda for igual a 0, processa-a.
* Operações com id igual a -1 são ignoradas (op inválida) e se data->terminate for igual
* a 1 é porque foi dada ordem de terminação do programa, portanto deve-se fazer return do
* número de operações processadas. Para efetuar estes passos, pode usar os outros
* métodos auxiliares definidos em enterprise.h.
*/
int execute_enterprise(int enterp_id, struct comm_buffers* buffers, struct main_data* data){
    struct operation* op = &(struct operation){-1}; // Talvez esta operação necessite de ser revista (compiund literals)
    while (*data->terminate == 0){
        enterprise_receive_operation(op, enterp_id, buffers, data);
        if (op->id != -1 && *data->terminate != 1){
            enterprise_process_operation(op,enterp_id,data,data->enterprise_stats);
        }
    }
    return *data->enterprise_stats;
}
