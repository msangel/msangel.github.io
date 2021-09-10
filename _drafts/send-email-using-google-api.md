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
Подробней о сервисном аккаунте и **процедура делегирования** описана [тут](https://developers.google.com/identity/protocols/oauth2/service-account?hl=ro) или [тут](https://developers.google.com/admin-sdk/directory/v1/guides/delegation). Основная доку [тут](https://cloud.google.com/iam/docs/service-accounts), но она скучная. [Пример](https://stackoverflow.com/questions/66103047/google-service-account-delegate-domain-wide-delegation-of-authority-to-imperson) с кодом.

### Авторизация и автентификация
Авторизация реализована с помощью oauth2, она нам и нужна. О ней можно почитать здесь:
https://developers.google.com/identity/protocols/oauth2. И как это реализовать на практике: https://developers.google.com/workspace/guides/create-credentials.

Так же существует авторизация с помощью API key. Как его использовать непонятно, и хотя гугл [делает попытку обьяснить](https://cloud.google.com/endpoints/docs/openapi/when-why-api-key) и [даже не раз](https://cloud.google.com/docs/authentication/api-keys), но очено неуспешно, потому что всеравно неочевидно какие вещи могут вызывать пользователи а какие приложениея и где грань. [Это](https://stackoverflow.com/questions/45128453/google-youtube-data-api-apikey-vs-oauth) и [это](https://stackoverflow.com/a/38054317/449553) и [это](https://stackoverflow.com/questions/39181501/whats-the-difference-between-api-key-client-id-and-service-account) обьяснения более понятны, но всеравно очень абстрактное. Хотелось бы конкретный список вещей, которых нельзя сделать с помощью API key. 

Просто для ясности, существует еще и механизм автентификации openid которая работает на базе oauth2, она описана в другом документе:
https://developers.google.com/identity/protocols/oauth2
Но в данном случае она не нужна. 

## Что есть авторизацией?
Много непонятных слов:
* refresh_token
* client_id
* client_secret
* 
*
* VerificationCodeReceiver ([my explanation](https://stackoverflow.com/a/69124583/449553))
* 

### oauth2
Описание процедуры.
Есть несколько режимов(access type): online и offline. В кратце - при offline режиме приложение кроме access_token получает и refresh_token(а детальней описано [тут](https://stackoverflow.com/questions/30637984/what-does-offline-access-in-oauth-mean/30638344)).
Полученный access_token используется для подписи запросов. Этот токен сессийный и работает пока пользователь в системе.
Полученный(если) refresh_token используется для создания новых access_token-ов.
По умолчанию для веб-приложений используется online режим, т.е. оно имеет доступ к даннім пользователя пока он работает с системой. 
По умолчанию в локальных приложениях используется offline режим, т.е. они имеют доступ к данным пользователя даже когда его нет на месте.
Есть [пример получения access_token из refresh_token.](https://github.com/heliosnarcissus/java-gmail-api/blob/main/src/main/java/com/gmailapijava/main/GmailAPIJavaMain.java#L110)) но он ненужный потому что если в обьекте типа `Credential` есть refresh_token, то можно просто вызвать метод `Credential#refreshToken`.
 

## Что по самому АПИ
Есть нормальная инструкция по настройке(там еще есть страницы) апи вообще: https://developers.google.com/api-client-library/java/google-api-java-client/setup

Установка с мавеном:
```xml
<dependency>
      <groupId>com.google.apis</groupId>
      <artifactId>google-api-services-gmail</artifactId>
      <version>v1-rev110-1.25.0</version>
    </dependency>
    <dependency>
      <groupId>com.google.oauth-client</groupId>
      <artifactId>google-oauth-client-jetty</artifactId>
      <version>1.32.1</version>
    </dependency>
    <dependency>
      <groupId>com.sun.mail</groupId>
      <artifactId>jakarta.mail</artifactId>
      <version>1.6.7</version>
    </dependency>
```
Там же примеры использования общих апи(todo: исследовать):
https://developers.google.com/api-client-library/java/google-api-java-client/oauth2

Есть туториал и по самому АПИ: 
* общее https://developers.google.com/gmail/api/quickstart/java?hl=en
* конкретно отправка https://developers.google.com/gmail/api/guides/sending
* и даже плейграунт(очень бесполезный) https://developers.google.com/gmail/api/reference/rest/v1/users.messages/send

Бесполезность плейграунда в том, что апи одновременно очень прjстое но и вместе с тем сложное. Необходимым является только поле `raw` и в него нужно записать base64 закодированное тело письма в соответствии с [rfc822](https://datatracker.ietf.org/doc/html/rfc822)(August 13, 1982). Да-да. Туда записывается та текстовая каша, которая на самом деле является письмом в сыром виде. С одной стороны - просто и понятно и нет вопросов ни по особых полях ни по аттачментах, и сбиблиотеки для генерирования этой каши буквально встроены в все языки программирования, с другой стороны ты ту кашу не софрматируешь "с руки".


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

## Альтернатива
Она всегда есть. Например [классическое SMTP](https://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/)

## Ссылки
* Примерно то же самое что и эта статья: https://mailtrap.io/blog/send-emails-with-gmail-api/
* else

