/* 
    Diogo Freaza 56969
    Francisco Resendes 57162
    Pedro Barata 54483 
*/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <unistd.h>

#include "stats.h"
#include "client_stub.h"
#include "client_stub-private.h"
#include "network_client.h"
#include "sdmessage.pb-c.h"
#include "table.h"


/* Remote table, que deve conter as informações necessárias para comunicar
 * com o servidor. A definir pelo grupo em client_stub-private.h
 */
struct rtable_t;

/* Função para estabelecer uma associação entre o cliente e o servidor, 
 * em que address_port é uma string no formato <hostname>:<port>.
 * Retorna a estrutura rtable preenchida, ou NULL em caso de erro.
 */
struct rtable_t *rtable_connect(char *address_port){
    if (address_port == NULL){
        return NULL;
    }
    struct rtable_t *rt = malloc(sizeof(struct rtable_t));

    char * hostname = strtok(address_port,":");
    char * port = strtok(NULL,":");

    rt->server_address = hostname; // será necessário alocar memória?
    rt->server_port = atoi(port);
    //network_connect(rt); aqui ou na main?

    // if ((rt->sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0 ) {
    //     return NULL;
    // }
    return rt;
}

/* Termina a associação entre o cliente e o servidor, fechando a 
 * ligação com o servidor e libertando toda a memória local.
 * Retorna 0 se tudo correr bem, ou -1 em caso de erro.
 */
int rtable_disconnect(struct rtable_t *rtable){
    if (rtable == NULL){
        return -1;
    }

    close(rtable->sockfd);
    free(rtable);
    return 0;
}

/* Função para adicionar um elemento na tabela.
 * Se a key já existe, vai substituir essa entrada pelos novos dados.
 * Retorna 0 (OK, em adição/substituição), ou -1 (erro).
 */
int rtable_put(struct rtable_t *rtable, struct entry_t *entry){
    if (rtable == NULL || entry == NULL){
        return -1;
    }
    MessageT msg;
    message_t__init(&msg);
    msg.opcode = 10;
    msg.c_type = 10;


    // msg.entry = malloc(sizeof(EntryT));
    // entry_t__init(msg.entry);
    // msg.entry->key = entry->key;
    // msg.entry->value.data = malloc(entry->value->datasize);
    // msg.entry->value.len = entry->value->datasize;
    // memcpy(msg.entry->value.data, entry->value->data, entry->value->datasize);
    EntryT et;
    entry_t__init(&et);
    et.key = entry->key;
    et.value.len = entry->value->datasize;
    et.value.data = entry->value->data;
    msg.entry = &et;
    


    MessageT *result = network_send_receive(rtable, &msg);
    entry_destroy(entry);
    int getreturn = 0;
    if (result->opcode == 99){
        getreturn = -1;
    }

    // entry_t__free_unpacked(msg.entry, NULL);
    // message_t__free_unpacked(&msg, NULL);

    return getreturn;
}

/* Retorna o elemento da tabela com chave key, ou NULL caso não exista
 * ou se ocorrer algum erro.
 */
struct data_t *rtable_get(struct rtable_t *rtable, char *key){
    if (rtable == NULL || key == NULL){
        return NULL;
    }
    MessageT msg;
    message_t__init(&msg);
    msg.opcode = 20;
    msg.c_type = 20;
    msg.key = key;

    MessageT *result = network_send_receive(rtable, &msg);
    
    if (result->opcode == 99){
        return NULL;
    }
    else{
        
        return data_create(result->value.len, result->value.data);
    }   
}

/* Função para remover um elemento da tabela. Vai libertar 
 * toda a memoria alocada na respetiva operação rtable_put().
 * Retorna 0 (OK), ou -1 (chave não encontrada ou erro).
 */
int rtable_del(struct rtable_t *rtable, char *key){
    if (rtable == NULL || key == NULL){
        return -1;
    }
    MessageT msg;
    message_t__init(&msg);
    msg.opcode = 30;
    msg.c_type = 20;
    msg.key = key;

    MessageT *result = network_send_receive(rtable, &msg);

    if (result->opcode == 99){
        return -1;
    }
    else{
        return 0;
    }  
}

/* Retorna o número de elementos contidos na tabela ou -1 em caso de erro.
 */
int rtable_size(struct rtable_t *rtable){
    if (rtable == NULL){
        return -1;
    }
    MessageT msg;
    message_t__init(&msg);
    msg.opcode = 40;
    msg.c_type = 70;

    MessageT *result = network_send_receive(rtable, &msg);
    
    if (result->opcode == 99){
        return -1;
    }
    else{
        return result->result;
    }  
}

/* Retorna um array de char* com a cópia de todas as keys da tabela,
 * colocando um último elemento do array a NULL.
 * Retorna NULL em caso de erro.
 */
char **rtable_get_keys(struct rtable_t *rtable){
    if (rtable == NULL ){
        return NULL;
    }
    MessageT msg;
    message_t__init(&msg);
    msg.opcode = 50;
    msg.c_type = 70;
    
    MessageT *result = network_send_receive(rtable, &msg);
    
    if (result->opcode == 99){
        return NULL;
    }
     else{
        char ** keys = malloc(1000);
        for(int i = 0; i < result->n_keys; i++){

            keys[i] = strdup(result->keys[i]);
        }

        keys[result->n_keys] = NULL;
        return keys;

    }    
}

/* Liberta a memória alocada por rtable_get_keys().
 */
void rtable_free_keys(char **keys){
    table_free_keys(keys);
}

/* Retorna um array de entry_t* com todo o conteúdo da tabela, colocando
 * um último elemento do array a NULL. Retorna NULL em caso de erro.
 */
struct entry_t **rtable_get_table(struct rtable_t *rtable){
    if (rtable == NULL){
        return NULL;
    }
    MessageT msg;
    message_t__init(&msg);
    msg.opcode = 60;
    msg.c_type = 70;

    MessageT *result = network_send_receive(rtable, &msg);
    


    if (result->opcode == 99){
        return NULL;
    }
    else{
        struct entry_t **e = malloc(1000);
        for(int i = 0; i < result->n_entries; i++){
            e[i] = entry_create(result->entries[i]->key, data_create(result->entries[i]->value.len, result->entries[i]->value.data));
        }
        e[result->n_entries] = NULL;
        return e;
    }  
}

/* Liberta a memória alocada por rtable_get_table().
 */
void rtable_free_entries(struct entry_t **entries){   
    int i = 0;
    while(entries[i] != NULL){
        entry_destroy(entries[i]);
        i++;
    }
    free(entries);       
}

/* Obtém as estatísticas do servidor. */
struct statistics_t *rtable_stats(struct rtable_t *rtable){
    if (rtable == NULL){
        return NULL;
    }
    MessageT msg;
    message_t__init(&msg);
    msg.opcode = 70;
    msg.c_type = 70;

    MessageT *result = network_send_receive(rtable, &msg);
    
    if (result == NULL || result->opcode == 99){
        return NULL;
    }
    else{
        struct statistics_t *t = malloc(100);
        
        t->n_clients = result->client;
        t->total_time = result->times;
        t->n_ops = result->ops;

        return t;
    }  
}
