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
```
### Ansible configuration
Ansible is just an executable and it is driven by config files. Main one called `ansible.cfg`. Search for this file will be in the following order:
 - ANSIBLE_CONFIG (environment variable if set)
 - ansible.cfg (in the current directory)
 - ~/.ansible.cfg (in the home directory)
 - /etc/ansible/ansible.cfg
Ansible will process the above list and use the first file found, all others are ignored.
This way, depending on needs the configuration can be: per command, per folder, per user and per machine. There are `ansible-config` commamd that allows to `{list,dump,view,init}` custom configs.
 
Important part of the `ansible.cfg` is `inventory` key, that is usually point to another [INI-like config file](https://docs.ansible.com/ansible/latest/user_guide/intro_inventory.html).
Sample `ansible.cfg`:
```ini
[defaults]
hostfile = hosts.ini
```
In that `hosts.ini` file are listed all the nodes ansible need to operate on. 
Sample hostfile:
```ini
1.2.3.4 ansible_ssh_user=ubuntu
```
where `1.2.3.4` is public IP i got from aws. And some extra parameter -  in my case target node username was differ from local username, so I have to define that explisitly.
If none `hostfile` is set in config, default one, located at `/etc/ansible/hosts` will be used. Also possible to override `hostfile` location by:
- passing `-i <path>` parameter to ansible executable
- setting `ANSIBLE_HOSTS` environment variable: `export ANSIBLE_HOSTS=~/hosts`

### Test run
Having a node, installed software and basic configuration(I used global one), we can try how it actually works. 
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
where:
 - -m ping : Module name to execute such as ping, shell, apt, yum and so on
 -  all : The all means "all hosts." You can speificy group name such as 'devservers' or host names too.
 
### Playbook
Playbook is just a scenario to be run, written is `yaml`-file.
You can run that using `ansible` executable directly.

In our case application deployment will consists of these steps:
 - build application locally from sources using maven
 - upload artifact to node
 - restart service

Lets create basic playbook(`java_deploy.yml`):
```yaml

```
### Build application locally
Ansible is designed to execute commands on remote nodes, but it also can execute ones on local machine.
some docs: 
- https://stackoverflow.com/questions/56048959/ansible-local-action-example-how-does-it-work
- https://docs.ansible.com/ansible/latest/user_guide/playbooks_delegation.html
- https://coderwall.com/p/xlbxkq/run-a-local-script-before-after-a-play
- https://stackoverflow.com/questions/53190538/running-mvn-clean-install-maven-command-using-ansible-module
- https://stackoverflow.com/questions/53190538/running-mvn-clean-install-maven-command-using-ansible-module
- https://docs.ansible.com/ansible/latest/collections/ansible/builtin/command_module.html
- https://docs.ansible.com/ansible/latest/collections/ansible/builtin/shell_module.html

Command we run:
```bash
> mvn clean install
```
### Upload artifact

### Start/restart service
https://docs.ansible.com/ansible/latest/collections/ansible/builtin/service_module.html
or PID in file
https://www.google.com/search?q=ansible+start+and+stop+process&oq=ansible+stop+process+&aqs=chrome.5.69i57j0i19j0i19i22i30l4.9031j0j1&sourceid=chrome&ie=UTF-8

### Final Playbook



### More power with Ansible lightsail module
If fact, Ansible can manage lightsail for its own - it can create instances, delete them, etc. Take a look on the module:
[community.aws.lightsail module – Manage instances in AWS Lightsail](https://docs.ansible.com/ansible/latest/collections/community/aws/lightsail_module.html) 

### Extra

 
> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTM0OTc1NDgwNCwtMTU4NzY2OTI0NywxNz
czMTU1NTY4LDM3NjI1OTgzLC0xOTk1MDY0MDQ5LC0xODU1OTA4
MTkzLDQwODA4NjMwMCwxNzkyOTE4OTI4LDcwMjM0NDYzOCwxNT
AzMTEzNjk2LDE0NTQzMzgzOTIsMzkwNjU4NDI4LC00ODk0OTI0
MjQsMTc3MjgwNjk4OSwtMTQ5NjQwMjMzMSwtMjAyODc1Mjg0My
wtMjA2NDMxNjE1MywxMjA5NTY4MDI4LC0xOTg3MjAyNDA2LDU5
MTM4MzI3M119
-->