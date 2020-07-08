package com.hzy.controller;

import com.hzy.service.base.BaseService;
import com.hzy.service.file.UploadService;
import com.hzy.utils.ImageUtils;
import com.hzy.utils.ResultUtil;
import com.hzy.utils.UnZipUtil;
import java.io.File;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName UploadController
 * @Description TODO
 * @Author Chensb
 * @Date 2020/3/18 15:53
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController extends BaseService {
	@Autowired
	private UploadService uploadService;

	/**
	 * TODO 描述:保存文件
	 *
	 * @param files
	 * @param type
	 * @return java.lang.Object
	 * @author Chensb
	 * @date 2020/3/19 14:00
	 */
	@RequestMapping(value = "/saves")
	public Object saves(@RequestParam("files") MultipartFile[] files, Integer type) {
		return uploadService.saveFiles(files, type);
	}

	/**
	 * TODO 描述:保存base64位图片
	 *
	 * @param base64File
	 * @return java.util.Map
	 * @author Chensb
	 * @date 2020/3/19 13:59
	 */
	@RequestMapping(value = "/base64_save")
	public Map base64Save(String base64File) {
		return uploadService.base64Save(base64File);
	}

	/**
	 * TODO 描述:保存网络图片
	 *
	 * @param url 图片链接
	 * @return java.util.Map
	 * @author Chensb
	 * @date 2020/3/19 14:00
	 */
	@RequestMapping(value = "/save_intnet")
	public Map saveIntnet(String url) {
		return ResultUtil.getResult(uploadService.saveIntnet(url));
	}

	/**
	 * TODO 描述:保存微信素材
	 *
	 * @param media_id 微信临时素材ID,多个用逗号拼接
	 * @param type 素材类别
	 * @return java.util.Map
	 * @author Chensb
	 * @date 2020/6/15 10:52
	 */
	@RequestMapping(value = "/fetchTmpFile")
	public Map fetchTmpFile(String media_id, String type) {
		return uploadService.fetchTmpFile(media_id, type);
	}

	/**
	 * @param file 前端更新
	 * @return java.lang.Object
	 * @作者 Chensb
	 * @描述 TODO 保存图片
	 * @日期 2018/10/22 17:49
	 */
	@ResponseBody
	@RequestMapping(value = "/upload_test_project")
	public Object saveTestProjectFile(@RequestParam("project") MultipartFile file, String savePath) {
		try {
			if (!file.getOriginalFilename().toLowerCase().endsWith(".zip")) {
				System.out.println("非zip文件！");
				return ResultUtil.fail("非zip文件！");
			}
			String fileId = file.getOriginalFilename().split("\\.")[0];
			String filePath = ImageUtils.fileUp(file, savePath, fileId);
			filePath = savePath + filePath;
			UnZipUtil.unZipFiles(new File(filePath), savePath);
			return ResultUtil.success("保存成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultUtil.fail("保存出错");
		}
	}

}
