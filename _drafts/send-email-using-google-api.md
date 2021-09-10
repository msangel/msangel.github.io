# Настройка Gmail Api и отправка email-ов с его помощью
## Создать гугл проект
Все есть проект, потому выбираем существующий или создаем новый.
Там: https://console.cloud.google.com/
Замечание: будет проект принадлежать организации или нет - выбирается на этом етапе. 

## Включить в нем нужные АПИ
В разделе "APIs & Services" правого выдвижного меню есть все необходимое. Найти можно в библиотеке. Нас интересует конкретно "Gmail API". Можно через поиск, можно прямой ссылкой: https://console.cloud.google.com/apis/library/gmail.googleapis.com

## Авторизация
Самый сложный вопрос. Во первых актором может выступать:
* сам пользователь
* сервисной аккаунт

### Об сервисном аккаунте 
Это особые аккаунты, которые нужны для межсерверного взаимодействия. Создание и управление ими находится в разделе "IAM & Admin > Service accounts". Некоторые апи доступны только конечному пользователю, а не сервесному аккауннту. Какие - хз. Например интересующие нас "Gmail API" доступны _только_ пользователю, ибо невозможно отправлять личную почту с аккаунта для межсерверного взаимодействия. Однако. Сервисному аккаунту можно делегировать полномочия пользователя. Потому что можно. 

### Авторизация и автентификация
Авторизация реализована с помощью oauth2

## Что есть авторизацией?
Много непонятных слов:
* refresh_token
* client_id
* client_secret
* access_token([пример получения](https://github.com/heliosnarcissus/java-gmail-api/blob/main/src/main/java/com/gmailapijava/main/GmailAPIJavaMain.java#L110))
* access type(online/offline)
* VerificationCodeReceiver ([my explanation](https://stackoverflow.com/a/69124583/449553))
* 

Способы авторизации:
* authorization with oauth2, Официальная дока по вопросу там: https://developers.google.com/identity/protocols/oauth2

Есть еще процедура автентификации openidб которая работает на базе oauth2, она описана в другом документе : https://developers.google.com/identity/protocols/oauth2/openid-connect

### oauth2
Описание процедуры.
Есть несколько режимов(access type): online и offline. В кратце - при offline режиме приложение кроме access_token получает и refresh_token.
Полученный access_token используется для подписи запросов. Этот токен сессийный и работает пока пользователь в системе.
Полученный(если) refresh_token используется для создания новых access_token-ов.
По умолчанию для веб-приложений используется online режим, т.е. 
.
 

https://stackoverflow.com/questions/30637984/what-does-offline-access-in-oauth-mean/30638344

## Примеры
todo: перенести важнные участки кода сюда
Начнем с базового:
https://github.com/search?l=Java&p=9&q=%22service.users%28%29.messages%28%29.send%22&type=Code
Отдельно интерестные:
* самый тупняковый пример из первой ссылки: https://github.com/kkashyap1707/javaApi/blob/master/src/test/java/javaApi/utilities/GMailUtil.java
* использование сервисного аккаунта и HTML для тела:
 * https://github.com/rleiwang/kakee/blob/master/Server/src/main/java/biz/kakee/external/gmail/GmailService.java
 * https://github.com/mkarthik415/telosws_old/blob/master/src/main/java/telosws/util/EmailService.java
* использование более-менее нормального флов в веб-приложении, когда в контроллере есть редирект билдер: https://github.com/rubycse/send-email-by-google/blob/master/src/main/java/net/signin/controller/SignInController.java
* как использовать refresh_token: https://github.com/ESIPFed/Geoweaver/blob/master/src/main/java/com/gw/utils/GmailAPI.java
* как хранить файлы на сервере:
  * лучшее место для временных файлов - домашняя директория пользователя: `private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/digideal");`
  * или код: ` String credentials = "{\"web\":{\"client_id\":\"\",\"project_id\":\"\",\"auth_uri\":\"\",\"token_uri\":\"\",\"auth_provider_x509_cert_url\":\"\",\"client_secret\":\"\",\"redirect_uris\":[\"\"]}}";
        InputStream in = new ByteArrayInputStream(credentials.getBytes());
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));`
 * вложения файлов: https://github.com/LaimisMilas/gmail-mail-sb/blob/master/src/main/java/lt/gmail/mail/sender/gmail/api/GmailAPIHelper.java
 * настройка сертификатов транспорта: https://github.com/mkarthik415/telosws_testing/blob/master/src/main/java/telosws/util/EmailService.java
 * таки без встроеного сервера: https://github.com/googleinterns/ResumeBuddy/blob/master/resume-buddy/src/main/java/com/google/sps/api/GmailService.java
 * Вчера был валидный а сегодня уже нет. Нужно проверять exirity и обновлять завременно(чтоб быть уверенным что на следующие 60 секунд его хватит)ю ОЧЕНЬ ВАЖНО иметь refresh token: https://github.com/1559913323/jstock/blob/master/src/org/yccheok/jstock/google/Utils.java
 *  


