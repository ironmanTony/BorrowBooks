package com.hgdonline.entity;

import java.sql.Date;

public class Book {
	//������
	private String donator;
	//�鱾����
	private String bookName;
	//�鱾���
	private String bookId;
	//�����
	private String searchId;
	//������
	private String publishingCompany;
	//�������
	private Date borrowDate;
	//�Ƿ����ڽ��� 0��ʾ�ѻ���1��ʾ���ڽ���
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
	//�Ķ�
	public void setBorrowDate(java.util.Date date) {
		this.borrowDate = (Date) date;
	}
	public int getIsBorrowing() {
		return isBorrowing;
	}
	public void setIsBorrowing(int isBorrowing) {
		this.isBorrowing = isBorrowing;
	}
	public String getSearchId() {
		return searchId;
	}
	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}
	public String getDonator() {
		return donator;
	}
	public void setDonator(String donator) {
		this.donator = donator;
	}
	
	
	
	

}
