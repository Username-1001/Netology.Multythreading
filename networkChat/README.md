# Курсовой проект "Сетевой чат"

## Описание проекта

Разработано два приложения для обмена текстовыми сообщениями по сети с помощью консоли (терминала) между двумя и более пользователями. 

**Первое приложение - сервер чата**, ожидает подключения пользователей.

**Второе приложение - клиент чата**, подключается к серверу чата и осуществляет доставку и получение новых сообщений.

Все сообщения записываются в File.log как на сервере, так и на клиентах. File.log дополняется при каждом запуске, а также при отправленном или полученном сообщении. Выход из чата осуществляется по команде exit.

## Реализация сервера

- Установка порта для подключения клиентов через файл настроек config.properties;
- Одновременное ожидание новых пользователей и обработка поступающих сообщений;
- Возможность подключиться к серверу в любой момент и присоединиться к чату;
- Отправка новых сообщений клиентам;
- Запись всех отправленных через сервер сообщений с указанием имени пользователя и времени отправки.

## Реализация клиента

- Выбор имени для участия в чате при подключении к серверу;
- Настройки приложения читаются из файла config.properties(порт и IP-адрес сервера);
- Для выхода из чата нужно набрать команду выхода - “/exit”;
- Каждое сообщение участников записывается в текстовый файл логирования File.log. При каждом запуске приложения файл дополняется.