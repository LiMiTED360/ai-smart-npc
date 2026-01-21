package aiSmartNpc.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractHelper {
    private static final Pattern CONTENT_PATTERN = Pattern.compile("\"content\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");

    public static String extractTextFromJSON(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return "[error] Received empty reply";
        }

        Matcher matcher = CONTENT_PATTERN.matcher(jsonResponse);

        String foundContent = null;
        while (matcher.find()) {
            foundContent = matcher.group(1);
        }

        if (foundContent != null) {
            return cleanUpAnswer(foundContent) + "[Test]";
        }

        return "[error] Could not understand response, DEBUG - Could not read JSON: " + jsonResponse;
    }

    private static String cleanUpAnswer(String jsonResponse) {
        return jsonResponse
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\u00e4", "ä")
                .replace("\\u00c4", "Ä")
                .replace("\\u00f6", "ö")
                .replace("\\u00d6", "Ö")
                .replace("\\u00fc", "ü")
                .replace("\\u00dc", "Ü")
                .replace("\\u00df", "ß");
    }


}
