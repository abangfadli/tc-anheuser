package topcoder.topcoder.anheuser.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import rx.functions.Action0;

/**
 * Created by ahmadfadli on 12/28/15.
 * Code from https://github.com/nhaarman/ListViewAnimations
 * File location: com.nhaarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter.java
 */
public class ViewExpandCollapseUtil {

    public static void animateCollapsing(final View view) {
        animateCollapsing(view, null);
    }

    public static void animateCollapsing(final View view, @Nullable Action0 onFinish) {
        int origHeight = view.getHeight();

        ValueAnimator animator = createHeightAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(final Animator animator) {
                view.setVisibility(View.GONE);
                if(onFinish != null) {
                    onFinish.call();
                }
            }
        });
        animator.start();
    }

    public static void animateExpanding(final View view) {
        animateExpanding(view, () -> {});
    }

    public static void animateExpanding(final View view, @Nullable Action0 onFinish) {
        animateExpanding(view, 0, onFinish);
    }

    public static void animateExpanding(final View view, int additionalHeight, Action0 onFinish) {
        view.setVisibility(View.VISIBLE);

        View parent = (View) view.getParent();
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.AT_MOST);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);

        ValueAnimator animator = createHeightAnimator(view, 0, view.getMeasuredHeight() + additionalHeight);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(onFinish != null) {
                    onFinish.call();
                }
            }
        });
        animator.start();
    }

    public static void animateExpanding(final View view, final AbsListView listView) {
        view.setVisibility(View.VISIBLE);

        View parent = (View) view.getParent();
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.AT_MOST);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);

        ValueAnimator animator = createHeightAnimator(view, 0, view.getMeasuredHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            final int listViewHeight = listView.getHeight();
            final int listViewBottomPadding = listView.getPaddingBottom();
            final View v = findDirectChild(view, listView);

            @Override
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                final int bottom = v.getBottom();
                if (bottom > listViewHeight) {
                    final int top = v.getTop();
                    if (top > 0) {
                        listView.smoothScrollBy(Math.min(bottom - listViewHeight + listViewBottomPadding, top), 0);
                    }
                }
            }
        });
        animator.start();
    }

    private static View findDirectChild(final View view, final AbsListView listView) {
        View result = view;
        View parent = (View) result.getParent();
        while (parent != listView) {
            result = parent;
            parent = (View) result.getParent();
        }
        return result;
    }

    public static ValueAnimator createHeightAnimator(final View view, final int start, final int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(valueAnimator -> {
            int value = (Integer) valueAnimator.getAnimatedValue();

            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = value;
            view.setLayoutParams(layoutParams);
        });
        return animator;
    }
}
