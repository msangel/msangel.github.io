### Definitions
**State machine** -  is a mathematical model of computation ([wiki](https://en.wikipedia.org/wiki/Finite-state_machine))
**Pushdown automaton** - a kind of state machine with stacked memory, when automate operates with the memory of it's stack, but also can exit stack level, or enter into new. A computational model of computers, programming languages and VM is based on this. Not a subject of this post, just for general knowledge. ([wiki](https://en.wikipedia.org/wiki/Pushdown_automaton))  
Business rules engine ([wiki](https://en.wikipedia.org/wiki/Business_rules_engine))
workflow engine
Business Process Management (BPM) Suite
State Chart XML (scxml)
Business Process Model and Notation (BPMN)

### Specs

Spec defined there: [https://www.w3.org/TR/scxml/](https://www.w3.org/TR/scxml/). BUT there no standart "dtd" or "xsd" schema file.

Options are:
 - unfinished draft: [https://www.w3.org/2011/04/SCXML/](https://www.w3.org/2011/04/SCXML/)
 - good enough: [https://github.com/carlos-verdes/scxml-java/blob/master/scxml-complete.xsd](https://github.com/carlos-verdes/scxml-java/blob/master/scxml-complete.xsd)
 - some unclear: [https://github.com/diamondq/dq-common-java/tree/master/utils/common-utils.scxml/src/main/resources](https://github.com/diamondq/dq-common-java/tree/master/utils/common-utils.scxml/src/main/resources)


### Engines option
scxml based:
- [https://commons.apache.org/proper/commons-scxml/](https://github.com/diamondq/dq-common-java/tree/master/utils/common-utils.scxml/src/main/resources) - hero of this post
- [https://github.com/carlos-verdes/scxml-java](https://github.com/diamondq/dq-common-java/tree/master/utils/common-utils.scxml/src/main/resources) - another java implementation
- [https://github.com/moraispgsi/fsm-engine](https://github.com/diamondq/dq-common-java/tree/master/utils/common-utils.scxml/src/main/resources) - node.js


### This engine
Homepage: [https://commons.apache.org/proper/commons-scxml/](https://github.com/diamondq/dq-common-java/tree/master/utils/common-utils.scxml/src/main/resources)<br/>
Sources: [https://github.com/apache/commons-scxml](https://github.com/diamondq/dq-common-java/tree/master/utils/common-utils.scxml/src/main/resources)<br/>
XSD schema seems not exists, but you can reffer [SCXMLReader](https://github.com/apache/commons-scxml/blob/master/src/main/java/org/apache/commons/scxml2/io/SCXMLReader.java) class from sources.

### Samples
[https://github.com/apache/commons-scxml/tree/master/src/test/java/org/apache/commons/scxml2](https://github.com/diamondq/dq-common-java/tree/master/utils/common-utils.scxml/src/main/resources)
[https://github.com/woonsan/commons-scxml-examples](https://github.com/diamondq/dq-common-java/tree/master/utils/common-utils.scxml/src/main/resources)
[https://alexzhornyak.github.io/SCXML-tutorial/](https://github.com/diamondq/dq-common-java/tree/master/utils/common-utils.scxml/src/main/resources)

### Sample app

### Complex sample app
Here we will recreate basic document workflow from hippo cms. It's implementation build on top of  commons-scxml2, but also contains custom actions from own library [hippo-repository-workflow](https://maven.onehippo.com/maven2/org/onehippo/cms7/hippo-repository-workflow/15.1.0/). In our sample we will mimic actions from there, but in simple straighforward way and so without that dependency.

<span style="text-align: left;">Documentation on above can be found there:</span>
[workflow](https://xmdocumentation.bloomreach.com/library/concepts/workflow/workflow.html)
Also, application-wide FSM event listener(service bus): [event-bus](https://xmdocumentation.bloomreach.com/library/concepts/workflow/workflow.html) for reacting on workflow events.

The configuration is read by `RepositorySCXMLRegistry` class. And all custom actions are loaded from jcr repository directly there:
```java
for (final Node actionNode : new NodeIterable(scxmlDefNode.getNodes())) {
    if (!actionNode.isNodeType(SCXML_ACTION)) {
        continue;
    }

    String namespace = actionNode.getProperty(SCXML_ACTION_NAMESPACE).getString();
    className = actionNode.getProperty(SCXML_ACTION_CLASSNAME).getString();
    @SuppressWarnings("unchecked")
    Class<? extends Action> actionClass = (Class<Action>) Thread.currentThread().getContextClassLoader().loadClass(className);
    customActions.add(new CustomAction(namespace, actionNode.getName(), actionClass));
}
```

Also, the reader contains multiple definitions.
Root configuration is: `/hippo:configuration/hippo:modules/scxmlregistry/hippo:moduleconfig`
Definitions config location against root is: `hipposcxml:definitions`.
So, definitions are:
`/hippo:configuration/hippo:modules/scxmlregistry/hippo:moduleconfig/hipposcxml:definitions/documentworkflow`

The executor is created in `RepositorySCXMLExecutorFactory` class.
During its initialization it read in `readGlobalScript` and then executes root `<script>` element in groovy language (for what?).

In the end, `DocumentWorkflowImpl` is instantiated where the `SCXMLWorkflowExecutor<SCXMLWorkflowContext, DocumentHandle> workflowExecutor` is main member.
That implementation is obtained from `WorkflowManagerImpl`.
Since the evaluator (`org.apache.commons.scxml2.Evaluator`) kind is CustomGroovyEvaluator (`org.onehippo.repository.scxml.RepositorySCXMLRegistry.CustomGroovyEvaluator`) all the conditions of the `<transition>` tag is executed on that evaluator. Other known evaluators are: 
- org.apache.commons.scxml2.env.groovy.GroovyEvaluator
- org.apache.commons.scxml2.env.javascript.JSEvaluator
- org.apache.commons.scxml2.env.jexl.JexlEvaluator
- org.apache.commons.scxml2.env.xpath.XPathEvaluator
So if we will use transition with `condition` attribute, the evaluator must have.
The best way to debug transition evaluation is real executor method: `SCXMLSemanticsImpl#matchTransition`.
Other tags to know:
- <hippo:feedback key="status" value="editable"/>
- <hippo:action action="checkModified" enabledExpr="!!draft and !!unpublished and branchExists"/>
- <hippo:setHolder holder="null"/> or <hippo:setHolder holder="user"/>
- <hippo:setTransferable transferable="false"/>
- <hippo:setRetainable retainable="false"/>
- <hippo:result value="preview ? unpublished : published"/> or <hippo:result value="draft"/>
- <hippo:version variant="unpublished"/>
- <hippo:checkoutBranch variant="unpublished"/>
- <hippo:copyVariant sourceState="unpublished" targetState="draft"/>
- <hippo:configVariant variant="published" availabilities="live"/>
- <hippo:requestAction identifierExpr="request.identity" action="rejectRequest" enabledExpr="true"/>
- <hippo:rejectRequest requestExpr="_event.data?.request" reasonExpr="_event.data?.reason"/>
- <hippo:deleteRequest requestExpr="request"/>
- <hippo:workflowRequest type="depublish" contextVariantExpr="published"/>
- <hippo:scheduleWorkflow type="depublish" targetDateExpr="_event.data?.targetDate"/>
- <hippo:restoreVersion historic="_event.data?.date"  variant="unpublished" />
-  <hippo:restoreVersionByVersion version="_event.data?.version" target="unpublished" />
- <hippo:listBranches/>
- <hippo:getBranch state="_event.data?.state" unpublished="unpublished" published="published" draft="draft"/>
- <hippo:checkoutBranch variant="unpublished" branchId="'master'"/>
- <hippo:branch variant="unpublished" branchName="_event.data?.branchName"/>/
- <cs:var name="workflowType" expr="request.workflowType"/> 


So. The diagramm.
<div style="width: 640px; height: 480px; margin: 10px; position: relative;"><iframe allowfullscreen frameborder="0" style="width:640px; height:480px" src="https://lucid.app/documents/embedded/a599b825-f33e-42f8-8ad6-a393f9d6227e" id="BtGS6rOs8mYj"></iframe></div>



### Resources

### Other workflow engines
todo: here https://github.com/meirwah/awesome-workflow-engines

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
eyJoaXN0b3J5IjpbMTM5MTU0MTkxLDEyMzU4NDk2ODYsNzM4OD
EwNzI2LC0xOTgxNDM2Nzk4LC0xMjU1MjI5NTIxLDkyNjc3OTk1
MiwtMTM5ODQ0NjMwMywtMTM4MDU0NzEwLDg2NzQwMjQ0MywtMT
U0NTU4MjE3NywxMDQ1ODM3NTczLC01OTgzMzA2NDMsMTA1MDEx
ODE0MywxNDI4MDU2NzUwLC00NjU2NjM0NjcsLTEyNzIzMjY2OT
IsMTE4MTE0NDMxMCwyMDk4MTI1OTMsLTE5Mzg5NDQ5MzAsLTI3
NzcxNDA0MF19
-->