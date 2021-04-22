---
title: starting jetty on amazon
date: 2013-10-18 00:14:00 Z
---

Ok. We have  Server at amazon. (If not - go here(detailed russian manual))
We connect to it and even make apt-get update (I like centos more but i select ubuntu becose it is popular system and more tech solution are done in network)
1) Install JDK
 sudo apt-get install openjdk-6-jdk

So. First lets install jetty. Note about jetty version and java class version: http://www.eclipse.org/jetty/about.php. Jetty6 support only 1.5 java MAXIMUM.
apt-get install jetty
If you need jetty8(or other version -  download debian instalation package:
sudo wget http://dist.codehaus.org/jetty/deb/8.1.13.v20130916/jetty-deb-8.1.13.v20130916.deb
install it:
sudo dpkg -i jetty-deb-8.1.13.v20130916.deb
And thats all.



In case of normal instalation correct commands will be add in network sturtup.
Now we have jetty.
Lets make it runnable and accessible - edit /etc/default/jetty and change 
    NO_START to be 0 (or comment it out).
    JETTY_HOST=0.0.0.0

Lets start it:
 service jetty start
Do not forget to add a rule to amazone firewall. (For 8080 port, for conficuration time? 80 port for HTTP, and echo request).
Now need to make jetty runnable on 80 port. Its not simple because linux restriction for 80 port, so we just create port forwarding rule(more [here](http://docs.codehaus.org/display/JETTY/port80)):
/sbin/iptables -t nat -I PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080
And [lets make this rule persistent](http://rackerhacker.com/2009/11/16/automatically-loading-iptables-on-debianubuntu/):
iptables-save > ~/firewall.conf
vim /etc/network/if-up.d/iptables
where write this:
#!/bin/sh
iptables-restore < /home/ubuntu/firewall.conf
and make this runnable:
chmod +x /etc/network/if-up.d/iptables


Restart instance and check 80 port.
Ok. Its fine. Lets hide 8080 out of firewall.

For finding standart jetty folders and config files - see here: 
http://docs.codehaus.org/display/JETTY/Debian+Packages - it is applicable just change "jetty6" to "jetty"

Jetty8 on ubuntu has little bit diref location:
Its webapps folder are at:
/opt/jetty
Anyway you can find them with find:
find / -type d -name 'webapps'

Another good tutorila is: https://degreesofzero.com/article/19




Also most possible you need this:
sudo apt-get install libjetty-extra

Next step - protect from ddos.
There are two ways: protect on iptables level and protect on webservice level.
https://www.google.com.ua/search?q=iptable+block+dos
todo: http://blog.bodhizazen.net/linux/prevent-dos-with-iptables/

second: http://wiki.eclipse.org/Jetty/Reference/DoSFilter
TODO to


http://qnatech.wordpress.com/2008/11/25/how-to-prevent-dos-denial-of-service-attack/

Because Amazon not provide adequate ddos protection.
http://webmasters.stackexchange.com/questions/12064/is-hosting-my-web-apps-at-amazon-ec2-s3-an-adequate-defense-against-ddos

