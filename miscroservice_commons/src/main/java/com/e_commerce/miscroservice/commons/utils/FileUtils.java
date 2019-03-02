package com.e_commerce.miscroservice.commons.utils;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/27 22:34
 * @Copyright 玖远网络
 */
public class FileUtils{

    private static Log logger = Log.getInstance(FileUtils.class);


    public static void createDir(String dir) {
        File file = new File (dir);
        if (! file.exists () && !file.isDirectory ()) {
            file.mkdir ();
        }
    }



    public static InputStream yjjLog() {
        InputStream download = HttpUtils.download ("http://www.yujiejie.com/images/public/logo.png");
        if (download == null) {
            logger.warn ("俞姐姐log地址是空");
        }
        return download;
    }


    /**
     * 存放在阿里云上的文件名
     *
     * @param phone   phone
     * @param version 版本号,做个标识,以后好维护
     * @param prex    前缀
     * @return java.lang.String
     * @author Charlie
     * @date 2018/11/5 11:57
     */
    public static String generateFileNameOnOss(String phone, int version, String prex) {
        return new StringBuilder ().append (prex).append (version).append (phone).append (".jpg").toString ();
    }

    public static void main(String[] args) {
        yjjLog();
    }

//=========================//============================//
    /**
     * 阿里云图片地址转为服务器本地地址
     * wxaProductShareImageTempDir
     */
    public static String aliImgToServerImg(String aliImage, HttpServletRequest request) {
        //阿里云图片地址转为服务器本地地址
//		String s = UUID.randomUUID().toString();
//        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
//        ServletContext servletContext = webApplicationContext.getServletContext();
//        String filePath = servletContext.getRealPath("/wxaProductShareImageTempDir/");
        String filePath = System.getProperty("java.io.tmpdir") + "/wxaProductShareImageTempDir/";

        createfile(filePath);

        //下载到指定文件夹
        String[] split = aliImage.split("/");
        String imageName = split[split.length - 1];
        File file = new File(filePath+"/"+imageName);
        InputStream inputStream = getInputStreamByGet(aliImage);
        saveData(inputStream, file);
        String webRoot = getWebBaseUrl(request,"https");
        String serverImg  = webRoot + "/wxaProductShareImageTempDir/"+ imageName;
        return serverImg;
    }


    /**
     * 或许项目根路径
     * 格式：http://dev.yujiejie.com:8080/item/
     * @param request
     * @param scheme  http or https
     *
     * @return
     */

    public static String getWebBaseUrl(HttpServletRequest request,String scheme) {
        StringBuffer webBaseUrl = new StringBuffer();
        webBaseUrl.append(scheme);
        webBaseUrl.append("://");
        webBaseUrl.append(request.getServerName());
        int port = request.getServerPort();
        if(port != 80 && port != 0 ){
            webBaseUrl.append(":");
            webBaseUrl.append(port);
        }
        String path = request.getContextPath();
        if(StringUtils.isNotEmpty(path)){
            webBaseUrl.append("/");
            webBaseUrl.append(path);
        }
        //webBaseUrl.append("/");
        return webBaseUrl.toString();
    }
    // 将服务器响应的数据流存到本地文件
    public static void saveData(InputStream is, File file) {
        try (BufferedInputStream bis = new BufferedInputStream(is);
             BufferedOutputStream bos = new BufferedOutputStream(
                     new FileOutputStream(file));) {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 通过get请求得到读取器响应数据的数据流
    public static InputStream getInputStreamByGet(String url) {
        InputStream in = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                return HttpUtils.cloneInputStream(in);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            IOUtils.close(in);
        }
        return null;
    }
    //创建临时文件夹
    public static void createfile(String filename){
        File file =new File(filename);
        //如果文件夹不存在则创建
        if (!file .exists() && !file .isDirectory())
        {
            file .mkdir();
        }
    }

    /**
     * 将image输出成文件
     *
     * @param image          image
     * @param targetFilePath 输出文件,eg D:\test.jpg
     * @return boolean
     * @author Charlie
     * @date 2018/7/16 18:42
     */
    public static boolean bufferedImg2ImgFile(BufferedImage image, String targetFilePath) throws IOException {
        String path = targetFilePath.split ("\\.")[1];
        File file = new File (targetFilePath);
        return ImageIO.write (
                image,
                path,
                file);
    }
    /**
     * 图片拼接
     *
     * @param isHorizontal isHorizontal
     * @param imgs         imgs
     * @author Charlie
     * @date 2018/7/16 18:05
     */
    public static BufferedImage mergeImage(boolean isHorizontal, BufferedImage... imgs) {
        // 生成新图片
        BufferedImage destImage;
        // 计算新图片的长和高
        int allw = 0, allh = 0, allwMax = 0, allhMax = 0;
        // 获取总长、总宽、最长、最宽
        for (int i = 0; i < imgs.length; i++) {
            BufferedImage img = imgs[i];
            allw += img.getWidth ();
            allh += img.getHeight ();
            if (img.getWidth () > allwMax) {
                allwMax = img.getWidth ();
            }
            if (img.getHeight () > allhMax) {
                allhMax = img.getHeight ();
            }
        }
        // 创建新图片
        if (isHorizontal) {
            destImage = new BufferedImage (allw, allhMax, BufferedImage.TYPE_INT_RGB);
        }
        else {
            destImage = new BufferedImage (allwMax, allh, BufferedImage.TYPE_INT_RGB);
        }
        // 合并所有子图片到新图片
        int wx = 0, wy = 0;
        for (int i = 0; i < imgs.length; i++) {
            BufferedImage img = imgs[i];
            int w1 = img.getWidth ();
            int h1 = img.getHeight ();
            // 从图片中读取RGB
            int[] imagearrayone = new int[w1 * h1];
            // 逐行扫描图像中各个像素的RGB到数组中
            imagearrayone = img.getRGB (0, 0, w1, h1, imagearrayone, 0, w1);
            if (isHorizontal) {
                // 水平方向合并,设置上半部分或左半部分的RGB
                destImage.setRGB (wx, 0, w1, h1, imagearrayone, 0, w1);
            }
            else {
                // 垂直方向合并,设置上半部分或左半部分的RGB
                destImage.setRGB (0, wy, w1, h1, imagearrayone, 0, w1);
            }
            wx += w1;
            wy += h1;
        }
        return destImage;
    }
    /**
     * 小图片贴到大图片形成一张图(合成)
     *
     * @param bigImg   大图
     * @param smallImg 小图
     * @param x 小图坐标
     * @param y 大图坐标
     * @author Charlie
     * @date 2018/7/16 17:59
     */
    public static BufferedImage overlapImage(BufferedImage bigImg, BufferedImage smallImg, int x, int y) {
        try {
            Graphics2D g = bigImg.createGraphics ();
            g.drawImage (smallImg, x, y, smallImg.getWidth (), smallImg.getHeight (), null);
            g.dispose ();
            return bigImg;
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
    }
    /**
     * 将商品详情生成图片
     *
     * @param width 生成图片的宽度
     * @return java.awt.image.BufferedImage
     * @author Charlie
     * @date 2018/7/16 20:02
     */
    public static BufferedImage generateProductShareDescriptionImg( Integer width, Integer height, Integer unableWith) {
        //============================= 配置 =============================
        BufferedImage image = new BufferedImage (width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d = image.createGraphics ();
        //消除锯齿
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
        g2d.setClip (0, 0, width, height);
        //背景色
        g2d.setColor (Color.white);
        g2d.fillRect (0, 0, width, height);
        g2d.dispose ();
        return image;
    }

    /**
     * 缩放图片
     *
     * @param src src
     * @param w w
     * @param h h
     * @return void
     * @author Charlie
     * @date 2018/7/18 14:59
     */
    public static BufferedImage zoomImage(BufferedImage src,int w,int h){
        double wr=0,hr=0;
        //获取缩放比例
        wr=w*1.0/src.getWidth();
        hr=h*1.0 / src.getHeight();

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        try {
            return (BufferedImage) ato.filter(src, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * 关闭流
     *
     * @param cs 流
     * @return void
     * @author Charlie
     * @date 2018/7/17 15:42
     */
    public static void closeStream(Closeable... cs) {
        for (Closeable stream : cs) {
            if (stream != null) {
                try {
                    stream.close ();
                } catch (IOException e) {
                    //ignore...
                }
            }
        }
    }
    /**
     * 将图片路径加载成imgBuffered
     *
     * @param in 图片文件资源
     * @return java.awt.image.BufferedImage
     * @author Charlie
     * @date 2018/7/16 19:02
     */
    public static BufferedImage imgFile2BufferedImg(InputStream in) throws IOException {
        return ImageIO.read (in);
    }

    /**
     * 删除合成图
     *
     * @param directory 合成图的临时目录
     * @return void
     * @author Charlie
     * @date 2018/7/17 11:40
     */
    public static void deleteTempFile(File directory) {
        if (!ObjectUtils.isEmpty (directory) && directory.exists ()) {
            deleteFile(directory);
        }
    }
    //删除文件
    public static void deleteFile(File file) {
        if (file.exists()) {//判断文件是否存在
            if (file.isFile()) {//判断是否是文件
                file.delete();//删除文件
            } else if (file.isDirectory()) {//否则如果它是一个目录
                File[] files = file.listFiles();//声明目录下所有的文件 files[];
                for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件
                    deleteFile(files[i]);//把每个文件用这个方法进行迭代
                }
                file.delete();//删除文件夹
            }
        } else {
            System.out.println("所删除的文件不存在");
        }
    }






}
