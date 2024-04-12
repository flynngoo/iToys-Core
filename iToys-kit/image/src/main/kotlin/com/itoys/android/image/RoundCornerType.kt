package com.itoys.android.image

import jp.wasabeef.transformers.types.CornerType

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/12
 */
enum class RoundCornerType(private val type: CornerType) {

    ALL(CornerType.ALL),

    TOP_LEFT(CornerType.TOP_LEFT),

    TOP_RIGHT(CornerType.TOP_RIGHT),

    BOTTOM_LEFT(CornerType.BOTTOM_LEFT),

    BOTTOM_RIGHT(CornerType.BOTTOM_RIGHT),

    TOP(CornerType.TOP),

    BOTTOM(CornerType.BOTTOM),

    LEFT(CornerType.LEFT),

    RIGHT(CornerType.RIGHT),

    OTHER_TOP_LEFT(CornerType.OTHER_TOP_LEFT),

    OTHER_TOP_RIGHT(CornerType.OTHER_TOP_RIGHT),

    OTHER_BOTTOM_LEFT(CornerType.OTHER_BOTTOM_LEFT),

    OTHER_BOTTOM_RIGHT(CornerType.OTHER_BOTTOM_RIGHT),

    DIAGONAL_FROM_TOP_LEFT(CornerType.DIAGONAL_FROM_TOP_LEFT),

    DIAGONAL_FROM_TOP_RIGHT(CornerType.DIAGONAL_FROM_TOP_RIGHT);

    /**
     * 获取类型
     */
    fun getType() = type
}