package hr.fer.oprpp2.custom.scripting.lexer;

/**
 * Class representing a token for smart script lexical analyzer.
 */
public class SmartScriptToken {

    /**
     * Type of the token.
     */
    private final SmartScriptTokenType type;

    /**
     * Value of the token.
     */
    private final Object value;

    /**
     * Constructs an instance of token by its type and value.
     * @param type Type of the token
     * @param value Value of the token
     */
    public SmartScriptToken(SmartScriptTokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Returns the token value.
     * @return Token value
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Returns the token type.
     * @return Token type
     */
    public SmartScriptTokenType getType() {
        return this.type;
    }

}
