package com.baayso.bms;

import java.util.ArrayList;

import com.google.gson.Gson;

public class Test {

    public static void main(String[] args) {

        String json = "{\"message\":\"super链接成功。连接主机为：ChenFangjie-PC\",\"status\":true, \"data\":\"sdfsdfsd\"}";

        OperationResult op = new Gson().fromJson(json, OperationResult.class);

        System.out.println(op);

        Object data = op.getData();

        if (data instanceof String) {
            String ops = (String) data;
            System.out.println(ops);
        }
        else if (data instanceof ArrayList) {

        }
        else {

        }

    }

}
