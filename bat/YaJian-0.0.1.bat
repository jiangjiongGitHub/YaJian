@echo off
title YaJian Application 28888

echo Starting YaJian Application
echo.

java -Xms64m -Xmx256m -jar YaJian-0.0.1.jar --server.port=28888

echo.
echo YaJian Application Exited
pause
