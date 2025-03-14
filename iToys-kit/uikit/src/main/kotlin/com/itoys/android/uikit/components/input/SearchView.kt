package com.itoys.android.uikit.components.input

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.drake.softinput.hideSoftInput
import com.itoys.android.uikit.R
import com.itoys.android.uikit.databinding.UikitLayoutSearchBinding
import com.itoys.android.utils.InputUtils.hideSoftInput
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.empty
import com.itoys.android.utils.expansion.gone
import com.itoys.android.utils.expansion.size
import com.itoys.android.utils.expansion.then
import com.itoys.android.utils.expansion.visible

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/6
 */
class SearchView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /**
     * 是否允许清除
     */
    private var allowClear = true

    /**
     * 搜索输入框
     */
    private var searchInput: AppCompatEditText? = null

    /**
     * 搜索回调
     */
    private var searchCallback: ISearchCallback? = null

    init {
        initView(attrs)
    }

    /**
     * 初始化
     */
    private fun initView(attrs: AttributeSet?) {
        val binding = UikitLayoutSearchBinding.inflate(
            LayoutInflater.from(context), this, false
        )
        this.addView(binding.root)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.SearchView)
        // 是否允许清除
        allowClear = ta.getBoolean(R.styleable.SearchView_searchAllowClear, allowClear)

        // 搜索框前缀
        val prefix = ta.getDrawable(R.styleable.SearchView_searchPrefix)
        val prefixSize = ta.getDimensionPixelSize(R.styleable.SearchView_searchPrefixSize, 0)
        binding.prefix.setImageDrawable(prefix)
        val prefixParams = binding.prefix.layoutParams as LayoutParams
        if (prefixSize > 0) {
            prefixParams.width = prefixSize
            prefixParams.height = prefixSize
        }
        binding.prefix.layoutParams = prefixParams

        // 搜索框后缀
        val suffix = ta.getDrawable(R.styleable.SearchView_searchSuffix)
        val suffixSize = ta.getDimensionPixelSize(R.styleable.SearchView_searchSuffixSize, 0)
        binding.suffix.setImageDrawable(suffix)
        val suffixParams = binding.suffix.layoutParams as LayoutParams
        if (suffixSize > 0) {
            suffixParams.width = suffixSize
            suffixParams.height = suffixSize
        }
        binding.suffix.layoutParams = suffixParams
        if (allowClear) binding.suffix.gone()

        // 搜索框内边距
        val paddingHorizontal = ta.getDimensionPixelOffset(R.styleable.SearchView_searchPaddingHorizontal, 0)
        val paddingVertical = ta.getDimensionPixelOffset(R.styleable.SearchView_searchPaddingVertical, 0)
        val hint = ta.getString(R.styleable.SearchView_searchHint)
        val hintColor = ta.getColor(R.styleable.SearchView_searchHintColor, 0)
        val background = ta.getDrawable(R.styleable.SearchView_searchBackground)
        binding.searchInput.setHint(hint)
        if (hintColor != 0) {
            binding.searchInput.setHintTextColor(hintColor)
        }
        val leftPadding = (prefix == null).then(paddingHorizontal, prefixSize)
        val rightPadding = (suffix == null).then(paddingHorizontal, suffixSize)
        binding.searchInput.setPadding(leftPadding, paddingVertical, rightPadding, paddingVertical)
        binding.searchInput.setBackgroundDrawable(background)

        ta.recycle()

        searchInput = binding.searchInput
        binding.searchInput.addTextChangedListener {
            searchCallback?.onChange(it ?: String.empty())

            // 输入内容长度大于0 && 允许清除, 显示清除按钮
            (it.size() > 0 && allowClear).then(
                { binding.suffix.visible() }, { binding.suffix.gone() }
            )
        }

        binding.searchInput.setOnEditorActionListener { edit, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH -> {
                    searchCallback?.onSearch(edit.text)
                    binding.searchInput.hideSoftInput()
                    true
                }
                else -> false
            }
        }

        binding.prefix.doOnClick { searchCallback?.onPrefixClick() }

        binding.suffix.doOnClick {
            if (allowClear) {
                binding.searchInput.setText(String.empty())
            }

            searchCallback?.onSuffixClick(allowClear)
        }
    }

    fun setSearchCallback(searchCallback: ISearchCallback) {
        this.searchCallback = searchCallback
    }

    fun clear() {
        searchInput?.setText(String.empty())
        searchInput?.hideSoftInput()
        searchInput?.clearFocus()
    }

    interface ISearchCallback {

        /**
         * 输入框内容改变
         */
        fun onChange(input: CharSequence)

        /**
         * 搜索
         */
        fun onSearch(input: CharSequence) {}

        /**
         * 前缀点击
         */
        fun onPrefixClick() {}

        /**
         * 后缀点击
         */
        fun onSuffixClick(allowClear: Boolean) {}
    }
}