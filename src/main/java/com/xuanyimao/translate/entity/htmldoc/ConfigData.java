package com.xuanyimao.translate.entity.htmldoc;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 配置文件数据
 */
public class ConfigData {

    /** 版本工作目录 */
    private String versionFolder;
    /** 翻译平台名 */
    String platformName;
    /** 词库路径 */
    String dictPath;
    /** 选择的版本词库 */
    List<String> checkVersionIds;
    /** 正则表达式 */
    String rgx;
    /** 原文 */
    String srcLanguage;
    /** 译文 */
    String destLanguage;
    /** 文件编码 */
    String fileEncode;
    /** 同时翻译的文件个数 */
    Integer translateCount;
    /** 文件列表 */
    List<FileInfo> fileList;

    /**
     * 获取 版本工作目录
     *
     * @return versionFolder 版本工作目录
     */
    public String getVersionFolder() {
        return this.versionFolder;
    }

    /**
     * 设置 版本工作目录
     *
     * @param versionFolder 版本工作目录
     */
    public void setVersionFolder(String versionFolder) {
        this.versionFolder = versionFolder;
    }

    /**
     * 获取 词库路径
     *
     * @return dictPath 词库路径
     */
    public String getDictPath() {
        return this.dictPath;
    }

    /**
     * 设置 词库路径
     *
     * @param dictPath 词库路径
     */
    public void setDictPath(String dictPath) {
        this.dictPath = dictPath;
    }

    /**
     * 获取 选择的版本词库
     *
     * @return checkVersionIds 选择的版本词库
     */
    public List<String> getCheckVersionIds() {
        return this.checkVersionIds;
    }

    /**
     * 设置 选择的版本词库
     *
     * @param checkVersionIds 选择的版本词库
     */
    public void setCheckVersionIds(List<String> checkVersionIds) {
        this.checkVersionIds = checkVersionIds;
    }

    /**
     * 获取 正则表达式
     *
     * @return rgx 正则表达式
     */
    public String getRgx() {
        return this.rgx;
    }

    /**
     * 设置 正则表达式
     *
     * @param rgx 正则表达式
     */
    public void setRgx(String rgx) {
        this.rgx = rgx;
    }

    /**
     * 获取 原文
     *
     * @return srcLanguage 原文
     */
    public String getSrcLanguage() {
        return this.srcLanguage;
    }

    /**
     * 设置 原文
     *
     * @param srcLanguage 原文
     */
    public void setSrcLanguage(String srcLanguage) {
        this.srcLanguage = srcLanguage;
    }

    /**
     * 获取 译文
     *
     * @return destLanguage 译文
     */
    public String getDestLanguage() {
        return this.destLanguage;
    }

    /**
     * 设置 译文
     *
     * @param destLanguage 译文
     */
    public void setDestLanguage(String destLanguage) {
        this.destLanguage = destLanguage;
    }

    /**
     * 获取 文件编码
     *
     * @return fileEncode 文件编码
     */
    public String getFileEncode() {
        return StringUtils.isBlank(this.fileEncode)?"utf-8":this.fileEncode;
    }

    /**
     * 设置 文件编码
     *
     * @param fileEncode 文件编码
     */
    public void setFileEncode(String fileEncode) {
        this.fileEncode = fileEncode;
    }

    /**
     * 获取 同时翻译的文件个数
     *
     * @return translateCount 同时翻译的文件个数
     */
    public Integer getTranslateCount() {
        return this.translateCount==null?1:this.translateCount;
    }

    /**
     * 设置 同时翻译的文件个数
     *
     * @param translateCount 同时翻译的文件个数
     */
    public void setTranslateCount(Integer translateCount) {
        this.translateCount = translateCount;
    }

    /**
     * 获取 文件列表
     *
     * @return fileList 文件列表
     */
    public List<FileInfo> getFileList() {
        return this.fileList;
    }

    /**
     * 设置 文件列表
     *
     * @param fileList 文件列表
     */
    public void setFileList(List<FileInfo> fileList) {
        this.fileList = fileList;
    }

    /**
     * 获取 翻译平台名
     *
     * @return platformName 翻译平台名
     */
    public String getPlatformName() {
        return this.platformName;
    }

    /**
     * 设置 翻译平台名
     *
     * @param platformName 翻译平台名
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}
