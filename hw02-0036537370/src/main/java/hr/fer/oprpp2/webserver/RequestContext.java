package hr.fer.oprpp2.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Class representing a HTTP request context.
 */
public class RequestContext {

    private final OutputStream outputStream;

    private Charset charset;

    public String encoding = "UTF-8";

    public int statusCode = 200;

    public String statusText = "OK";

    public String mimeType = "text/html";

    public Long contentLength = null;

    private final Map<String, String> parameters;

    private Map<String, String> temporaryParameters = new HashMap<>();

    private final Map<String, String> persistentParameters;

    private List<RCCookie> outputCookies;

    private boolean headerGenerated = false;

    private IDispatcher dispatcher;

    private String SID;

    public RequestContext(OutputStream outputStream, Map<String, String> parameters,
                          Map<String, String> persistentParameters, List<RCCookie> outputCookies, String SID) {
        this.outputStream = Objects.requireNonNull(outputStream, "Output stream cant be null.");
        this.parameters = (parameters == null) ? new HashMap<>() : parameters;
        this.persistentParameters = (persistentParameters == null) ? new HashMap<>() : persistentParameters;
        this.outputCookies = (outputCookies == null) ? new ArrayList<>() : outputCookies;
        this.SID = SID;
    }

    public RequestContext(OutputStream outputStream, Map<String, String> parameters,
                          Map<String, String> persistentParameters, List<RCCookie> outputCookies, String SID,
                          Map<String, String> temporaryParameters, IDispatcher dispatcher) {
        this(outputStream, parameters, persistentParameters, outputCookies, SID);
        this.temporaryParameters = temporaryParameters;
        this.dispatcher = dispatcher;
    }

    public void setEncoding(String encoding) {
        if (headerGenerated) throw new RuntimeException("Cant change encoding after header has been created.");
        this.encoding = encoding;
    }

    public void setStatusCode(int statusCode) {
        if (headerGenerated) throw new RuntimeException("Cant change status code after header has been created.");
        this.statusCode = statusCode;
    }

    public void setStatusText(String statusText) {
        if (headerGenerated) throw new RuntimeException("Cant change status text after header has been created.");
        this.statusText = statusText;
    }

    public void setMimeType(String mimeType) {
        if (headerGenerated) throw new RuntimeException("Cant change mime type after header has been created.");
        this.mimeType = mimeType;
    }

    public void setOutputCookies(List<RCCookie> outputCookies) {
        if (headerGenerated) throw new RuntimeException("Cant change output cookies after header has been created.");
        this.outputCookies = outputCookies;
    }

    public void setContentLength(Long contentLength) {
        if (headerGenerated) throw new RuntimeException("Cant change content length after header has been created.");
        this.contentLength = contentLength;
    }

    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    public String getParameter(String name) {
        return this.parameters.get(name);
    }

    public Set<String> getParameterNames() {
        return Collections.unmodifiableSet(this.parameters.keySet());
    }

    public String getPersistentParameter(String name) {
        return this.persistentParameters.get(name);
    }

    public Set<String> getPersistentParameterNames() {
        return Collections.unmodifiableSet(this.persistentParameters.keySet());
    }

    public void setPersistentParameter(String name, String value) {
        this.persistentParameters.put(name, value);
    }

    public void removePersistentParameter(String name) {
        this.persistentParameters.remove(name);
    }

    public String getTemporaryParameter(String name) {
        return this.temporaryParameters.get(name);
    }

    public Set<String> getTemporaryParameterNames() {
        return Collections.unmodifiableSet(this.temporaryParameters.keySet());
    }

    public void addRCCookie(RCCookie cookie) {
        this.outputCookies.add(cookie);
    }

    public String getSessionID() {
        return this.SID;
    }

    public void setTemporaryParameter(String name, String value) {
        this.temporaryParameters.put(name, value);
    }

    public void removeTemporaryParameter(String name) {
        this.temporaryParameters.remove(name);
    }

    /**
     * Write a byte data to the request.
     * @param data Data to be written
     * @return Request after writing
     * @throws IOException Error while writing to the request
     */
    public RequestContext write(byte[] data) throws IOException {
        this.generateHeader();

        this.outputStream.write(data);

        return this;
    }

    /**
     * Write byte data of predefined length and with a defined offset to the request.
     * @param data Data to be written
     * @param offset Writing offset
     * @param len Data length
     * @return Request after writing
     * @throws IOException Error while writing to the request
     */
    public RequestContext write(byte[] data, int offset, int len) throws IOException {
        this.generateHeader();

        this.outputStream.write(data, offset, len);

        return this;
    }

    /**
     * Write a single string to the request.
     * @param text Text to be added to the request
     * @return Request after writing
     * @throws IOException Error while writing to the request
     */
    public RequestContext write(String text) throws IOException {
        this.generateHeader();

        this.outputStream.write(text.getBytes(this.charset));

        return this;
    }

    private void generateHeader() throws IOException {
        if (this.headerGenerated) return;

        this.charset = Charset.forName(this.encoding);

        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 ").append(this.statusCode).append(" ").append(this.statusText).append("\r\n")
                .append("Content-Type: ").append(this.mimeType);

        if (this.mimeType.startsWith("text/")) sb.append("; charset=").append(this.encoding);

        sb.append("\r\n");

        if (this.contentLength != null) sb.append("Content-Length: ").append(this.contentLength).append("\r\n");

        outputCookies.forEach((cookie) -> {
            sb.append("Set-Cookie: ").append(cookie.getName()).append("=\"").append(cookie.getValue()).append("\"");
            if (cookie.getDomain() != null) sb.append("; Domain=").append(cookie.getDomain());
            if (cookie.getPath() != null) sb.append("; Path=").append(cookie.getPath());
            if (cookie.getMaxAge() != null) sb.append("; Max-Age=").append(cookie.getMaxAge());
            if (cookie.name.equals("sid")) sb.append("; HttpOnly");
            sb.append("\r\n");
        });

        sb.append("\r\n");

        this.outputStream.write(sb.toString().getBytes(StandardCharsets.ISO_8859_1));

        this.headerGenerated = true;
    }

    /**
     * Class representing a simple cookie for a request context.
     */
    public static class RCCookie {

        private final String name;

        private final String value;

        private final Integer maxAge;

        private final String domain;

        private final String path;

        public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
            this.name = name;
            this.value = value;
            this.maxAge = maxAge;
            this.domain = domain;
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getDomain() {
            return domain;
        }

        public String getPath() {
            return path;
        }

        public Integer getMaxAge() {
            return maxAge;
        }

    }

}
