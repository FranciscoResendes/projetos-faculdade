/* 
    Diogo Freaza 56969
    Francisco Resendes 57162
    Pedro Barata 54483 
*/
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>

#include "table.h"
#include "sdmessage.pb-c.h"
#include "stats.h"
#include "threads-private.h"

//VARIÀVEIS GLOBAIS
int write_n = 0;
int write_w = 0;
int read_n = 0;
pthread_mutex_t m_skel = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t c_skel = PTHREAD_COND_INITIALIZER;
extern struct statistics_t stats;
extern pthread_mutex_t m1;
extern pthread_cond_t c1;
extern int write_stats_n;
extern int write_stats_w;
extern int read_stats_n;

/* Inicia o skeleton da tabela.
 * O main() do servidor deve chamar esta função antes de poder usar a
 * função invoke(). O parâmetro n_lists define o número de listas a
 * serem usadas pela tabela mantida no servidor.
 * Retorna a tabela criada ou NULL em caso de erro.
 */
struct table_t *table_skel_init(int n_lists){

    return table_create(n_lists);
}


/* Liberta toda a memória ocupada pela tabela e todos os recursos 
 * e outros recursos usados pelo skeleton.
 * Retorna 0 (OK) ou -1 em caso de erro.
 */
int table_skel_destroy(struct table_t *table){
    return table_destroy(table);
}

/* Executa na tabela table a operação indicada pelo opcode contido em msg 
 * e utiliza a mesma estrutura MessageT para devolver o resultado.
 * Retorna 0 (OK) ou -1 em caso de erro.
*/
int invoke(MessageT *msg, struct table_t *table) {

    if (msg == NULL || table == NULL) {
        return -1; 
    }

    
    switch (msg->opcode) {
        //caso put recebe <entry> devolve <> 
        case MESSAGE_T__OPCODE__OP_PUT: {
            //printf("T = %d \n",msg->entry != NULL);
            if (msg->c_type == MESSAGE_T__C_TYPE__CT_ENTRY && msg->entry != NULL) {

                //cria o tipo de a data
                struct data_t *data_value = data_create(msg->entry->value.len, msg->entry->value.data);

                start_write(m_skel, c_skel,  write_n,  write_w, read_n);
                int result = table_put(table, msg->entry->key, data_value);
                end_write(m_skel, c_skel, write_n);

                if(result == -1){
                    msg->opcode = 99;
                    msg->c_type = 70;
                    return -1;
                }

                msg->opcode++;
                msg->c_type = 70;


            } else {
                msg->opcode = 99;
                msg->c_type = 70;
                return -1; 
            }
            break;
        }
        //Caso de get recebe <key> devolve <value>
        case MESSAGE_T__OPCODE__OP_GET: {

            if (msg->c_type == MESSAGE_T__C_TYPE__CT_KEY && msg->key != NULL) {

                start_read(m_skel,  c_skel, write_n,  write_w);
                struct data_t *result_data = table_get(table, msg->key);
                end_read(m_skel,  c_skel, read_n);
                //Verifica se existe a data na tabela
                if(result_data == NULL){
                    msg->opcode = 99;
                    msg->c_type = 70;
                    return -1;
                }

                //Devolve a struct data no value de msg
                msg->value.len = result_data->datasize;
                msg->value.data = result_data->data;

                msg->opcode++;
                msg->c_type = 30;

            } else {
                msg->opcode = 99;
                msg->c_type = 70;

                return -1; 
            }
            break;
        }
        //Caso de delete recebe <key> devolve <>
        case MESSAGE_T__OPCODE__OP_DEL: {

            if (msg->c_type == MESSAGE_T__C_TYPE__CT_KEY && msg->key != NULL) {

                start_write(m_skel, c_skel,  write_n,  write_w, read_n);
                int result_data = table_remove(table, msg->key);
                end_write(m_skel, c_skel, write_n);

                //Verifica se existe a data na tabela
                if(result_data != 0){
                    msg->opcode = 99;
                    msg->c_type = 70;
                    return -1;
                }

                msg->opcode++;
                msg->c_type = 70;

            } else {
                msg->opcode = 99;
                msg->c_type = 70;

                return -1; 
            }
            break;
        }
        //Caso de size recebe <> devolve <int>
        case MESSAGE_T__OPCODE__OP_SIZE: {

            if (msg->c_type == MESSAGE_T__C_TYPE__CT_NONE) {

                start_read(m_skel,  c_skel, write_n,  write_w);
                int size = table_size(table);
                end_read(m_skel,  c_skel, read_n);
                msg->result = size;

                msg->opcode++;
                msg->c_type = 40;
                

            } else {
                msg->opcode = 99;
                msg->c_type = 70;

                return -1; 
            }
            break;
        }
        //Caso de getKeys recebe <> devolve <keys>
        case MESSAGE_T__OPCODE__OP_GETKEYS: {

            if (msg->c_type == MESSAGE_T__C_TYPE__CT_NONE) {

                start_read(m_skel,  c_skel, write_n,  write_w);
                msg->n_keys = table_size(table);
                msg->keys = table_get_keys(table);
                end_read(m_skel,  c_skel, read_n);

                msg->opcode++;
                msg->c_type = 50;

            } else {
                msg->opcode = 99;
                msg->c_type = 70;

                return -1; 
            }
            break;
        }
        //Caso de gettable recebe <> devolve <entries>
        case MESSAGE_T__OPCODE__OP_GETTABLE: {

            if (msg->c_type == MESSAGE_T__C_TYPE__CT_NONE) {
                start_read(m_skel,  c_skel, write_n,  write_w);
                char **keys = table_get_keys(table);
                
                if (keys != NULL) {
                    msg->n_entries = table_size(table);
                    msg->entries = (EntryT **)malloc(sizeof(EntryT *) * msg->n_entries);

                    if (msg->entries != NULL) {
  
                        for (int i = 0; i < msg->n_entries; i++) {
                            struct data_t *value_data = table_get(table, keys[i]);
                            end_read(m_skel,  c_skel, read_n);
                            msg->entries[i] = (EntryT *)malloc(sizeof(EntryT));

                            entry_t__init(msg->entries[i]);

                            msg->entries[i]->key = strdup(keys[i]);
                            msg->entries[i]->value.data = value_data->data;
                            msg->entries[i]->value.len = value_data->datasize;
                        }
                    }
                    table_free_keys(keys);
                }
                else{
                    msg->opcode = 99;
                    msg->c_type = 70;

                    return -1; 
                }

                msg->opcode++;
                msg->c_type = 60;

            } else {
                msg->opcode = 99;
                msg->c_type = 70;

                return -1; 
            }
            break;
        }
        //caso de stats recebe <> devolve <stats>
        case MESSAGE_T__OPCODE__OP_STATS: {
            if (msg->c_type == MESSAGE_T__C_TYPE__CT_NONE) {

                start_read(m1,  c1, write_stats_n,  write_stats_w);

                msg->client = stats.n_clients;
                msg->times = stats.total_time;
                msg->ops = stats.n_ops;
                
                end_read( m1,  c1, read_stats_n);
            }
            else{
                msg->opcode = 99;
                msg->c_type = 70;
            }
            msg->opcode++;
            msg->c_type = 80;
            break;
        }         
        default:
            msg->opcode = 99;
            msg->c_type = 70;
            return -1;


    }

    return 0; 
}
