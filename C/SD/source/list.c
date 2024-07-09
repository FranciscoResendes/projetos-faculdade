/* 
    Diogo Freaza 56969
    Francisco Resendes 57162
    Pedro Barata 54483 
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "list.h"
#include "list-private.h"


/* Função que cria e inicializa uma nova lista (estrutura list_t a
 * ser definida pelo grupo no ficheiro list-private.h).
 * Retorna a lista ou NULL em caso de erro.
 */
struct list_t *list_create(){
    struct list_t *newList = malloc(sizeof(struct list_t));

    if(newList == NULL){
        return NULL;
    }

    newList->size = 0;
    newList->head = NULL;

    return newList;
}

/* Função que elimina uma lista, libertando *toda* a memória utilizada
 * pela lista.
 * Retorna 0 (OK) ou -1 em caso de erro.
 */
int list_destroy(struct list_t *list){

    struct node_t *corrente = list->head;
    struct node_t *next;

    if(list == NULL){
        return -1;
    }

    while (corrente != NULL)
    {
        next = corrente->next;
        entry_destroy(corrente->entry); //liberta o entry do no
        free(corrente);     //liberta o no.  
        corrente = next;
        list->size -= 1;
    }

    if(list->size != 0){;
        return -1;
    }
    
    free(list);

    return 0;
}

/* Função que adiciona à lista a entry passada como argumento.
 * A entry é inserida de forma ordenada, tendo por base a comparação
 * de entries feita pela função entry_compare do módulo entry e
 * considerando que a entry menor deve ficar na cabeça da lista.
 * Se já existir uma entry igual (com a mesma chave), a entry
 * já existente na lista será substituída pela nova entry,
 * sendo libertada a memória ocupada pela entry antiga.
 * Retorna 0 se a entry ainda não existia, 1 se já existia e foi
 * substituída, ou -1 em caso de erro.
 */
int list_add(struct list_t *list, struct entry_t *entry){
    if (list == NULL || entry == NULL) {
        return -1; // Retorna erro se a lista ou a entry forem inválidas
    }

    struct node_t *new_node = (struct node_t *)malloc(sizeof(struct node_t));
    if (new_node == NULL) {
        return -1; // Retorna erro se a alocação de memória falhar
    }

    new_node->entry = entry;
    new_node->next = NULL;

    if (list->head == NULL || entry_compare(entry, list->head->entry) < 0) {
        new_node->next = list->head;
        list->head = new_node;
        list->size++;
        return 0; // Adicionou na cabeça da lista
    }

    if(entry_compare(entry, list->head->entry) == 0){
        struct node_t *temp = list->head;
        list->head = new_node;
        new_node->next = temp->next;
        entry_destroy(temp->entry);
        free(temp);
        return 1; // Substituiu entry existente
    }

    struct node_t *current = list->head;
    while (current->next != NULL && entry_compare(entry, current->next->entry) > 0) {
        current = current->next;
    }

    if (current->next != NULL && entry_compare(entry, current->next->entry) == 0) {
        struct node_t *temp = current->next;
        current->next = new_node;
        new_node->next = temp->next;
        entry_destroy(temp->entry);
        free(temp);
        return 1; // Substituiu entry existente
    }

    new_node->next = current->next;
    current->next = new_node;
    list->size++;

    return 0; // Adicionou no meio ou no final da lista
}



/* Função que elimina da lista a entry com a chave key, libertando a
 * memória ocupada pela entry.
 * Retorna 0 se encontrou e removeu a entry, 1 se não encontrou a entry,
 * ou -1 em caso de erro.
 */
int list_remove(struct list_t *list, char *key){

    if(list == NULL || key == NULL){
        return -1;
    }

    struct node_t *corrente = list->head;
    struct node_t *prev = NULL;

    while (corrente != NULL) {
        if (strcmp(corrente->entry->key, key) == 0) {
            if (prev == NULL) {
                list->head = corrente->next;
            } else {
                prev->next = corrente->next;
            }
            entry_destroy(corrente->entry);
            free(corrente);
            list->size--;
            return 0; // Encontrou e removeu a entry
        }
        prev = corrente;
        corrente = corrente->next;
    }

    return 1; // Não encontrou a entry
}

/* Função que obtém da lista a entry com a chave key.
 * Retorna a referência da entry na lista ou NULL se não encontrar a
 * entry ou em caso de erro.
*/
struct entry_t *list_get(struct list_t *list, char *key) {
    if (list == NULL || key == NULL) {
        return NULL; // Retorna NULL se a lista ou a chave forem inválidas
    }

    struct node_t *corrente = list->head;

    while (corrente != NULL) {
        if (strcmp(corrente->entry->key, key) == 0) {
            return corrente->entry; // Retorna a entry se encontrar a chave
        }
        corrente = corrente->next;
    }

    return NULL; // Retorna NULL se não encontrar a entry com a chave
}

/* Função que conta o número de entries na lista passada como argumento.
 * Retorna o tamanho da lista ou -1 em caso de erro.
 */
int list_size(struct list_t *list){
    
    if(list == NULL){
        return -1;
    }

    return list->size;
}

/* Função que constrói um array de char* com a cópia de todas as keys na 
 * lista, colocando o último elemento do array com o valor NULL e
 * reservando toda a memória necessária.
 * Retorna o array de strings ou NULL em caso de erro.
 */
char **list_get_keys(struct list_t *list) {
    if (list == NULL || list->size == 0) {
        return NULL; // Retorna NULL se a lista for inválida
    }

    char **keys_array = (char **)malloc((list->size + 1) * sizeof(char *));
    if (keys_array == NULL) {
        return NULL; // Retorna NULL se a alocação de memória falhar
    }

    struct node_t *corrente = list->head;
    int i = 0;

    while (corrente != NULL) {
        keys_array[i] = strdup(corrente->entry->key); // Copia a chave para o array
        if (keys_array[i] == NULL) {
            for (int j = 0; j < i; j++) {
                free(keys_array[j]);
            }
            free(keys_array);
            return NULL; // Retorna NULL se a cópia da chave falhar
        }
        corrente = corrente->next;
        i++;
    }

    keys_array[i] = NULL; // Define o último elemento como NULL
    return keys_array;
}

/* Função que liberta a memória ocupada pelo array de keys obtido pela 
 * função list_get_keys.
 * Retorna 0 (OK) ou -1 em caso de erro.
 */
int list_free_keys(char **keys) {
    if (keys == NULL) {
        return -1; // Retorna erro se o array de keys for inválido
    }

    int i = 0;
    while (keys[i] != NULL) {
        free(keys[i]); // Libera a memória de cada chave
        i++;
    }

    free(keys); // Libera a memória do array de keys
    return 0; // Retorna sucesso
}