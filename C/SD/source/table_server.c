/* 
    Diogo Freaza 56969
    Francisco Resendes 57162
    Pedro Barata 54483 
*/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "network_server.h"
#include "table_skel.h"
#include "table-private.h"

int main(int argc, char const *argv[]){
    if (argc != 3){
        printf("Invalid arguments!\n");
        printf("Usage: table-server <port> <n_lists>");
        return -1;
    }
    const char * port = argv[1];
    const char * ntabelas = argv[2];

    short n = atoi(ntabelas); //usar prog defensiva aqui ou input de port é sempre correto?
    int p = atoi(port);

    int socketfd = network_server_init(p);

    printf("Server ready, waiting for connections\n"); //experimentar se fizermos um if com socketfd == -1 dá erro/ciclo infinito

    struct table_t *table = table_skel_init(n);
    network_main_loop(socketfd, table);
    return 0;
}
