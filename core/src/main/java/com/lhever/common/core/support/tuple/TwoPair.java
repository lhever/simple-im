package com.lhever.common.core.support.tuple;

public class TwoPair<FIRST, SECOND> {
    public final FIRST first;
    public final SECOND second;

    public TwoPair(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }

    public int hashCode() {
        return 17 * (this.first != null ? this.first.hashCode() : 0) + 17 * (this.second != null ? this.second.hashCode() : 0);
    }

    public boolean equals(Object o) {
        if (!(o instanceof TwoPair)) {
            return false;
        } else {
            TwoPair<?, ?> that = (TwoPair) o;
            return equal(this.first, that.first) && equal(this.second, that.second);
        }
    }

    private static boolean equal(Object a, Object b) {
        return a == b || a != null && a.equals(b);
    }

    public String toString() {
        return String.format("{%s,%s}", this.first, this.second);
    }

}
