package com.itoys.android.uikit.components.bottomnavigation

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.textview.MaterialTextView
import com.itoys.android.uikit.R
import com.itoys.android.uikit.databinding.UikitLayoutNavigationMenuBinding
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/10
 */
class NavigationMenu(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /** 菜单-icon */
    private var icon: AppCompatImageView? = null

    /** 菜单-icon drawable */
    private var menuIcon: Drawable? = null

    /** 菜单-icon 大小 */
    private var menuIconSize = -1

    /** 菜单-间隔 */
    private var spacingView: View? = null

    /** 菜单-icon & text 间隔 */
    private var spacing = -1

    /** 菜单-label */
    private var label: MaterialTextView? = null

    /** 菜单-label text */
    private var menuLabel = ""

    /** 菜单-label 大小 */
    private var menuLabelSize = -1

    /** 菜单-label 选中大小 */
    private var menuLabelActiveSize = -1

    /** 菜单-label 颜色 */
    private var menuLabelColor = -1

    /** 菜单-label 选中颜色 */
    private var menuLabelActiveColor = -1

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.NavigationMenu)

        val binding = UikitLayoutNavigationMenuBinding.inflate(
            LayoutInflater.from(context), this, false
        )
        val menuLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.addView(binding.root, menuLayoutParams)

        // icon
        menuIcon = ta.getDrawable(R.styleable.NavigationMenu_menuIcon)
        menuIconSize =
            ta.getDimensionPixelOffset(R.styleable.NavigationMenu_menuIconSize, menuIconSize)
        initMenuIcon(binding.icon)

        // label
        menuLabel = ta.getString(R.styleable.NavigationMenu_menuLabel).invalid()
        menuLabelSize = ta.getDimensionPixelOffset(
            R.styleable.NavigationMenu_menuLabelSize, menuLabelSize
        )
        menuLabelActiveSize = ta.getDimensionPixelOffset(
            R.styleable.NavigationMenu_menuLabelActiveSize, menuLabelActiveSize
        )
        menuLabelColor = ta.getColor(
            R.styleable.NavigationMenu_menuLabelColor, menuLabelColor
        )
        menuLabelActiveColor = ta.getColor(
            R.styleable.NavigationMenu_menuLabelActiveColor, menuLabelActiveColor
        )
        initMenuLabel(binding.label)

        // 菜单-icon & text 间隔
        spacing = ta.getDimensionPixelOffset(R.styleable.NavigationMenu_spacing, spacing)
        val spacingLayoutParams = binding.spacing.layoutParams
        if (spacing != -1) spacingLayoutParams.height = spacing
        binding.spacing.layoutParams = spacingLayoutParams
        this.spacingView = binding.spacing

        // 如果菜单label不显示, spacing也需要不显示
        binding.spacing.visibility = binding.label.visibility

        ta.recycle()
    }

    /**
     * 菜单-icon
     */
    fun iconView() = this.icon

    /**
     * 初始化菜单-icon
     */
    private fun initMenuIcon(icon: AppCompatImageView) {
        this.icon = icon
        icon.setImageDrawable(menuIcon)
        val iconLayoutParams = icon.layoutParams
        if (menuIconSize != -1) {
            iconLayoutParams.width = menuIconSize
            iconLayoutParams.height = menuIconSize
        }
        icon.layoutParams = iconLayoutParams
    }

    /**
     * 菜单-label
     */
    fun labelView() = this.label

    /**
     * 初始化菜单-label
     */
    private fun initMenuLabel(label: MaterialTextView) {
        this.label = label
        label.visibility = menuLabel.isBlank().then(View.GONE, View.VISIBLE)
        label.text = menuLabel
        if (menuLabelColor != -1) label.setTextColor(menuLabelColor)
        if (menuLabelSize != -1) label.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            menuLabelSize.toFloat()
        )
    }

    /**
     * 设置菜单label
     */
    fun setMenuLabel(label: String?) {
        this.label?.text = label.invalid()
        this.label?.visibility = label.isBlank().then(View.GONE, View.VISIBLE)
        this.spacingView?.visibility = this.label?.visibility ?: View.GONE
    }

    /**
     * 选中状态
     */
    fun selected(selected: Boolean) {
        this.icon?.isSelected = selected
        this.label?.isSelected = selected
        this.label?.typeface = Typeface.create(
            selected.then("sans-serif-medium", "sans-serif"), Typeface.NORMAL
        )
        val textColor = selected.then(menuLabelActiveColor, menuLabelColor)
        if (textColor != -1) this.label?.setTextColor(textColor)
    }
}