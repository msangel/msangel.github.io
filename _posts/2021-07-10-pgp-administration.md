---
title: Основы работы с GnuPG, часть 1. Администрирование ключей
date: 2021-07-10 09:41:00 Z
---

Подробней о инструменте можно почитать на его [сайте](https://www.gnupg.org/index.en.html), но если вкратце, то это такой мультикомбайн в мире криптографии. **Понимание происходящего упрощается в разы, если считать gpg просто как менеджер ключей.** Проект GnuPG это реализация спецификации OpenPGP (так же [rfc4880](https://www.ietf.org/rfc/rfc4880.txt)), поэтому существуют и альтернативные реализации и расширения.  Помимо инфраструктуры управления ключами спецификация описывает использование симметричной криптографии и криптографии с открытым ключом, может применяться как для шифрования файлом, так и целых дисков.

Самым частым сценарием использования является **гибридная криптография**. В целом её суть такова:
Для шифрования создается сеансовый ключ симметричного шифрования, им шифруются данные, потом открытым ключом шифруется сам сеансовый ключ и передается вместе с шифрованными данными
Для расшифрования закрытым ключом расшифровывается сеансовый ключ, а потом ним расшифровывается само сообщение.
Таким образом эта схема имеет удобство асимметричной криптографии на публичных ключах и скорость работы симметричной криптографии.

### Создадим тестовую среду
Пропустить этот шаг если уверен в своих силах.
Контейнер:
```bash
docker run -it --rm ubuntu
```
Для тестового образа нужно распаковать и установить документацию(еще пригодится), минимальный набор инструментов и создадим пользователя для максимальной реалистичности(но не забудем привязать к пользователю девайс клавиатуры иначе генерация ключей вылетит с ошибкой) :
```bash
unminimize
apt update
apt install man sudo curl -y
adduser msangel
adduser msangel sudo
su - msangel
sudo chown msangel /dev/pts/0
```

### Установка инструментария
Теперь установим необходимые инструменты с которыми будем работать:
```bash
sudo apt update
sudo apt install gnupg2 -y
```
<details>
<summary markdown='span'>Разница между gnupg и gnupg2</summary>
  
Вторая версия этой программы хоть и совместима с первой на уровне протокола(т.е. с точки зрения использования они взаимозаменяемые), но по факту она переписана с нуля. Также изменен подход к формату сохранения ключей, однако в целях совместимости вторая версия будет использовать старый формат, если в системе есть хотя бы один keyring в старом формате. Первая версия в современных дистрибутивах помечена как `deprecated`, и хотя её еще можно поставить параллельно с второй и пользоваться, но зачем? Сама же команда `gpg` в современных дистрибутивах это просто синоним для `gpg2`, с добавлением некоторой магии совместимости.
Больше об этом:

 - https://superuser.com/questions/655246/are-gnupg-1-and-gnupg-2-compatible-with-each-other

</details>



### Исследуем файлы и возможности
Для того, чтоб узнать достатучную иформация об установленой программе, её файлах и среде, достаточно выполнить эту команду:
```bash
> gpg --version
``` 
Пример ответа ниже:
```
gpg (GnuPG) 2.2.19
libgcrypt 1.8.5
Copyright (C) 2019 Free Software Foundation, Inc.
License GPLv3+: GNU GPL version 3 or later <https://gnu.org/licenses/gpl.html>
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.

Home: /home/msangel/.gnupg
Supported algorithms:
Pubkey: RSA, ELG, DSA, ECDH, ECDSA, EDDSA
Cipher: IDEA, 3DES, CAST5, BLOWFISH, AES, AES192, AES256, TWOFISH,
        CAMELLIA128, CAMELLIA192, CAMELLIA256
Hash: SHA1, RIPEMD160, SHA256, SHA384, SHA512, SHA224
Compression: Uncompressed, ZIP, ZLIB, BZIP2
```
Из ответа мы узнали о версии программы, поддерживаемых алгоритмах шифрования и компрессии и о месте, где все ключи будут храниться. К слову, это место можно изменить, установив переменную окружения(`export GNUPGHOME=/path/to/the/folder`)

Что находится в папке home? Начнем с того, что пока мы не создали ни одного ключа, эта папка еще даже не существует:
```bash
> ls -la /home/msangel/.gnupg
ls: cannot access '/home/msangel/.gnupg': No such file or directory
```

Для того, чтоб получить справку по использовании программы, можно воспользоваться и стандартными для этого средствами(в наличии только в нормальных системах):
```bash
man gpg2
```

**Перечислить все публичные ключи** можно командой
```bash
> gpg --list-keys
```
Команда ничего не вернет, потому что пока что нет никаких ключей. 

Но зато будут созданы хранилища для них, так как их пока не было:
```bash
gpg: directory '/home/msangel/.gnupg' created
gpg: keybox '/home/msangel/.gnupg/pubring.kbx' created
gpg: /home/msangel/.gnupg/trustdb.gpg: trustdb created
```
Что это за файлы?

`pubring.kbx` - это хранилище для публичных ключей в специально формате ([подробней](https://www.gnupg.org/documentation/manuals/gnupg/kbxutil.html)
посмотреть внутрь можно командой: 
```bash
> kbxutil --stats ~/.gnupg/pubring.kbx
```
`trustdb.gpg` - файл базы доверия. Хранит информацию об уровне доверия к наличным ключам.

### О ключах
Ключи сохраняются либо в виде пар(приватный-публичный) либо только публичные. 

1. Публичные. Используются для закодирования сообщений, которые в итоге могут быть прочитаны только владельцем соответственного приватного ключа и для проверки подписи, сделаной приватным ключем. Именно этими ключами из пары обмениваются в сети.
2. Приватные. Используются для раскодирования сообщений. Вопрос о том, можно ли имея _только_ приватный ключ получить публичный [открытый](https://stackoverflow.com/q/5244129/449553). [Обычно](https://superuser.com/a/814421) публичный ключ экспортируется вместе с приватным. Так же используются для создания сигнатуры, которая можут быть проверена только соответствующим публичным ключем. Этот ключ хранится дома и никому никуда никогда не оправляется.

### О ключах
Помимо всего, у ключей бывает разное предназначение. В соответствии с систематикой pgp это:
```txt
[C]           (0x01)       Key (C)ertification (sign a key)
[S]           (0x02)       (S)ign (sign some data ,like a file)
[E]           (0x04, 0x08) (E)ncrypt Communications(0x04) and (E)ncrypt Storage(0x08)
[<no letter>] (0x10)       Split key
[A]           (0x20)       Authentication (authenticate yourself to a computer)
[<no letter>] (0x80)       Held by more than one person
```

Теперь, об встречаемых далее аббревиатурах:
```text
sec => 'SECret key'
ssb => 'Secret SuBkey'
pub => 'PUBlic key'
sub => 'public SUBkey'
uid => 'User ID, this is the user information associated with the secret key'
ssb> => '>' after ssb means that your subkeys are not the machine. Instead they are on a smartcard.
sec# => ''#' after sec means that your secret key is missing from the machine. But it has a reference to the secret key.
```
И много [других](https://github.com/gpg/gnupg/blob/master/doc/DETAILS#field-1---type-of-record).

Так же есть выбор в выборе алгоритмов для ключей. Разные алгоритмы для разных задач. [RSA](https://uk.wikipedia.org/wiki/RSA) является вроде как универсальным. У меня длина ключа 2048 bit, хотя уже сейчас рекомендуется создавать новые ключи длиной 4096 bit. 

### Импорт публичного ключа
Вот пример публичного ключа(скачаный из интернета), который можно импортировать, просто сохранить как `chriserin.pub` и добавить такой контент:
```txt
-----BEGIN PGP PUBLIC KEY BLOCK-----
Version: GnuPG v2

mQENBFjceoMBCADDgOGwaQAVv53E+vT1RhYtUBdU7igdKb+K1cBb/0y7SV9A7zBi
/N5z2GhsL6cU7vXoX2QoMpDhQ2MYP48nWCmVeff9izinqrjXA05ViSMnPhpEGuq0
joiAy3QE1xzdiYdvmOcr4PR2rDuz7kIybJi41a+4TPA2fqkGtOsaXfwm6qH5KwhE
oEu1QuZi8X7aiHA0A/tN2sKSos7JSf7G/Ps1XQlkCgF8hQ19jSy3uaZ3d69Rplqf
UMMukPzlQYQCXltuAyqac86G/jVHJPMmL4ttAagC9vQMnHdQyOhzXhjERGSOCt6w
MW+sv1JGUROB+rNIYBktF7sviQBwwDyxODX9ABEBAAG0OkNocmlzIEVyaW4gKEJl
IEtpbmQgVG8gT3RoZXJzKSA8Y2hyaXMuZXJpbkBoYXNocm9ja2V0LmNvbT6JATkE
EwEIACMFAljceoMCGwMHCwkIBwMCAQYVCAIJCgsEFgIDAQIeAQIXgAAKCRBaN+T0
fHMfFK8cCACtkhMQ0AumUL9M3eDR8P5DPhGOH14SuobeL5fl/iLTaho2P9ie+R+f
wFG6m1s0+HxNGMqHTqDdkRYejfwlovPh96aDWz7rwzlECw5zDoLKvxnbk7Lkcy8I
Sl+9JwzAEra2wn+V2iTD9kBNBrzB6IqLny6Q/+i6xaLamANH3ZYgwVQbxt05nUPO
anGiex5Gu0i5O9FfuHwUjiFdkdtpP5pHX1Lbq0rW8c4rt/6uTQ4DAISIKgphZDw5
Bcg1QytVQ4jdB7Wjyf6uTtDc01pDj79v0VA2O4r/cA6Qk5UwdCmBbKYFARFEEUQT
CdVKPP/TfOdmPbLjhbn4A3d5V8AsCi1TuQENBFjceoMBCACy7x3s5kprHmNiKpga
8YP4BhZKG29UTd5TSiPi2M66pXTEz7HHFcW/sZH0i9o5UGS2ueh7m5tRQk8+REVp
Cn7MCOOaaQuS3ISRbZxJIloojHwT7W1SV25KH2Xf2Hs/R//Evu9ajdX5ohfVsZ55
DFyptEi7BX1dz445+htCMUwAFwjkWnUSG3D1ueFOG/bhZbytfthBiInqHgVh6gYQ
e18ond4fcsFqzwj2FpfoXa7DYl/RiywfYRkZFjaddJ8+cqdU3Vrt57lRoLd206US
s5ql77yam3pzWWif+nmiSnPJujj5PzLZWlcUSz3cQ8rAs6ZVFFoxULbBu97ZQ3T6
/lOXABEBAAGJAR8EGAEIAAkFAljceoMCGwwACgkQWjfk9HxzHxRU/Af9HkN3dqAx
KsIyXQRnPq33JHTw82urcpRlytAozDhym5PJzUeQsVZ8EZYrjHMa+YgjJ6OR2d4R
aH7cAJUHgig7DZog7ilPJDv/JOtMlHkHBqY7O8SNicuNVr6SNRbO5FRhaXTi4pIE
WMYl/Jni+0DL0GyIhnzIpxzJGYtY88EJ0obpqWENj829gqFUaHjPs7k0L/1uPTWY
AT8jdSukbKLc4xtFTa9R/8G8QLW+DtQiDWk+cuVpnF4YiCOIKWCp/rVtdv55EytH
g+4Xod7FGO4t8WQTPEXb08DLae6ikLMp7gMXDs0kXnk3P1w646nwbpBUi1z0AkZv
SUlCJaGiY+Eclg==
=qZVn
-----END PGP PUBLIC KEY BLOCK-----
```
Собственно импорт можно сделать командой 
```bash
> gpg --import chriserin.pub
gpg: key 5A37E4F47C731F14: public key "Chris Erin (Be Kind To Others) <chris.erin@hashrocket.com>" imported
gpg: Total number processed: 1
gpg:               imported: 1

```
После этого можно проверить свои ключи:
```bash
> gpg --list-keys
/home/msangel/.gnupg/pubring.kbx
--------------------------------
pub   rsa2048 2017-03-30 [SC]
9D1CA7775CDE4532873128E05A37E4F47C731F14
uid           [ unknown] Chris Erin (Be Kind To Others) <chris.erin@hashrocket.com>
sub   rsa2048 2017-03-30 [E]
```
Окей. Что мы здесь видим? На самом деле больше чем ожидалось. Можно даже [подробно](https://davesteele.github.io/gpg/2014/09/20/anatomy-of-a-gpg-key/):
```bash
> gpg -a --export "Chris Erin" | gpg --list-packets --verbose --debug 0x02
```
Так вот. Вместе с публичным ключем в бандл так же попал субключ, которым собственно и будет проводиться процедура закодирования сообщений. Этот субключ был создан вместе с основным ключем еще на этапе генерации пары. Да. Сгенерировано было две пары. Причины на это [есть](https://serverfault.com/questions/397973/gpg-why-am-i-encrypting-with-subkey-instead-of-primary-key). Т.е. один основной ключ - используется только для подписания всего и еще один ключ используется для кодирования сообщений. Все в соответствии с флагами: [SC] и [E].
Этот ключ (вместе с подключем) был сохранен в `pubring.kbx`:
```bash
> kbxutil --stats ~/.gnupg/pubring.kbx
Total number of blobs:        2
header:                       1
empty:                        0
openpgp:                      1
x509:                         0
non flagged:                  1
secret flagged:               0
ephemeral flagged:            0
```


### Генерация своего приватного ключа
Варианты:
* `gpg --quick-generate-key` - самый простой вариант, вызов в формате `--quick-generate-key USER-ID [ALGO [USAGE [EXPIRE]]]`
* `gpg --generate-key` - простой диалог с основными вопросами. Так же сгенерирует ключ отзыва.
* `gpg --full-generate-key` - диалог со всеми опциями.

Выбираем алгоритм: `(1) RSA and RSA`, выставляем нужное время истечения годности, вводим желаемое имя и email и желательно не заполняем поле `Comment` ничем ненужным, потому что этот текст будет всюду.
После ввода всех данных программа выдаст что-то вроде этого:
```bash
gpg: key A0FECC5008F47665 marked as ultimately trusted
gpg: revocation certificate stored as '/home/msangel/.gnupg/openpgp-revocs.d/F0FB65DD715EB503E228D077A0FECC5008F47665.rev'
public and secret key created and signed.

pub   rsa4096 2021-10-17 [SC]
      F0FB65DD715EB503E228D077A0FECC5008F47665
uid                      Artem Ivanov <ivanov@mail.com>
sub   rsa4096 2021-10-17 [E]

```
Теперь у нас есть: пара приватного и публичного основного ключа и пара приватного и публичного суб-ключа.
Публичные ключи можно проверить все так же:
```bash
> gpg --list-keys --with-subkey-fingerprints
/home/msangel/.gnupg/pubring.kbx
--------------------------------
pub   rsa2048 2017-03-30 [SC]
      9D1CA7775CDE4532873128E05A37E4F47C731F14
uid           [ unknown] Chris Erin (Be Kind To Others) <chris.erin@hashrocket.com>
sub   rsa2048 2017-03-30 [E]
      6BD310F7401C9B6FAF72FFEEB083E3061CC31A83

pub   rsa4096 2021-10-17 [SC]
      F0FB65DD715EB503E228D077A0FECC5008F47665
uid           [ultimate] Artem Ivanov <ivanov@mail.com>
sub   rsa4096 2021-10-17 [E]
      80E5073750BCF6E01678DA0E493DE320BD233CA6

```

Для просмотра приватных ключей используется такая команда:
```bash
gpg --list-secret-keys  --with-subkey-fingerprints --keyid-format=long
/home/msangel/.gnupg/pubring.kbx
--------------------------------
sec   rsa4096/A0FECC5008F47665 2021-10-17 [SC]
      F0FB65DD715EB503E228D077A0FECC5008F47665
uid           [ultimate] Artem Ivanov <ivanov@mail.com>
ssb   rsa4096/493DE320BD233CA6 2021-10-17 [E]
      80E5073750BCF6E01678DA0E493DE320BD233CA6

```

Оригинальный приватный ключ: F0FB65DD715EB503E228D077A0FECC5008F47665
И кже упоминаемый суб-ключ 80E5073750BCF6E01678DA0E493DE320BD233CA6 который был сгенерирован вместе с ним (маркирован [E], т.е. только для шифрования).
Часто рекомендуется создать еще один суб-ключ для подписывания[S], чтоб не использовать мастер-ключ для этого.


### Экспортируем ключи
#### Публичный
В файл:
```bash
> gpg --output public.pgp  --armor --export "ivanov@mail.com"
```
или в консоль:
```bash
gpg --armor --export "Artem Ivanov"
```
где `--armor` указывает на текстовый вывод (по умолчанию бинарный).

#### Приватный
В файл:
```bash
gpg --output private.pgp --armor --export-secret-key "ivanov@mail.com"
```
или в консоль:
```bash
gpg --armor --export-secret-key "Artem Ivanov"
```
### Импортируем ключи

#### Публичный
Для примера возьмем ключ Киррила Раевского(кем бы он не был):
```bash
> curl -sSL https://python273.pw/key.asc | gpg --import -
gpg: key B8A168B3A02C01E3: public key "python273 (python273.pw) <iam@python273.pw>" imported
gpg: Total number processed: 1
gpg:               imported: 1
```
Ну и по умолчанию уровень доверия к ключам [ unknown], и это можно исправить:
```bash
> gpg --edit-key iam@python273.pw
gpg> trust
```

#### Приватный
Та так же. По самому ключу понятно какой он:
```bash
gpg --import  my.key 
gpg: key 41CB98F33B06146E: public key "Vasyl Khrystiuk <h6.msangel@gmail.com>" imported
gpg: key 41CB98F33B06146E: secret key imported
gpg: Total number processed: 1
gpg:               imported: 1
gpg:       secret keys read: 1
gpg:   secret keys imported: 1
```

### Удаление ключей
Удаление самих ключей:
```bash
gpg --delete-key key-ID # публичные
gpg --delete-secret-keys key-ID # приватные
```
Суб-ключи будут удалены вместе с основными.
Так же можно удалять только суб-ключи:
```bash
> gpg --edit-key key-ID
gpg> list
gpg> key 0
gpg> delkey 
```

### О серверах ключей
Для публикации публичных ключей используются специальные публичные сервера (keyserver). Вот краткий список некоторых таких серверов:

- `keyserver.ubuntu.com` (recommended, supported by maven central)
- `keys.openpgp.org` (supported by maven central)
- `pgp.mit.edu` (supported by maven central)
- `keyserver.pgp.com`

#### Пару слов о протоколе сервера ключей
В отличие от `HTTP` сервер ключей работает на собственном протоколе `HKP` (OpenPGP HTTP Keyserver Protocol) описанном в [черновике](https://datatracker.ietf.org/doc/html/draft-shaw-openpgp-hkp-00). Технически это текстовый протокол который использует TCP порт 11371 по умолчанию. Современная модификация этого протокола: hkps (HKP over TLS). Тем не менее, большинство современных реализаций кейсерверов имеют и HTTP/HTTPS интерфейсы. И как напоминание - наличие ключа на кейсервере это всего лишь фактор для доверия ему, но не достаточная причина.

#### Работа с сервером ключей
##### Поиск чужих ключей на сервере:
```bash
> gpg --keyserver keyserver.ubuntu.com --search-keys 50B670A8DE1F3CD89583895241CB98F33B06146E
gpg: data source: http://162.213.33.9:11371
(1)	Vasyl Khrystiuk <h6.msangel@gmail.com>
	  2048 bit RSA key 41CB98F33B06146E, created: 2021-03-17
Keys 1-1 of 1 for "50B670A8DE1F3CD89583895241CB98F33B06146E".  Enter number(s), N)ext, or Q)uit > Q
```

##### Отправка ключа на сервер.
Отправка на дефолтный keys.openpgp.org выглядит так: 
```bash
> gpg --send-keys F0FB65DD715EB503E228D077A0FECC5008F47665
gpg: sending key A0FECC5008F47665 to hkps://keys.openpgp.org
```
Однако следует помнить, что keys.openpgp.org [имеет систему подтверждения пользователей](https://superuser.com/a/1485255) которая доступна только через [веб-интерфейс](https://keys.openpgp.org/upload). 

Можно выбрать на какой сервер отправить ключ:
```bash
gpg --keyserver hkps://keyserver.ubuntu.com --send-keys F0FB65DD715EB503E228D077A0FECC5008F47665
```
Проверить что ключ там(и при желании импортировать):
```bash
gpg --keyserver hkps://keyserver.ubuntu.com --search-keys F0FB65DD715EB503E228D077A0FECC5008F47665
```

### Отзыв ключей
Если приватный ключ был дискредитирован, его нужно отозвать. Наивный способ это сделать - сгенерировать специальный сертификат и отправить на сервер с ключами. Это работает:
```bash
> # Revoke your key
> gpg --output revoke.asc --gen-revoke key-ID
> # Import revocation certificate into your keyring
> gpg --import revoke.asc
> # Search your key on the key-server
> gpg --keyserver pgp.mit.edu --search-keys key-ID
> # Send the revoked public key to the key-server
> gpg --keyserver pgp.mit.edu --send-keys key-ID
```
Однако для генерации такого сертификата нужно указать пароль, которым защищен приватный ключ ну и нужен сам приватный ключ. Пароль можно забыть, файл приватного ключа можно потерять. Потому при генерации каждой пары ключей pgp так же создает сразу ключ для отзыва. Для того чтоб им воспользоваться, достаточно его импортировать и отправить на сервер:
```bash
# наш ключ отзыва
> ls `~/.gnupg/openpgp-revocs.d/
F0FB65DD715EB503E228D077A0FECC5008F47665.rev`
# отключаем предохранитель в виде ":" в начале ключа, удаляем один символ
sed -i -e 's/:-----BEGIN PGP/-----BEGIN PGP/g' ~/.gnupg/openpgp-revocs.d/
F0FB65DD715EB503E228D077A0FECC5008F47665.rev

# импортируем ключ, это обозначает данный ключ как отозванный в локальной связке.
> gpg --import ~/.gnupg/openpgp-revocs.d/F0FB65DD715EB503E228D077A0FECC5008F47665.rev 
gpg: key A0FECC5008F47665: "Artem Ivanov <ivanov@mail.com>" revocation certificate imported
gpg: Total number processed: 1
gpg:    new key revocations: 1
gpg: marginals needed: 3  completes needed: 1  trust model: pgp
gpg: depth: 0  valid:   1  signed:   0  trust: 0-, 0q, 0n, 0m, 0f, 1u
# Теперь отправляем отозванный сертификат на кей-сервер
> gpg --keyserver hkps://keyserver.ubuntu.com --send-keys F0FB65DD715EB503E228D077A0FECC5008F47665
gpg: sending key A0FECC5008F47665 to hkps://keyserver.ubuntu.com
# проверяем статус
> gpg --list-secret-keys  --with-subkey-fingerprints --keyid-format=long
/home/msangel/.gnupg/pubring.kbx
--------------------------------
sec   rsa4096/A0FECC5008F47665 2021-10-17 [SC] [revoked: 2021-10-17]
      F0FB65DD715EB503E228D077A0FECC5008F47665
uid                 [ revoked] Artem Ivanov <ivanov@mail.com>
```
Для чистоты эксперимента создадим еще одную тестовую среду и попробуем импортировать там ключ из кей-сервера. Обновление на сервере заняло около 5 минут.
```bash
> gpg  --keyserver hkps://keyserver.ubuntu.com --search-keys F0FB65DD715EB503E228D077A0FECC5008F47665
gpg: data source: https://162.213.33.9:443
(1)	Artem Ivanov <ivanov@mail.com>
	  4096 bit RSA key A0FECC5008F47665, created: 2021-10-17
Keys 1-1 of 1 for "F0FB65DD715EB503E228D077A0FECC5008F47665".  Enter number(s), N)ext, or Q)uit > 1
gpg: key A0FECC5008F47665: "Artem Ivanov <ivanov@mail.com>" revocation certificate added
gpg: key A0FECC5008F47665: "Artem Ivanov <ivanov@mail.com>" 1 new signature
gpg: Total number processed: 1
gpg:         new signatures: 1
> gpg --list-keys  --with-subkey-fingerprints --keyid-format=long
/home/msangel/.gnupg/pubring.kbx
--------------------------------
pub   rsa4096/A0FECC5008F47665 2021-10-17 [SC] [revoked: 2021-10-17]
      F0FB65DD715EB503E228D077A0FECC5008F47665
uid                 [ revoked] Artem Ivanov <ivanov@mail.com>

```

### Субключи
TBD
Как сертифицировать. 
Как публиковать. 
Как проверить сертификацию. 
Как обновить срок жизни. 
Как опубликовать изменения. 
Как скачать изменения.

### Бекап и восстановление
Хотя для этого нужно всего лишь экспортировать свои пары ключей и сохранить их в надежном месте, а рецепты для этого написаны выше, но не стоит игнорировать это. Компьютеры приходят и уходят, а цифровая идентичность остается навсегда. Пожалуй что мне действительно понравилось - это идея хранить ключи в [печатном виде](https://www.saminiir.com/paper-storage-and-recovery-of-gpg-keys/). Я бы распечатал ключ для отзыва.

### Другие возможности
1. В один ключ можно добавить несколько идентификаторов(например домашнюю и рабочую адреса почт) и обновить ключ на сервере. 
2. Так же идентификаторы можно удалить (если вы отправили ключ в реестр, то его можно
только отозвать). 
3. Можно создать субключ только с возможностью аутентификации и использовать его в качестве ssh ключа.
4. Можно установить [опции](https://www.gnupg.org/documentation/manuals/gnupg/GPG-Configuration-Options.html) работы с системой внеся пожелания в `~/.gnupg/gpg.conf` файл. 
5. Система задумывалась как "распределенная сеть доверия", где я могу, например, верить какому-то ключу "с большей вероятностью", если его подписало, например "больше трех проверенных мной человек". [Так-то](https://www.gnupg.org/gph/en/manual/x334.html).
6. К публичному ключу можно присоединить фото. Это не должно уверять вас в правильности ключа. Поэтому я бы не стал. Но возможность [есть](
https://help.gnome.org/users/seahorse/stable/pgp-photoid.html.en).
7. Кроме fingerprints есть и [другие возможности](https://security.stackexchange.com/questions/231295/what-is-the-difference-between-the-many-identifiers-in-gnupg) идентифицировать ключи. Например, имя файла приватного ключа формируется на основе keygrip (--with-keygrip) но это внутрении детали. Fingerprints достаточно.


### Заключение
В целом консольная программа удобна и понятна, однако если нет желания играться в консоль, существует много GUI-инструментов. При желании их можно найти и сравнить. Сам же я предпочитаю пользоваться программой `GPA` которая поставляется в комплекте с всем пакетом.
![GPA](https://k.co.ua/resources/pgp/Screenshot_2021-10-17_17-56-40.png){: pretty}

### Используемые материалы
- [https://www.gnupg.org/gph/en/manual.html](https://www.gnupg.org/gph/en/manual.html)
- [https://davesteele.github.io/gpg/2014/09/20/anatomy-of-a-gpg-key/](https://davesteele.github.io/gpg/2014/09/20/anatomy-of-a-gpg-key/)
- [https://hashrocket.com/blog/posts/encryption-with-gpg-a-story-really-a-tutorial](https://hashrocket.com/blog/posts/encryption-with-gpg-a-story-really-a-tutorial)
- [https://github.com/gpg/gnupg/blob/master/doc/DETAILS#field-1---type-of-record](https://github.com/gpg/gnupg/blob/master/doc/DETAILS#field-1---type-of-record)

### Хорошие статьи по теме
Мне очень понравился цикл статей от [Jan Mosig](https://blogs.itemis.com/en/author/jan-mosig) об безопасности вокруг pgp. Не уверен, что их нужно читать, но вот ознакомиться точно не помешает:
 1. https://blogs.itemis.com/en/openpgp-on-the-job-part-1-what-is-it
 2. https://blogs.itemis.com/en/openpgp-on-the-job-part-2-before-we-start
 3. https://blogs.itemis.com/en/openpgp-on-the-job-part-3-install-and-configure
 4. https://blogs.itemis.com/en/openpgp-on-the-job-part-4-generating-keys
 5. https://blogs.itemis.com/en/openpgp-on-the-job-part-5-thunderbird-enigmail
 6. https://blogs.itemis.com/en/openpgp-on-the-job-part-6-e-mail-encryption-on-android-with-k-9-mail-openkeychain
 7. https://blogs.itemis.com/en/openpgp-on-the-job-part-7-improved-security-with-yubikey
 8. https://blogs.itemis.com/en/openpgp-on-the-job-part-8-ssh-with-openpgp-and-yubikey

