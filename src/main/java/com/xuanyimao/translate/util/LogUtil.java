package com.xuanyimao.translate.util;

import com.xuanyimao.translate.StartupApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 */
public class LogUtil {

    private final static Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public final static Logger getLogger(){
        return logger;
    }
}
