package jp.gihyo.projava.tasklist;

public class RunLengthCompression {
    public void runLengthComp() {
        final var COUNTER_BASE = -1;
        var data = "abbcccbaaaabccccccccccccddd";

        var count = COUNTER_BASE;
        char prev = 0;
        var builder = new StringBuilder();
        for (var ch : data.toCharArray()) {
            if (prev == ch) {
                // Same character continues
                count++;
                if (count == 9) {
                    // When count reaches 9, add count to the result string
                    builder.append((char) ('0' + count)); // Append the count as a character
                    count = COUNTER_BASE;
                    prev = 0;
                }
            } else {
                // Different character encountered
                if (count >= 0) {
                    builder.append((char) ('0' + count)); // Append the count as a character
                    count = COUNTER_BASE;
                }
                builder.append(ch);
                prev = ch;
            }
        }
        // Add the count for the last sequence if it's present
        if (count >= 0) {
            builder.append((char) ('0' + count));
        }

        var result = builder.toString();
        System.out.println(data);
        System.out.println(result);
    }
}