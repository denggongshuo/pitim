package com.pit.im.framework.config;

import com.pit.im.server.ImServer;


/**
 * @author deng
 * @date 2020/1/6 11:06
 */

public class Imconfig {

    public ImServer imServer() {
        ImServer imServer = new ImServer();
        imServer.setPort(21000);
        return imServer;
    }


}
