---
title: Example of usage Ansible with Amazon Lightsail
date: 2022-07-07 19:59:00 Z
lang: en
---
Ansible is a tool for managing a cluster of computers. It's idea is simple - working on top of remote ssh, it execute the same command on each registered node. In opposite to classical orchestration tools, where the controller shoult be accessible(online) to dependent nodes, Ansible just requires nodes to be accessible to controller. In simple and popular case, controller is your computer. Even if it has a [lot of additional features](https://www.redhat.com/en/technologies/management/ansible/what-is-ansible), in this sample it will be used for automation of deployment java-application to remote host, in this case to Amazon Lightsail node. It is easy, straightforward, and right for that tool. No need to worry about manually connection to the server, uploading artifacts, restart service, etc. Also it is secure, as does't expose anything except already axposed ssh(22 tcp port).

### Acquiring Lightsail node
Just go there: [https://lightsail.aws.amazon.com/](https://lightsail.aws.amazon.com/), register and create a node. 

####  ssh keys
During node creation you will have a choice - either create new, either use existing ssh key. As this was my first run, I create new and the `my-key.pem` file was downloaded on my computer. `ssh` can use that to connect, just put key in safe location and grant propriate permissions to the file, like only user readable:
```bash
chmod 400 my-key.pem
```
after you can import that file
```bash
> ssh-add my-key.pem
Identity added: my-key.pem (my-key.pem)
```
Well, this will allow your ssh client to connect to target node. But Ansible by default itself uses traditional, public-key auth. The default can be changed by simply defining `my-key.pem` in Ansible settings, [example](https://www.cyberciti.biz/faq/define-ssh-key-per-host-using-ansible_ssh_private_key_file/). Or just use default, export your public key to target host using `scp` or `ssh-copy-id`:
```bash
ssh-copy-id -i $HOME/.ssh/id_rsa.pub ubuntu@1.2.3.4
```
`ssh-copy-id` is from openssh-client package, if you dont have it, just install it :
```bash
sudo apt install openssh-client
```

#### IP address of your node.
By default fresh Lightsail instance doesnt have public IP, only private one (in aws network). But that can be changed, aws provide free IP for each node that is in use. Just go to Lightsail's "network" tab and create one association. Like this:
![lightsail_ip](https://k.co.ua/resources/lightsail/lightsail_ip.png){: pretty}

Formal documentation on above: [https://lightsail.aws.amazon.com/ls/docs/en_us/articles/lightsail-create-static-ip](https://lightsail.aws.amazon.com/ls/docs/en_us/articles/lightsail-create-static-ip)

### Install Ansible
There are many ways to do that. In [documentation](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html#selecting-an-ansible-package-and-version-to-install) it suggest install it as python3 module as this will be the fresher version. Still, if you are not about dealing with python libraries, you can install in more sifisticated way some stable version. Instructions on that also in [documentation](https://docs.ansible.com/ansible/latest/installation_guide/installation_distros.html).
In my case it as simple as:
```bash
> sudo apt update
> sudo apt install software-properties-common
> sudo add-apt-repository --yes --update ppa:ansible/ansible
> sudo apt install ansible
```
After that you can test your Ansible version:
```bash
> ansible --version
ansible [core 2.12.7]
  config file = /etc/ansible/ansible.cfg
  configured module search path = ['/home/msangel/.ansible/plugins/modules', '/usr/share/ansible/plugins/modules']
  ansible python module location = /usr/lib/python3/dist-packages/ansible
  ansible collection location = /home/msangel/.ansible/collections:/usr/share/ansible/collections
  executable location = /usr/bin/ansible
  python version = 3.8.10 (default, Mar 15 2022, 12:22:08) [GCC 9.4.0]
  jinja version = 3.1.2
  libyaml = True

```
### Test run
Having a node and installed software, we can try how it actually works. As first step   here Ansible need to know his node location and how to access that. This is done by [INI-like config file](https://docs.ansible.com/ansible/latest/user_guide/intro_inventory.html). There is a default one(`/etc/ansible/hosts`). Or per command you can feed own crafted config file. This way Ansible configuration is portable. 
Lets edit my default by adding out Lightsail node:
```
1.2.3.4 ansible_ssh_user=ubuntu
```
where `1.2.3.4` is public IP i got from aws. And some extra parameter -  in my case target node username was differ from local username, so I have to define that explisitly.

It's show time!
```bash
> ansible -m ping all
1.2.3.4 | SUCCESS => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python3"
    },
    "changed": false,
    "ping": "pong"
}
```


> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTEyMzE3MjAwMzEsOTY1MjEzNjg1LDE3Mz
Y5NDA2MDYsLTIwNzU2Mjg4NjgsMjY3ODQ2MjMwLC01MjY1ODIw
MjksMTY1MjQyMDg2MywtMTIwNDI2NTkxOSwtMjA2MDM4OTQxNi
wtMTcxODU1NTk5NiwtMTc0MjcxMzI1OCwtNTg5MzAyNDcwLC0x
NjEwMDQ3Mjg1LC0xNDgyMTIwNzM3LDUxNjYyMDQ3NywxODU1OT
EzNDgwXX0=
-->