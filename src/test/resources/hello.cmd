@rem A test script for windows which prints out the arguments
@echo off
echo This is hello.cmd
echo Your arguments are:
for %%A in (%*) do (
   echo %%A
)