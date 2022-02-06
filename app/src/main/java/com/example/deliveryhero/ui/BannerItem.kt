package com.example.deliveryhero.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.deliveryhero.R
import com.example.deliveryhero.databinding.BannerBinding


class BannerItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = BannerBinding.inflate(LayoutInflater.from(context), this)
    private val DARK_COLOR = "#333333"
    private val LIGHT_COLOR = "#EBEBEB"
    private val COLLAPSE_TEXT_LINES = 3
    private val EXPAND_TEXT_LINES = 400


    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {

        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.BannerItem)
        try {
            setupUi(styledAttrs)

        } finally {
            styledAttrs.recycle()
        }
    }

    private fun setupUi(styledAttrs: TypedArray) {
        val bannerText = styledAttrs.getString(R.styleable.BannerItem_text)
        val bannerAsset = styledAttrs.getResourceId(R.styleable.BannerItem_asset, 0)
        val bannerState = styledAttrs.getBoolean(R.styleable.BannerItem_state, false)
        binding.tvText.text = bannerText
        if (bannerAsset != 0) {
            binding.imAsset.setImageDrawable(
                AppCompatResources.getDrawable(
                    context,
                    bannerAsset
                )
            )
        }
        if (bannerState) {
            binding.root.setBackgroundColor(Color.parseColor(LIGHT_COLOR))
            binding.tvText.setTextColor(Color.parseColor(DARK_COLOR))
            binding.tvCta.setTextColor(Color.parseColor(DARK_COLOR))
        } else {
            binding.root.setBackgroundColor(Color.parseColor(DARK_COLOR))
            binding.tvText.setTextColor(Color.WHITE)
            binding.tvCta.setTextColor(Color.WHITE)
        }

        initAnimations(binding.tvText, binding.tvCta, bannerAsset)
        toggleText(binding.tvText, binding.tvCta, bannerAsset)

    }

    private fun toggleText(tvText: TextView, tvCta: TextView, bannerAsset: Int?) {
        var isExpanded: Boolean

        tvText.post {
            isExpanded = tvText.lineCount > 2
            tvCta.visibility = if (isExpanded) View.VISIBLE else View.GONE
            tvCta.setOnClickListener {
                if (!isExpanded) {
                    tvText.maxLines = COLLAPSE_TEXT_LINES
                    tvCta.text = context.getString(R.string.show_more)
                    bannerAsset?.let {
                        binding.imAsset.startAnimation(
                            AnimationUtils.loadAnimation(
                                context,
                                R.anim.move_asset_expanded
                            )
                        )
                    }


                } else {
                    tvText.maxLines = EXPAND_TEXT_LINES
                    tvCta.text = context.getString(R.string.show_less)
                }
                isExpanded = !isExpanded

            }

        }


    }


    private fun initAnimations(tvText: TextView, tvCta: TextView, bannerAsset: Int?) {
        val animScale = AnimationUtils.loadAnimation(context, R.anim.scale_down)
        val animAlpha = AnimationUtils.loadAnimation(context, R.anim.alpha)
        val animMoveUpText = AnimationUtils.loadAnimation(context, R.anim.move_up_text)
        val animMoveUpAsset = AnimationUtils.loadAnimation(context, R.anim.move_up_asset)

        val animationSet = AnimationSet(false)
        animationSet.addAnimation(animAlpha)
        animationSet.addAnimation(animMoveUpText)
        binding.root.startAnimation(animScale)
        tvText.startAnimation(animationSet)
        tvCta.startAnimation(animationSet)
        bannerAsset?.let {
            binding.imAsset.startAnimation(animMoveUpAsset)
        }


    }


}