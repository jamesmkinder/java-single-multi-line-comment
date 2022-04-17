package com.google.common.escape;

public abstract class ArrayBasedUnicodeEscaper {
    /* The first code point in the safe range. */
    private int safeMin;
    /* The last code point in the safe range. */
    private int safeMax;

    private char safeMinChar;
    private char safeMaxChar;

    protected ArrayBasedUnicodeEscaper() {
        /* 
        This is a bit of a hack but lets us do quicker per-character checks in
        the fast path code. The safe min/max values are very unlikely to extend
        into the range of surrogate characters, but if they do we must not test
        any values in that range. To see why, consider the case where:
        safeMin <= {hi,lo} <= safeMax
        where {hi,lo} are characters forming a surrogate pair such that:
        codePointOf(hi, lo) > safeMax
        which would result in the surrogate pair being (wrongly) considered safe.
        If we clip the safe range used during the per-character tests so it is
        below the values of characters in surrogate pairs, this cannot occur.
        This approach does mean that we break out of the fast path code in cases
        where we don't strictly need to, but this situation will almost never
        occur in practice.
        */
        if (safeMin >= Character.MIN_HIGH_SURROGATE) {
            this.safeMinChar = Character.MAX_VALUE;
            this.safeMaxChar = 0;
        } else {
            this.safeMinChar = (char) safeMin;
            this.safeMaxChar = (char) Math.min(safeMax, Character.MIN_HIGH_SURROGATE - 1);
        }
    }

    
    // This is overridden to improve performance. Rough benchmarking shows that this almost doubles
    // the speed when processing strings that do not require any escaping.
    
    public final String escape(String s) {
        return s;
    }

    // Overridden for performance.
    protected final int nextEscapeIndex(CharSequence csq, int index, int end) {
        return index;
    }
}
