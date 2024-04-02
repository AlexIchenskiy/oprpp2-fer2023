package hr.fer.oprpp2.custom.scripting.lexer;

/**
 * Class representing a smart script lexer.
 */
public class SmartScriptLexer {

    /**
     * Input text data.
     */
    private final char[] data;

    /**
     * Current token.
     */
    private SmartScriptToken token;

    /**
     * Index of the first unprocessed symbol.
     */
    private int currentIndex;

    /**
     * Current lexer state.
     */
    private SmartScriptLexerState state = SmartScriptLexerState.BASIC;

    /**
     * Constructs a lexer from a document body text.
     * @param documentBody Document body to be analyzed
     */
    public SmartScriptLexer(String documentBody) {
        this.data = documentBody.toCharArray();
    }

    /**
     * Generates and return the next token from the input text.
     * @return The next token
     */
    public SmartScriptToken nextToken() {
        if (this.token != null && this.token.getType() == SmartScriptTokenType.EOF) {
            throw new SmartScriptLexerException("No more tokens left!");
        }

        if (this.currentIndex >= this.data.length) {
            this.token = new SmartScriptToken(SmartScriptTokenType.EOF, null);
            return this.token;
        }

        if (this.state == SmartScriptLexerState.BASIC) {
            return this.tokenizeBasic();
        }

        if (this.state == SmartScriptLexerState.TAG) {
            return this.tokenizeTag();
        }

        throw new SmartScriptLexerException("Invalid input!");
    }

    /**
     * Returns the token that was lastly generated.
     * @return Last generated token
     */
    public SmartScriptToken getToken() {
        return this.token;
    }

    /**
     * Setter for the smart script lexer state.
     * @param state State for lexer to be set
     */
    public void setState(SmartScriptLexerState state) {
        if (state == null) {
            throw new NullPointerException("State cant be null!");
        }

        this.state = state;
    }

    /**
     * Function to check whether the provided char is a whitespace.
     * @param value Value to be checked
     * @return Boolean representing whether the provided char is a whitespace
     */
    private boolean isWhitespace(char value) {
        return value == '\r' || value == '\n' || value == '\t' || value == ' ';
    }

    /**
     * Function to check if escape for basic string (i.e. outside of tags) is correctly used.
     */
    private void checkEscapeStringBasic() {
        if (this.data[this.currentIndex] == '\\' && this.currentIndex + 1 >= this.data.length ||
                this.data[this.currentIndex] == '\\' &&
                        !(this.data[this.currentIndex + 1] == '\\' || this.data[this.currentIndex + 1] == '{')) {
            throw new SmartScriptLexerException("Invalid escape!");
        }
    }

    /**
     * Function to clear all incoming whitespaces.
     */
    private void clearWhitespace() {
        while (true) {
            if (this.currentIndex < this.data.length && this.isWhitespace(this.data[this.currentIndex])) {
                this.currentIndex++;
                continue;
            }

            break;
        }
    }

    /**
     * Function to create a token for the BASIC lexer mode.
     * @return Analyzed token
     */
    private SmartScriptToken tokenizeBasic() {
        if (isTagStart()) {
            return this.tokenizeTagStart();
        }

        if (this.data[this.currentIndex] == '{') {
            throw new SmartScriptLexerException("Invalid input!");
        }

        return this.tokenizeText();
    }

    /**
     * Function to create a token for the TAG lexer mode.
     * @return Analyzed token
     */
    private SmartScriptToken tokenizeTag() {
        clearWhitespace();

        if (this.isTagEnd()) {
            return this.tokenizeTagEnd();
        }

        if (this.data[this.currentIndex] == '=') {
            this.token = new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "=");
            this.currentIndex++;
            return this.token;
        }

        if (Character.isLetter(this.data[this.currentIndex])) {
            return this.tokenizeIdentifier();
        }

        if (Character.isDigit(this.data[this.currentIndex]) ||
                (this.data[this.currentIndex] == '-' && this.currentIndex + 1 < this.data.length
                        && Character.isDigit(this.data[this.currentIndex + 1]))) {
            return this.tokenizeNumber();
        }

        if (this.data[this.currentIndex] == '@' && this.currentIndex + 1 < this.data.length
                && Character.isLetter(this.data[this.currentIndex + 1])) {
            return this.tokenizeFunction();
        }

        if (isValidOperator()) {
            return this.tokenizeOperator();
        }

        if (this.data[this.currentIndex] == '"') {
            return this.tokenizeString();
        }

        throw new SmartScriptLexerException("Invalid token value!");
    }

    /**
     * Function to tokenize a tag start.
     * @return Token representing a tag start
     */
    private SmartScriptToken tokenizeTagStart() {
        this.currentIndex += 2;
        this.token = new SmartScriptToken(SmartScriptTokenType.TAG_START, "{$");
        return this.token;
    }

    /**
     * Function to tokenize a tag end.
     * @return Token representing a tag end
     */
    private SmartScriptToken tokenizeTagEnd() {
        this.currentIndex += 2;
        this.token = new SmartScriptToken(SmartScriptTokenType.TAG_END, "$}");
        return this.token;
    }

    /**
     * Function to tokenize a part of text.
     * @return String token representing a part of text
     */
    private SmartScriptToken tokenizeText() {
        StringBuilder sb = new StringBuilder();

        while (this.currentIndex < this.data.length && !isTagStart()) {
            if (this.data[this.currentIndex] == '\\') {
                this.checkEscapeStringBasic();
                this.currentIndex++;
                sb.append(this.data[this.currentIndex]);
                this.currentIndex++;
                continue;
            }

            sb.append(this.data[this.currentIndex]);
            this.currentIndex++;
        }

        this.token = new SmartScriptToken(SmartScriptTokenType.STRING_BASIC, sb.toString());
        return this.token;
    }

    /**
     * Function to check whether the tag start is on input.
     * @return Boolean representing whether the tag start is on input
     */
    private boolean isTagStart() {
        return this.data[this.currentIndex] == '{'
                && this.currentIndex + 1 < this.data.length
                && this.data[this.currentIndex + 1] == '$';
    }

    /**
     * Function to check whether the tag end is on input.
     * @return Boolean representing whether the tag end is on input
     */
    private boolean isTagEnd() {
        return this.data[this.currentIndex] == '$'
                && this.currentIndex + 1 < this.data.length
                && this.data[this.currentIndex + 1] == '}';
    }

    /**
     * Function that checks whether the next symbol is valid for the identifier name.
     * @return Boolean representing whether the next symbol is valid for the identifier name
     */
    private boolean isValidSymbolForIdentifier() {
        return Character.isLetter(this.data[this.currentIndex])
                || Character.isDigit(this.data[this.currentIndex])
                || this.data[this.currentIndex] == '_';
    }

    /**
     * Function that checks whether the next symbol is a valid operator.
     * @return Boolean representing whether the next symbol is a valid operator
     */
    private boolean isValidOperator() {
        return this.data[this.currentIndex] == '+' || this.data[this.currentIndex] == '-'
                || this.data[this.currentIndex] == '*' || this.data[this.currentIndex] == '/'
                || this.data[this.currentIndex] == '^';
    }

    /**
     * Function to tokenize an identifier.
     * @return Token representing an identifier
     */
    private SmartScriptToken tokenizeIdentifier() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.data[this.currentIndex]);
        this.currentIndex++;

        while (this.currentIndex < this.data.length) {
            if (this.isValidSymbolForIdentifier()) {
                sb.append(this.data[this.currentIndex]);
                this.currentIndex++;
                continue;
            }

            break;
        }

        this.token = new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, sb.toString());
        return this.token;
    }

    /**
     * Function to tokenize a number.
     * @return Token representing a number
     */
    private SmartScriptToken tokenizeNumber() {
        StringBuilder sb = new StringBuilder();

        if (this.data[this.currentIndex] == '-') {
            sb.append('-');
            this.currentIndex++;
        }

        boolean isDouble = false;

        while (this.currentIndex < this.data.length
                && (Character.isDigit(this.data[this.currentIndex]) || this.data[this.currentIndex] == '.')) {
            if (this.data[this.currentIndex] == '.' && isDouble) {
                break;
            }

            if (this.data[this.currentIndex] == '.') {
                isDouble = true;
            }

            sb.append(this.data[this.currentIndex]);
            this.currentIndex++;
        }

        try {
            if (isDouble) {
                this.token = new SmartScriptToken(SmartScriptTokenType.DOUBLE, Double.parseDouble(sb.toString()));
            } else {
                this.token = new SmartScriptToken(SmartScriptTokenType.INTEGER, Integer.parseInt(sb.toString()));
            }
        } catch (Exception e) {
            throw new SmartScriptLexerException("Invalid number format!");
        }
        return this.token;
    }

    /**
     * Function to tokenize a function.
     * @return Token representing a function
     */
    private SmartScriptToken tokenizeFunction() {
        StringBuilder sb = new StringBuilder();

        this.currentIndex++;
        sb.append(this.data[this.currentIndex]);
        this.currentIndex++;

        while (this.currentIndex < this.data.length) {

            if (this.isValidSymbolForIdentifier()) {
                sb.append(this.data[this.currentIndex]);
                this.currentIndex++;
                continue;
            }

            break;
        }

        this.token = new SmartScriptToken(SmartScriptTokenType.FUNCTION, sb.toString());
        return this.token;
    }

    /**
     * Function to tokenize an operator.
     * @return Token representing an operator
     */
    private SmartScriptToken tokenizeOperator() {
        this.token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, this.data[this.currentIndex]);
        this.currentIndex++;
        return this.token;
    }

    /**
     * Function to tokenize a string.
     * @return Token representing a string
     */
    private SmartScriptToken tokenizeString() {
        this.currentIndex++;

        StringBuilder sb = new StringBuilder();

        while (this.currentIndex < this.data.length) {
            if (this.data[this.currentIndex] == '"') {
                this.currentIndex++;
                break;
            }

            if (this.data[this.currentIndex] == '\\' && this.currentIndex + 1 < this.data.length) {
                if (this.data[this.currentIndex + 1] == 'n') {
                    this.currentIndex += 2;
                    sb.append('\n');
                    continue;
                }

                if (this.data[this.currentIndex + 1] == 'r') {
                    this.currentIndex += 2;
                    sb.append('\r');
                    continue;
                }

                if (this.data[this.currentIndex + 1] == 't') {
                    this.currentIndex += 2;
                    sb.append('\t');
                    continue;
                }

                if (this.data[this.currentIndex + 1] == '"') {
                    this.currentIndex += 2;
                    sb.append('"');
                    continue;
                }

                if (this.data[this.currentIndex + 1] == '\\') {
                    this.currentIndex += 2;
                    sb.append('\\');
                    continue;
                }

                throw new SmartScriptLexerException("Invalid escape inside of a tag string!");
            }

            sb.append(this.data[this.currentIndex]);
            this.currentIndex++;
        }

        this.token = new SmartScriptToken(SmartScriptTokenType.STRING_TAG, sb.toString());
        return this.token;
    }

}
