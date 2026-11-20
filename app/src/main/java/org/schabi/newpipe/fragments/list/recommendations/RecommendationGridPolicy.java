package org.schabi.newpipe.fragments.list.recommendations;

public final class RecommendationGridPolicy {
    private static final int MIN_COLUMN_WIDTH_DP = 300;

    private RecommendationGridPolicy() {
    }

    public static int getColumnCount(final int screenWidthDp) {
        return Math.max(1, screenWidthDp / MIN_COLUMN_WIDTH_DP);
    }
}
