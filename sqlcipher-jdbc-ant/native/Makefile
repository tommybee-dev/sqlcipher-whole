MYINCL      = ./ -Wl,--kill-at  
MYBIN       = .
OPENSSL_HOME = /home/tobee/openssl
CIPHER_HOME = /home/tobee/sqlcipher
CIPHER_SRC = /home/tobee/sqlcipher-build/sqlcipher
CIPHER_BULD = /home/tobee/sqlcipher-build/sqlcipher/build
JAVA_HOME   = /C/DEV/COMP/Java/jdk1.8.0_111
CC   = i686-w64-mingw32-gcc
AR   = ar
RM   = rm -Rf
CP	 = cp
STRIP  = strip
ECHO  = echo
ARFLAGS = -ruv
SOFLAGS = $(SHARED) -o
SUN64   = -xarch=v9 
HP64    = +W42 

Windows-x86_CC           := i686-w64-mingw32-gcc
Windows-x86_STRIP        := strip
Windows-x86_CFLAGS       := -D_JNI_IMPLEMENTATION_ -Ilib/inc_win -O2
Windows-x86_LINKFLAGS    := -Wl,--kill-at -shared
Windows-x86_LIBNAME      := sqlitejdbc.dll
Windows-x86_SQLITE_FLAGS := 

#JNI     = _JNI_IMPLEMENTATION_ -D_HAVE__MINGW_H_ 
#JNI     = -D_JNI_IMPLEMENTATION_ -I./inc_win -O2
JNI = -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/win32 -O2

SQLITEDEF=	-DSQLITE_HAS_CODEC \
	-DSQLITE_HAVE_ISNAN \
	-DSQLITE_ENABLE_COLUMN_METADATA \
	-DSQLITE_OMIT_LOAD_EXTENSION \
	-DSQLITE_ENABLE_UPDATE_DELETE_LIMIT \
	-DSQLITE_ENABLE_COLUMN_METADATA \
	-DSQLITE_CORE -DSQLITE_ENABLE_FTS3 \
	-DSQLITE_ENABLE_FTS3_PARENTHESIS \
	-DSQLITE_ENABLE_RTREE \
	-DSQLITE_ENABLE_STAT2

TSCFLAG = -Wl,--kill-at -shared -Wl,--out-implib,$(MYBIN)/libsqlitejdbc.a
#TSCFLAG = -Wl,--kill-at -shared 

CIPHERINCL= -I/mingw32/include -I$(OPENSSL_HOME)/include -I$(CIPHER_HOME)/include/sqlcipher -I$(CIPHER_SRC) -I$(CIPHER_SRC)/src -I$(CIPHER_BULD)
#CIPHERINCL= -I/mingw32/include -I$(OPENSSL_HOME)/include -I$(CIPHER_HOME)/include/sqlcipher
#JNIINCL = -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/win32

#JNIWW32 = 
#JNIINCL = -I./inc_win
#TISLIB   = -L$(CIPHER_HOME)/lib -lsqlcipher -L$(OPENSSL_HOME)/lib -lcrypto -lgdi32
#TISLIB   = -L$(CIPHER_HOME)/lib -lsqlcipher -L$(OPENSSL_HOME)/lib -lcrypto -lgcc -lgdi32
#TISLIB   = $(CIPHER_HOME)/lib/libsqlcipher.a $(OPENSSL_HOME)/lib/libcrypto.a -lgcc -lgdi32
TISLIB   = -L$(CIPHER_HOME)/lib -lsqlcipher -L$(OPENSSL_HOME)/lib -lcrypto -static-libgcc -lgdi32
#CFLAGS   =  $(JNI) $(TSCFLAG) $(JNIINCL) $(CIPHERINCL) -D_REENTRANT
CFLAGS   =  $(SQLITEDEF) $(JNI) $(TSCFLAG) $(CIPHERINCL)

#----------------------------------------------------------------------------
# Module Define
#
#----------------------------------------------------------------------------
SHAREDLIB = $(MYBIN)/sqlitejdbc.dll

all: $(SHAREDLIB)
#OBJS =   NativeDB.o  extension-functions.o
OBJS =   extension-functions.o NativeDB.o  

copy:
	@$(RM)	org/sqlite/native/Windows/x86/*.dll
	@$(CP)	*.dll org/sqlite/native/Windows/x86/
	
clean:
	@$(RM) $(SHAREDLIB) $(OBJS) core
	@$(ECHO) "clean $(SHAREDLIB) $(OBJS) OK ..."
	
.SUFFIXES: .c .cpp .o

.c.o:
	@$(ECHO) "@$(CC) -c $(CFLAGS) $< -o $@"
	@$(CC) -c $(CFLAGS) $< -o $@
	@$(ECHO) "$< compile ..."
	
$(SHAREDLIB) : $(OBJS)
	@$(ECHO) "$@ Program Linking"
	@$(ECHO) "@$(CC)    $(TSCFLAG) -o $@ $(OBJS) $(TISLIB)"
	@$(CC)    $(TSCFLAG) -o $@ $(OBJS) $(TISLIB) 
	@$(RM)    $(OBJS) 
	@$(STRIP) $(SHAREDLIB)
