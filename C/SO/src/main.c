/* Trabalho realizado pelo grupo SO-007:
Diogo Freaza nº56969
Francisco Resendes nº57162
Guilherme Dias nº57163
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "main.h"
#include "memory.h"
#include "process.h"
#include "main-private.h"


/* Função que lê os argumentos da aplicação, nomeadamente o número
* máximo de operações, o tamanho dos buffers de memória partilhada
* usados para comunicação, e o número de clientes, de intermediários e de
* empresas. Guarda esta informação nos campos apropriados da
* estrutura main_data. 
*/
void main_args(int argc, char* argv[], struct main_data* data) {

	if (argc != 6) { //mais argumentos ou menos argumentos que o necessário
        fprintf(stderr, "Usage: %s max_ops buffers_size n_clients n_intermediaries n_enterprises\n", argv[0]);
        exit(EXIT_FAILURE);
    }
    
    data->max_ops = atoi(argv[1]);
    data->buffers_size = atoi(argv[2]);
    data->n_clients = atoi(argv[3]);
    data->n_intermediaries = atoi(argv[4]);
    data->n_enterprises = atoi(argv[5]);
}

/* Função que reserva a memória dinâmica necessária para a execução
* do AdmPor, nomeadamente para os arrays *_pids e *_stats da estrutura 
* main_data. Para tal, pode ser usada a função create_dynamic_memory.
*/
void create_dynamic_memory_buffers(struct main_data* data){

	//Clientes
	data->client_pids = create_dynamic_memory(sizeof(data->client_pids) * data->n_clients);
	data->client_stats = create_dynamic_memory(sizeof(data->client_stats) * data->max_ops);
	
	//Intermediary
	data->intermediary_pids = create_dynamic_memory(sizeof(data->intermediary_pids) * data->n_intermediaries);
	data->intermediary_stats =  create_dynamic_memory(sizeof(data->intermediary_stats) * data->max_ops);

	//Enterprise 
	data->enterprise_pids  = create_dynamic_memory(sizeof(data->enterprise_pids) * data->n_enterprises);
	data->enterprise_stats = create_dynamic_memory(sizeof(data->enterprise_stats) * data->max_ops);

}

/* Função que reserva a memória partilhada necessária para a execução do
* AdmPor. É necessário reservar memória partilhada para todos os buffers da
* estrutura comm_buffers, incluindo os buffers em si e respetivos
* pointers, assim como para o array data->results e variável data->terminate.
* Para tal, pode ser usada a função create_shared_memory. O array data->results
* deve ser limitado pela constante MAX_RESULTS.
*/
void create_shared_memory_buffers(struct main_data* data, struct comm_buffers* buffers){
	//memória dos buffers
	buffers->main_client->ptrs = create_shared_memory("SHM_MAIN_CLIENT_PTR", sizeof(buffers->main_client->ptrs) * data->buffers_size);
	buffers->main_client->buffer = create_shared_memory("SHM_MAIN_CLIENT_BUFFER", sizeof(buffers->main_client->buffer) * data->buffers_size);
	buffers->client_interm->ptrs = create_shared_memory("SHM_CLIENT_INTERM_PTR", sizeof(buffers->client_interm->ptrs) * data->buffers_size);
	buffers->client_interm->buffer = create_shared_memory("SHM_CLIENT_INTERM_BUFFER", sizeof(buffers->client_interm->buffer) * data->buffers_size);
	buffers->interm_enterp->ptrs = create_shared_memory("SHM_INTERM_ENTERP_PTR", sizeof(buffers->interm_enterp->ptrs) * data->buffers_size);
	buffers->interm_enterp->buffer = create_shared_memory("SHM_INTERM_ENTERP_BUFFER", sizeof(buffers->interm_enterp->buffer) * data->buffers_size);
	
	//memória do array results e variável terminate
	data->results = create_shared_memory("SHM_RESULTS", sizeof(data->results) * MAX_RESULTS);
	data->terminate = create_shared_memory("SHM_TERMINATE", sizeof(int));	
}

/* Função que inicia os processos dos clientes, intermediários e
* empresas. Para tal, pode usar as funções launch_*,
* guardando os pids resultantes nos arrays respetivos
* da estrutura data.
*/
void launch_processes(struct comm_buffers* buffers, struct main_data* data){
	for(int i = 0; i <  data->n_clients; i++){
		int pid = launch_client(i, buffers, data);
		data->client_pids[i] = pid;
	}
	for(int i = 0; i < data->n_intermediaries; i++){
		int pid2 = launch_interm(i, buffers, data);
		data->intermediary_pids[i] = pid2;
	}
	for(int i = 0; i < data->n_enterprises; i++){
		int pid3 = launch_enterp(i, buffers, data);
		data->enterprise_pids[i] = pid3;
	}
}

/* Função que faz interação do utilizador, podendo receber 4 comandos:
* op - cria uma nova operação, através da função create_request
* status - verifica o estado de uma operação através da função read_status
* stop - termina o execução do AdmPor através da função stop_execution
* help - imprime informação sobre os comandos disponiveis
*/
void user_interaction(struct comm_buffers* buffers, struct main_data* data){
	char command [20] = "";
	int *counter = calloc(1, sizeof(int));
	printf("Ações disponíveis:\n");
	printf("	op cliente empresa - criar uma nova operação\n");
	printf("	status id - consultar o estado de uma operação\n");
	printf("	stop - termina a execução do AdmPor.\n");
	printf("	help - imprime informação sobre as ações disponíveis.\n");
	while(strcmp(command, "stop") != 0){
		printf("Introduzir ação\n");
		scanf("%s", command);
		if(strcmp(command, "op") == 0){
			create_request(counter, buffers, data);
		}
		else if(strcmp(command, "status") == 0){
			read_status(data);
		}
		else if(strcmp(command, "stop") == 0){
			stop_execution(data, buffers);
		}
		else if(strcmp(command, "help") == 0){
			printf("Ações disponíveis:\n");
			printf("	op cliente empresa - criar uma nova operação\n");
			printf("	status id - consultar o estado de uma operação\n");
			printf("	stop - termina a execução do AdmPor.\n");
			printf("	help - imprime informação sobre as ações disponíveis.\n");
		}
		else{
			printf("Ação não reconhecida, insira 'help' para assistência.\n");
		}
		usleep(100000);
	}
	free(counter);
}

/* Cria uma nova operação identificada pelo valor atual de op_counter e com os 
* dados introduzidos pelo utilizador na linha de comandos, escrevendo a mesma 
* no buffer de memória partilhada entre main e clientes. Imprime o id da 
* operação e incrementa o contador de operações op_counter. Não deve criar 
* mais operações para além do tamanho do array data->results.
*/
void create_request(int* op_counter, struct comm_buffers* buffers, struct main_data* data){
	int client_request, enterprise_request;
	scanf("%d", &client_request);
	scanf("%d", &enterprise_request);
	printf("O pedido #%d foi criado!\n", *op_counter);
	if(*op_counter < MAX_RESULTS){
		struct operation op = {*op_counter, client_request, enterprise_request, 'M', -1, -1, -1};
		write_main_client_buffer(buffers->main_client, data->buffers_size, &op);
		writeOperationToDatasLog(data, &op);
		++*op_counter;
	}
}

/* Função que lê um id de operação do utilizador e verifica se a mesma é valida.
* Em caso afirmativo imprime informação da mesma, nomeadamente o seu estado, o 
* id do cliente que fez o pedido, o id da empresa requisitada, e os ids do cliente,
* intermediário, e empresa que a receberam e processaram.
*/
void read_status(struct main_data* data){
	int id;
	int i = 0;
	int done = 0;
	scanf("%d", &id);
	if(id != -1){
		while(i < MAX_RESULTS && done == 0){
			if(id == data->results[i].id && data->results[i].status != '\0'){
				printOperationStatus(&data->results[i]);
				done = 1;
			}
			i++;
		}
		if(done == 0) { //pedido não encontrado ou futuramente válido
			printf("Pedido %d não é válido!\n", id);
		}
	}
	else { //pedido inválido
		printf("id de pedido fornecido é inválido!\n");
	}
}


/* Função que termina a execução do programa AdmPor. Deve começar por 
* afetar a flag data->terminate com o valor 1. De seguida, e por esta
* ordem, deve esperar que os processos filho terminem, deve escrever as
* estatisticas finais do programa, e por fim libertar
* as zonas de memória partilhada e dinâmica previamente 
* reservadas. Para tal, pode usar as outras funções auxiliares do main.h.
*/
void stop_execution(struct main_data* data, struct comm_buffers* buffers){
	*data->terminate = 1;
	wait_processes(data);
	printf("Terminando o AdmPor! Imprimindo estatísticas:\n");
	write_statistics(data);
	destroy_memory_buffers(data, buffers);
}

/* Função que espera que todos os processos previamente iniciados terminem,
* incluindo clientes, intermediários e empresas. Para tal, pode usar a função 
* wait_process do process.h.
*/
void wait_processes(struct main_data* data){
	int counter = 0;
	for(int i = 0; i < data->n_clients; i++){
		data->client_stats[i] = wait_process(data->client_pids[i]);
	}
	for(int i = 0; i < data->n_intermediaries; i++){
		data->intermediary_stats[i] = wait_process(data->intermediary_pids[i]);
	}
	for(int i = 0; i < data->n_enterprises; i++){
		data->enterprise_stats[i] = wait_process(data->enterprise_pids[i]);
	}
}

/* Função que imprime as estatisticas finais do AdmPor, nomeadamente quantas
* operações foram processadas por cada cliente, intermediário e empresa.
*/
void write_statistics(struct main_data* data){
	
	for (int i = 0; i < data->n_clients; i++){
		printf("Cliente %d preparou %d pedidos!\n", i, data->client_stats[i]);
	}
	for (int i = 0; i < data->n_intermediaries; i++){
		printf("Intermediário %d entregou %d pedidos!\n", i, data->intermediary_stats[i]);
	}
	for (int i = 0; i < data->n_enterprises; i++){
		printf("Empresa %d recebeu %d pedidos!\n", i, data->enterprise_stats[i]);
	}
}

/* Função que liberta todos os buffers de memória dinâmica e partilhada previamente
* reservados na estrutura data.
*/
void destroy_memory_buffers(struct main_data* data, struct comm_buffers* buffers){
	destroy_shared_memory("SHM_MAIN_CLIENT_PTR", buffers->main_client->ptrs, sizeof(buffers->main_client->ptrs) * data->buffers_size);
	destroy_shared_memory("SHM_MAIN_CLIENT_BUFFER", buffers->main_client->buffer, sizeof(buffers->main_client->buffer) * data->buffers_size);
	destroy_shared_memory("SHM_CLIENT_INTERM_PTR", buffers->client_interm->ptrs, sizeof(buffers->client_interm->ptrs) * data->buffers_size);
	destroy_shared_memory("SHM_CLIENT_INTERM_BUFFER", buffers->client_interm->buffer, sizeof(buffers->client_interm->buffer) * data->buffers_size);
	destroy_shared_memory("SHM_INTERM_ENTERP_PTR", buffers->interm_enterp->ptrs, sizeof(buffers->interm_enterp->ptrs) * data->buffers_size);
	destroy_shared_memory("SHM_INTERM_ENTERP_BUFFER", buffers->interm_enterp->buffer, sizeof(buffers->interm_enterp->buffer) * data->buffers_size);

	destroy_shared_memory("SHM_RESULTS", data->results, sizeof(data->results) * MAX_RESULTS);
	destroy_shared_memory("SHM_TERMINATE", data->terminate, sizeof(data->terminate));
}


int main(int argc, char *argv[]) { 
 //init data structures 
 struct main_data* data = create_dynamic_memory(sizeof(struct main_data)); 
 struct comm_buffers* buffers = create_dynamic_memory(sizeof(struct comm_buffers)); 
 buffers->main_client = create_dynamic_memory(sizeof(struct circular_buffer)); 
 buffers->client_interm = create_dynamic_memory(sizeof(struct rnd_access_buffer)); 
 buffers->interm_enterp = create_dynamic_memory(sizeof(struct circular_buffer)); 
 
 //execute main code 
 main_args(argc, argv, data); 
 create_dynamic_memory_buffers(data); 
 create_shared_memory_buffers(data, buffers); 
 launch_processes(buffers, data); 
 user_interaction(buffers, data); 
  
 //release memory before terminating 
 destroy_dynamic_memory(data); 
 destroy_dynamic_memory(buffers->main_client); 
 destroy_dynamic_memory(buffers->client_interm); 
 destroy_dynamic_memory(buffers->interm_enterp); 
 destroy_dynamic_memory(buffers); 
} 
