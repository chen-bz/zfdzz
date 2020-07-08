package com.hzy.service.file.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.hzy.service.base.BaseService;
import com.hzy.service.file.UploadService;
import com.hzy.service.weixin.WeixinService;
import com.hzy.utils.DateUtil;
import com.hzy.utils.ImageUtils;
import com.hzy.utils.ResultUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName UploadServiceImpl
 * @Description TODO 图片上传相关
 * @Author Chensb
 * @Date 2020/3/19 13:55
 * @Version 1.0
 */
@Service
public class UploadServiceImpl extends BaseService implements UploadService {

	@Autowired
	private WeixinService weixinService;

	/**
	 * TODO 描述:保存文件
	 *
	 * @param files
	 * @param type
	 * @return java.lang.Object
	 * @author Chensb
	 * @date 2020/3/19 14:00
	 */
	@Override
	public Map saveFiles(MultipartFile[] files, Integer type) {
		try {
			String savePath = this.getSetting("sys_setting", "file_save_path");
			String prefix = this.getSetting("sys_setting", "img_prefix");
			String dayFolder = DateUtil.getDays();
			JSONArray array = new JSONArray();
			for (MultipartFile multipartFile : files) {
				JSONObject ob = new JSONObject();
				String fileId = new Date().getTime() + "" + ((int) (Math.random() * 89) + 9);
				String filePath = ImageUtils.fileUp(multipartFile, savePath + dayFolder, fileId);
				ob.put("url", dayFolder + "/" + filePath);
				if (type != null) {
					String prefixs = prefix.replaceAll("http", "https");
					ob.put("urls", prefixs + "/" + dayFolder + "/" + filePath);
				} else {
					ob.put("urls", prefix + "/" + dayFolder + "/" + filePath);
				}
				//files.add(prefix+"/"+dayFolder + "/" + filePath);
				array.add(ob);
			}
			return ResultUtil.getResult("保存成功", array);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultUtil.fail("保存出错");
		}
	}

	/**
	 * TODO 描述:保存base64位图片
	 *
	 * @param base64File
	 * @return java.util.Map
	 * @author Chensb
	 * @date 2020/3/19 13:59
	 */
	@Override
	public Map base64Save(String base64File) {
		try {
			Map map = new HashMap();
			String savePath = this.getSetting("sys_setting", "file_save_path");
			String prefix = this.getSetting("sys_setting", "img_prefix");
			String dayFolder = DateUtil.getDays();
			String fileId = new Date().getTime() + "" + ((int) (Math.random() * 89) + 9);
			String filePath = ImageUtils.base64Up(base64File, savePath + dayFolder, fileId);
			map.put("url", dayFolder + "/" + filePath);
			map.put("urls", prefix + "/" + dayFolder + "/" + filePath);
			return ResultUtil.getResult("保存成功", map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultUtil.fail("保存出错");
		}
	}

	/**
	 * TODO 描述:保存远程图片
	 *
	 * @param url 图片链接
	 * @return java.util.Map
	 * @author Chensb
	 * @date 2020/3/19 13:56
	 */
	@Override
	public String saveIntnet(String url) {
		try {
			List<String> files = new ArrayList<String>();
			String savePath = this.getSetting("sys_setting", "file_save_path");
			String hz = "jpg";
			String[] last = url.split("=");
			if (last.length > 1) {
				hz = last[1];
			}
			if ("jpg|png|jpeg|gif|JPG|PNG|JPEG|GIF".indexOf(hz) == -1) {
				hz = "jpg";
			}
			String dayFolder = DateUtil.getDays();
			String fileId = new Date().getTime() + "" + ((int) (Math.random() * 89) + 9) + "." + hz;
			String paths = savePath + dayFolder + "/" + fileId;
			File file = new File(savePath + dayFolder, fileId);
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			ImageUtils.downloadPicture(url, paths);
			return dayFolder + "/" + fileId;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * TODO 描述:保存微信临时素材接口
	 *
	 * @param media_ids
	 * @param type
	 * @return void
	 * @author Chensb
	 * @date 2020/6/15 10:45
	 */
	public Map fetchTmpFile(String media_ids, String type) {
		//定义两个成员变量常量
		//获取临时素材(视频不能使用https协议)
		String GET_TMP_MATERIAL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
		//获取临时素材(视频)
		String GET_TMP_MATERIAL_VIDEO = "http://api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";

		//获取微信服务器中生成的媒体文件

		//由于视频使用的是http协议，而图片、语音使用http协议，故此处需要传递media_id和type
		try {
			String token = weixinService.getWeixinAccessToken();
			String[] mediaIds = media_ids.split(",");
			JSONArray imgs = new JSONArray();
			for (int i = 0; i < mediaIds.length; i++) {
				String media_id = mediaIds[i];
				String url = null;
				//视频是http协议
				if ("video".equalsIgnoreCase(type)) {
					url = String.format(GET_TMP_MATERIAL_VIDEO, token, media_id);
				} else {
					url = String.format(GET_TMP_MATERIAL, token, media_id);
				}
				URL u = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
				conn.setRequestMethod("POST");
				conn.connect();
				BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
				String content_disposition = conn.getHeaderField("content-disposition");
				//微信服务器生成的文件名称
				String file_name = "";
				String[] content_arr = content_disposition.split(";");
				if (content_arr.length == 2) {
					String tmp = content_arr[1];
					int index = tmp.indexOf("\"");
					file_name = tmp.substring(index + 1, tmp.length() - 1);
				}

				//生成不同文件名称
				String savePath = this.getSetting("sys_setting", "file_save_path");
				String dir = DateUtil.getDays();
				File file = new File(savePath + dir + "/" + file_name);
				if (!file.exists()) {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					file.createNewFile();
				}
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
				byte[] buf = new byte[2048];
				int length = bis.read(buf);
				while (length != -1) {
					bos.write(buf, 0, length);
					length = bis.read(buf);
				}
				bos.close();
				bis.close();

				JSONObject ob = new JSONObject();
				ob.put("url", dir + "/" + file_name);
				ob.put("urls", this.getSetting("sys_setting", "img_prefix") + "/" + dir + "/" + file_name);
				imgs.add(ob);
			}
			return ResultUtil.getResult(imgs);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResultUtil.fail("上传失败");
	}
}
