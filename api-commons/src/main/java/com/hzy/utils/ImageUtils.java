package com.hzy.utils;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtils {

  private static String IMAGE_TYPE_GIF = "gif"; // 图形交换格式
  private static String IMAGE_TYPE_JPG = "jpg"; // 联合照片专家组
  private static String IMAGE_TYPE_JPEG = "jpeg"; // 联合照片专家组
  private static String IMAGE_TYPE_BMP = "bmp"; // 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
  private static String IMAGE_TYPE_PNG = "png"; // 可移植网络图形
  private static String IMAGE_TYPE_PSD = "psd"; // Photoshop的专用格式Photoshop

  /**
   * @param file     //文件对象
   * @param filePath //上传路径
   * @param fileName //文件名
   * @return 文件名
   */
  public static String fileUp(MultipartFile file, String filePath, String fileName) {
    String extName = ""; // 扩展名格式：
    try {
      if (file.getOriginalFilename().lastIndexOf(".") >= 0) {
        extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
      }
      copyFile(file.getInputStream(), filePath, fileName + extName).replaceAll("-", "");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return fileName + extName;
  }
  
  
  /**
   * @param file     //文件对象
   * @param filePath //上传路径
   * @param fileName //文件名
   * @return 文件名
   */
  public static String base64Up(String base64File, String filePath, String fileName) {
    String extName = ""; // 扩展名格式：
    try {
    	MimeTypes allTypes = MimeTypes.getDefaultMimeTypes(); 
    	String filetype=base64File.substring(base64File.indexOf(":")+1,
    			base64File.indexOf(";")); // 扩展名格式：
    	MimeType jpeg = allTypes.forName(filetype);  
    	extName = jpeg.getExtension();
    	base64File = base64File.replaceAll("data:[\\S]{0,};base64,", ""); 
    	// 解码，然后将字节转换为文件
        byte[] bytes = Base64.getDecoder().decode(base64File); // 将字符串转换为byte数组
    	InputStream input = new ByteArrayInputStream(bytes);
        copyFile(input, filePath, fileName + extName).replaceAll("-", "");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (MimeTypeException e) {
        e.printStackTrace();
    }
    return fileName + extName;
  }
  
  
  /**
   * 写文件到当前目录的upload目录中
   *
   * @param in
   * @param realName
   * @throws IOException
   */
  private static String copyFile(InputStream in, String dir, String realName) throws IOException {
    File file = new File(dir, realName);
    if (!file.exists()) {
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      file.createNewFile();
    }
    FileUtils.copyInputStreamToFile(in, file);
    return realName;
  }

  /**
   * 缩放图像： 按比例缩放
   *
   * @param srcImageFile 原图路径
   * @param result       存放路径
   * @param scale        缩放比例
   * @param flag         true：放大；false：缩小
   */
  public final static void scale(String srcImageFile, String result, int scale, boolean flag) {
    try {
      BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
      int width = src.getWidth(); // 得到源图宽
      int height = src.getHeight(); // 得到源图长
      if (scale <= 0) {
        scale = 1;
      }
      if (flag) {// 放大
        width = width * scale;
        height = height * scale;
      } else {// 缩小
        width = width / scale;
        height = height / scale;
      }
      Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
      BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics g = tag.getGraphics();
      g.drawImage(image, 0, 0, null); // 绘制缩小后的图
      g.dispose();
      ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 缩放图像： 按比例缩放
   *
   * @param file     MultipartFile文件
   * @param savePath 存放路径
   * @param ratio    缩放比例
   * @return String
   */
  public static String scale(String savePath, MultipartFile file, double ratio) {
    try {
      String extName = ""; // 扩展名格式：
      String dir = "";
      String filename = file.getOriginalFilename();
      String filetype = file.getContentType();
      if (filename.lastIndexOf(".") >= 0) {
        extName = filename.substring(filename.lastIndexOf("."));
      } else {
        filetype = file.getContentType();
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        MimeType jpeg = allTypes.forName(filetype);
        extName = jpeg.getExtension();
      }
      dir = DateUtil.getDays();
      String fileName = getUUID() + extName;
      BufferedImage src = ImageIO.read(file.getInputStream()); // 读入文件
      int width = src.getWidth(); // 得到源图宽
      int height = src.getHeight(); // 得到源图长
      if (ratio > 1) {
        ratio = 1.0;
      }
      width = Integer.parseInt(new java.text.DecimalFormat("0").format(width * ratio));
      height = Integer.parseInt(new java.text.DecimalFormat("0").format(height * ratio));
      Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
      BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics g = tag.getGraphics();
      g.drawImage(image, 0, 0, null); // 绘制缩小后的图
      g.dispose();
      File newFile = new File(savePath + dir, fileName);
      if (!newFile.exists()) {
        if (!newFile.getParentFile().exists()) {
          newFile.getParentFile().mkdirs();
        }
        newFile.createNewFile();
      }
      ImageIO.write(tag, extName.replace(".", ""), newFile); // 输出到文件流
      return dir + "/" + fileName;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } catch (MimeTypeException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 缩放图像： 按比例缩放
   *
   * @param file   MultipartFile文件
   * @param result 存放路径
   * @param ratio  缩放比例
   */
  public final static String scale(File file, double ratio) {
    try {
      String extName = ""; // 扩展名格式：
      String dir = "";
      String filename = file.getName();
      String filetype = filename.substring(filename.lastIndexOf("."), filename.length());
      dir = "F:/" + filetype + "/" + DateUtil.getDays();
      String fileName = getUUID() + extName;
      BufferedImage src = ImageIO.read(file); // 读入文件
      int width = src.getWidth(); // 得到源图宽
      int height = src.getHeight(); // 得到源图长
      if (ratio > 1) {
        ratio = 1.0;
      }
      width = Integer.parseInt(new java.text.DecimalFormat("0").format(width * ratio));
      height = Integer.parseInt(new java.text.DecimalFormat("0").format(height * ratio));
      Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
      BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics g = tag.getGraphics();
      g.drawImage(image, 0, 0, null); // 绘制缩小后的图
      g.dispose();
      File newFile = new File(dir, fileName);
      if (!newFile.exists()) {
        if (!newFile.getParentFile().exists()) {
          newFile.getParentFile().mkdirs();
        }
        newFile.createNewFile();
      }
      ImageIO.write(tag, "JPEG", newFile);// 输出到文件流
      return dir + "/" + fileName;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 缩放图像：按高度和宽度缩放
   *
   * @param srcImageFile 原图路径
   * @param result       保存路径
   * @param height       保存高度
   * @param width        保存宽度
   * @param bb           true:补白
   */
  public final static void scale2(String srcImageFile, String result, int height, int width, boolean bb) {
    try {
      double ratio = 0.0; // 缩放比例
      File f = new File(srcImageFile);
      BufferedImage bi = ImageIO.read(f);
      Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
      // 计算比例
      if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
        if (bi.getHeight() > bi.getWidth()) {
          ratio = (new Integer(height)).doubleValue() / bi.getHeight();
        } else {
          ratio = (new Integer(width)).doubleValue() / bi.getWidth();
        }
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
        itemp = op.filter(bi, null);
      }
      if (bb) {// 补白
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        if (width == itemp.getWidth(null)) {
          g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                  itemp.getWidth(null),
                  itemp.getHeight(null), Color.white, null);
        } else {
          g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null),
                  itemp.getHeight(null), Color.white, null);
        }
        g.dispose();
        itemp = image;
      }
      ImageIO.write((BufferedImage) itemp, "JPEG", new File(result));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 切割图像： 方法一：按指定起点坐标和宽高切割
   *
   * @param srcImageFile 原图路径
   * @param result       保存路径
   * @param x            起点x
   * @param y            起点y
   * @param width        截取宽度
   * @param height       截取高度
   */
  public final static void cut(String srcImageFile, String result, int x, int y, int width, int height) {
    try {
      // 读取源图像
      BufferedImage bi = ImageIO.read(new File(srcImageFile));
      int srcWidth = bi.getHeight(); // 源图宽度
      int srcHeight = bi.getWidth(); // 源图高度
      if (srcWidth > 0 && srcHeight > 0) {
        Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
        // 四个参数分别为图像起点坐标和宽高
        // 即: CropImageFilter(int x,int y,int width,int height)
        ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
        Image img = Toolkit.getDefaultToolkit()
                .createImage(new FilteredImageSource(image.getSource(), cropFilter));
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
        g.dispose();
        // 输出为文件
        ImageIO.write(tag, "JPEG", new File(result));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  // 角度
  private static final int[] angles = {0, 90, 180, 270};
  public static void main(String[] args) {
    cut2("D:/资料/文件/test2.jpg", "D:/资料/文件/test/", 2, 2);
  }


  /**
   * 切割图像：方法二：指定切片的行数和列数
   *
   * @param srcImageFile 原图路径
   * @param descDir      保存路径
   * @param rows         行数
   * @param cols         列数
   */
  public final static void cut2(String srcImageFile, String descDir, int rows, int cols) {
    try {
      if (rows <= 0 || rows > 20)
        rows = 2; // 切片行数
      if (cols <= 0 || cols > 20)
        cols = 2; // 切片列数
      // 读取源图像
      BufferedImage bi = ImageIO.read(new File(srcImageFile));
      int srcWidth = bi.getWidth(); // 源图宽度
      int srcHeight = bi.getHeight(); // 源图高度
      if (srcWidth > 0 && srcHeight > 0) {
        Image img;
        ImageFilter cropFilter;
        Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
        int destWidth = srcWidth; // 每张切片的宽度
        int destHeight = srcHeight; // 每张切片的高度
        // 计算切片的宽度和高度
        if (srcWidth % cols == 0) {
          destWidth = srcWidth / cols;
        } else {
          destWidth = (int) Math.floor(srcWidth / cols) + 1;
        }
        if (srcHeight % rows == 0) {
          destHeight = srcHeight / rows;
        } else {
          destHeight = (int) Math.floor(srcWidth / rows) + 1;
        }
        // 循环建立切片
        // 改进的想法:是否可用多线程加快切割速度
        for (int i = 0; i < rows; i++) {
          for (int j = 0; j < cols; j++) {
            // 四个参数分别为图像起点坐标和宽高
            // 即: CropImageFilter(int x,int y,int width,int height)
            cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
            img = Toolkit.getDefaultToolkit()
                    .createImage(new FilteredImageSource(image.getSource(), cropFilter));
            BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(img, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            // 输出为文件
            BufferedImage des1 = RotateImage.Rotate(tag, 90);
            ImageIO.write(des1, "JPEG", new File(descDir + "_r" + i + "_c" + j + ".jpg"));
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 切割图像：方法三：指定切片的宽度和高度
   *
   * @param srcImageFile 原图路径
   * @param descDir      保存路径
   * @param destWidth    切片宽度
   * @param destHeight   切片高度
   */
  public final static void cut3(String srcImageFile, String descDir, int destWidth, int destHeight) {
    try {
      if (destWidth <= 0)
        destWidth = 200; // 切片宽度
      if (destHeight <= 0)
        destHeight = 150; // 切片高度
      // 读取源图像
      BufferedImage bi = ImageIO.read(new File(srcImageFile));
      int srcWidth = bi.getHeight(); // 源图宽度
      int srcHeight = bi.getWidth(); // 源图高度
      if (srcWidth > destWidth && srcHeight > destHeight) {
        Image img;
        ImageFilter cropFilter;
        Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
        int cols = 0; // 切片横向数量
        int rows = 0; // 切片纵向数量
        // 计算切片的横向和纵向数量
        if (srcWidth % destWidth == 0) {
          cols = srcWidth / destWidth;
        } else {
          cols = (int) Math.floor(srcWidth / destWidth) + 1;
        }
        if (srcHeight % destHeight == 0) {
          rows = srcHeight / destHeight;
        } else {
          rows = (int) Math.floor(srcHeight / destHeight) + 1;
        }
        // 循环建立切片
        // 改进的想法:是否可用多线程加快切割速度
        for (int i = 0; i < rows; i++) {
          for (int j = 0; j < cols; j++) {
            // 四个参数分别为图像起点坐标和宽高
            // 即: CropImageFilter(int x,int y,int width,int height)
            cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
            img = Toolkit.getDefaultToolkit()
                    .createImage(new FilteredImageSource(image.getSource(), cropFilter));
            BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(img, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            // 输出为文件
            ImageIO.write(tag, "JPEG", new File(descDir + "_r" + i + "_c" + j + ".jpg"));
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 3-图像类型转换
   *
   * @param srcImageFile  原图路径
   * @param formatName    更改类型
   * @param destImageFile 保存路径
   */
  public final static void convert(String srcImageFile, String formatName, String destImageFile) {
    try {
      File f = new File(srcImageFile);
      f.canRead();
      f.canWrite();
      BufferedImage src = ImageIO.read(f);
      ImageIO.write(src, formatName, new File(destImageFile));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 4-彩色转黑白
   *
   * @param srcImageFile  原图路径
   * @param destImageFile 保存路径
   */
  public final static void gray(String srcImageFile, String destImageFile) {
    try {
      BufferedImage src = ImageIO.read(new File(srcImageFile));
      ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
      ColorConvertOp op = new ColorConvertOp(cs, null);
      src = op.filter(src, null);
      ImageIO.write(src, "JPEG", new File(destImageFile));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 5-给图片添加文字水印
   *
   * @param pressText     水印文字
   * @param srcImageFile  原图路径
   * @param destImageFile 保存路径
   * @param fontName      字体名称
   * @param fontStyle     字体样式
   * @param color         字体颜色
   * @param fontSize      字体大小
   * @param x             字体位置x
   * @param y             字体位置y
   * @param alpha         水印透明度
   */
  public final static void pressText(String pressText, String srcImageFile, String destImageFile, String fontName,
                                     int fontStyle, Color color, int fontSize, int x, int y, float alpha) {
    try {
      File img = new File(srcImageFile);
      Image src = ImageIO.read(img);
      int width = src.getWidth(null);
      int height = src.getHeight(null);
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics2D g = image.createGraphics();
      g.drawImage(src, 0, 0, width, height, null);
      g.setColor(color);
      g.setFont(new Font(fontName, fontStyle, fontSize));
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
      // 在指定坐标绘制水印文字
      g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);
      g.dispose();
      ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));// 输出到文件流
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 5-给图片添加文字水印
   *
   * @param pressText     水印文字
   * @param srcImageFile  原图路径
   * @param destImageFile 保存路径
   * @param fontName      字体名称
   * @param fontStyle     字体样式
   * @param color         字体颜色
   * @param fontSize      字体大小
   * @param x             字体位置x
   * @param y             字体位置y
   * @param alpha         水印透明度
   */
  public final static void pressText2(String pressText, String srcImageFile, String destImageFile, String fontName,
                                      int fontStyle, Color color, int fontSize, int x, int y, float alpha) {
    try {
      File img = new File(srcImageFile);
      Image src = ImageIO.read(img);
      int width = src.getWidth(null);
      int height = src.getHeight(null);
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics2D g = image.createGraphics();
      g.drawImage(src, 0, 0, width, height, null);
      g.setColor(color);
      g.setFont(new Font(fontName, fontStyle, fontSize));
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
      // 在指定坐标绘制水印文字
      g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);
      g.dispose();
      ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 6-给图片添加图片水印
   *
   * @param pressImg      水印图片路径
   * @param srcImageFile  原图路径
   * @param destImageFile 保存路径
   * @param x             水印位置x
   * @param y             水印位置y
   * @param alpha         水印透明度
   */
  public final static void pressImage(String pressImg, String srcImageFile, String destImageFile, int x, int y,
                                      float alpha) {
    try {
      File img = new File(srcImageFile);
      Image src = ImageIO.read(img);
      int wideth = src.getWidth(null);
      int height = src.getHeight(null);
      BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
      Graphics2D g = image.createGraphics();
      g.drawImage(src, 0, 0, wideth, height, null);
      // 水印文件
      Image src_biao = ImageIO.read(new File(pressImg));
      int wideth_biao = src_biao.getWidth(null);
      int height_biao = src_biao.getHeight(null);
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
      g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2, wideth_biao, height_biao,
              null);
      // 水印文件结束
      g.dispose();
      ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  
  
  //链接url下载图片
  public static void downloadPicture(String urlList,String imageName) {
      URL url = null;
      int imageNumber = 0;
      try {
          url = new URL(urlList);
          HttpURLConnection con = (HttpURLConnection) url.openConnection();
          con.setConnectTimeout(5000);
          con.setReadTimeout(5000);
          con.setRequestMethod("GET");
          //con.setRequestProperty("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
          con.setRequestProperty("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3418.2 Mobile Safari/537.36");
          DataInputStream dataInputStream = new DataInputStream(con.getInputStream());
          //String imageName =  "D:/test111111111.jpg";
          FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
          ByteArrayOutputStream output = new ByteArrayOutputStream();
          byte[] buffer = new byte[1024];
          int length;
          while ((length = dataInputStream.read(buffer)) > 0) {
              output.write(buffer, 0, length);
          }
          byte[] context=output.toByteArray();
          fileOutputStream.write(output.toByteArray());
          dataInputStream.close();
          fileOutputStream.close();
      } catch (MalformedURLException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
  
  
  
  
  

  public final static int getLength(String text) {
    int length = 0;
    for (int i = 0; i < text.length(); i++) {
      if (new String(text.charAt(i) + "").getBytes().length > 1) {
        length += 2;
      } else {
        length += 1;
      }
    }
    return length / 2;
  }

  public static String getUUID() {
    String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
    return uuid;
  }
}
