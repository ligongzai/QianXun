package com.example.qianxuncartoon;

/**
 * Created by Alex on 2017/2/19.
 * 回调接口
 */
public interface HttpResponeCallBack {
    public void onResponeStart(String apiName);

    /**
     * 此回调只有调用download方法下载数据时才生效
     *
     * @param apiName
     * @param count
     * @param current
     */
    public void onLoading(String apiName, long count, long current);

    public void onSuccess(String apiName, Object object);

    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg);
}
