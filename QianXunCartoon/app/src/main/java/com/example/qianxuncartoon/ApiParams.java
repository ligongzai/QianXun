package com.example.qianxuncartoon;

import java.util.HashMap;

/**
 * Created by Alex on 2017/2/19.
 */

public class ApiParams extends HashMap<String, String> {
    public ApiParams with(String key, String value) {
        put(key, value);
        return this;
    }
}
