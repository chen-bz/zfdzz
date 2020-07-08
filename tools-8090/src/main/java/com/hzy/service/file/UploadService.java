package com.hzy.service.file;

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

	/**
	 * TODO 描述:保存文件
	 *
	 * @param files
	 * @param type
	 * @return java.lang.Object
	 * @author Chensb
	 * @date 2020/3/19 14:00
	 */
	Map saveFiles(MultipartFile[] files, Integer type);

	/**
	 * TODO 描述:保存base64位图片
	 *
	 * @param base64File
	 * @return java.util.Map
	 * @author Chensb
	 * @date 2020/3/19 13:59
	 */
	Map base64Save(String base64File);

	/**
	 * TODO 描述:保存远程图片
	 *
	 * @param url 图片链接
	 * @return java.lang.String
	 * @author Chensb
	 * @date 2020/3/19 13:56
	 */
	String saveIntnet(String url);

	/**
	 * TODO 描述:保存微信素材
	 * 
	 * @param media_ids
	 * @param type
	 * @return java.lang.String
	 * @author Chensb
	 * @date 2020/6/15 10:51
	 */
	Map fetchTmpFile(String media_ids, String type);

}
