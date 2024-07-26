# PSI

Coisas Para Corrigir:
- Tanto o delete pages e o evaluate selected pages, não atualizam a tabela quando clico no botão, dando reload da pagina o delete atualiza tabela mas o evaluate não. Para atualizar o evaluate preciso de ir para as tabelas de websites e voltar para o website que eu fiz o evaluate.

Coisas que faltam no user 6:
-Css da pagina como no qualWeb

Para Correr No AppServer:

URL para ver o site: http://appserver.alunos.di.fc.ul.pt:3021/

- ssh PSI021@appserver.alunos.di.fc.ul.pt

- git pull (na pasta PSI)

- mongo --username psi021 --password --authenticationDatabase psi021 appserver.alunos.di.fc.ul.pt/psi021

- ng serve --port 3021 --host 0.0.0.0 --disable-host-check (se der Unknown argument trocar para isto -> --disable-host-check)

- node server.js


Para Correr Abrir Quatro Terminais (o mongod e mongo tem de ser pela ordem):

/frontend ng serve

/backend node server.js

/PSI mongod 

/PSI mongo

Comados do mongo:
-show dbs (myapp é a nossa)

-use myapp (para escolher a base de dados)

-show collections (mostra as tabelas que existem?? acho eu)

-db.mycoll.find() (mostra os objetos na minha coleção)
-db.websites.find() (mostra os websites)

-db.mycolldeleteMany({}) para apagar todos
