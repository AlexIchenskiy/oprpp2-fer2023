package hr.fer.oprpp2.custom.scripting.exec;

import hr.fer.oprpp2.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp2.custom.scripting.nodes.INodeVisitor;
import hr.fer.oprpp2.webserver.RequestContext;

public class SmartScriptEngine {

    private final DocumentNode documentNode;

    private final RequestContext requestContext;

    private final ObjectMultistack multistack = new ObjectMultistack();

    private final INodeVisitor visitor;

    public SmartScriptEngine(DocumentNode documentNode, RequestContext
            requestContext) {
        this.documentNode = documentNode;
        this.requestContext = requestContext;

        this.visitor = new SmartScriptNodeVisitor(this.requestContext, this.multistack);
    }

    public void execute() {
        this.documentNode.accept(this.visitor);
    }

}
