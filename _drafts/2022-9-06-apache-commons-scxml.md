### Definitions
**State machine** -  is a mathematical model of computation ([wiki](https://en.wikipedia.org/wiki/Finite-state_machine))
**Pushdown automaton** - a kind of state machine with stacked memory, when automate operates with the memory of its stack, but also can exit stack level, or enter into new. A computational model of computers, programming languages and VM is based on this. Not a subject of this post, just for general knowledge. ([wiki](https://en.wikipedia.org/wiki/Pushdown_automaton))  
Business rules engine ([wiki](https://en.wikipedia.org/wiki/Business_rules_engine))
workflow engine
Business Process Management (BPM) Suite
State Chart XML (scxml)
Business Process Model and Notation (BPMN)

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


### Resources

### Workflow engines
When designing system it is very important what you need - state machine, rule engine or workflow engine. Depending on the implementation, the difference may disappears.
Anyway, here's a list of workflow engines: 
 - [Camunda](https://github.com/camunda/camunda-bpm-platform)  - BPMN-based workflow engine that can be embedded as java library (e.g. Spring Boot) or used standalone, including a graphical modeler and operations tooling.
 - [Copper](https://github.com/copper-engine/copper-engine)  - A high performance Java workflow engine.
 - [easy-rules](https://github.com/j-easy/easy-rules) - The simple, stupid rules engine for Java.
 - [Piper](https://github.com/okayrunner/piper)  - A distributed Java workflow engine designed to be dead simple.
 - [Workflow Engine](https://workflowengine.io/)  - A lightweight .NET and Java workflow engine.
 - -   [YAWL](https://yawlfoundation.github.io/index.html)(https://github.com/yawlfoundation/yawl)  - (Yet Another Workflow Language), Java-based, handles complex data transformations, and full integration with organizational resources and external Web Services.
 - 

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
eyJoaXN0b3J5IjpbLTEzNjg0NDY2MzMsLTQ2NTY2MzQ2NywtMT
I3MjMyNjY5MiwxMTgxMTQ0MzEwLDIwOTgxMjU5MywtMTkzODk0
NDkzMCwtMjc3NzE0MDQwLDE3NzQ3MDYzNTRdfQ==
-->