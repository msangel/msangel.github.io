Some samples:
https://github.com/search?q=WorkflowManager+getWorkflow+%22embedded%22+language%3AJava&type=code
https://github.com/fzwartbol/dxticket/blob/012537a4564673887ba2de61d3f8c83751ba64af/repository-data/application/src/main/java/org/dxticket/documents/documentmodifier/DocumentModifier.java
https://github.com/fzwartbol/dxticket/blob/012537a4564673887ba2de61d3f8c83751ba64af/repository-data/application/src/main/java/org/dxticket/documents/documentmodifier/VenueDocumentModifier.java
https://github.com/fzwartbol/dxticket/blob/012537a4564673887ba2de61d3f8c83751ba64af/repository-data/application/src/main/java/org/dxticket/documents/documentmodifier/DocumentModifier.java

Discussion on that:
https://www.google.com/search?q=hippocms+add+sub+document+programmatically
https://groups.google.com/g/hippo-community/c/el4nrTDXTuw

Document workflow general:
https://xmdocumentation.bloomreach.com/library/concepts/workflow/document-workflow.html
https://xmdocumentation.bloomreach.com/library/enterprise/enterprise-features/advanced-search/use-a-custom-bulk-workflow.html
https://xmdocumentation.bloomreach.com/13/library/concepts/workflow/scxml-workflow-execution.html
https://xmdocumentation.bloomreach.com/library/concepts/content-repository/document-model.html

Another discussion:
https://groups.google.com/g/hippo-community/c/zu2oJhN_N24/m/qqeYnBCkAwAJ

Custom UI toolbar(that edit document properties):
https://developers.bloomreach.com/blog/2015/adding-a-workflow-to-hippo-cms-editor-toolbar.html#
Some on binaries:
https://xmdocumentation.bloomreach.com/library/concepts/images-and-assets/binaries-that-need-workflow.html
Some on CDN files:
https://jackrabbit.apache.org/jcr/node-types.html
https://modeshape.wordpress.com/2010/09/01/custom-properties-on-ntfile-and-ntfolder-nodes/
https://stackoverflow.com/questions/15896950/jcr-new-nodetype-with-custom-properties
https://stackoverflow.com/questions/39305827/javax-jcr-nodetype-constraintviolationexception-no-matching-property-definition

Some on binary store:
https://jackrabbit.apache.org/archive/wiki/JCR/DataStore_115513387.html
https://github.com/woonsanko/hippo-davstore-demo

Samples with jcr-pojo-binding library(also mappesr there) to<->from JSON:
https://bloomreach-forge.github.io/jcr-pojo-binding/examples-bindings.html

Some on versioning:
https://xmdocumentation.bloomreach.com/library/concepts/content-repository/configure-document-versioning.html

```java
 //noinspection unchecked
            InputStream inputStream = FileConverters.get("array2ccsv").convert(data);
            if (inputStream == null) {
                log.info("Associate reward, but no data as stream");
                return null;
            }

            session.refresh(true);
            session.save();

            WorkflowManager manager = ((HippoSession)session).getWorkspace().getWorkflowManager();

            FolderWorkflow folderWorkflow = (FolderWorkflow) manager.getWorkflow("embedded", node);

            String documentVariantPath = folderWorkflow.add("new-document", "hippo:resource", "staplesconnect:rewardsFile");
            Node resourceNode = session.getNode(documentVariantPath);

            DocumentWorkflow workflow = (DocumentWorkflow) manager.getWorkflow("default", node.getParent());
            Document document = workflow.obtainEditableInstance();
            Node editableNode = document.getNode(session);

            DefaultJcrContentNodeMapper mapper = new DefaultJcrContentNodeMapper();
            ContentNode editableContent = mapper.map(editableNode);
//
//            ContentNode resourceNode;
//            if (editableContent.hasNode("staplesconnect:rewardsFile")) {
//                resourceNode = editableContent.getNode("staplesconnect:rewardsFile");
//            } else {
//                resourceNode = new ContentNode("staplesconnect:rewardsFile", "hippo:resource");
//                editableContent.addNode(resourceNode);
//            }
            resourceNode.setProperty("jcr:data", IOUtils.toString(inputStream, StandardCharsets.UTF_8));
            resourceNode.setProperty("jcr:mimeType", "text/csv");
            resourceNode.setProperty("jcr:encoding", "UTF-8");
            resourceNode.setProperty("hippo:filename", "associate.csv");
            resourceNode.setProperty("jcr:lastModified", Calendar.getInstance());
//            resourceNode.setProperty("jcr:lastModified", ContentPropertyType.DATE, ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            DefaultJcrContentNodeBinder binder = new DefaultJcrContentNodeBinder();
            binder.bind(editableNode, editableContent);
            workflow.commitEditableInstance();
            workflow.publish();

            session.refresh(false);

//
//            Node fileNode;
//            if (editableNode.hasNode("staplesconnect:rewardsFile")) {
//                fileNode = editableNode.getNode("staplesconnect:rewardsFile");
//            } else {
//                fileNode = editableNode.addNode("staplesconnect:rewardsFile", "hippo:resource");
//            }
//            Binary binary = node.getSession().getValueFactory().createBinary(inputStream);
//            try {
//                fileNode.setProperty("jcr:data", binary);
//                fileNode.setProperty("jcr:mimeType", "text/csv");
//                fileNode.setProperty("jcr:encoding", "UTF-8");
//                fileNode.setProperty("hippo:filename", "associate.csv");
//                fileNode.setProperty("jcr:lastModified", Calendar.getInstance());
//            } finally {
//                binary.dispose();
//            }
//            workflow.commitEditableInstance();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            IOUtils.copy(inputStream, baos);
//
//            Node handle = node.getParent();
//            Node rewardsFile;
//            if (handle.hasNode("rewardsFile")) {
//                rewardsFile = handle.getNode("rewardsFile");
//            } else {
//                rewardsFile = handle.addNode("rewardsFile");
//            }
//
//            attachRewardsToNode(handle, new ByteArrayInputStream(baos.toByteArray()));
//            session.save();
        }
```

