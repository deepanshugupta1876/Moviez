package deepanshu.example.com.moviez.core.ui

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.animation.Interpolator
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import deepanshu.example.com.moviez.R


class CircleIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var mIndicatorMargin = -1
    private var mIndicatorWidth = -1
    private var mIndicatorHeight = -1
    private var mIndicatorBackgroundResId = 0
    private var mIndicatorUnselectedBackgroundResId = 0
    private var mAnimatorOut: Animator? = null
    private var mAnimatorIn: Animator? = null
    private var mImmediateAnimatorOut: Animator? = null
    private var mImmediateAnimatorIn: Animator? = null
    private var mLastPosition = -1

    private var mViewpager: ViewPager2? = null

    @Nullable
    private var mIndicatorCreatedListener: IndicatorCreatedListener? = null

    init {
        initialize()
    }

    private fun initialize() {
        val size = (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            DEFAULT_INDICATOR_WIDTH.toFloat(), resources.displayMetrics
        ) + 0.5f).toInt()
        mIndicatorWidth = size
        mIndicatorHeight = size
        mIndicatorMargin = size
        mAnimatorOut = createAnimatorOut()
        mImmediateAnimatorOut = createAnimatorOut()
        mImmediateAnimatorOut!!.duration = 0
        mAnimatorIn = createAnimatorIn()
        mImmediateAnimatorIn = createAnimatorIn()
        mImmediateAnimatorIn!!.duration = 0
        mIndicatorBackgroundResId = R.drawable.ic_dark_grey_dot
        mIndicatorUnselectedBackgroundResId = R.drawable.ic_grey_dot
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
    }

    interface IndicatorCreatedListener {
        fun onIndicatorCreated(view: View?, position: Int)
    }

    fun setIndicatorCreatedListener(
        @Nullable indicatorCreatedListener: IndicatorCreatedListener?
    ) {
        mIndicatorCreatedListener = indicatorCreatedListener
    }

    fun setViewPager(viewPager: ViewPager2) {
        mViewpager = viewPager
        if (mViewpager != null && mViewpager!!.adapter != null) {
            mLastPosition = -1
            createIndicators()
            mViewpager!!.unregisterOnPageChangeCallback(mInternalPageChangeCallback)
            mViewpager!!.registerOnPageChangeCallback(mInternalPageChangeCallback)
            mInternalPageChangeCallback.onPageSelected(mViewpager!!.currentItem)
        }
    }

    private fun createIndicators() {
        val adapter = mViewpager!!.adapter
        val count: Int = adapter?.itemCount ?: 0
        createIndicators(count, mViewpager!!.currentItem)
    }

    private val mInternalPageChangeCallback: OnPageChangeCallback =
        object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == mLastPosition || mViewpager!!.adapter == null || mViewpager!!.adapter!!.itemCount <= 0) {
                    return
                }
                animatePageSelected(position)
            }
        }

    private val mAdapterDataObserver: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            if (mViewpager == null) {
                return
            }
            val adapter = mViewpager!!.adapter
            val newCount = adapter?.itemCount ?: 0
            val currentCount = childCount
            mLastPosition = when {
                newCount == currentCount -> return      // No change
                mLastPosition < newCount -> mViewpager!!.currentItem
                else -> RecyclerView.NO_POSITION
            }
            createIndicators()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            onChanged()
        }

        override fun onItemRangeChanged(
            positionStart: Int, itemCount: Int,
            payload: Any?
        ) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            onChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            onChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            onChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            onChanged()
        }
    }

    fun getAdapterDataObserver(): AdapterDataObserver {
        return mAdapterDataObserver
    }

    private fun createAnimatorOut(): Animator {
        return AnimatorInflater.loadAnimator(context, R.animator.scale_to_alpha)
    }

    private fun createAnimatorIn(): Animator {
        val animatorIn: Animator = AnimatorInflater.loadAnimator(context, R.animator.scale_to_alpha)
        animatorIn.interpolator = ReverseInterpolator()
        return animatorIn
    }

    private fun createIndicators(count: Int, currentPosition: Int) {
        if (mImmediateAnimatorOut!!.isRunning) {
            mImmediateAnimatorOut!!.end()
            mImmediateAnimatorOut!!.cancel()
        }
        if (mImmediateAnimatorIn!!.isRunning) {
            mImmediateAnimatorIn!!.end()
            mImmediateAnimatorIn!!.cancel()
        }

        // Diff View
        val childViewCount = childCount
        if (count < childViewCount) {
            removeViews(count, childViewCount - count)
        } else if (count > childViewCount) {
            val addCount = count - childViewCount
            val orientation = orientation
            for (i in 0 until addCount) {
                addIndicator(orientation)
            }
        }

        // Bind Style
        var indicator: View
        for (i in 0 until count) {
            indicator = getChildAt(i)
            if (currentPosition == i) {
                indicator.setBackgroundResource(mIndicatorBackgroundResId)
                mImmediateAnimatorOut!!.setTarget(indicator)
                mImmediateAnimatorOut!!.start()
                mImmediateAnimatorOut!!.end()
            } else {
                indicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId)
                mImmediateAnimatorIn!!.setTarget(indicator)
                mImmediateAnimatorIn!!.start()
                mImmediateAnimatorIn!!.end()
            }
            if (mIndicatorCreatedListener != null) {
                mIndicatorCreatedListener!!.onIndicatorCreated(indicator, i)
            }
        }
        mLastPosition = currentPosition
    }

    private fun addIndicator(orientation: Int) {
        val indicator = View(context)
        val params = generateDefaultLayoutParams()
        params.width = mIndicatorWidth
        params.height = mIndicatorHeight
        if (orientation == HORIZONTAL) {
            params.leftMargin = mIndicatorMargin
            params.rightMargin = mIndicatorMargin
        } else {
            params.topMargin = mIndicatorMargin
            params.bottomMargin = mIndicatorMargin
        }
        addView(indicator, params)
    }

    fun animatePageSelected(position: Int) {
        if (mLastPosition == position) {
            return
        }
        if (mAnimatorIn!!.isRunning) {
            mAnimatorIn!!.end()
            mAnimatorIn!!.cancel()
        }
        if (mAnimatorOut!!.isRunning) {
            mAnimatorOut!!.end()
            mAnimatorOut!!.cancel()
        }
        val currentIndicator = getChildAt(mLastPosition)
        if (mLastPosition >= 0 && currentIndicator != null) {
            currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId)
            mAnimatorIn!!.setTarget(currentIndicator)
            mAnimatorIn!!.start()
        }
        val selectedIndicator: View? = getChildAt(position)
        if (selectedIndicator != null) {
            selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId)
            mAnimatorOut!!.setTarget(selectedIndicator)
            mAnimatorOut!!.start()
        }
        mLastPosition = position
    }

    private class ReverseInterpolator : Interpolator {
        override fun getInterpolation(value: Float): Float {
            return Math.abs(1.0f - value)
        }
    }

    companion object {
        private const val DEFAULT_INDICATOR_WIDTH = 5
    }
}