/* 
    Diogo Freaza 56969
    Francisco Resendes 57162
    Pedro Barata 54483 
*/
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <pthread.h>
#include <sys/time.h>

#include "sdmessage.pb-c.h"
#include "table_skel.h"
#include "network_server.h"
#include "stats.h"
#include "threads-private.h"
#include "message-private.h"

//VARIÀVEIS GLOBAIS
struct table_t *global_table;
struct statistics_t stats;
int write_stats_n = 0;
int write_stats_w = 0;
int read_stats_n = 0;
pthread_mutex_t m1 = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t c1 = PTHREAD_COND_INITIALIZER;

/* Função para preparar um socket de receção de pedidos de ligação
 * num determinado porto.
 * Retorna o descritor do socket ou -1 em caso de erro.
 */

int network_server_init(short port) {
    int sockfd;
    struct sockaddr_in server;

    // Cria um socket TCP
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) {
        perror("Erro ao criar o socket");
        return -1;
    }

    // Preenche estrutura server para bind
    server.sin_family = AF_INET;
    server.sin_port = htons(port); /* port é a porta TCP */
    server.sin_addr.s_addr = htonl(INADDR_ANY);

    // Faz bind
    if (bind(sockfd, (struct sockaddr *) &server, sizeof(server)) < 0){
        perror("Erro ao fazer bind");
        close(sockfd);
        return -1;
    };


    // Faz listen
    if (listen(sockfd, 0) < 0){
        perror("Erro ao executar listen");
        close(sockfd);
        return -1;
    };

    return sockfd;
}


void *thread_message(void *arg) {
    start_write(m1, c1,  write_stats_n,  write_stats_w, read_stats_n);
    stats.n_clients++;
    end_write(m1, c1, write_stats_n);

    printf("Client connection established\n");

    int consock = *((int *)arg); 
    MessageT *msg_recebida;

    struct timeval start_time, end_time;

    while ((msg_recebida = network_receive(consock)) != NULL) {

        gettimeofday(&start_time,NULL);
        invoke(msg_recebida, global_table);
        gettimeofday(&end_time,NULL);

        int sec = end_time.tv_sec - start_time.tv_sec;
        int usec = (end_time.tv_usec - start_time.tv_usec);

        start_write(m1, c1,  write_stats_n,  write_stats_w, read_stats_n);
        stats.total_time += (sec+ usec); // o tempo é em microsegundos?
        end_write(m1, c1, write_stats_n);

        network_send(consock, msg_recebida);

        start_write(m1, c1,  write_stats_n,  write_stats_w, read_stats_n);
        stats.n_ops++;
        end_write(m1, c1, write_stats_n);
    }

    printf("Client connection closed\n");
    network_server_close(consock);
    close(consock);
    
    start_write(m1, c1,  write_stats_n,  write_stats_w, read_stats_n);
    stats.n_clients--;
    end_write(m1, c1, write_stats_n);

    pthread_exit(NULL);
}

/* A função network_main_loop() deve:
 * - Aceitar uma conexão de um cliente;
 * - Receber uma mensagem usando a função network_receive;
 * - Entregar a mensagem de-serializada ao skeleton para ser processada
     na tabela table;
 * - Esperar a resposta do skeleton;
 * - Enviar a resposta ao cliente usando a função network_send.
 * A função não deve retornar, a menos que ocorra algum erro. Nesse
 * caso retorna -1.
 */
int network_main_loop(int listening_socket, struct table_t *table) {
    stats.n_clients = 0;
    stats.n_ops = 0;
    stats.total_time = 0;

    while (1) {
        int client_socket = accept(listening_socket, NULL, NULL);

        if (client_socket == -1) {
            perror("Erro ao aceitar conexão do cliente");
            return -1;
        }

        global_table = table;
        pthread_t tid;
        int *client_sock_ptr = malloc(sizeof(int)); 
        *client_sock_ptr = client_socket;

        if (pthread_create(&tid, NULL, thread_message, (void *)client_sock_ptr) != 0) {
            fprintf(stderr, "Erro ao criar thread\n");
            free(client_sock_ptr); 
            return -1;
        }
    }

    return -1;
}

/* A função network_receive() deve:
 * - Ler os bytes da rede, a partir do client_socket indicado;
 * - De-serializar estes bytes e construir a mensagem com o pedido,
 *   reservando a memória necessária para a estrutura MessageT.
 * Retorna a mensagem com o pedido ou NULL em caso de erro.
 */
MessageT *network_receive(int client_socket) {
    

    return read_all(client_socket);
}





/* A função network_send() deve:
 * - Serializar a mensagem de resposta contida em msg;
 * - Enviar a mensagem serializada, através do client_socket.
 * Retorna 0 (OK) ou -1 em caso de erro.
 */
int network_send(int client_socket, MessageT *msg){
    
 
    return write_all(client_socket, msg);
}

/* Liberta os recursos alocados por network_server_init(), nomeadamente
 * fechando o socket passado como argumento.
 * Retorna 0 (OK) ou -1 em caso de erro.
 */
int network_server_close(int socket){
    if (close(socket) == -1) {
        perror("Erro ao fechar o socket");
        return -1;
    }

    return 0;
}

