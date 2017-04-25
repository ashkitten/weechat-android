/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */

package com.ubergeek42.WeechatAndroid.utils;

import android.support.v7.widget.LinearSmoothScroller;

// if the position of the element we are scrolling to is *just* outside the RecyclerView, i.e.
// if it's not a child of it, an attempt to scroll to it may lead to overscrolling—the View appears
// in the middle of RecyclerView—and then everything is scrolled in the opposite direction, until
// the View touches the border of RecyclerView. The end result is fine, but animation looks pretty
// bad. the problem is in that the first scroll is performed *before* extra Views in the direction
// of the scroll are created. this class mitigates the problem, until it's fixed by google
class LinearSmoothScrollerFix extends LinearSmoothScroller {
    private final AnimatedRecyclerView recycler;

    LinearSmoothScrollerFix(AnimatedRecyclerView recycler) {
        super(recycler.getContext());
        this.recycler = recycler;
    }

    // called by android.support.v7.widget.RecyclerView.SmoothScroller.start
    @SuppressWarnings("ConstantConditions")
    @Override protected void onStart() {
        super.onStart();
        if (findViewByPosition(getTargetPosition()) == null) {
            float direction = computeScrollVectorForPosition(getTargetPosition()).y;
            recycler.scrollBy(0, direction > 0 ? 1 : -1);
        }
    }
}