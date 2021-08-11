Content Repository API for Java (JCR) - API for accessing repositories for CMS(content management system) and EMS(enterprise content management).

Standarts
Two standarts
 - Content Repository for Java technology API: [JSR-170](https://download.oracle.com/otndocs/jcp/contentrepository-1.0-fr-oth-JSpec/) 
 - Content Repository for Java Technology 2.0: [JSR-283](https://download.oracle.com/otndocs/jcp/content_repository-2.0-fr-oth-JSpec/)

Motivation
As of JSR-170 document intro:
> As the number of vendors offering proprietary content repositories has increased, the need for a common programmatic interface to these repositories has become apparent. The aim of the Content Repository for Java Technology API specification is to provide such an interface and, in doing so, lay the foundations for a true industry-wide content infrastructure.
    Application developers and custom solution integrators will be able to avoid the costs associated with learning the particular API of each repository vendor. Instead, programmers will be able to develop content-based application logic independently of the underlying repository architecture or physical storage.
    Customers will also benefit by being able to exchange their underlying repositories without touching any of the applications built on top of them.

Use cases:
 - CMS(Magnolia, Jahia and Hippo)
 - data structures, orginazed as tries
 - implementation-agnostic storage intermediate(works with abstract stores without relying on it's implementations)
- as back-end for some data web-servers, nice example: https://sling.apache.org/
	- [slides](http://events17.linuxfoundation.org/sites/events/files/slides/ApacheConNA-ApacheSling.pdf)

Implementations:
https://jackrabbit.apache.org/jcr/index.html
https://modeshape.jboss.org/
Oracle Beehive 
old versions of nuxeo, but no longer: [here's why](https://www.nuxeo.com/blog/why-nuxeo-dropped-jcr/)
old versions of exoplatform, but no longer: [here's why](https://www.exoplatform.com/blog/2016/06/02/why-are-we-moving-away-from-jcr/)
some PHP: https://typo3.org/, others: http://phpcr.github.io/implementations/


Resources:

 - [ecm vs cms](https://www.aodocs.com/blog/ecm-vs-cms-difference)
 - [Is jcr dead?](https://www.cmswire.com/cms/web-cms/is-jcr-dead-009676.php)

> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTEzNjQ4Nzk5NDNdfQ==
-->