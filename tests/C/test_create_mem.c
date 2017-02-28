#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sqlite3.h>    

int main()
{
    sqlite3 * connection;
    sqlite3_open_v2(":memory:", &connection, SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE, NULL);
    sqlite3_key(connection, "passphrase", 10);
    sqlite3_close(connection);
}
