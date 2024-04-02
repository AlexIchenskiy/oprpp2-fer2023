package hr.fer.oprpp2.custom.scripting.lexer;

/**
 * Enum representing a smart lexer token type.
 */
public enum SmartScriptTokenType {
    EOF,
    DOUBLE,
    INTEGER,
    FUNCTION,
    OPERATOR,
    IDENTIFIER,
    STRING_BASIC,
    STRING_TAG,
    TAG_START,
    TAG_END
}
