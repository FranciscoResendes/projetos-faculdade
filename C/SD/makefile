BIN_DIR = binary
INC_DIR = include
LIB_DIR = lib
OBJ_DIR = object
SRC_DIR = source
DEP_DIR = dependencies


CC = gcc
CFLAGS = -Wall -MMD -MP -MF $(DEP_DIR)/$*.d -I $(INC_DIR)


OBJCLIENT = $(OBJ_DIR)/sdmessage.pb-c.o $(OBJ_DIR)/message.o $(OBJ_DIR)/threads.o $(OBJ_DIR)/client_stub.o $(OBJ_DIR)/network_client.o $(OBJ_DIR)/table_client.o 
OBJSERVER = $(OBJ_DIR)/sdmessage.pb-c.o $(OBJ_DIR)/message.o $(OBJ_DIR)/threads.o $(OBJ_DIR)/table_skel.o $(OBJ_DIR)/network_server.o $(OBJ_DIR)/table_server.o 
LIB = $(OBJ_DIR)/data.o $(OBJ_DIR)/entry.o $(OBJ_DIR)/list.o $(OBJ_DIR)/table.o $(OBJ_DIR)/table-private.o


all: $(OBJ_DIR)/sdmessage.pb-c.o $(LIB_DIR)/libtable.a $(BIN_DIR)/table_client $(BIN_DIR)/table_server


$(OBJ_DIR)/sdmessage.pb-c.o: sdmessage.proto
	protoc-c --c_out=source/ sdmessage.proto 	
	mv source/sdmessage.pb-c.h include
	gcc -c -I $(INC_DIR) source/sdmessage.pb-c.c -o object/sdmessage.pb-c.o
	
$(LIB_DIR)/libtable.a: $(LIB)
	ar -r -c -s $(LIB_DIR)/libtable.a $(LIB)

$(BIN_DIR)/table_client: $(OBJCLIENT)
	$(CC) $(OBJCLIENT) -L. lib/libtable.a -lprotobuf-c -o $(BIN_DIR)/table_client
$(BIN_DIR)/table_server: $(OBJSERVER)
	$(CC) $(OBJSERVER) -L. lib/libtable.a -lprotobuf-c -o $(BIN_DIR)/table_server


$(OBJ_DIR)/%.o: $(SRC_DIR)/%.c 
	$(CC) $(CFLAGS) -c $< -o $@
	

include $(wildcard $(DEP_DIR)/*.d)

clean:
	rm -rf $(DEP_DIR)/* $(OBJ_DIR)/* $(BIN_DIR)/* $(LIB_DIR)/*


	

