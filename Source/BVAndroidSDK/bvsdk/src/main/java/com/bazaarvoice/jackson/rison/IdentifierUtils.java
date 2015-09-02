package com.bazaarvoice.jackson.rison;

/**
 * Utilities for dealing with Rison unquoted identifiers.
 */
class IdentifierUtils {

    // Ascii idstart and idchar that match the rison spec precisely.  non-ascii (>127) are also allowed.
    private static final int[] _idStartStrict = toIdSet(true, "_./~");
    private static final int[] _idCharStrict =  toIdSet(true, "_./~-0123456789");

    // Lenient idstart and idchar that accept strings encoded by http://mjtemplate.org/dist/mjt-0.9.2/rison.js
    // which allows a number of punctuation and whitespace characters that are excluded in the Rison spec.
    private static final int[] _idStartLenient = invert(toIdSet(false, " '!:(),*@$-0123456789"));
    private static final int[] _idCharLenient =  invert(toIdSet(false, " '!:(),*@$"));

    /**
     * Returns true if the specified character is a legal first character in an unquoted identifier.
     * Uses lenient rules appropriate for parsing.
     */
    public static boolean isIdStartLenient(int ch) {
        return ch > 127 || in(ch, _idStartLenient);
    }

    /**
     * Returns true if the specified character is a legal following character in an unquoted identifier.
     * Uses lenient rules appropriate for parsing.
     */
    public static boolean isIdCharLenient(int ch) {
        return ch > 127 || in(ch, _idCharLenient);
    }

    /**
     * Returns true if the specified character is a legal first character in an unquoted identifier.
     * Uses the strict definition described in the Rison specification, appropriate for encoding.
     */
    public static boolean isIdStartStrict(int ch) {
        return ch > 127 || in(ch, _idStartStrict);
    }

    /**
     * Returns true if the specified character is a legal following character in an unquoted identifier.
     * Uses the strict definition described in the Rison specification, appropriate for encoding.
     */
    public static boolean isIdCharStrict(int ch) {
        return ch > 127 || in(ch, _idCharStrict);
    }

    /**
     * Returns true if a string does not need to be quoted when serialized.
     * Uses the strict definition described in the Rison specification, appropriate for encoding.
     */
    public static boolean isIdStrict(String string) {
        int len = string.length();
        if (len == 0) {
            return false;
        }
        if (!isIdStartStrict(string.charAt(0))) {
            return false;
        }
        for (int i = 1; i < len; i++) {
            if (!isIdCharStrict(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the string represented by the specified character array does not
     * Uses the strict definition described in the Rison specification, appropriate for encoding.
     */
    public static boolean isIdStrict(char[] chars, int offset, int len) {
        if (len == 0) {
            return false;
        }
        int end = offset + len;
        if (!isIdStartStrict(chars[offset++])) {
            return false;
        }
        while (offset < end) {
            if (!isIdCharStrict(chars[offset++])) {
                return false;
            }
        }
        return true;
    }

    private static boolean in(int ch, int[] bits) {
        return (bits[ch >>> 5] & (1 << (ch & 31))) != 0;
    }

    /**
     * Encodes a set of ASCII characters (< 127) as a bitset of 4 32-bit values.
     */
    private static int[] toIdSet(boolean includeAlphaCharacters, String nonAlphaCharacters) {
        int[] bits = new int[4];  // 4*32=128 which covers all ASCII
        if (includeAlphaCharacters) {
            for (int ch = 'A'; ch <= 'Z'; ch++) {
                bits[ch >>> 5] |= (1 << (ch & 31));
            }
            for (int ch = 'a'; ch <= 'z'; ch++) {
                bits[ch >>> 5] |= (1 << (ch & 31));
            }
        }
        for (int i = 0; i < nonAlphaCharacters.length(); i++) {
            int ch = nonAlphaCharacters.charAt(i);
            if (ch >= 128) {
                throw new AssertionError(); // out of range
            }
            bits[ch >>> 5] |= (1 << (ch & 31));
        }
        return bits;
    }

    private static int[] invert(int[] bits) {
        for (int i = 0; i < bits.length; i++) {
            bits[i] = ~bits[i];
        }
        return bits;
    }
}
