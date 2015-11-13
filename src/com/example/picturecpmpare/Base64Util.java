package com.example.picturecpmpare;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Base64Util {
	
	/**
	 * File to Base64
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String getBase64StringFromFile(String filePath) throws FileNotFoundException, IOException {  
		byte[] buffer = null;   
		File file = new File(filePath);  
		FileInputStream fis = null;  
		ByteArrayOutputStream bos = null; 
		byte[] b = new byte[1000];
		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream(1000);
			int n;  
			while ((n = fis.read(b)) != -1) {  
				bos.write(b, 0, n);  
			}  
			fis.close();  
			bos.close();
			buffer = bos.toByteArray();  
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (bos != null) {  
				try {  
					bos.close();  
				} catch (IOException e1) {  
					e1.printStackTrace();  
				}  
			}  
			if (fis != null) {  
				try {  
					fis.close();  
				} catch (IOException e1) {  
					e1.printStackTrace();  
				}  
			} 
		}

		return Base64.encodeToString(buffer, Base64.DEFAULT);
	}

	/**
	 * Base64 to File
	 * @param base64Str
	 * @param filePath
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void saveBase64StringToFile(String base64Str, String filePath, String fileName) throws FileNotFoundException, IOException {  
		BufferedOutputStream bos = null;  
		FileOutputStream fos = null;  
		File file = null;
		try {  
			File dir = new File(filePath);  
			if(!dir.exists()&&dir.isDirectory()){
				dir.mkdirs();  
			}  
			file = new File(filePath, fileName);  
			fos = new FileOutputStream(file);  
			bos = new BufferedOutputStream(fos);
			byte[] bfile = Base64.decode(base64Str, Base64.DEFAULT);
			bos.write(bfile);  
		} catch (FileNotFoundException e) {  
			throw e; 
		} catch (IOException e) {
			throw e;
		} finally {  
			if (bos != null) {  
				try {  
					bos.close();  
				} catch (IOException e1) {  
					e1.printStackTrace();  
				}  
			}  
			if (fos != null) {  
				try {  
					fos.close();  
				} catch (IOException e1) {  
					e1.printStackTrace();  
				}  
			}  
		}  
	}
	
	/** 
	 * bitmapתΪbase64 
	 * @param bitmap 
	 * @return 
	 */  
	public static String BitmapToBase64(Bitmap bitmap) {  
	  
	    String result = null;  
	    ByteArrayOutputStream baos = null;  
	    try {  
	        if (bitmap != null) {  
	            baos = new ByteArrayOutputStream();  
	            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);  
	  
	            baos.flush();  
	            baos.close();  
	  
	            byte[] bitmapBytes = baos.toByteArray();  
	            result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);  
	        }  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (baos != null) {  
	                baos.flush();  
	                baos.close();  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    return result;  
	} 
	
	/** 
	 * base64תΪbitmap 
	 * @param base64Data 
	 * @return 
	 */  
	public static Bitmap base64ToBitmap(String base64Data) {  
	    byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);  
	    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);  
	}
}
