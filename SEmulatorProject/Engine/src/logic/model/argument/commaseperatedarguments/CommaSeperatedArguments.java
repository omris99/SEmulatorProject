package logic.model.argument.commaseperatedarguments;

import logic.model.argument.Argument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommaSeperatedArguments implements Argument {
    private final String arguments;
    List<Argument> argumentList;

    public CommaSeperatedArguments(String arguments) {
        this.arguments = arguments;
        this.argumentList = convertCommaSeperatedArgumentsStringToArgumentsList(arguments);
    }
    @Override
    public String getRepresentation() {
        return arguments;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    private List<Argument> convertCommaSeperatedArgumentsStringToArgumentsList(String arguments) {
        return null;
    }

//    private List<String> extractFunctionCall(String commaSeperatedArguments) {
//        if (commaSeperatedArguments.startsWith("(")) {
//            int endOfFunctionCallIndex = commaSeperatedArguments.indexOf(")");
//            commaSeperatedArguments.substring(0, endOfFunctionCallIndex);
//
//            commaSeperatedArguments = commaSeperatedArguments.substring(1, commaSeperatedArguments.length() - 1);
//        }
//
//        List<String> parts = new ArrayList<>();
//        StringBuilder current = new StringBuilder();
//        int depth = 0;
//
//        for (char c : commaSeperatedArguments.toCharArray()) {
//            if (c == '(') {
//                depth++;
//                current.append(c);
//            } else if (c == ')') {
//                depth--;
//                current.append(c);
//            } else if (c == ',' && depth == 0) {
//                parts.add(current.toString().trim());
//                current.setLength(0);
//            } else {
//                current.append(c);
//            }
//        }
//
//        if (current.length() > 0) {
//            parts.add(current.toString().trim());
//        }
//
//        argumentList = parts.stream().map(Argument::parse).toList();
//    }

//    public class SplitByTopLevelComma {
//        public static List<String> splitTopLevel(String s) {
//            List<String> parts = new ArrayList<>();
//            StringBuilder current = new StringBuilder();
//            int depth = 0;
//
//            for (char c : s.toCharArray()) {
//                if (c == '(') {
//                    depth++;
//                    current.append(c);
//                } else if (c == ')') {
//                    depth--;
//                    current.append(c);
//                } else if (c == ',' && depth == 0) {
//                    // פסיק חיצוני -> סיום חלק
//                    parts.add(current.toString().trim());
//                    current.setLength(0);
//                } else {
//                    current.append(c);
//                }
//            }
//
//            if (current.length() > 0) {
//                parts.add(current.toString().trim());
//            }
//
//            return parts;
//        }
}
