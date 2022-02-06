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
    private val darkColor = "#333333"
    private val lightColor = "#EBEBEB"


    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {

        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.BannerItem)
        try {
            setupUi(styledAttrs)
            collapseText(binding.tvText, binding.tvCta)

        } finally {
            styledAttrs.recycle()
        }
    }

    private fun setupUi(styledAttrs: TypedArray) {
        val bannerText = styledAttrs.getString(R.styleable.BannerItem_text)
        val bannerCta = styledAttrs.getString(R.styleable.BannerItem_cta)
        val bannerAsset = styledAttrs.getResourceId(R.styleable.BannerItem_asset, 0)
        val bannerState = styledAttrs.getBoolean(R.styleable.BannerItem_state, false)
        binding.tvText.text = bannerText
        binding.tvCta.text = bannerCta
        if (bannerAsset != 0) {
            binding.imAsset.setImageDrawable(
                AppCompatResources.getDrawable(
                    context,
                    bannerAsset
                )
            )
        }

        if (bannerState) {
            binding.root.setBackgroundColor(Color.parseColor(lightColor))
            binding.tvText.setTextColor(Color.parseColor(darkColor))
            binding.tvCta.setTextColor(Color.parseColor(darkColor))
        } else {
            binding.root.setBackgroundColor(Color.parseColor(darkColor))
            binding.tvText.setTextColor(Color.WHITE)
            binding.tvCta.setTextColor(Color.WHITE)
        }

        animateViews(binding.tvText, binding.tvCta, bannerAsset)
    }

    private fun collapseText(tvText: TextView, tvCta: TextView) {
        var isCollapsed: Boolean
        tvText.post {
            isCollapsed = tvText.lineCount > 2
            tvCta.visibility = if (isCollapsed) View.VISIBLE else View.GONE
            tvCta.setOnClickListener {
                if (!isCollapsed) {
                    tvText.maxLines = 40
                    tvCta.text = context.getString(R.string.show_less)
                } else {
                    tvText.maxLines = 3
                    tvCta.text = context.getString(R.string.show_more)

                }
                isCollapsed = !isCollapsed
            }
        }


    }

    private fun animateViews(tvText: TextView, tvCta: TextView, bannerAsset: Int?) {
        val animScale = AnimationUtils.loadAnimation(context, R.anim.scale)
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