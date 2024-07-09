/* 
    Diogo Freaza 56969
    Francisco Resendes 57162
    Pedro Barata 54483 
*/

#include <string.h>
#include "table-private.h"

/* Função que calcula o índice da lista a partir da chave
 */
int hash_code(char *key, int n){
	int sum = 0, i;
	for(i = 0; i < strlen(key); i++){
		sum += key[i];
	}
	return sum % n;
}