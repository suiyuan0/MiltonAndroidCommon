package com.milton.common.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import java.util.BitSet;

/**
 * Wraps a ListAdapter to give it expandable list view functionality. The main
 * thing it does is add a listener to the getToggleButton which expands the
 * getExpandableView for each list item.
 */
public class ExpandableListAdapter extends WrapperListAdapterImpl {
    private int toggle_button_id;
    private int click_view_id;
    private int expandable_view_id;
    /**
     * Reference to the last expanded list item. Since lists are recycled this
     * might be null if though there is an expanded list item
     */
    private View lastOpen = null;
    private final int ROTATE_ANIM_DURATION = 180;
    private View lastBtn = null;
    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    /**
     * The position of the last expanded list item. If -1 there is no list item
     * expanded. Otherwise it points to the position of the last expanded list
     * item
     */
    private int lastOpenPosition = -1;
    private View lastOpenParent;
    /**
     * Default Animation duration Set animation duration with @see
     * setAnimationDuration
     */
    private int animationDuration = 330;

    /**
     * A list of positions of all list items that are expanded. Normally only
     * one is expanded. But a mode to expand multiple will be added soon.
     * <p/>
     * If an item onj position x is open, its bit is set
     */
    private BitSet openItems = new BitSet();
    /**
     * We remember, for each collapsable view its height. So we dont need to
     * recalculate. The height is calculated just before the view is drawn.
     */
    private final SparseIntArray viewHeights = new SparseIntArray(10);

    private OnItemExpandCollapseListener mItemExpandCollapseListener;

    private int mFirstPositionX;
    private int mTouchSlop = -1;

    public ExpandableListAdapter(ListAdapter adapter,
                                 int toggle_button_id, int click_view_id, int expandable_view_id) {
        super(adapter);
        this.toggle_button_id = toggle_button_id;
        this.click_view_id = click_view_id;
        this.expandable_view_id = expandable_view_id;
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = wrapped.getView(position, view, viewGroup);
        enableFor(view, position);
//        Log.e("alinmi", " isItemExpanded  position = " + isItemExpanded(position));
//        view.setBackgroundColor(isItemExpanded(position) ? Color.parseColor("#CACACA") : Color.parseColor("#FFFFFF"));

        if (null != mItemExpandCollapseListener) {
            mItemExpandCollapseListener.onItemInitStatus(view, position, isItemExpanded(position) ? ExpandCollapseAnimation.EXPAND : ExpandCollapseAnimation.COLLAPSE);
        }
        return view;
    }

    /**
     * Gets the duration of the collapse animation in ms. Default is 330ms.
     * Override this method to change the default.
     *
     * @return the duration of the anim in ms
     */
    public int getAnimationDuration() {
        return animationDuration;
    }

    /**
     * Set's the Animation duration for the Expandable animation
     *
     * @param duration The duration as an integer in MS (duration > 0)
     * @throws IllegalArgumentException if parameter is less than zero
     */
    public void setAnimationDuration(int duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Duration is less than zero");
        }

        animationDuration = duration;
    }

    /**
     * Check's if any position is currently Expanded To collapse the open item @see
     * collapseLastOpen
     *
     * @return boolean True if there is currently an item expanded, otherwise
     * false
     */
    public boolean isAnyItemExpanded() {
        return (lastOpenPosition != -1) ? true : false;
    }

    public boolean isItemExpanded(int position) {
        if (position < 0 || lastOpenPosition < 0)
            return false;
        return lastOpenPosition == position;
    }

    public void enableFor(View parent, int position) {
        if (mTouchSlop == -1) {
            mTouchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }

        View more = parent.findViewById(toggle_button_id);
        View click = parent.findViewById(click_view_id);

        View itemToolbar = parent.findViewById(expandable_view_id);
        itemToolbar.measure(parent.getWidth(), parent.getHeight());

        enableFor(more, click, itemToolbar, position, parent);
    }

    private void enableFor(final View button, final View click,
                           final View target, final int position, final View parent) {
        if (target == lastOpen && position != lastOpenPosition) {
            // lastOpen is recycled, so its reference is false
            lastOpen = null;
            lastBtn = null;
        }
        if (position == lastOpenPosition) {
            // re reference to the last view
            // so when can animate it when collapsed
            lastOpen = target;
            lastOpenParent = parent;
            lastBtn = button;
        }
        int height = viewHeights.get(position, -1);
        if (height == -1) {
            viewHeights.put(position, target.getMeasuredHeight());
            updateExpandable(target, position);
        } else {
            updateExpandable(target, position);
        }
        if (button != null && button.getVisibility() == View.VISIBLE) {
            click.setOnTouchListener(
                    new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(final View view, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN: {
                                    mFirstPositionX = (int) event.getRawX();
                                    break;
                                }
                                case MotionEvent.ACTION_UP: {
                                    if (Math.abs((int) event.getRawX() - mFirstPositionX) < mTouchSlop) {
                                        click2Expadable(view, button, target, position, parent);
                                    }
                                    break;
                                }
                            }
                            return true;
                        }
                    }

            );
        }

//        click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                click2Expadable(view, button, target, position, parent);
//            }
//        });
    }

    private void click2Expadable(final View view, final View button,
                                 final View target, final int position, final View parent) {
        Animation a = target.getAnimation();
        if (a != null && a.hasStarted() && !a.hasEnded()) {
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.performClick();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

        } else {
            if (null == mItemExpandCollapseListener || !mItemExpandCollapseListener.onItemNeedExpand(parent, position)) {
                return;
            }
            target.setAnimation(null);

            int type = target.getVisibility() == View.VISIBLE ? ExpandCollapseAnimation.COLLAPSE
                    : ExpandCollapseAnimation.EXPAND;

            // remember the state
            if (type == ExpandCollapseAnimation.EXPAND) {
                openItems.set(position, true);
            } else {
                openItems.set(position, false);
            }
            // check if we need to collapse a different view
            if (type == ExpandCollapseAnimation.EXPAND) {
                if (lastOpenPosition != -1
                        && lastOpenPosition != position) {
                    if (lastOpen != null) {
                        animateView(lastOpen, lastBtn,
                                ExpandCollapseAnimation.COLLAPSE);
                        if (null != mItemExpandCollapseListener) {
                            mItemExpandCollapseListener.onItemExpandCollapse(lastOpenParent, lastOpenPosition, ExpandCollapseAnimation.COLLAPSE);
                        }
                    }
                    openItems.set(lastOpenPosition, false);
                }
                lastOpen = target;
                lastOpenParent = parent;
                lastBtn = button;
                lastOpenPosition = position;
            } else if (lastOpenPosition == position) {
                lastOpenPosition = -1;
            }
            animateView(target, button, type);
            if (null != mItemExpandCollapseListener) {
                mItemExpandCollapseListener.onItemExpandCollapse(parent, position, type);
            }
        }
    }

    private void updateExpandable(View target, int position) {

        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) target
                .getLayoutParams();
        if (openItems.get(position)) {
            target.setVisibility(View.VISIBLE);
            params.bottomMargin = 0;
        } else {
            target.setVisibility(View.GONE);
            params.bottomMargin = 0 - viewHeights.get(position);
        }
    }

    /**
     * Performs either COLLAPSE or EXPAND animation on the target view
     *
     * @param target the view to animate
     * @param type   the animation type, either ExpandCollapseAnimation.COLLAPSE or
     *               ExpandCollapseAnimation.EXPAND
     */
    private void animateView(final View target, final View button,
                             final int type) {
        Animation anim = new ExpandCollapseAnimation(target, type);
        anim.setDuration(getAnimationDuration());
        target.startAnimation(anim);
        switch (type) {
            case ExpandCollapseAnimation.EXPAND:
                if (mRotateUpAnim != null)
                    button.startAnimation(mRotateUpAnim);
                break;
            case ExpandCollapseAnimation.COLLAPSE:
                if (mRotateDownAnim != null)
                    button.startAnimation(mRotateDownAnim);
                break;

            default:
                break;
        }
    }

    /**
     * Closes the current open item. If it is current visible it will be closed
     * with an animation.
     *
     * @return true if an item was closed, false otherwise
     */
    public boolean collapseLastOpen() {
        if (isAnyItemExpanded()) {
            // if visible animate it out
            if (lastOpen != null && lastBtn != null) {
                animateView(lastOpen, lastBtn, ExpandCollapseAnimation.COLLAPSE);
            }
            openItems.set(lastOpenPosition, false);
            lastOpenPosition = -1;
            return true;
        }
        return false;
    }

    public Parcelable onSaveInstanceState(Parcelable parcelable) {

        SavedState ss = new SavedState(parcelable);
        ss.lastOpenPosition = this.lastOpenPosition;
        ss.openItems = this.openItems;
        return ss;
    }

    public void onRestoreInstanceState(SavedState state) {

        this.lastOpenPosition = state.lastOpenPosition;
        this.openItems = state.openItems;
    }

    /**
     * Utility methods to read and write a bitset from and to a Parcel
     */
    private static BitSet readBitSet(Parcel src) {
        int cardinality = src.readInt();

        BitSet set = new BitSet();
        for (int i = 0; i < cardinality; i++) {
            set.set(src.readInt());
        }

        return set;
    }

    private static void writeBitSet(Parcel dest, BitSet set) {
        int nextSetBit = -1;

        dest.writeInt(set.cardinality());

        while ((nextSetBit = set.nextSetBit(nextSetBit + 1)) != -1) {
            dest.writeInt(nextSetBit);
        }
    }

    /**
     * The actual state class
     */
    static class SavedState extends View.BaseSavedState {
        public BitSet openItems = null;
        public int lastOpenPosition = -1;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            in.writeInt(lastOpenPosition);
            writeBitSet(in, openItems);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            lastOpenPosition = out.readInt();
            openItems = readBitSet(out);
        }

        // required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /**
     * Animation that either expands or collapses a view by sliding it down to
     * make it visible. Or by sliding it up so it will hide. It will look like
     * it slides behind the view above.
     */
    public static class ExpandCollapseAnimation extends Animation {
        private View mAnimatedView;
        private int mEndHeight;
        private int mType;
        public final static int COLLAPSE = 1;
        public final static int EXPAND = 0;
        private LinearLayout.LayoutParams mLayoutParams;

        /**
         * Initializes expand collapse animation, has two types, collapse (1)
         * and expand (0).
         *
         * @param view The view to animate
         * @param type The type of animation: 0 will expand from gone and 0 size
         *             to visible and layout size defined in xml. 1 will collapse
         *             view and set to gone
         */
        public ExpandCollapseAnimation(View view, int type) {

            mAnimatedView = view;
            mEndHeight = mAnimatedView.getMeasuredHeight();
            mLayoutParams = ((LinearLayout.LayoutParams) view.getLayoutParams());
            mType = type;
            if (mType == EXPAND) {

                mLayoutParams.bottomMargin = -mEndHeight;
            } else {

                mLayoutParams.bottomMargin = 0;
            }
            view.setVisibility(View.VISIBLE);
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {

            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                if (mType == EXPAND) {
                    mLayoutParams.bottomMargin = -mEndHeight
                            + (int) (mEndHeight * interpolatedTime);
                } else {
                    mLayoutParams.bottomMargin = -(int) (mEndHeight * interpolatedTime);
                }
                mAnimatedView.requestLayout();
            } else {
                if (mType == EXPAND) {
                    mLayoutParams.bottomMargin = 0;
                    mAnimatedView.requestLayout();
                } else {
                    mLayoutParams.bottomMargin = -mEndHeight;
                    mAnimatedView.setVisibility(View.GONE);
                    mAnimatedView.requestLayout();
                }
            }
        }
    }

    public void setOnItemExpandCollapseListener(OnItemExpandCollapseListener listener) {
        mItemExpandCollapseListener = listener;
    }

    public interface OnItemExpandCollapseListener {
        public void onItemExpandCollapse(View view, int position, int type);

        public void onItemInitStatus(View view, int position, int type);

        public boolean onItemNeedExpand(View view, int position);
    }
}
