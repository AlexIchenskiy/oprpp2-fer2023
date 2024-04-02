package hr.fer.oprpp2.custom.scripting.parser;

import hr.fer.oprpp2.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp2.custom.collections.ObjectStack;
import hr.fer.oprpp2.custom.scripting.elems.*;
import hr.fer.oprpp2.custom.scripting.lexer.*;
import hr.fer.oprpp2.custom.scripting.nodes.*;

/**
 * Class representing a smart script parser.
 */
public class SmartScriptParser {

    /**
     * Lexer for this parser.
     */
    private final SmartScriptLexer lexer;

    /**
     * Stack for storing parser data.
     */
    private final ObjectStack stack;

    /**
     * Document node of this parser.
     */
    private final DocumentNode documentNode;

    /**
     * Current token being analyzed by parser.
     */
    private SmartScriptToken currentToken;

    /**
     * Constructs a parser from a document body.
     *
     * @param documentBody Document body to be parsed
     */
    public SmartScriptParser(String documentBody) {
        if (documentBody == null) {
            throw new NullPointerException("Document body cant be null!");
        }

        this.lexer = new SmartScriptLexer(documentBody);
        this.stack = new ObjectStack();

        try {
            this.documentNode = new DocumentNode();
            this.stack.push(this.documentNode);
            this.parse();
        } catch (Exception e) {
            throw new SmartScriptParserException(e.getMessage());
        }
    }

    /**
     * Getter for the document node.
     * @return Document node of this parser
     */
    public DocumentNode getDocumentNode() {
        return documentNode;
    }

    /**
     * Method for document body parsing.
     */
    private void parse() {
        this.currentToken = this.lexer.nextToken();

        while(this.currentToken.getType() != SmartScriptTokenType.EOF) {
            if (this.currentToken.getType() == SmartScriptTokenType.TAG_START) {
                this.lexer.setState(SmartScriptLexerState.TAG);
                this.currentToken = this.lexer.nextToken();
                continue;
            }

            if (this.currentToken.getType() == SmartScriptTokenType.IDENTIFIER) {
                this.parseTag();
                continue;
            }

            if (this.currentToken.getType() == SmartScriptTokenType.STRING_BASIC) {
                ((Node) this.stack.peek()).addChildNode(new TextNode(this.currentToken.getValue().toString()));
                this.currentToken = this.lexer.nextToken();
                continue;
            }

            if (this.currentToken.getType() == SmartScriptTokenType.STRING_TAG) {
                throw new SmartScriptLexerException("Invalid string tag!");
            }

            this.currentToken = this.lexer.nextToken();
        }

        if (this.stack.isEmpty()) {
            throw new SmartScriptParserException("Invalid non-empty tag closing!");
        }

        if (this.stack.size() > 1) {
            throw new SmartScriptLexerException("No closing tag for a non-empty tag!");
        }
    }

    /**
     * Function for parsing tags.
     */
    private void parseTag() {
        if (this.currentToken.getValue().toString().equalsIgnoreCase("FOR")) {
            this.parseForTag();
            this.lexer.setState(SmartScriptLexerState.BASIC);
            return;
        }

        if (this.currentToken.getValue().toString().equals("=")) {
            this.parseEchoTag();
            this.lexer.setState(SmartScriptLexerState.BASIC);
            return;
        }

        if (this.currentToken.getValue().toString().equalsIgnoreCase("END")) {
            if (this.lexer.nextToken().getType() != SmartScriptTokenType.TAG_END) {
                throw new SmartScriptParserException("Invalid non-empty tag ending!");
            }

            this.stack.pop();
            this.lexer.setState(SmartScriptLexerState.BASIC);
            this.currentToken = this.lexer.nextToken();
            return;
        }

        throw new SmartScriptParserException("Invalid empty tag closing!");
    }

    /**
     * Function for parsing for tags.
     */
    private void parseForTag() {
        ArrayIndexedCollection forData = new ArrayIndexedCollection();

        this.currentToken = this.lexer.nextToken();

        while (this.currentToken.getType() != SmartScriptTokenType.TAG_END) {
            if (this.currentToken.getType() == SmartScriptTokenType.EOF) {
                throw new SmartScriptParserException("No closing for the for loop provided!");
            }

            forData.add(parseElementFromToken(this.currentToken));
            this.currentToken = this.lexer.nextToken();
        }

        if (forData.size() > 4 || forData.size() < 3) {
            throw new SmartScriptParserException("Invalid argument number for the for loop!");
        }

        ForLoopNode forLoopNode;
        ElementVariable forLoopVar;
        Element forLoopStart;
        Element forLoopEnd;
        Element forLoopStep;

        try {
            forLoopVar = (ElementVariable) forData.get(0);
        } catch (Exception e) {
            throw new SmartScriptParserException("Invalid for loop variable provided!");
        }

        try {
            forLoopStart = (Element) forData.get(1);
            if (!isValidForLoopElement(forLoopStart)) {
                throw new SmartScriptParserException("");
            }
        } catch (Exception e) {
            throw new SmartScriptParserException("Invalid for loop start provided!");
        }

        try {
            forLoopEnd = (Element) forData.get(2);
            if (!isValidForLoopElement(forLoopEnd)) {
                throw new SmartScriptParserException("");
            }
        } catch (Exception e) {
            throw new SmartScriptParserException("Invalid for loop end provided!");
        }

        try {
            forLoopStep = (Element) forData.get(3);
            if (!isValidForLoopElement(forLoopEnd)) {
                throw new SmartScriptParserException("");
            }
        } catch (Exception e) {
            forLoopStep = null;
        }

        forLoopNode = new ForLoopNode(forLoopVar, forLoopStart, forLoopEnd, forLoopStep);

        ((Node) this.stack.peek()).addChildNode(forLoopNode);
        this.stack.push(forLoopNode);
    }

    /**
     * Functions that checks whether an element is a valid for loop element.
     * @param element Element to be checked
     * @return Boolean representing whether an element is a valid for loop element
     */
    private boolean isValidForLoopElement(Element element) {
        return (element instanceof ElementVariable)
                || (element instanceof ElementString)
                || (element instanceof ElementConstantDouble)
                || (element instanceof ElementConstantInteger);
    }

    /**
     * Function for parsing echo tags.
     */
    private void parseEchoTag() {
        ArrayIndexedCollection elements = new ArrayIndexedCollection();

        this.currentToken = this.lexer.nextToken();

        while (this.currentToken.getType() != SmartScriptTokenType.TAG_END) {
            if (this.currentToken.getType() == SmartScriptTokenType.EOF) {
                throw new SmartScriptParserException("No closing for the echo tag provided!");
            }

            elements.add(parseElementFromToken(this.currentToken));
            this.currentToken = this.lexer.nextToken();
        }

        Element[] elementsArr = new Element[elements.size()];

        for (int i = 0; i < elements.size(); i++) {
            elementsArr[i] = (Element) elements.get(i);
        }

        ((Node) this.stack.peek()).addChildNode(new EchoNode(elementsArr));
    }

    /**
     * Function that returns an element from a token.
     * @param token Token to be parsed
     * @return Parsed element
     */
    private Element parseElementFromToken(SmartScriptToken token) {
        return switch (token.getType()) {
            case EOF -> throw new SmartScriptParserException("Invalid token!");
            case DOUBLE -> new ElementConstantDouble((double) token.getValue());
            case INTEGER -> new ElementConstantInteger((int) token.getValue());
            case FUNCTION -> new ElementFunction(token.getValue().toString());
            case OPERATOR -> new ElementOperator(token.getValue().toString());
            case IDENTIFIER -> new ElementVariable(token.getValue().toString());
            case STRING_TAG -> new ElementString(token.getValue().toString());
            default -> throw new SmartScriptParserException("Invalid token type!");
        };
    }

}