@echo off
call setenv.bat
@echo on
call ant clean
call del .\bin\smtp-tobee-0.1.jar