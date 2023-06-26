# practJava
Простой пример, как можно работать с таблицами в MySQL на Java (желудёвый кофе)

Внешний вид главного окна в браузере:

![image](https://github.com/alex1543/practJava/assets/10297748/492ab4fb-b62f-458e-8a2a-325ac32ba2f0)

Пример гарантированно работает при последовательности следующих действий:
1) установить JDK, например: jdk-20_windows-x64_bin.msi
2) установить Connector/J, например из файла: mysql-installer-community-8.0.33.0.msi
3) добавить в системную переменную CLASSPATH след. строку: .;C:\Program Files (x86)\MySQL\Connector J 8.0\mysql-connector-j-8.0.33.jar;

Пример не требует web-сервера Apache. Достаточно запустить файл Test.bat и открыть страницу: http://localhost:8000/ Для работы с одной таблицей, необходимо выполнить скрипт экспорта: import_test.sql

# practJava/mac
Допустима работа скрипта на macOS.
Необходимо установить: 
1) JDK, например: jdk-20_macos-aarch64_bin.dmg с официального сайта: https://www.oracle.com/java/technologies/downloads/
2) mysql-8.0.33-macos13-arm64.pkg
3) mysql-connector-c++-8.0.33-macos13-arm64.dmg
Проверить, что есть файл по след. пути: /Library/Java/Extensions/mysql-connector-j-8.0.33.jar

Пример гарантированно работает под macOS Ventura 13.4.1 (22F82).

Внешний вид главного одна в родном браузере Safari:

<img width="979" alt="sc" src="https://github.com/alex1543/practJava/assets/10297748/7fd63282-ae80-404c-bae1-d68175522631">
