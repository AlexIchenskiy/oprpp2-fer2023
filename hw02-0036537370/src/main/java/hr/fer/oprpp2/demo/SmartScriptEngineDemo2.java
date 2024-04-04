package hr.fer.oprpp2.demo;

import hr.fer.oprpp2.custom.scripting.exec.SmartScriptEngine;
import hr.fer.oprpp2.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp2.webserver.RequestContext;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartScriptEngineDemo2 {

    public static void main(String[] args) {
        String documentBody;
        try {
            documentBody = Files.readString(Paths.get("zbrajanje.smscr"));
        } catch (Exception e) {
            System.out.println("Could not read file.");
            return;
        }

        Map<String,String> parameters = new HashMap<>();
        Map<String,String> persistentParameters = new HashMap<>();
        List<RequestContext.RCCookie> cookies = new ArrayList<>();
        parameters.put("a", "4");
        parameters.put("b", "2");
        // create engine and execute it
        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(),
                new RequestContext(System.out, parameters, persistentParameters, cookies, "")
        ).execute();
    }

}
