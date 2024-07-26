/* Trabalho realizado pelo grupo SO-007:
Diogo Freaza nº56969
Francisco Resendes nº57162
Guilherme Dias nº57163
*/

#include<stdlib.h>
#include<sys/mman.h>
#include<sys/stat.h>
#include<fcntl.h>
#include <stdio.h>
#include <unistd.h>

#include "memory-private.h"
#include "main.h"

/* Função que reserva uma zona de memória partilhada com tamanho indicado
* por size e nome name, preenche essa zona de memória com o valor 0, e 
* retorna um apontador para a mesma. Pode concatenar o resultado da função
* getuid() a name, para tornar o nome único para o processo.
*/
void* create_shared_memory(char* name, int size){
	//podemos usar getuid() em vez de name na função abaixo.
	int fd = shm_open(name, O_RDWR | O_CREAT, S_IRUSR | S_IWUSR);
	int ret;
	int *ptr;

	if ( fd == -1){
		perror(name);
		exit(1);
	}

	ret = ftruncate(fd, size);

	if (ret == -1){
		perror(name);
		exit(2);
	}

	ptr = mmap(0, size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);

	if (ptr == MAP_FAILED){
		perror(name);
		exit(3);
	}
	return ptr;
}

/* Função que reserva uma zona de memória dinâmica com tamanho indicado
* por size, preenche essa zona de memória com o valor 0, e retorna um 
* apontador para a mesma.
*/
void* create_dynamic_memory(int size){
	int *dm = calloc(size, sizeof(int));
	return dm;
}


/* Função que liberta uma zona de memória dinâmica previamente reservada.
*/
void destroy_shared_memory(char* name, void* ptr, int size){
	int ret = munmap(ptr, size);
	if (ret == -1){
		perror(name);
		exit(7);
	}
	ret = shm_unlink(name);
	if (ret == -1){
		perror(name);
		exit(8);
	}
}

/* Função que liberta uma zona de memória partilhada previamente reservada.
*/
void destroy_dynamic_memory(void* ptr){
	free(ptr);
}

/* Função que escreve uma operação no buffer de memória partilhada entre a Main
* e os clientes. A operação deve ser escrita numa posição livre do buffer, 
* tendo em conta o tipo de buffer e as regras de escrita em buffers desse tipo.
* Se não houver nenhuma posição livre, não escreve nada.
*/
void write_main_client_buffer(struct rnd_access_buffer* buffer, int buffer_size, struct operation* op){
	int *ptrs = buffer->ptrs;
	int i = 0;
	int done = 0;
	while (i < buffer_size && done == 0){
		if (ptrs[i] == 0){ //se o buffer está vazio na posição, then
			ptrs[i] = 1;			 //escreve no array 1's e 0's
			buffer->buffer[i] = *op; //escreve no buffer
			done = 1; 				 //sai do loop
		}
		i++;
	}
}


/* Função que escreve uma operação no buffer de memória partilhada entre os clientes
* e intermediários. A operação deve ser escrita numa posição livre do buffer, 
* tendo em conta o tipo de buffer e as regras de escrita em buffers desse tipo.
* Se não houver nenhuma posição livre, não escreve nada.
*/
void write_client_interm_buffer(struct circular_buffer* buffer, int buffer_size, struct operation* op){
	int in = buffer->ptrs->in;
	int out = buffer->ptrs->out;
	if (!isCircularFull(buffer, buffer_size)){//se não estiver cheio:
		buffer->buffer[in] = *op;			//escreve
		buffer->ptrs->in = (buffer->ptrs->in + 1) % buffer_size; //atualiza o pointer de input
	}
	//REVIEW G
}

/* Função que escreve uma operação no buffer de memória partilhada entre os intermediários
* e as empresas. A operação deve ser escrita numa posição livre do buffer, 
* tendo em conta o tipo de buffer e as regras de escrita em buffers desse tipo.
* Se não houver nenhuma posição livre, não escreve nada.
*/
void write_interm_enterp_buffer(struct rnd_access_buffer* buffer, int buffer_size, struct operation* op){
	int *ptrs = buffer->ptrs;
	int i = 0;
	int done = 0;
	while (i < buffer_size && done == 0){
		if (ptrs[i] == 0){ //se o buffer está vazio na posição, then
			ptrs[i] = 1;			 //escreve no array 1's e 0's
			buffer->buffer[i] = *op; //escreve no buffer
			done = 1; 				 //sai do loop
		}
		i++;
	}
}


/* Função que lê uma operação do buffer de memória partilhada entre a Main
* e os clientes, se houver alguma disponível para ler que seja direcionada ao cliente especificado.
* A leitura deve ser feita tendo em conta o tipo de buffer e as regras de leitura em buffers desse tipo.
* Se não houver nenhuma operação disponível, afeta op->id com o valor -1.
*/
void read_main_client_buffer(struct rnd_access_buffer* buffer, int client_id, int buffer_size, struct operation* op){
	int *ptrs = buffer->ptrs;
	int i = 0;
	int done = 0;
	while (i < buffer_size && done == 0){
		if (buffer->ptrs[i] == 1 && buffer->buffer[i].requesting_client == client_id ){
			*op = buffer->buffer[i];  //a operação é atualizada
			buffer->ptrs[i] = 0;	 //apaga-se a informação lá escrita
			done++;
		}
		i++;
	}
	if (done == 0){
		op->id = -1;  //não houve operação disponível
	}
}


/* Função que lê uma operação do buffer de memória partilhada entre os clientes e intermediários,
* se houver alguma disponível para ler (qualquer intermediário pode ler qualquer operação).
* A leitura deve ser feita tendo em conta o tipo de buffer e as regras de leitura em buffers desse tipo.
* Se não houver nenhuma operação disponível, afeta op->id com o valor -1.
*/
void read_client_interm_buffer(struct circular_buffer* buffer, int buffer_size, struct operation* op){
	int out = buffer->ptrs->out;
	if (isCircularEmpty(buffer)){ //não há operações
		op->id = -1;
	} else {
		*op = buffer->buffer[out]; //novo valor
		buffer->ptrs->out = (buffer->ptrs->out + 1) % buffer_size; //atualizar a posição de escrita
	}
}


/* Função que lê uma operação do buffer de memória partilhada entre os intermediários e as empresas,
* se houver alguma disponível para ler dirijida à empresa especificada. A leitura deve
* ser feita tendo em conta o tipo de buffer e as regras de leitura em buffers desse tipo. Se não houver
* nenhuma operação disponível, afeta op->id com o valor -1.
*/
void read_interm_enterp_buffer(struct rnd_access_buffer* buffer, int enterp_id, int buffer_size, struct operation* op){
	int *ptrs = buffer->ptrs;
	int i = 0;
	int done = 0;
	while (i < buffer_size && done == 0){
		if (buffer->ptrs[i] == 1 && buffer->buffer[i].requested_enterp == enterp_id ){
			*op = buffer->buffer[i]; //a operação é atualizada
			buffer->ptrs[i] = 0;	 //apaga-se a informação lá escrita
			done++;
		}
		i++;
	}
	if (done == 0){
		op->id = -1;  //não houve operação disponível
	}
}