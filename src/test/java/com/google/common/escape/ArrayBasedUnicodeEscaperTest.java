package com.google.common.escape;

import com.github.hcsp.test.helper.ProjectSourceFileReader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class ArrayBasedUnicodeEscaperTest {
    @Test
    public void test() {
        String javaCode = ProjectSourceFileReader.readAsString(ArrayBasedUnicodeEscaper.class);
        Assertions.assertTrue(
                containsMatch(javaCode, multiline("The first code point in the safe range.")));
        Assertions.assertTrue(
                containsMatch(javaCode, multiline("The last code point in the safe range.")));
        Assertions.assertTrue(
                containsMatch(javaCode, multiline("This is a bit of a hack.*in practice.")));
        Assertions.assertTrue(containsMatch(javaCode, singleline("This is overridden to improve")));
        Assertions.assertTrue(containsMatch(javaCode, singleline("the speed when processing")));
        Assertions.assertTrue(containsMatch(javaCode, singleline("Overridden for performance")));
    }

    private boolean containsMatch(String str, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        return pattern.matcher(str).find();
    }

    private String singleline(String content) {
        return "//\\s*" + content;
    }

    private String multiline(String content) {
        return "(?s)/\\*\\s*" + content + "\\s*\\*/";
    }
}
