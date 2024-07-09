/* 
    Diogo Freaza 56969
    Francisco Resendes 57162
    Pedro Barata 54483 
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "entry.h"


/* Função que cria uma entry, reservando a memória necessária e
 * inicializando-a com a string e o bloco de dados de entrada.
 * Retorna a nova entry ou NULL em caso de erro.
 */
struct entry_t *entry_create(char *key, struct data_t *data) {
    if (key == NULL || data == NULL) {
        return NULL; // Verifica se os argumentos são válidos
    }

    struct entry_t *entry = malloc(sizeof(struct entry_t)); // Aloca memória para a entrada

    if (entry == NULL) {
        return NULL; // Verifica se a alocação foi bem-sucedida
    }

    //entry->key = malloc(strlen(key) + 1); // Aloca memória para a chave
    entry->key = key; // Copia o conteúdo de key para entry->key

    if (entry->key == NULL) {
        free(entry);
        return NULL; // Verifica se a alocação foi bem-sucedida
    }

    
    entry->value = data; // Atribui o bloco de dados à entrada

    return entry; // Retorna a nova entrada
}


/* Função que elimina uma entry, libertando a memória por ela ocupada.
 * Retorna 0 (OK) ou -1 em caso de erro.
 */
int entry_destroy(struct entry_t *entry){
    
    if(entry == NULL || entry->key == NULL || entry->value == NULL){
        return -1;
    }
    free(entry->key);
    data_destroy(entry->value);
    free(entry);

    return 0;
}

/* Função que duplica uma entry, reservando a memória necessária para a
 * nova estrutura.
 * Retorna a nova entry ou NULL em caso de erro.
 */
struct entry_t *entry_dup(struct entry_t *entry){
    if(entry == NULL){
        return NULL;
    }

    // Aloca memória para a nova entrada
    struct entry_t *new_entry = malloc(sizeof(struct entry_t));

    if(new_entry == NULL){
        return NULL; // Se a alocação falhar, retorna NULL
    }

    // Aloca memória para a nova chave e copia o conteúdo da chave original
    new_entry->key = malloc(strlen(entry->key) + 1);

    if(new_entry->key == NULL){
        free(new_entry);
        return NULL; // Se a alocação falhar, retorna NULL
    }

    strcpy(new_entry->key, entry->key);

    // Duplica a estrutura data_t (se necessário)
    struct data_t *new_data = data_dup(entry->value);

    if(new_data == NULL){
        free(new_entry->key);
        free(new_entry);
        return NULL; // Se a duplicação falhar, retorna NULL
    }

    // Atribui o novo bloco de dados à nova entrada
    new_entry->value = new_data;

    return new_entry;
}


/* Função que substitui o conteúdo de uma entry, usando a nova chave e
 * o novo valor passados como argumentos, e eliminando a memória ocupada
 * pelos conteúdos antigos da mesma.
 * Retorna 0 (OK) ou -1 em caso de erro.
 */
int entry_replace(struct entry_t *entry, char *new_key, struct data_t *new_value){

    if(entry == NULL || new_key == NULL || new_value == NULL){
        return -1;
    }

    free(entry->key);
    data_destroy(entry->value);

    //entry->key = malloc(strlen(new_key) + 1);
    entry->key = new_key; // Copia o conteúdo de key para entry->key

    if(data_replace(new_value, new_value->datasize, new_value->data) == -1){
        free(entry->key);
        free(entry);
        return -1;    
    }

    entry->value = new_value;

    return 0;
}

/* Função que compara duas entries e retorna a ordem das mesmas, sendo esta
 * ordem definida pela ordem das suas chaves.
 * Retorna 0 se as chaves forem iguais, -1 se entry1 < entry2,
 * 1 se entry1 > entry2 ou -2 em caso de erro.
 */
int entry_compare(struct entry_t *entry1, struct entry_t *entry2){
     if (entry1 == NULL || entry2 == NULL || entry1->key == NULL || entry2->key == NULL) {
        return -2; // Retorna erro se alguma entrada ou chave for inválida
    }

    int result = strcmp(entry1->key, entry2->key);

    if (result < 0) {
        return -1; // entry1 < entry2
    } else if (result > 0) {
        return 1; // entry1 > entry2
    } else {
        return 0; // Chaves iguais
    }

}