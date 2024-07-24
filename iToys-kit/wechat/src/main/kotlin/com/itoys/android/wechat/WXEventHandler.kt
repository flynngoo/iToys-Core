package com.itoys.android.wechat

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/7/23
 */
interface WXEventHandler {

    fun onReq(req: BaseReq)

    fun onResp(resp: BaseResp)
}