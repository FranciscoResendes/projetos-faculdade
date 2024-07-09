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

#include "message-private.h"
#include "sdmessage.pb-c.h"

MessageT *read_all(int client_socket) {
    // 1. Receber o tamanho do buffer
    int packed_size;
    read(client_socket, &packed_size, sizeof(int));
    // printf("tamanho %d\n", packed_size);
    if (packed_size <= 0) {
        //perror("Erro ao receber o tamanho do buffer");
        return NULL;
    }

    // 2. Alocar memória para o buffer
    uint8_t *packed_msg = malloc(packed_size);
    if (packed_msg == NULL) {
        perror("Erro ao alocar memória para o buffer");
        return NULL;
    }

    // 3. Receber o buffer
    read(client_socket, packed_msg, packed_size);
    // printf("tamanho2 %d\n", packed_size);
    if (packed_msg == NULL) {
        perror("Erro ao receber dados do buffer");
        free(packed_msg);
        return NULL;
    }

    // 4. Deserializar a mensagem
    MessageT *msg = message_t__unpack(NULL, packed_size, packed_msg);
    if(msg == NULL){
        // perror("Erro a desincriptar");
        return NULL;
    }

    
    // Limpar a memória alocada
    free(packed_msg);

    return msg;
}

int write_all(int client_socket, MessageT *msg){
    
    //tamanho da mensagem
    int packed_size = message_t__get_packed_size(msg);

    //mensagem empacotada
    uint8_t *packed_msg = malloc(packed_size);

    if (packed_msg == NULL) {
        perror("Erro ao alocar memória para a mensagem serializada");
        return -1;
    }

    if (message_t__pack(msg, packed_msg) != packed_size) {
        perror("Erro ao empacotar a mensagem");
        free(packed_msg);
        return -1;
    }

    // Enviar a mensagem serializada
    write(client_socket, &packed_size, sizeof(int));
    ssize_t bytes_sent = write(client_socket, packed_msg, packed_size);

    if (bytes_sent == -1) {
        perror("Erro ao enviar mensagem pela rede");
        free(packed_msg);
        return -1;
    }
    //printf("Server envia: %s -- %d\n",(char *)packed_msg, packed_size);
    free(packed_msg);
    return 0;
}