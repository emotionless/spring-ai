package com.eazybytes.openai.rag;

import java.util.*;
import java.util.regex.*;

public class BasicCodeChunker {

    private static final Pattern CASE_PATTERN =
            Pattern.compile("^\\s*CASE\\b(.*)", Pattern.CASE_INSENSITIVE);

    private static final Pattern BEGIN_CASE_PATTERN =
            Pattern.compile("^\\s*BEGIN\\s+CASE\\b", Pattern.CASE_INSENSITIVE);

    private static final Pattern END_CASE_PATTERN =
            Pattern.compile("^\\s*END\\s+CASE\\b", Pattern.CASE_INSENSITIVE);

    /**
     * Matches executable statements.
     * IMPORTANT:
     * - CASE lines are explicitly excluded from executable detection
     * - '=' is allowed but only outside CASE labels
     */
    private static final Pattern EXECUTABLE_PATTERN =
            Pattern.compile(
                    "\\b(IF|CALL|READ|WRITE|LOCATE|FOR|DEL|INS|RETURN|GOTO|=)\\b",
                    Pattern.CASE_INSENSITIVE
            );

    public static List<CodeChunk> chunkSource(String sourceCode) {
        List<CodeChunk> chunks = new ArrayList<>();

        String currentCase = null;
        StringBuilder buffer = new StringBuilder();
        boolean hasExecutable = false;

        String[] lines = sourceCode.split("\\R"); // handles all line endings

        for (String line : lines) {

            // Skip structural BEGIN CASE
            if (BEGIN_CASE_PATTERN.matcher(line).find()) {
                continue;
            }

            // CASE start
            Matcher caseMatcher = CASE_PATTERN.matcher(line);
            if (caseMatcher.find()) {
                flushCase(chunks, currentCase, buffer, hasExecutable);

                currentCase = caseMatcher.group(1).trim();
                buffer.setLength(0);
                hasExecutable = false;

                buffer.append(line).append("\n");
                continue;
            }

            // END CASE
            if (END_CASE_PATTERN.matcher(line).find()) {
                flushCase(chunks, currentCase, buffer, hasExecutable);

                currentCase = null;
                buffer.setLength(0);
                hasExecutable = false;
                continue;
            }

            // Accumulate content
            buffer.append(line).append("\n");

            // Detect executable logic (exclude CASE lines)
            if (!CASE_PATTERN.matcher(line).find()
                    && EXECUTABLE_PATTERN.matcher(line).find()) {
                hasExecutable = true;
            }
        }

        // Flush final CASE
        flushCase(chunks, currentCase, buffer, hasExecutable);

        return chunks;
    }

    private static void flushCase(
            List<CodeChunk> chunks,
            String caseCondition,
            StringBuilder buffer,
            boolean hasExecutable
    ) {
        if (caseCondition == null) return;

        String content = buffer.toString().trim();
        if (content.isEmpty()) return;

        chunks.add(new CodeChunk(
                caseCondition,
                content,
                hasExecutable
        ));
    }

    /**
     * DTO representing a semantic CASE chunk
     */
    public static class CodeChunk {
        public final String caseCondition;
        public final String content;
        public final boolean executable;

        public CodeChunk(String caseCondition, String content, boolean executable) {
            this.caseCondition = caseCondition;
            this.content = content;
            this.executable = executable;
        }

        @Override
        public String toString() {
            return "CASE " + caseCondition +
                    " [executable=" + executable + "]";
        }
    }
}
