package com.saburi.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Pluralizer {

    private static final Map<String, String> irregularPlurals = new HashMap<>();
    private static final Map<String, String> reverseIrregularPlurals = new HashMap<>();
    private static final Set<String> unchangingPlurals = new HashSet<>();

    static {
        // Irregular plurals
        irregularPlurals.put("child", "children");
        irregularPlurals.put("person", "people");
        irregularPlurals.put("man", "men");
        irregularPlurals.put("woman", "women");
        irregularPlurals.put("tooth", "teeth");
        irregularPlurals.put("foot", "feet");
        irregularPlurals.put("mouse", "mice");
        irregularPlurals.put("goose", "geese");

        // Reverse irregular plurals
        for (Map.Entry<String, String> entry : irregularPlurals.entrySet()) {
            reverseIrregularPlurals.put(entry.getValue(), entry.getKey());
        }

        // Unchanging plurals
        unchangingPlurals.add("sheep");
        unchangingPlurals.add("fish");
        unchangingPlurals.add("deer");
        unchangingPlurals.add("species");
        unchangingPlurals.add("aircraft");
        unchangingPlurals.add("data");
        unchangingPlurals.add("info");
        unchangingPlurals.add("information");
        unchangingPlurals.add("lookupdata");
        unchangingPlurals.add("revinfo");
    }

    public static String pluralize(String singular) {
        if (singular == null || singular.isEmpty()) {
            return singular;
        }

        String lowerSingular = singular.toLowerCase();

        // Check for unchanging plurals
        if (unchangingPlurals.contains(lowerSingular)) {
            return singular;
        }

        // Check for irregular plurals
        if (irregularPlurals.containsKey(lowerSingular)) {
            return matchCase(irregularPlurals.get(lowerSingular), singular);
        }

        // Common cases for pluralization
        if (lowerSingular.endsWith("y") && !isVowel(lowerSingular.charAt(lowerSingular.length() - 2))) {
            return matchCase(singular.substring(0, singular.length() - 1) + "ies", singular);
        } else if (lowerSingular.endsWith("s") || lowerSingular.endsWith("sh") || lowerSingular.endsWith("ch") || lowerSingular.endsWith("x") || lowerSingular.endsWith("z")) {
            return matchCase(singular + "es", singular);
        } else if (lowerSingular.endsWith("f")) {
            return matchCase(singular.substring(0, singular.length() - 1) + "ves", singular);
        } else if (lowerSingular.endsWith("fe")) {
            return matchCase(singular.substring(0, singular.length() - 2) + "ves", singular);
        } else {
            return matchCase(singular + "s", singular);
        }
    }

    public static String singularize(String plural) {
        if (plural == null || plural.isEmpty()) {
            return plural;
        }

        String lowerPlural = plural.toLowerCase();

        // Check for unchanging plurals
        if (unchangingPlurals.contains(lowerPlural)) {
            return plural;
        }

        // Check for reverse irregular plurals
        if (reverseIrregularPlurals.containsKey(lowerPlural)) {
            return matchCase(reverseIrregularPlurals.get(lowerPlural), plural);
        }

        // Common cases for singularization
        if (lowerPlural.endsWith("ies") && lowerPlural.length() > 3 && !isVowel(lowerPlural.charAt(lowerPlural.length() - 4))) {
            return matchCase(plural.substring(0, plural.length() - 3) + "y", plural);
        } else if (lowerPlural.endsWith("es")) {
            if (lowerPlural.endsWith("ses") || lowerPlural.endsWith("shes") || lowerPlural.endsWith("ches") || lowerPlural.endsWith("xes") || lowerPlural.endsWith("zes")) {
                return matchCase(plural.substring(0, plural.length() - 2), plural);
            }
            return matchCase(plural.substring(0, plural.length() - 1), plural);
        } else if (lowerPlural.endsWith("ves")) {
            if (lowerPlural.endsWith("fves")) {
                return matchCase(plural.substring(0, plural.length() - 3) + "fe", plural);
            }
            return matchCase(plural.substring(0, plural.length() - 3) + "f", plural);
        } else if (lowerPlural.endsWith("s")) {
            return matchCase(plural.substring(0, plural.length() - 1), plural);
        } else {
            return plural;
        }
    }

    private static boolean isVowel(char c) {
        return "AEIOUaeiou".indexOf(c) != -1;
    }

    private static String matchCase(String result, String original) {
        if (Character.isUpperCase(original.charAt(0))) {
            return Character.toUpperCase(result.charAt(0)) + result.substring(1);
        }
        return result;
    }

    public static void main(String[] args) {
        // Test cases for pluralize
        System.out.println(pluralize("Cat"));         // Cats
        System.out.println(pluralize("Dog"));         // Dogs
        System.out.println(pluralize("Box"));         // Boxes
        System.out.println(pluralize("Church"));      // Churches
        System.out.println(pluralize("Baby"));        // Babies
        System.out.println(pluralize("Leaf"));        // Leaves
        System.out.println(pluralize("Life"));        // Lives
        System.out.println(pluralize("Bus"));         // Buses
        System.out.println(pluralize("Child"));       // Children
        System.out.println(pluralize("Person"));      // People
        System.out.println(pluralize("Sheep"));       // Sheep
        System.out.println(pluralize("Fish"));        // Fish
        System.out.println(pluralize("Data"));        // Data
        System.out.println(pluralize("COUNTRY")); 

        System.out.println("****************************************************************");
        // Test cases for singularize
        System.out.println(singularize("Cats"));         // Cat
        System.out.println(singularize("Dogs"));         // Dog
        System.out.println(singularize("Boxes"));        // Box
        System.out.println(singularize("Churches"));     // Church
        System.out.println(singularize("Babies"));       // Baby
        System.out.println(singularize("Leaves"));       // Leaf
        System.out.println(singularize("Lives"));        // Life
        System.out.println(singularize("Buses"));        // Bus
        System.out.println(singularize("Children"));     // Child
        System.out.println(singularize("People"));       // Person
        System.out.println(singularize("Sheep"));        // Sheep
        System.out.println(singularize("Fish"));         // Fish
        System.out.println(singularize("Data"));         // Data
    }
}
