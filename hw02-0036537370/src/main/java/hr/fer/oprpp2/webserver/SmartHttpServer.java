package hr.fer.oprpp2.webserver;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmartHttpServer {

    private String address;

    private String domainName;

    private int port;

    private int workerThreads;

    private int sessionTimeout;

    private Map<String,String> mimeTypes = new HashMap<>();

    private ServerThread serverThread;

    private ExecutorService threadPool;

    private Path documentRoot;

    public SmartHttpServer(String configFileName) {
        Properties serverProps = new Properties();
        Properties mimeProps = new Properties();

        try {
            serverProps.load(new FileInputStream(configFileName));
            mimeProps.load(new FileInputStream(serverProps.get("server.mimeConfig").toString()));

            this.address = serverProps.get("server.address").toString();
            this.domainName = serverProps.get("server.domainName").toString();
            this.port = Integer.parseInt(serverProps.get("server.port").toString());
            this.workerThreads = Integer.parseInt(serverProps.get("server.workerThreads").toString());
            this.sessionTimeout = Integer.parseInt(serverProps.get("session.timeout").toString());
            this.documentRoot = Paths.get(serverProps.get("server.documentRoot").toString());

            this.serverThread = new ServerThread();

            mimeProps.forEach((key, value) -> this.mimeTypes.put(key.toString(), value.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load server properties.");
        }

        this.start();
    }

    protected synchronized void start() {
        this.threadPool = Executors.newFixedThreadPool(workerThreads);

        if (!this.serverThread.isAlive()) this.serverThread.start();
    }

    protected synchronized void stop() {
        if (!this.serverThread.isInterrupted()) this.serverThread.interrupt();

        this.threadPool.shutdown();
    }

    protected class ServerThread extends Thread {
        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket()) {
                serverSocket.bind(new InetSocketAddress(address, port));
                while (true) {
                    Socket client = serverSocket.accept();
                    ClientWorker cw = new ClientWorker(client);
                    threadPool.submit(cw);
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize server thread.");
            }
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
            Path reqPath = Paths.get(documentRoot.toString(), urlPath);
            if (!reqPath.startsWith(documentRoot.toString())) {
                this.sendStatusMessage(403, "Forbidden");
                return;
            }

            String extension;
            if (Files.exists(reqPath) && !Files.isDirectory(reqPath) && Files.isReadable(reqPath)) {
                extension = urlPath.substring(urlPath.lastIndexOf(".") + 1);
            } else {
                this.sendStatusMessage(404, "Not found");
                return;
            }

            String mimeType = mimeTypes.getOrDefault(extension, "application/octet-stream");

            RequestContext rc = new RequestContext(ostream, params, permPrams, outputCookies);
            rc.setMimeType(mimeType);
            rc.setStatusCode(200);

            try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(reqPath))) {
                byte[] buffer = new byte[8192];
                int read;

                while (true) {
                    read = bis.read(buffer);
                    if (read == -1) break;
                    rc.write(buffer, 0, read);
                }

                this.ostream.flush();
            } catch (IOException e) {
                throw new RuntimeException("Could not send message.");
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
            RequestContext context = new RequestContext(this.ostream, this.params, this.permPrams, this.outputCookies);

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
            System.out.println("Param: " + paramString);
            String[] params = paramString.split("&");

            for (String param : params) {
                String[] parts = param.split("=");
                tempParams.put(parts[0], parts[1]);
            }
        }

    }

}
