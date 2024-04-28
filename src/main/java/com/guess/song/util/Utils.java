package com.guess.song.util;

import java.io.File;
import java.net.URL;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

import com.guess.song.model.RestFile;

public class Utils {
	
	public static String fileUpload(RestFile restFile, HttpServletRequest request) {
		String saveFileNm = "";
//		String rPath = request.getServletContext().getRealPath("/");
//		String path =rPath + "upload/songBoard/";
		//String path ="C:/upload/songBoard/";
		String path ="/home/ubuntu/apps/upload/songBoard/";
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		MultipartFile mf  = restFile.getSongImg();
		if(mf != null) {
			try {
				String originNm = mf.getOriginalFilename();
				if(!"".equals(originNm)) {
					String ext = originNm.substring(originNm.lastIndexOf("."));
					saveFileNm = UUID.randomUUID()+ext;
					mf.transferTo(new File(path + saveFileNm));
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return saveFileNm;
	}
	
	
	public static int fileDelete(String imgName, String path) {
		int result = 0;
		File file = new File(path + imgName);
		
		if(file.exists()) {
			if(file.delete()) {
				System.out.println("파일 삭제 완료");
				result = 1;
			}else {
				System.out.println("파일 삭제 실패");
			}
		}
		return result;
		
	}
	
	

	public static JSONObject JsonToObjectParser(String jsonStr) {
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(jsonStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return obj;
	}
	

	
	public static String getSalt() {
		return BCrypt.gensalt();
	}
	
	public static String getBcryptPw(String salt, String pw) {
		return BCrypt.hashpw(pw, salt);
	}
	
	public static String htmlTagChg(String beforeText) {
		beforeText.replaceAll("<", "&lt");
		beforeText.replaceAll(">", "&gt");
		return beforeText;
	}

}
