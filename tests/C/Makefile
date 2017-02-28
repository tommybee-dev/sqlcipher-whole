EXE=test_cryptdb.exe
TARGET_OS = mingw

OPENSSL_HOME = /home/tobee/openssl
SQLCIPHER_HOME = /home/tobee/sqlcipher
CC = gcc
RM = rm
STRIP = strip
RANLIB = ranlib 
AR = ar
MV = mv

CFLAGS = -static -std=gnu99 -Wall -I$(SQLCIPHER_HOME)/include/sqlcipher -I$(OPENSSL_HOME)/include
LIBS =  $(SQLCIPHER_HOME)/lib/libsqlcipher.a $(OPENSSL_HOME)/lib/libcrypto.a   -static-libgcc -lgdi32

LDFLAGS='-ldl'

TARGET  = $(EXE)
CC=gcc
STRIP=strip

LIB_SRCS =test_cryptdb.c
LIB_OBJS = $(LIB_SRCS:%.c=%.o)

.SUFFIXES : .c .o 

ifeq ($(TARGET_OS),)


all : $(TARGET) 


$(TARGET): $(LIB_OBJS)       
	$(CC) $(CFLAGS) -o $@ $(LIB_OBJS) $(LIBS)
	$(STRIP) $(TARGET) 

clean :      
	$(RM) $(LIB_OBJS) $(TARGET)