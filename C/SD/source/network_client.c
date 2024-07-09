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

#include "client_stub.h"
#include "client_stub-private.h"
#include "network_client.h"
#include "sdmessage.pb-c.h"
#include "table.h"
#include "message-private.h"

/* Esta função deve:
 * - Obter o endereço do servidor (struct sockaddr_in) com base na
 *   informação guardada na estrutura rtable;
 * - Estabelecer a ligação com o servidor;
 * - Guardar toda a informação necessária (e.g., descritor do socket)
 *   na estrutura rtable;
 * - Retornar 0 (OK) ou -1 (erro).
 */
int network_connect(struct rtable_t *rtable){
    struct sockaddr_in server;
     if((rtable->sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        return -1;
     }
    server.sin_family = AF_INET;
    server.sin_port = htons(rtable->server_port);

    if (connect(rtable->sockfd,(struct sockaddr *)&server, sizeof(server)) < 0) {
        printf("Error connecting to server\n");
        return -1;
    }
    return 0;
}

/* Esta função deve:
 * - Obter o descritor da ligação (socket) da estrutura rtable_t;
 * - Serializar a mensagem contida em msg;
 * - Enviar a mensagem serializada para o servidor;
 * - Esperar a resposta do servidor;
 * - De-serializar a mensagem de resposta;
 * - Tratar de forma apropriada erros de comunicação;
 * - Retornar a mensagem de-serializada ou NULL em caso de erro.
 */
MessageT *network_send_receive(struct rtable_t *rtable, MessageT *msg){

    write_all(rtable->sockfd, msg);

    return read_all(rtable->sockfd);
}

/* Fecha a ligação estabelecida por network_connect().
 * Retorna 0 (OK) ou -1 (erro).
 */
int network_close(struct rtable_t *rtable){
    int result = rtable_disconnect(rtable);
    return result;
}