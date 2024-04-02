package hr.fer.oprpp2.custom.scripting.exec;

import hr.fer.oprpp2.custom.collections.ObjectStack;
import hr.fer.oprpp2.custom.scripting.elems.*;
import hr.fer.oprpp2.custom.scripting.nodes.*;
import hr.fer.oprpp2.webserver.RequestContext;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;

public class SmartScriptNodeVisitor implements INodeVisitor {

    private final RequestContext context;

    private final ObjectMultistack multistack;

    public SmartScriptNodeVisitor(RequestContext context, ObjectMultistack multistack) {
        this.context = context;
        this.multistack = multistack;
    }

    @Override
    public void visitTextNode(TextNode node) {
        try {
            this.context.write(node.getText());
        } catch (Exception e) {
            throw new RuntimeException("Could not write to the context.");
        }
    }

    @Override
    public void visitForLoopNode(ForLoopNode node) {
        Double endValue = Double.parseDouble(node.getEndExpression().asText());

        String variable = node.getVariable().asText();

        multistack.push(variable, new ValueWrapper(node.getStartExpression().asText()));

        while (true) {
            for (int i = 0; i < node.numberOfChildren(); i++) node.getChild(i).accept(this);

            ValueWrapper current = multistack.pop(variable);
            current.add(node.getStepExpression().asText());
            multistack.push(variable, current);

            if (current.numCompare(endValue) > 0) break;
        }
    }

    @Override
    public void visitEchoNode(EchoNode node) {
        ObjectStack tempStack = new ObjectStack();

        for (Element element : node.getElements()) {
            if (element instanceof ElementConstantInteger || element instanceof ElementConstantDouble
                    || element instanceof ElementString) {
                // System.out.println("Const: " + element.asText());
                tempStack.push(element.asText());
                continue;
            }

            if (element instanceof ElementVariable) {
                // System.out.println("Var: " + element.asText());
                tempStack.push(multistack.peek(element.asText()).getValue());
                continue;
            }

            if (element instanceof ElementOperator) {
                // System.out.println("Operator: " + element.asText());
                ValueWrapper val1 = new ValueWrapper(tempStack.pop());
                ValueWrapper val2 = new ValueWrapper(tempStack.pop());

                try {
                    switch (element.asText()) {
                        case "+":
                            val1.add(val2.getValue());
                            break;
                        case "*":
                            val1.multiply(val2.getValue());
                            break;
                        case "-":
                            val1.subtract(val2.getValue());
                            break;
                        case "/":
                            val1.divide(val2.getValue());
                            break;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Could not perform operation.");
                }

                tempStack.push(val1.getValue());

                continue;
            }

            if (element instanceof ElementFunction) {
                // System.out.println("Func: " + element.asText());
                ElementFunction elementFunction = (ElementFunction) element;

                switch (elementFunction.asText().substring(1)) {
                    case "sin":
                        tempStack.push(Math.sin(Double.parseDouble(tempStack.pop().toString()) * Math.PI / 180));
                        break;
                    case "decfmt":
                        tempStack.push(new DecimalFormat(tempStack.pop().toString(),
                                new DecimalFormatSymbols(Locale.ENGLISH)).format(tempStack.pop()));
                        break;
                    case "dup":
                        tempStack.push(tempStack.peek());
                        break;
                    case "swap":
                        Object a = tempStack.pop();
                        Object b = tempStack.pop();
                        tempStack.push(a);
                        tempStack.push(b);
                        break;
                    case "setMimeType":
                        this.context.setMimeType(tempStack.pop().toString().replace("\"", ""));
                        break;
                    case "paramGet":
                        Object dvpg = tempStack.pop();
                        Object namepg = tempStack.pop();

                        String parameterpg = this.context.getParameter(
                                namepg.toString().replace("\"", ""));

                        tempStack.push(parameterpg == null ? dvpg : parameterpg);
                        break;
                    case "pparamGet":
                        Object dvppg = tempStack.pop();
                        Object nameppg = tempStack.pop();

                        String parameterppg = this.context.getPersistentParameter(nameppg.toString().replace("\"", ""));
                        tempStack.push(parameterppg == null ? dvppg : parameterppg);
                        break;
                    case "pparamSet":
                        this.context.setPersistentParameter(
                                tempStack.pop().toString().replace("\"", ""),
                                tempStack.pop().toString().replace("\"", ""));
                        break;
                    case "pparamDel":
                        this.context.removePersistentParameter(
                                tempStack.pop().toString().replace("\"", ""));
                        break;
                    case "tparamGet":
                        Object dvtpg = tempStack.pop();
                        Object nametpg = tempStack.pop();

                        String parametertpg = this.context.getTemporaryParameter(
                                nametpg.toString().replace("\"", ""));
                        tempStack.push(parametertpg == null ? dvtpg : parametertpg);
                        break;
                    case "tparamSet":
                        this.context.setTemporaryParameter(
                                tempStack.pop().toString().replace("\"", ""),
                                tempStack.pop().toString().replace("\"", ""));
                        break;
                    case "tparamDel":
                        this.context.removeTemporaryParameter(
                                tempStack.pop().toString().replace("\"", ""));
                        break;
                }
            }
        }

        if (!tempStack.isEmpty()) {
            ObjectStack reversedStack = new ObjectStack();
            int iterations = tempStack.size();

            for (int i = 0; i < iterations; i++) {
                reversedStack.push(tempStack.pop());
            }

            for (int i = 0; i < iterations; i++) {
                try {
                    this.context.write(reversedStack.pop().toString().replace("\"", ""));
                } catch (Exception e) {
                    throw new RuntimeException("Could not write to context.");
                }
            }
        }
    }

    @Override
    public void visitDocumentNode(DocumentNode node) {
        for (int i = 0; i < node.numberOfChildren(); i++) node.getChild(i).accept(this);
    }

}
