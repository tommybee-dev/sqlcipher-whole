#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sqlite3.h>    


static int callback(void *NotUsed, int argc, char **argv, char **azColName) {
	/*feed this callback function to handle theresultset returned by the select statement*/
	int i;
	for (i = 0; i < argc; i++) { //loop over results
		printf("%s = %s\n", azColName[i], argv[i] ? argv[i] : "NULL"); //gota love how human radable c is
	}
	printf("\n");
	return 0;
} /*end callback*/

int main(int argc, char** argv)
{
	sqlite3 *db;
	/*int ch;*/
	if (sqlite3_open("mydb.db", &db) == SQLITE_OK) {
		printf("DB file is open\n");
		if (sqlite3_exec(db, (const char*)"PRAGMA key ='password'", NULL, NULL, NULL) == SQLITE_OK){
			printf("Accepted Key\n");
		};
		if (sqlite3_exec(db, (const char*)"CREATE TABLE testtable (id int, name varchar(20));", NULL, NULL, NULL) == SQLITE_OK) {
			printf("Created Table\n");
		};
		if (sqlite3_exec(db, (const char*)"INSERT INTO testtable (id,name) values (0,'alice'), (1,'bob'), (2,'charlie');", NULL, NULL, NULL) == SQLITE_OK) {
			printf("Gave it some data\n");
		};
		if (sqlite3_exec(db, (const char*)"SELECT * FROM testtable;", callback, NULL, NULL) == SQLITE_OK) {
			printf("Sent Select\n");
		};
	}
	sqlite3_close(db);  /*close it up properly*/
	
	/*ch = getch(); just a hack to keep the console open during debugging*/
	/*printf("%c\n", ch);*/
	return 0;
	
}/* end SQLCipherTest.cpp*/
