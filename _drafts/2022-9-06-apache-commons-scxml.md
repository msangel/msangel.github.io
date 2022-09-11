### Definitions
State machine -  is a mathematical model of computation ([wiki](https://en.wikipedia.org/wiki/Finite-state_machine))
Pushdown automaton - a kind of state machine with stacked memory, when automate operate with memory of it's stack, but also can exit stack level, or enter into new. Computional model of computer languages interpretters and VM(and also physical ones) based on this. Not a subject of this post, just for general knowing.   
business rules engine
workflow engine
scxml

### Specs
Spec defined there: https://www.w3.org/TR/scxml/
BUT there no standart "dtd" or "xsd" schema file.
Options are:
 - unfinished draft: https://www.w3.org/2011/04/SCXML/
 - good enought: https://github.com/carlos-verdes/scxml-java/blob/master/scxml-complete.xsd
 - some unclear: https://github.com/diamondq/dq-common-java/tree/master/utils/common-utils.scxml/src/main/resources

### Engines options
scxml based:
https://commons.apache.org/proper/commons-scxml/ - hero of this post
https://github.com/carlos-verdes/scxml-java - another java implementation
https://github.com/moraispgsi/fsm-engine - node.js


### This engine
Homepage: https://commons.apache.org/proper/commons-scxml/
Sources: https://github.com/apache/commons-scxml

### Samples
https://github.com/woonsan/commons-scxml-examples
https://alexzhornyak.github.io/SCXML-tutorial/

### Complex sample


### Resources:
https://github.com/okayrunner/piper
https://spring.io/blog/2015/03/08/getting-started-with-activiti-and-spring-boot
https://github.com/meirwah/awesome-workflow-engines
https://copper-engine.org/docs/getting-started/tutorial2/
https://www.jeasy.org/
https://docs.camunda.org/get-started/spring/service-task/
https://java-source.net/open-source/workflow-engines
https://stackoverflow.com/questions/14474294/lightweight-workflow-engine-for-java
https://github.com/spring-projects/spring-webflow
https://stackoverflow.com/questions/2353564/use-cases-of-the-workflow-engine
https://projects.spring.io/spring-statemachine/#quick-start
https://workflowengine.io/blog/java-workflow-engines-comparison/
https://github.com/j-easy/easy-flows
___
https://xmdocumentation.bloomreach.com/library/concepts/workflow/scxml-workflow-engine.html
https://commons.apache.org/proper/commons-scxml/
https://xmdocumentation.bloomreach.com/library/concepts/workflow/document-workflow.html

https://www.google.com/search?q=workflow+vs+rule+engine
https://www.google.com/search?q=business+rules+engine+vs+state+machine

https://www.businessprocessincubator.com/content/business-rules-engine-and-workflow-engine-difference/


> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTI4MDY1MTM3MSwtMTI3MjMyNjY5MiwxMT
gxMTQ0MzEwLDIwOTgxMjU5MywtMTkzODk0NDkzMCwtMjc3NzE0
MDQwLDE3NzQ3MDYzNTRdfQ==
-->