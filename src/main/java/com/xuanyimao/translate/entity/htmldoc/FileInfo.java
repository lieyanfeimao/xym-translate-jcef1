package com.xuanyimao.translate.entity.htmldoc;

/**
 * 文件信息
 */
public class FileInfo {

    /**文件id**/
    private String id;
    /**文件路径，相对于工作目录的路径**/
    private String path;

    /**
     * 获取 文件id
     *
     * @return id 文件id
     */
    public String getId() {
        return this.id;
    }

    /**
     * 设置 文件id
     *
     * @param id 文件id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取 文件路径，相对于工作目录的路径
     *
     * @return path 文件路径，相对于工作目录的路径
     */
    public String getPath() {
        return this.path;
    }

    /**
     * 设置 文件路径，相对于工作目录的路径
     *
     * @param path 文件路径，相对于工作目录的路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    public FileInfo(String id, String path) {
        this.id = id;
        this.path = path;
    }
}
