package love.image.util;

import java.io.Serializable;
import java.util.List;

/**
 * 翻页模板工具类
 *
 */
public final class PageModel<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private int currentPage;// 当前页
	private int pageSize;// 每页显示条数
	private int totalPage = 1;// 总页数
	private int totalRecord;// 总记录数
	private List<T> dataList;// 分页数据

	public PageModel() {
	}

	/*
	 * 初始化PageModel实例
	 */
	public PageModel(final int pageSize, final int currentPage,
			final int totalRecord, List<T> dataList) {
		// 初始化每页显示条数
		this.pageSize = pageSize;
		// 设置总记录数
		this.totalRecord = totalRecord;
		// 初始化总页数
		this.dataList = dataList;
		// 初始化当前页
		this.currentPage = currentPage;
		this.totalPage = this.totalRecord == 0 ? 1 : (this.totalRecord
				+ this.pageSize - 1)
				/ this.pageSize;
	}

	/*
	 * 外界获得PageModel实例
	 */
	@SuppressWarnings("unchecked")
	public static PageModel<?> newPageModel(final int pageSize,
			final int currentPage, final int totalRecord, List<?> dataList) {

		return new PageModel(pageSize, currentPage, totalRecord, dataList);
	}

	// 设置当前请求页
	private void setCurrentPage(int currentPage) {
		if (currentPage < 1) {
			this.currentPage = 1;
		}
		if (currentPage > totalPage) {
			this.currentPage = totalPage;
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/*
	 * 获得开始行数
	 */
	public int getStartRow() {
		return (currentPage - 1) * pageSize;
	}

	/*
	 * 获得结束行
	 */
	public int getEndRow() {
		return currentPage * pageSize;
	}

	/*
	 * 获得翻页数据
	 */
	public List<T> getDataList() {
		return dataList;
	}

	/*
	 * 设置翻页数据
	 */
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	// 首页
	public int getFirst() {
		return 1;
	}

	// 上一页

	public int getPrevious() {
		return currentPage - 1;
	}

	// 下一页
	public int getNext() {
		return currentPage + 1;
	}

	// 尾页
	public int getLast() {
		return totalPage;
	}
}