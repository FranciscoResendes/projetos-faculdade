#diretorias
OBJ_dir = obj
#nomes de ficheiros agrupados
OBJETOS = client.o enterprise.o intermediary.o memory.o process.o private.o memory-private.o main-private.o main.o
#Nome do executável
NOME_EXEC = AdmPor
#compilador
CC = gcc


out: $(OBJETOS)
	$(CC) $(addprefix $(OBJ_dir)/,$(OBJETOS)) -o bin/$(NOME_EXEC)
%.o: src/%.c $($@)
	$(CC) -I include -o $(OBJ_dir)/$@ -c $<
