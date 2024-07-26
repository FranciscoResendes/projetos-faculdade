# SO_projeto
Trabalho realizado pelo grupo SO-007:
Diogo Freaza      56969
Francisco Resendes 57162
Guilherme Dias    57163

Comando para correr:
$./AdmPor [max_ops] [buffers_size] [n_clients] [n_intermediaries] [n_enterprises]

Exemplo de execução:
$./AdmPor 10 100 1 1 1

Notas:
Se forem usados mais que um intermediário, como não há locks pode acontecer que vários intermediários respondam a esse pedido.
Esse problema é resolvido na fase 2 deste projeto.

\bin      -> contém executável AdmPor 
\include  -> contém ficheiros .h !Não alteráveis! com a exceção dos -private.h
\obj      -> contém ficheiros .o
\src      -> contém ficheiros .c -- nós vamos trabalhar aqui





