package com.hgdonline.entity;

import java.sql.Date;

public class Book {
	//书本名称
	private String bookName;
	//书本编号
	private String bookId;
	//出版社
	private String publishingCompany;
	//借出日期
	private Date borrowDate;
	//是否正在借阅 0表示已还，1表示正在借阅
	private int isBorrowing = -1;
	
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getPublishingCompany() {
		return publishingCompany;
	}
	public void setPublishingCompany(String publishingCompany) {
		this.publishingCompany = publishingCompany;
	}
	public Date getBorrowDate() {
		return borrowDate;
	}
	//改动
	public void setBorrowDate(java.util.Date date) {
		this.borrowDate = (Date) date;
	}
	public int getIsBorrowing() {
		return isBorrowing;
	}
	public void setIsBorrowing(int isBorrowing) {
		this.isBorrowing = isBorrowing;
	}
	
	

}
