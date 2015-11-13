package com.example.picturecpmpare;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener{

	private Button button;
	private ImageView image;
	private int PHOTO_GALLERY = 1;
	private String path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button = (Button) findViewById(R.id.yasuo);
		image = (ImageView) findViewById(R.id.image);
		image.setImageURI(Uri.parse("http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg"));
		button.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.yasuo:
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
		switch(requestCode)
		{
		
			case 1:
				String [] proj = {MediaStore.Images.Media.DATA};
				//获取游标
				Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
				//获取索引
				int ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				//光标一直在开头
				cursor.moveToFirst();
				//获取图片路径
				path = cursor.getString(ColumnIndex);
				//将图片转化为流的形式
			try {
				FileInputStream is = new FileInputStream(path);
				String filepath = getDishPath(MainActivity.this);
				Log.d("图片路径----------------------- ", filepath);
				File file = new File("/storage/sdcard1/DCIM/","yashuohoude.jpg");
				int size = 2;
				Options option = new Options();
				option.inSampleSize=size;
				Bitmap image = BitmapFactory.decodeStream(is,null,option);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//100 表示不压缩 将数据保存到baos
				image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				int per = 100;
				is.close();
				while(baos.size() > 1024*500)
				{
					baos.reset();
					image.compress(Bitmap.CompressFormat.JPEG, per, baos);
					per -=10;
				}
				//回收图片 清理内存
				if(image!=null && !image.isRecycled())
				{
					image.recycle();
					image = null;
				}
				ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
				byte[] byte1 = baos.toByteArray();
				//Log.d("---base64------------", Base64.encodeToString(byte1,Base64.DEFAULT));
				System.out.println("---bae64--- "+Base64.encodeToString(byte1,Base64.DEFAULT));
				FileOutputStream out = new FileOutputStream(file);
				out.write(byte1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;
			default :
				break;
		}
	}
	
	public String getDishPath(Context context)
	{
		String cachePath;
		//判断SD卡是否存在，并且是否具有读写权限
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageDirectory()) &&
				(!Environment.isExternalStorageRemovable() || !Environment.isExternalStorageRemovable()))
		{
			cachePath = context.getExternalCacheDir().getPath();
			//获取路径
			//  /sdcard/Android/data/<application package>/cache 
		}
		else
		{
			cachePath = context.getCacheDir().getPath();
			//获取路径
			// /data/data/<application package>/cache
		}
		return cachePath;
	}
}
