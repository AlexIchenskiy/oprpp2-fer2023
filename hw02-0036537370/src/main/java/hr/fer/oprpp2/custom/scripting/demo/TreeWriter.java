package hr.fer.oprpp2.custom.scripting.demo;

import hr.fer.oprpp2.custom.scripting.nodes.WriterVisitor;
import hr.fer.oprpp2.custom.scripting.parser.SmartScriptParser;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TreeWriter {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide a file path as a single parameter.");
            return;
        }

        String docBody;

        try {
            docBody = Files.readString(Paths.get(args[0]));
        } catch (Exception e) {
            System.out.println("Could not parse a file from path provided.");
            return;
        }

        SmartScriptParser parser = new SmartScriptParser(docBody);
        WriterVisitor visitor = new WriterVisitor();

        parser.getDocumentNode().accept(visitor);
    }

}
