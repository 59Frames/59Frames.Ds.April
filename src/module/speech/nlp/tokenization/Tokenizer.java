package module.speech.nlp.tokenization;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * {@link Tokenizer}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Tokenizer {

    @NotNull
    public static Token[] tokenize(String sentence) {
        final var tokens = new ArrayList<Token>();
        final var tokenizer = new StringTokenizer(sentence);

        while (tokenizer.hasMoreTokens())
            tokens.add(new Token(tokenizer.nextToken()));

        Token[] returnArray = new Token[tokens.size()];
        return tokens.toArray(returnArray);
    }
}
