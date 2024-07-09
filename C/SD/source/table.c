/* 
    Diogo Freaza 56969
    Francisco Resendes 57162
    Pedro Barata 54483 
*/

#include <stdlib.h>
#include <string.h>

#include "table-private.h"
#include "table.h"
#include "list-private.h"
#include "list.h"
#include "entry.h"
#include "data.h"



/* Função para criar e inicializar uma nova tabela hash, com n
 * linhas (n = módulo da função hash).
 * Retorna a tabela ou NULL em caso de erro.
 */
struct table_t *table_create(int n){
	if (n <= 0){
		return NULL;
	}
    struct table_t *t = (struct table_t *) malloc(sizeof(struct table_t));
	t->size = n;
	t->lists = malloc(n * sizeof(struct list_t));
    for(int i = 0; i < t->size; i++){
		t->lists[i] = list_create();
	}


	return t;
}


/* Função que elimina uma tabela, libertando *toda* a memória utilizada
 * pela tabela.
 * Retorna 0 (OK) ou -1 em caso de erro.
 */
int table_destroy(struct table_t *table){
	if (table == NULL || table->lists == NULL){
		return -1;
	}
	
	for(int i = 0; i < table->size && table_size > 0; i++){
		
		list_destroy(table->lists[i]); 
	}
	free(table->lists);
	free(table);
	return 0;
}

/* Função para adicionar um par chave-valor à tabela. Os dados de entrada
 * desta função deverão ser copiados, ou seja, a função vai criar uma nova
 * entry com *CÓPIAS* da key (string) e dos dados. Se a key já existir na
 * tabela, a função tem de substituir a entry existente na tabela pela
 * nova, fazendo a necessária gestão da memória.
 * Retorna 0 (ok) ou -1 em caso de erro.
 */
int table_put(struct table_t *table, char *key, struct data_t *value){
	if(table == NULL || key == NULL || value == NULL){
		return -1;
	}
	int index = hash_code(key, table->size);
	struct entry_t *t =entry_create(key, value);
	list_add(table->lists[index], entry_dup(t));
	free(t);
	return 0;
	
}

/* Função que procura na tabela uma entry com a chave key. 
 * Retorna uma *CÓPIA* dos dados (estrutura data_t) nessa entry ou 
 * NULL se não encontrar a entry ou em caso de erro.
 */
struct data_t *table_get(struct table_t *table, char *key){
	for(int i = 0; i < table->size; i++){
		struct entry_t *e = list_get(table->lists[i], key);
		if(e != NULL){
			return data_dup(e->value);
		}
	}
	return NULL;
}

/* Função que remove da lista a entry com a chave key, libertando a
 * memória ocupada pela entry.
 * Retorna 0 se encontrou e removeu a entry, 1 se não encontrou a entry,
 * ou -1 em caso de erro.
 */
int table_remove(struct table_t *table, char *key){
	if(table == NULL || key == NULL){
		return -1;
	}
	int index = hash_code(key, table->size);//ver em que index da lista de listas em q a key deve estar
											//fazer assim ou verificar em todas as listas?
	return list_remove(table->lists[index], key);
}

/* Função que conta o número de entries na tabela passada como argumento.
 * Retorna o tamanho da tabela ou -1 em caso de erro.
 */
int table_size(struct table_t *table){
	if (table == NULL){
		return -1;
	}
	int counter = 0;
	for(int i = 0; i < table->size; i++){

		counter += list_size(table->lists[i]); // é o numero de listas (ou seja n) ou o número de entries?

	}
	return counter;
}

/* Função que constrói um array de char* com a cópia de todas as keys na 
 * tabela, colocando o último elemento do array com o valor NULL e
 * reservando toda a memória necessária.
 * Retorna o array de strings ou NULL em caso de erro.
 */
char **table_get_keys(struct table_t *table){
    
    if(table == NULL || table->lists == NULL || table_size(table) == 0)
        return NULL;

    char ** keys = malloc(sizeof(char *) * (table_size(table) + 1));

    if(keys == NULL)
        return NULL;

    int inc = 0; //Contador do total keys

    for(int i = 0; i < table->size; i++){
        if(table->lists[i] != NULL){
            int incListKeys = 0; //Contador das keys de cada lista
            char ** temp = list_get_keys(table->lists[i]);
            if(temp != NULL){
                while(temp[incListKeys] != NULL){
                    keys[inc] = temp[incListKeys];
                    inc++;
                    incListKeys++;
                }
            }
            free(temp);
        }
    }
    keys[inc] = NULL;
    return keys;
}

/* Função que liberta a memória ocupada pelo array de keys obtido pela 
 * função table_get_keys.
 * Retorna 0 (OK) ou -1 em caso de erro.
 */
int table_free_keys(char **keys){
	if(keys == NULL){
		return -1;
	}
	list_free_keys(keys);
	
	return 0;
}
