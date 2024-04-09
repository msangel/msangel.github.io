---
title:  Selecting self-hosted git service
date: 2024-04-09 16:55:00 Z
lang: en
---
###
 
### Purpose
In the world of open-source software, it's not a problem to find free-to-use git server for own work and so made this work available to everyone. Most popular are: github, gitlab, bitbucket. There are plenty of them. Most of them do provide paid features. Most do have a private repositories. But not all of them you can own. 

You can access to some servers and do some work on them, but physically your work on someone's computer. 

And this is not always an option for private software. Either you don't trust those servers, either want to keen your source codes on your own computer or some policy pushes you to that. 

So here I will list some popular git server software you can run on your own computer. As an option, I also will discuss some cloud services for that, which are something in the middle - you're still on someone's computer but run inside ov virtual VM you bought.

### Options
There a lot of them, but here I picked ones I found popular.

Self-hosted git services:
 - [Gitea](https://about.gitea.com/products/gitea/)
 - [GitLab](https://about.gitlab.com/install/)
 - [BitBucket](https://bitbucket.org/)
 - [Gerrit Code Review](https://www.gerritcodereview.com/)
 - [GNU Savannah](https://savannah.gnu.org/)
 - [GitBucket](https://gitbucket.github.io/)
 - [Gogs](https://gogs.io/)

 - [Buddy](https://buddy.works/docs/version-control)
 - [Perforce Helix TeamHub](https://www.perforce.com/products/helix-teamhub)

Cloud git services:
 - [Azure DevOps Server](https://azure.microsoft.com/ru-ru/products/devops/server)
 - [Gitea cloud](https://cloud.gitea.com/)
 - [Assembla Enterprise](https://get.assembla.com/assembla-enterprise)

Obviously, any self-hosted service can be deployed to the cloud. Idea behind to have it "cloudy" is deployment in a few clicks.

Let's define some features on which we will compare them: 
 - free
 - has builtin CI/CD
 - online code editor
 - code review
 - issue tracker
 - I had a successful experience working with it (very subjective BUT still a factor)

The table:

| product                | free?                                                                                  | builtin CI/CD                                                                                                                          | code editor | issue tracker                                                          | worked with it |
|------------------------|----------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------|-------------|------------------------------------------------------------------------|----------------|
| Gitea                  | yes                                                                                    | [yes](https://docs.gitea.com/usage/actions/overview)                                                                                   | yes         | yes                                                                    | yes            |
| GitLab                 | [partially](https://about.gitlab.com/pricing/feature-comparison/)                      | [yes](https://docs.gitlab.com/ee/ci/)                                                                                                  | yes         | yes                                                                    | yes            |
| BitBucket              | [no](https://www.atlassian.com/software/bitbucket/pricing?tab=self-manageddata-center) | [yes](https://bitbucket.org/product/ru/features/pipelines)                                                                             | yes         | yes                                                                    | yes            |
| Gerrit Code Review     | yes                                                                                    | no (but still [have hooks](https://gerrit-documentation.storage.googleapis.com/Documentation/3.9.2/config-hooks.html))                 | no          | [partially via plugins](https://www.gerritcodereview.com/plugins.html) | yes            |
| GNU Savannah           | yes                                                                                    | no                                                                                                                                     | no          | yes                                                                    | no             |
| GitBucket              | yes                                                                                    | [via plugin](https://github.com/takezoe/gitbucket-ci-plugin)                                                                           | yes         | yes                                                                    | no             |
| Gogs                   | yes                                                                                    | no (but still have [webhooks](https://gogs.io/docs/features/webhook))                                                                  | yes         | yes                                                                    | no             |
| Buddy                  | [no](https://buddy.works/on-premises)                                                  | [yes](https://buddy.works/docs/pipelines)                                                                                              | yes         | no                                                                     | no             |
| Perforce Helix TeamHub | [no](https://www.perforce.com/products/helix-teamhub/pricing)                          | no                                                                                                                                     | yes         | yes                                                                    | no             |
| Azure DevOps Server    | [no](https://azure.microsoft.com/en-gb/pricing/details/devops/azure-devops-services/)  | [yes](https://learn.microsoft.com/en-us/azure/devops/pipelines/architectures/devops-pipelines-baseline-architecture?view=azure-devops) | yes         | yes                                                                    | yes            |
| Gitea cloud            | [no](https://about.gitea.com/pricing/)                                                 | yes                                                                                                                                    | yes         | yes                                                                    | no             |
| Assembla Enterprise    | [no](https://get.assembla.com/pricing/)                                                | no                                                                                                                                     | no          | yes                                                                    | no             |{:class="table table-bordered"}

#### Summary
For new projects, I would rather pick Gitea or GitLab if the target is self-hosted and fully controlled environment. Because they are free, feature-rich and under active development. Bitbucket is a good product too, but is not in this list just because it is too pricey(starting from $2,300 per year).  

If the new project had to be developed in Azure infrastructure, I would pick Azure DevOps Server, because it is still feature-rich and because of integration.  

When the target is speed and easiness, I would pick paid Gitea cloud. Prices are not that high, but you are getting maganed Gitea instance in a few clicks.   

### Useful links
- [Services comparison by gitea](https://docs.gitea.com/next/installation/comparison)

