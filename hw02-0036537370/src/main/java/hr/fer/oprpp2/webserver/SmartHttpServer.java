package hr.fer.oprpp2.webserver;

import hr.fer.oprpp2.custom.scripting.exec.SmartScriptEngine;
import hr.fer.oprpp2.custom.scripting.parser.SmartScriptParser;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SmartHttpServer {

    private final static String WORKER_PREFIX = "/ext/";

    private final static String WORKER_PATH = "hr.fer.oprpp2.webserver.workers.";

    private String address;

    private String domainName;

    private int port;

    private int workerThreads;

    private int sessionTimeout;

    private Map<String,String> mimeTypes = new HashMap<>();

    private ServerThread serverThread;

    private DaemonCleaner daemonCleaner;

    private ExecutorService threadPool;

    private Path documentRoot;

    private Map<String,IWebWorker> workersMap = new HashMap<>();

    private Map<String, SessionMapEntry> sessions = new ConcurrentHashMap<>();

    private Random sessionRandom = new Random();

    public SmartHttpServer(String configFileName) {
        Properties serverProps = new Properties();
        Properties mimeProps = new Properties();
        Properties workersProps = new Properties();

        try {
            serverProps.load(new FileInputStream(configFileName));
            mimeProps.load(new FileInputStream(serverProps.get("server.mimeConfig").toString()));
            workersProps.load(new FileInputStream(serverProps.get("server.workers").toString()));

            this.address = serverProps.get("server.address").toString();
            this.domainName = serverProps.get("server.domainName").toString();
            this.port = Integer.parseInt(serverProps.get("server.port").toString());
            this.workerThreads = Integer.parseInt(serverProps.get("server.workerThreads").toString());
            this.sessionTimeout = Integer.parseInt(serverProps.get("session.timeout").toString());
            this.documentRoot = Paths.get(serverProps.get("server.documentRoot").toString());

            this.serverThread = new ServerThread();
            this.daemonCleaner = new DaemonCleaner();

            mimeProps.forEach((key, value) -> this.mimeTypes.put(key.toString(), value.toString()));
            workersProps.forEach((key, value) -> {
                workersMap.put(key.toString(), loadWorker(value.toString()));
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load server properties.");
        }

        this.start();
    }

    protected synchronized void start() {
        this.threadPool = Executors.newFixedThreadPool(workerThreads);

        if (!this.serverThread.isAlive()) this.serverThread.start();

        if (!this.daemonCleaner.isAlive()) {
            this.daemonCleaner.setDaemon(true);
            this.daemonCleaner.start();
        }
    }

    protected synchronized void stop() {
        if (!this.serverThread.isInterrupted()) this.serverThread.interrupt();

        if (!this.daemonCleaner.isInterrupted()) this.daemonCleaner.interrupt();

        this.threadPool.shutdown();
    }

    protected class ServerThread extends Thread {
        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket()) {
                serverSocket.bind(new InetSocketAddress(address, port));
                while (!this.isInterrupted()) {
                    Socket client = serverSocket.accept();
                    ClientWorker cw = new ClientWorker(client);
                    threadPool.submit(cw);
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize server thread.");
            }
        }
    }

    protected class DaemonCleaner extends Thread {
        @Override
        public void run() {
            while (!this.isInterrupted()) {
                try {
                    TimeUnit.MINUTES.sleep(5);
                } catch (InterruptedException e) {
                    break;
                }

                sessions.forEach((key, value) -> {
                    if (value.getValidUntil() < new Date().getTime()) {
                        sessions.remove(key);
                    }
                });
            }
        }
    }

    private static class SessionMapEntry {

        private String sid;

        private String host;

        private long validUntil;

        private Map<String,String> map;

        public SessionMapEntry(String sid, String host, long validUntil, Map<String, String> map) {
            this.sid = sid;
            this.host = host;
            this.validUntil = validUntil;
            this.map = map;
        }

        public String getSid() {
            return sid;
        }

        public String getHost() {
            return host;
        }

        public long getValidUntil() {
            return validUntil;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setValidUntil(long validUntil) {
            this.validUntil = validUntil;
        }

    }

    private class ClientWorker implements IDispatcher, Runnable {

        private Socket csocket;

        private InputStream istream;

        private OutputStream ostream;

        private String version;

        private String method;

        private String host;

        private Map<String,String> params = new HashMap<>();

        private Map<String,String> tempParams = new HashMap<>();

        private Map<String,String> permPrams = new HashMap<>();

        private List<RequestContext.RCCookie> outputCookies = new ArrayList<>();

        private String SID;

        private RequestContext context = null;

        public ClientWorker(Socket csocket) {
            super();
            this.csocket = csocket;
        }

        @Override
        public void run() {
            try {
                this.istream = csocket.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException("Could not obtain an input stream.");
            }

            try {
                this.ostream = csocket.getOutputStream();
            } catch (IOException e) {
                throw new RuntimeException("Could not obtain an output stream.");
            }

            List<String> request;
            try {
                request = extractHeaders(new String(readRequest(this.istream).orElseThrow(), StandardCharsets.US_ASCII));
            } catch (IOException e) {
                throw new RuntimeException("Could not read header.");
            }

            if (request.isEmpty()) this.sendStatusMessage(400, "Invalid header.");

            String firstLine = request.get(0);
            String requestedPath;
            try {
                String[] parts = firstLine.split(" ");
                this.method = parts[0];
                requestedPath = parts[1];
                this.version = parts[2];
            } catch (Exception e) {
                throw new RuntimeException("Could not parse header method, path and version.");
            }

            if (!this.method.equals("GET") || !(this.version.equals("HTTP/1.0") || this.version.equals("HTTP/1.1"))) {
                this.sendStatusMessage(400, "Invalid header.");
                return;
            }

            Map<String, String> headers = new HashMap<>();
            request.subList(1, request.size()).forEach((header) -> {
                String[] parts = header.split(": ", 2);
                headers.put(parts[0], parts[1]);
            });

            String hostName = headers.get("Host");
            this.host = hostName == null ? domainName : hostName;

            String[] pathParts = requestedPath.split("[?]");
            String path = pathParts[0];

            this.checkSession(headers);

            if (pathParts.length > 1) {
                this.parseParameters(pathParts[1]);
            }

            try {
                this.internalDispatchRequest(path, true);
            } catch (Exception e) {
                throw new RuntimeException("Could not dispatch request.");
            }

            try {
                csocket.close();
            } catch (Exception e) {
                throw new RuntimeException("Could not close the connection.");
            }
        }

        @Override
        public void dispatchRequest(String urlPath) throws Exception {
            internalDispatchRequest(urlPath, false);
        }

        private void internalDispatchRequest(String urlPath, boolean directCall)
                throws Exception {
            if (urlPath.equals("/")) urlPath = "/index2.html";
            Path reqPath = Paths.get(documentRoot.toString(), urlPath);
            if (!reqPath.startsWith(documentRoot.toString())) {
                this.sendStatusMessage(403, "Forbidden");
                return;
            }

            if (directCall && (reqPath.startsWith("/private") || reqPath.startsWith("/private/"))) {
                this.sendStatusMessage(404, "Not found");
                return;
            }

            if (this.context == null) this.context = new RequestContext(this.ostream, this.params,
                    this.permPrams, this.outputCookies, this.SID, tempParams, this);

            this.context.setStatusCode(200);
            this.context.setStatusText("OK");

            String requestedName = urlPath.substring(urlPath.indexOf("/") + 1);

            if (urlPath.toLowerCase().startsWith(WORKER_PREFIX)) {
                loadWorker(requestedName.replace(WORKER_PREFIX.substring(1), "")).processRequest(this.context);
                this.ostream.flush();
                return;
            }

            AtomicBoolean found = new AtomicBoolean(false);

            workersMap.forEach((key, value) -> {
                if (key.toLowerCase().endsWith(requestedName.toLowerCase())) {
                    found.set(true);
                    try {
                        value.processRequest(this.context);
                        this.ostream.flush();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            if (found.get()) return;

            String extension;
            if (Files.exists(reqPath) && !Files.isDirectory(reqPath) && Files.isReadable(reqPath)) {
                extension = urlPath.substring(urlPath.lastIndexOf(".") + 1);
            } else {
                this.sendStatusMessage(404, "Not found");
                return;
            }

            if (extension.equals("smscr")) {
                String docBody = Files.readString(reqPath);
                new SmartScriptEngine(
                        new SmartScriptParser(docBody).getDocumentNode(), this.context
                ).execute();
            } else {
                String mimeType = mimeTypes.getOrDefault(extension, "application/octet-stream");
                this.context.setMimeType(mimeType);

                try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(reqPath))) {
                    byte[] buffer = new byte[8192];
                    int read;

                    while (true) {
                        read = bis.read(buffer);
                        if (read == -1) break;
                        this.context.write(buffer, 0, read);
                    }

                    this.ostream.flush();
                } catch (IOException e) {
                    throw new RuntimeException("Could not send message.");
                }
            }
        }

        private static Optional<byte[]> readRequest(InputStream is) throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int state = 0;
            l:		while(true) {
                int b = is.read();
                if(b==-1) {
                    if(bos.size()!=0) {
                        throw new IOException("Incomplete header received.");
                    }
                    return Optional.empty();
                }
                if(b!=13) {
                    bos.write(b);
                }
                switch(state) {
                    case 0:
                        if(b==13) { state=1; } else if(b==10) state=4;
                        break;
                    case 1:
                        if(b==10) { state=2; } else state=0;
                        break;
                    case 2:
                        if(b==13) { state=3; } else state=0;
                        break;
                    case 3:
                        if(b==10) { break l; } else state=0;
                        break;
                    case 4:
                        if(b==10) { break l; } else state=0;
                        break;
                }
            }
            return Optional.of(bos.toByteArray());
        }

        private static List<String> extractHeaders(String requestHeader) {
            List<String> headers = new ArrayList<String>();
            String currentLine = null;
            for(String s : requestHeader.split("\n")) {
                if(s.isEmpty()) break;
                char c = s.charAt(0);
                if(c==9 || c==32) {
                    currentLine += s;
                } else {
                    if(currentLine != null) {
                        headers.add(currentLine);
                    }
                    currentLine = s;
                }
            }
            if(!currentLine.isEmpty()) {
                headers.add(currentLine);
            }
            return headers;
        }

        private void sendMessage(int statusCode, String statusMessage, String message) {
            RequestContext context = new RequestContext(this.ostream, this.params, this.permPrams,
                    this.outputCookies, this.SID);

            context.setStatusCode(statusCode);
            context.setStatusText(statusMessage);
            context.setContentLength((long) message.length());

            try {
                context.write(message);
                this.ostream.flush();
            } catch (IOException e) {
                throw new RuntimeException("Could not send message.");
            }
        }

        private void sendStatusMessage(int statusCode, String statusMessage) {
            this.sendMessage(statusCode, statusMessage, "");
        }

        private void parseParameters(String paramString) {
            String[] parameters = paramString.split("&");

            for (String param : parameters) {
                String[] parts = param.split("=");
                params.put(parts[0], parts[1]);
            }
        }

        private synchronized void checkSession(Map<String, String> headers) {
            Map<String, List<String>> cookies = new HashMap<>();

            headers.forEach((key, value) -> {
                if (key.startsWith("Cookie")) {
                    String[] cookiesParts = value.replaceAll("\"", "").split(";");

                    for (String cookiesPart : cookiesParts) {
                        String[] cookieParts = cookiesPart.replace("\"", "").split("=");
                        cookies.put(cookieParts[0], Arrays.stream(cookieParts[1].split(",")).toList());
                    }
                }
            });

            this.SID = cookies.get("sid") == null ? this.generateSid() : cookies.get("sid").get(0);

            SessionMapEntry entry = sessions.get(this.SID);

            long time = new Date().getTime();

            if (entry == null || !entry.getHost().equals(this.host)
                    || entry.getValidUntil() < time) {
                sessions.put(this.SID, new SessionMapEntry(this.SID, this.host,
                        time + sessionTimeout * 1000L, new ConcurrentHashMap<>()));
            } else {
                entry.setValidUntil(time + sessionTimeout * 1000L);
            }

            this.outputCookies.add(new RequestContext.RCCookie("sid", this.SID,
                    null, this.host.split(":")[0], "/"));

            this.permPrams = sessions.get(this.SID).getMap();
        }

        private String generateSid() {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < 20; i++) sb.append((char) (65 + sessionRandom.nextInt(26)));

            return sb.toString();
        }

    }

    private static IWebWorker loadWorker(String name) {
        Class<?> referenceToClass;
        Object newObject;
        String value = name.startsWith(WORKER_PATH) ? name : WORKER_PATH + name;

        try {
            referenceToClass = Class.forName(value);
            newObject = referenceToClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return (IWebWorker) newObject;
    }

}
