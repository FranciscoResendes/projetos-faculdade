# projetos-faculdade
Projeto de SIBD 3ª fase:

Objetivo
Traduzir para interrogações SQL os seguintes pedidos de dados:
1. NIF, nome, e idade das clientes femininas com apelido Dias, que compraram um ou mais
produtos de Beleza durante o ano de 2021. O EAN-13 e nome do(s) produto(s) também
devem ser mostrados, bem como o número e data da(s) respetiva(s) fatura(s) de compra. O
resultado deve vir ordenado de forma ascendente pela idade e nome das clientes, e de forma descendente pela data das faturas e nome dos produtos. Nota: a extração do ano a partir de uma data pode ser feita usando TO_CHAR(data, 'YYYY').
Variante com menor cotação: sem o cálculo da idade das clientes.
2. NIF e nome dos clientes masculinos que nunca compraram produtos de Beleza (independentemente do ano), e que, considerando apenas 2021, ou não compraram Roupa nesse
ano ou compraram Roupa em até duas ocasiões. Assuma que cada fatura com algum produto de Roupa representa uma ocasião de compra, não interessando se foi comprada pouca
ou muita roupa. O resultado deve vir ordenado pelo nome dos clientes de forma ascendente e pelo NIF dos clientes de forma descendente. Nota: a extração do ano a partir de uma
data pode ser feita usando TO_CHAR(data, 'YYYY').
Variantes com menor cotação: a) sem a verificação dos clientes nunca terem comprado
produtos de Beleza; e b) sem a verificação do número de ocasiões em que compraram
Roupa em 2021.
3. Produtos de Comida com preço abaixo da média dos preços de todos os produtos (independentemente da categoria), e que tenham sido alguma vez comprados por todos os clientes do Porto na parte da manhã dos dias, isto é, entre as 8h e as 12h. O resultado deve vir
ordenado pelo preço dos produtos de forma descendente e pelo EAN-13 dos produtos de
forma ascendente. Nota: a extração da hora do dia a partir de uma data pode ser feita
usando TO_CHAR(data, 'HH24').
Variantes com menor cotação: a) sem a verificação do preço dos produtos de Comida ser
inferior à média dos preços de todos os produtos; e b) sem as verificações da localidade
dos clientes e da hora das compras.
4. NIF e nome dos clientes que gastaram mais dinheiro em compras em cada ano, separadamente para clientes femininos e masculinos, devendo o género dos clientes e o total gasto
em cada ano também aparecer no resultado. A ordenação do resultado deve ser pelo ano de
forma descendente e pelo género dos clientes de forma ascendente. No caso de haver mais
do que um(a) cliente com o mesmo máximo de dinheiro gasto num ano, devem ser mostrados todos esses clientes.

