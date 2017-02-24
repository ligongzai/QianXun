package com.example.qianxuncartoon;

import com.squareup.otto.Bus;

/**
 * Created by Alex on 2017/2/24.
 */

public class AppBus extends Bus{
    private static com.example.qianxuncartoon.AppBus bus;

    public static com.example.qianxuncartoon.AppBus getInstance() {
        if (bus == null) {
            bus = new com.example.qianxuncartoon.AppBus();
        }
        return bus;

    }
}
