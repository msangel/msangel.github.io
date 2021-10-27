---
title: Публикация java-библиотеки в maven central
date: 2021-07-10 09:41:00 Z
lang: ru
---

TL;DR: пример [тут](https://github.com/msangel/strftime4j/blob/master/pom.xml)
- - -

<!-- excerpt-start -->
Так как одной из функций [gpg](https://k.co.ua/blog/2021/07/10/pgp-usage.html) является проверка аутентичности файлов, это нашло широкое использование в гарантировании, что то или иное приложение или библиотека была выпущена именно автором и по пути к пользователю не испытывало модификаций. Вопрос о том, использовать ли электронную подпись, если ты распространяешь бинарные файлы из своего сервера - это вопрос о том, волнует ли тебя безопасность твоих пользователей и твоё честное имя. Ведь если кто-то скачает вирус под видом твоей программы, то претензии, в том числе, будут и к тебе. И если на своем сайте у тебя есть выбор как выкладывать бинарники, то при использовании систем дистирибуции, таких как [maven central](https://search.maven.org/) или [npm](https://www.npmjs.co) выбора нет и вся дистрибуция идет обязательно с цифровой подписью. Вопрос только в том, кто именно будет создавать подпись. Например `npm` делает это само. Пользователи же `maven` должны позаботиться о создании подписи сами и загрузить её на сервер сами. О том, как это сделать, в том числе, и будет эта статья.
<!-- excerpt-start -->

## Инструменты
Все уже автоматизировано и в случае с maven для всего уже есть плагин. Так же публикация артефакта на сервер является встроенной функциональностью maven, поэтому множество настроек для этого тоже встроено. Однако для публикации на [maven central](https://central.sonatype.org/publish/publish-maven/) рекомендуется использовать `release` плагин вместо `deploy`.  Рассмотрим пример использования [в реальном проекте](https://github.com/msangel/strftime4j/blob/master/pom.xml).

## Структура файла
Вот эта секция отвечает за указание репозитория с исходниками. Как минимум, это необходимая настройка. А еще `release` плагин будет использовать это для создания тега в репозитории и установки новой версии внутри `pom.xml`.
```xml
<scm>
    <url>https://github.com/msangel/strftime4j</url>
    <connection>scm:git:git://github.com/msangel/strftime4j.git</connection>
    <developerConnection>scm:git:git@github.com:msangel/strftime4j.git</developerConnection>
    <tag>HEAD</tag>
</scm>
```

Эта настройка указывает куда загружать артефакты. Значения тут из рекомендаций sonatype(компании которая поддерживает `maven central`):
```xml
<distributionManagement>
    <snapshotRepository>
        <id>ossrh</id>
        <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
    </snapshotRepository>
    <repository>
        <id>ossrh</id>
        <url>https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/</url>
    </repository>
</distributionManagement>
```

Так как в моем случае я публиковал библиотеку, то и собирал я ее в формате `jar` с помощью `maven-jar-plugin`:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.2.0</version>
    <configuration>
        <archive>
            <manifest>
                <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
            </manifest>
            <manifestEntries>
                <Automatic-Module-Name>strftime4j</Automatic-Module-Name>
            </manifestEntries>
        </archive>
    </configuration>
</plugin>
```
Ключ `addDefaultImplementationEntries` сгенерирует стандартный `META-INF/MANIFEST.MF` файл. Но так как у нас библиотека и нет точки входа, то и отключить её нужно с помощью пустого свойства `main.class`:
```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <junit.version>4.13.1</junit.version>
    <main.class />
</properties>
```
А `Automatic-Module-Name` в `manifestEntries` сгенерируют необходимые для java9+ модульной системы поля.

Теперь, когда мы будем запускать профиль отправки в `maven`, нужно настроить еще и другие необходимые мелочи.
Создание `jar` с исходниками: 
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-source-plugin</artifactId>
    <version>3.2.1</version>
    <executions>
        <execution>
            <id>attach-sources</id>
            <goals>
                <goal>jar-no-fork</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
И документацией из javadoc:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>3.2.0</version>
    <configuration>
        <doclint>none</doclint>
    </configuration>
    <executions>
        <execution>
            <id>attach-javadocs</id>
            <goals>
                <goal>jar</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
И наконец-то создание подписей:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-gpg-plugin</artifactId>
    <version>1.6</version>
    <executions>
        <execution>
            <id>sign-artifacts</id>
            <phase>verify</phase>
            <goals>
                <goal>sign</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Чего-то не хватает? Да. Не хватает авторизации на `maven central` и настроек для `gpg`, как минимум пароля к приватному ключу по умолчанию(хотя и выбор ключа было бы неплохо настроить). Но так как этим настройкам место не в самой репозитории и не в `pom.xml` отчасти, то они хранятся в `~/.m2/settings.xml`
```xml
<settings>
<servers>
  <server>
    <id>ossrh</id>
    <username>MY_OSSRH_USERNAME</username>
    <password>MY_OSSRH_PASSWORD</password>
  </server>
</servers>
  <profiles>
    <profile>
      <id>ossrh-release</id>
      <activation>
        <activeByDefault>false</activeByDefault>
      </activation>
      <properties>
        <gpg.executable>gpg2</gpg.executable>
        <gpg.passphrase>GPG_PRIVATE_KEY_PASSWORD</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
</settings>
```
Кстати, `maven` имеет [встроенный механизм](https://maven.apache.org/guides/mini/guide-encryption.html) шифрования пароля. 

## Запуск
Когда все будет готово, просто выполнить эту команду и библиотека сбилдится и отправится в `maven central`:
```bash
mvn release:clean release:prepare release:perform -P ossrh
```

## Ресурсы
При написании сего использовались:
- [https://central.sonatype.org/publish/publish-guide/](https://central.sonatype.org/publish/publish-guide/)
- [https://maven.apache.org/plugins/maven-gpg-plugin/examples/deploy-signed-artifacts.html](https://maven.apache.org/plugins/maven-gpg-plugin/examples/deploy-signed-artifacts.html)
- [https://dzone.com/articles/deploy-maven-central](https://dzone.com/articles/deploy-maven-central)
- [https://andresalmiray.com/publishing-to-maven-central-using-apache-maven/](https://andresalmiray.com/publishing-to-maven-central-using-apache-maven/)