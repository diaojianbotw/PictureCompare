package com.example.picturecpmpare;

import java.io.FileInputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Yasuo extends Activity implements OnClickListener{

	private Button selectpic;
	private Button upload;
	private String path;
	private static final int STEP1 = 1;  
    private static final int STEP2 = 2;  
    private static final int STEP3 = 3; 
    private String base64;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yasuo);
		selectpic = (Button) findViewById(R.id.selectpic);
		upload = (Button) findViewById(R.id.upload_pic);
		selectpic.setOnClickListener(this);
		upload.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.selectpic:
				//启动选择相片
				Intent intent = new Intent(Intent.ACTION_PICK,null);
				//得到系统所有图片        image所有图片类型
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				//从活动1跳到活动2 ，当活动2结束时返回到活动1 并调用onActivityResult
				startActivityForResult(intent,1);
			break;		
		}
		
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode)
		{
			case RESULT_OK :
				String [] projection = {MediaStore.Images.Media.DATA};
				//获取游标
				Cursor cursor = managedQuery(data.getData(), projection, null, null, null);
				//获取索引
				int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				//光标一直在开头
				cursor.moveToFirst();
				//获取图片路径
				path = cursor.getString(columnIndex);
				//转为流文件
			try {
				FileInputStream filein = new FileInputStream(path);
				Bitmap bitmap = BitmapFactory.decodeStream(filein);
				base64 = Base64Util.BitmapToBase64(bitmap);
				JSONObject json = new JSONObject();
				json.put("imgurl", base64);
				final String param = json.toString();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						String request = HttpUtil.sendRequest(param);
						try {
							String flag = new JSONObject(request).getString("flag");
							if("1".equals(flag))
							{
								Message msg = new Message();
								msg.what =1;
								handler.sendMessage(msg);
							}
							if("0".equals(flag))
							{
								Message msg = new Message();
								msg.what =0;
								handler.sendMessage(msg);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what)
			{
				case 1:
					Toast.makeText(Yasuo.this, "成功", Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Toast.makeText(Yasuo.this, "失败", Toast.LENGTH_SHORT).show();
					break;
			}
			
		}
		
	};
	
	
	
}
