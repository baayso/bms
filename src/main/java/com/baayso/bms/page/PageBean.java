package com.baayso.bms.page;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * 实体：分页信息
 * 
 * currentPage 当前页
 * pageSize 每页显示多少条记录
 * 
 * recordList 本页的数据列表
 * totalRecord 总记录数
 * totalPage 总页数
 * 
 * beginPageIndex 页码列表的开始索引（包含）
 * endPageIndex 页面列表的结束索引（包含）
 * </pre>
 * 
 * @author ChenFangjie
 */
public class PageBean<T> implements Serializable {

    private static final long serialVersionUID = -6672285579571411902L;

    // 页面传递的参数或者是配置的参数
    private int currentPage; // 当前页
    private int pageSize; // 每页显示多少条记录

    // 需要查询数据库
    private List<T> recordList; // 本页的数据列表
    private long totalRecord; // 总记录数

    // 需要计算
    private long totalPage; // 总页数
    private long beginPageIndex; // 页码列表的开始索引（包含）
    private long endPageIndex; // 页面列表的结束索引（包含）

    /**
     * 接受必要的四个属性，然后自动计算出其他的三个属性值
     * 
     * @param currentPage
     *            当前面
     * @param pageSize
     *            每页显示多少条记录
     * @param recordList
     *            本页的数据列表
     * @param totalRecord
     *            总记录数
     */
    public PageBean(int currentPage, int pageSize, List<T> recordList, long totalRecord) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.recordList = recordList;
        this.totalRecord = totalRecord;

        // 计算 totalPage
        this.totalPage = (totalRecord + pageSize - 1) / pageSize;

        // 计算 beginPageIndex 和 endPageIndex
        // >>> 总页数小于等于10页面的时，全部显示
        if (this.totalPage <= 10) {
            this.beginPageIndex = 1;
            this.endPageIndex = this.totalPage;
        }
        // >>> 总页数大于10页时，就只显示当前页附近的共10个页码
        else {
            // 默认显示前4页 + 当前页 + 后5页
            this.beginPageIndex = currentPage - 4; // 当前第7页，7 - 4 = 3
            this.endPageIndex = currentPage + 5; // 当前第7页，7 + 5 = 12 --> 3页 ~ 12页

            // 如果前面不足4个页码时，则显示前10页
            if (this.beginPageIndex < 1) {
                this.beginPageIndex = 1;
                this.endPageIndex = 10;
            }
            // 如果后面不足5个页码时，则显示后10页
            else if (this.endPageIndex > this.totalPage) {
                this.beginPageIndex = this.totalPage - 9;
                this.endPageIndex = this.totalPage;
            }
        }
    }

    public List<T> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<T> recordList) {
        this.recordList = recordList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public long getBeginPageIndex() {
        return beginPageIndex;
    }

    public void setBeginPageIndex(int beginPageIndex) {
        this.beginPageIndex = beginPageIndex;
    }

    public long getEndPageIndex() {
        return endPageIndex;
    }

    public void setEndPageIndex(int endPageIndex) {
        this.endPageIndex = endPageIndex;
    }

}
