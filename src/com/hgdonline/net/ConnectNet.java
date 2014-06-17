package com.hgdonline.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.hgdonline.entity.Book;

public class ConnectNet {
	
	private static final ConnectNet conn = new ConnectNet();
	
	//��½״̬
	//IP ����
	public static final int IP_ERROR = -1;
	//��½�ɹ�
	public static final int CONN_OK = 200;
	//�û������������
	public static final int CONN_WRONG = 404;
	//�������
	public static final int NET_WRONG = 2;
	
	private ConnectNet(){
	}
	
	public static ConnectNet getConnect(){
		return conn;
	}
	
	/**
	 * 
	 * @return 1.��ʾ�û������������-1����ʾip����0����ʾ��½�ɹ�
	 * @throws IOException
	 */
	
	public int login(String userName, String password,boolean isSuperUser) throws IOException{
		//write something here
		String url;
		if(isSuperUser){
			url = "";
		}else{
			url = "http://book.hgdonline.net/index.php/Api/Index/login.html?stu_id="
					+userName
					+"&password="
					+password;
		}
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = getHttpClient().execute(httpGet);
		//��ʾip������ʾ�û��ڰ칫�ҵ�½
		if(response.getStatusLine().getStatusCode() == 403){
			return -1;
		}else if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			try{
				JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity(), "utf-8"));
//				String message = json.getString("message");
				int status = json.getInt("status");
				return status;
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		//���ٽ���httpGet���ص���Ϣ
		httpGet.abort();
		return -1;
	}
	
	//get book list 
	public List<Book> getBooks(){
		ArrayList<Book> books = new ArrayList<Book>();
		for(int i = 0;i<10;i++){
			Book book = new Book();
			book.setBookName("book"+i);
			books.add(book);
		}
		return books;
	}
	
	//����
	public int borrowBooks(String bookId){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
	//����
	public int returnBooks(String bookId){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
	
	
	//get httpClient
	private HttpClient getHttpClient(){
		HttpParams params = new BasicHttpParams();
		//�����������ӳ�ʱ
		HttpConnectionParams.setConnectionTimeout(params, 20*1000);
		//����socket��Ӧ��ʱ
		HttpConnectionParams.setSoTimeout(params, 20*1000);
		//���û����С
		HttpConnectionParams.setSocketBufferSize(params, 8*1024);
		
		HttpClient client = new DefaultHttpClient(params);
		return client;
	}
	
	
	
	

}
