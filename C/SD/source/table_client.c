/* 
    Diogo Freaza 56969
    Francisco Resendes 57162
    Pedro Barata 54483 
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "data.h"
#include "entry.h"
#include "client_stub.h"
#include "client_stub-private.h"
#include "network_client.h"
#include "sdmessage.pb-c.h"
#include "stats.h"

int conta_espaços(char *str){
    int counter = 0;
    for (int i = 0; i < strlen(str); i++){// como fgets poe \n não é preciso
        if(str[i] == ' '){                // adicionar +1 em strlen() ?
            counter += 1;
        }
    }
    return counter;//retorna o nº de " " na string
}

int main(int argc, char *argv[]){
     if(strlen(argv[1]) < 4 || strstr(argv[1],":") == NULL || argc != 2){
        printf("Invalid arguments!\n");
        printf("Usage: table-client <address>:<port>");
        return -1;
    }

    char * address = argv[1];
    struct rtable_t *rt = rtable_connect(address);
    network_connect(rt);
  

    char * str = malloc(1000);

    while (strcmp(str,"quit\n") != 0){
        //printf("\nEscreva algo \n");
        printf("Command: ");
        fgets(str,1000, stdin);// se a string for maior que o 1000 bytes isto terá problemas

        int is_quit = strcmp(str, "quit\n") == 0 || strcmp(str, "q\n") == 0;
        int is_size = strcmp(str, "size\n") == 0 || strcmp(str, "s\n") == 0;
        int is_getkeys = strcmp(str, "getkeys\n") == 0 || strcmp(str, "k\n") == 0;
        int is_gettable = strcmp(str, "gettable\n") == 0 || strcmp(str, "t\n") == 0;
        int is_stats = strcmp(str, "stats\n") == 0;

        if(is_quit){
            break;
        }
        
        else if(is_stats){
            struct statistics_t *t = rtable_stats(rt);
            if (t == NULL){
                printf("Error in rtable_stats!\n");
            }
            else{
                printf("Clients: %d - Time: %d - Ops: %d\n", t->n_clients, t->total_time, t->n_ops);
                
            }
            free(t);
        }

        else if(is_size){
            int s = rtable_size(rt);
            if(s >= 0){
                printf("%d\n", s);
            }
            else{
                printf("Error in rtable_size!\n");
            }
        }

        else if(is_getkeys){
            char ** keys = rtable_get_keys(rt);
            if(keys != NULL){
                int i = 0;

                while(keys[i] != NULL){
                    printf("%s\n", keys[i]);
                    i ++;
                }
            }
            else{
                printf("Error in rtable_get_keys!\n");
            }
            rtable_free_keys(keys);
            

        }

        else if(is_gettable){
            struct entry_t **entries = rtable_get_table(rt);
            if(entries != NULL){ //verifica se há keys
                int i = 0;
                
                while(entries[i] != NULL){
                    printf("%s :: %s\n", entries[i]->key, (char*) entries[i]->value->data);
                    i ++;
                }
                rtable_free_entries(entries);
            }
            else{
                printf("Error in rtable_get_table!\n");
            }
             
        }

       else{
            int conta = conta_espaços(str);
            strtok(str, " ");
            char * cmd = strtok(NULL, " ");
            char * cmd2 = strtok(NULL, " ");

            int is_put = strcmp(str, "put") == 0 || strcmp(str, "p") == 0;
            int is_del = strcmp(str, "del") == 0 || strcmp(str, "d") == 0;
            int is_get = strcmp(str, "get") == 0 || strcmp(str, "g") == 0;

            if(conta == 2){ //conta os " " na string
                cmd2[strcspn(cmd2,"\n")] = 0;//remove o \n introduzido por fgets()
            }

            else if(conta == 1){
                cmd[strcspn(cmd,"\n")] = 0; //remove o \n introduzido por fgets()
            }

            if(is_put && conta >= 2){
                struct data_t *d = data_create(strlen(cmd2)+1, cmd2);
                struct entry_t *e = entry_create(cmd, d);
                rtable_put(rt, entry_dup(e));
                free(d);
                free(e);
            }

            else if(is_del && conta >= 1){
                int result = rtable_del(rt,cmd);
                if(result == 0){
                    printf("Entry removed\n");
                }
                else{
                    printf("Error in rtable_del or key not found!\n");
                }
            }

            else if(is_get && conta >= 1){
                struct data_t *get = rtable_get(rt, cmd);
                if(get != NULL){
                    printf("%s\n", (char*) get->data);
                    free(get);
                }
                else{
                    printf("Error in rtable_get or key not found!\n");
                }
            }
            else{
                printf("Invalid command\n"); //falta dar print de todos os comandos possiveis
                printf("Usage: p[ut] <key> <value> | g[et] <key> | d[el] <key> |s[ize] | [get]k[eys] | [get]t[able] | q[uit]\n");
            }
            //printf("Info: %s -- %s -- %s\n", str, cmd, cmd2);

        }
    }

    free(str);
    network_close(rt);

    return 0;
}
