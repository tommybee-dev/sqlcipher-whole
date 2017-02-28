This is a testing sqlite for sqlcipher library for windows OS
There are several build processes by building openssl to sqlcipher library 
Even more using jdbc library 

Here is my summary

- Get a msys2 with mingw32 & mingw64

- Build OpenSSL 
version: openssl-1.0.1u
-- You must have the version described
-- You can make your own openssl library from https://wiki.qt.io/Compiling_OpenSSL_with_MinGW

./Configure --prefix=$PWD/dist no-shared no-asm mingw
make depend
make
make install

- Build sqlcipher
-- It is just my suggestion, you can find your own way.
-- Get a source code from git hub

git clone https://github.com/sqlcipher/sqlcipher


-- configuration & build with source

./configure  --prefix=$PWD/dist \
--disable-editline  --enable-tempstore=yes \
CFLAGS="-DSQLITE_HAS_CODEC -DSQLITE_ENABLE_COLUMN_METADATA -DSQLITE_OMIT_LOAD_EXTENSION -DSQLITE_ENABLE_UPDATE_DELETE_LIMIT -DSQLITE_ENABLE_COLUMN_METADATA -DSQLITE_CORE -DSQLITE_ENABLE_FTS3 -DSQLITE_ENABLE_FTS3_PARENTHESIS -DSQLITE_ENABLE_RTREE -DSQLITE_ENABLE_STAT2 -DSQLITE_HAS_CODEC -I/home/tobee/openssl/include " \
LIBS="-lcrypto -L/mingw/lib -lgcc -lgdi32 -lopengl32" LDFLAGS="-L/home/tobee/openssl/lib"

make
make install

- Build sqlcipher-jdbc

 -- Get a source code from git hub
 
git clone https://github.com/decamp/sqlcipher-jdbc

-- Java source compilation
You can have your jdbc library just entering 'ant' command in the sqlcipher-jdbc-ant directory

ant

-- Generate jni header for jdbc
ant gen-jni

-- open MinGW console
Type 'make' in the native directory, then you get a dll file named 'sqlitejdbc.dll'

-- Junit test
Open build.xml from the directory
Set the proper directory of native.ab.dir for your environment in which the dll file located
Then, type 'ant test'
Is everything okey? 

-- Stand alone test
Run SqlcipherTest class while compile the source code from java directory in the tests directory
You can find the way of using it, if you are a java programmer.

Have some fun with sqlcipher

Good luck.

Any Question?

send me tommybee@naver.com