/* Trabalho realizado pelo grupo SO-007:
Diogo Freaza nº56969
Francisco Resendes nº57162
Guilherme Dias nº57163
*/

#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>

#include "main.h"
#include "memory.h"
#include "client.h"
#include "intermediary.h"
#include "enterprise.h"

/* Função que inicia um novo processo cliente através da função fork do SO. O novo
* processo irá executar a função execute_client respetiva, fazendo exit do retorno.
* O processo pai devolve o pid do processo criado.
*/
int launch_client(int client_id, struct comm_buffers* buffers, struct main_data* data){
	//execute cliente no fim deste ficheiro
	int pid = fork();
	if(pid == -1){
		perror("Fork do client não resultou");
		exit(1);
	}
	else if(pid == 0){
		//processo filho		
		int exec = execute_client(client_id, buffers, data);
		exit(exec);
	}
	else {
		//processo pai
		//será preciso wait?
		return pid;
	}
}

/* Função que inicia um novo processo intermediário através da função fork do SO. O novo
* processo irá executar a função execute_intermediary, fazendo exit do retorno.
* O processo pai devolve o pid do processo criado.
*/
int launch_interm(int interm_id, struct comm_buffers* buffers, struct main_data* data){
	//execute interm no fim deste ficheiro
	int pid = fork();
	if(pid == -1){
		perror("Fork do intermediário não resultou");
		exit(1);
	}
	else if(pid == 0){
		//processo filho
		int exec2 = execute_intermediary(interm_id, buffers, data);
		exit(exec2);
	}
	else {
		//processo pai
		//será preciso wait?
		return pid;
	}
}


/* Função que inicia um novo processo empresa através da função fork do SO. O novo
* processo irá executar a função execute_enterprise, fazendo exit do retorno.
* O processo pai devolve o pid do processo criado.
*/
int launch_enterp(int enterp_id, struct comm_buffers* buffers, struct main_data* data){
	//execute enterp no fim deste ficheiro
	int pid = fork();
	if(pid == -1){
		perror("Fork da empresa não resultou");
		exit(1);
	}
	else if(pid == 0){
		//processo filho
		int exec3 =execute_enterprise(enterp_id, buffers, data);
		exit(exec3);
	}
	else {
		//processo pai
		//será preciso wait?
		return pid;
	}
}


/* Função que espera que um processo termine através da função waitpid. 
* Devolve o retorno do processo, se este tiver terminado normalmente.
*/
int wait_process(int process_id){
	int status;
	int result = waitpid(process_id, &status, WCONTINUED);

	if(WIFEXITED(status)){
        result = WEXITSTATUS(status);
	}
    else{
        result = -1;
    }
    return result;
}
