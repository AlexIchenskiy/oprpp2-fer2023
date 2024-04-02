package hr.fer.oprpp2.custom.scripting.parser;

/**
 * Exception for parser invalid cases.
 */
public class SmartScriptParserException extends RuntimeException {

    /**
     * Constructs an exception with error message.
     * @param message Error message
     */
    public SmartScriptParserException(String message) {
        super(message);
    }

}
