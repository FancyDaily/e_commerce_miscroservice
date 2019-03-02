package com.e_commerce.miscroservice.commons.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class QRCodeUtil{
    private static final Logger logger = LoggerFactory.getLogger(QRCodeUtil.class);

    private static final int BLACK = 0xFF000000;

    private static final int WHITE = 0xFFFFFFFF;

    private static Hashtable<EncodeHintType, String> hints = new Hashtable<> (1);

    static {
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
    }
    
    public static BufferedImage getQR(String str, int width, int height) {
        BufferedImage img = null;
        try {
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = new MultiFormatWriter ().encode(str, BarcodeFormat.QR_CODE, width, height, hints);
            img = toBufferedImage(bitMatrix);
        } catch (Exception e) {
            logger.error("", e);
        }
        return img;
    }

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }
    
    public static BufferedImage addLogo(BufferedImage image, String logoFile) throws IOException {
    	int width = image.getWidth();
    	int height = image.getHeight();
    	
        Graphics2D graph = image.createGraphics();  
        /** 
         * 读取Logo图片 
         */  
        URL url = new URL(logoFile);  
        InputStream is = url.openConnection().getInputStream();  
        BufferedImage logo = ImageIO.read(is);  
        
        int widthLogo = logo.getWidth(null)>width*16/100?(width*16/100):logo.getWidth(null);
        int heightLogo = logo.getHeight(null)>height*16/100?(height*16/100):logo.getHeight(null);  
        
        int x = (width - widthLogo)/2;
        int y = (height - heightLogo)/2;
        
        graph.drawImage(logo, x, y, widthLogo, heightLogo, null);
        Shape shape = new RoundRectangle2D.Float(x, y, widthLogo, heightLogo, 6, 6);  
        graph.setStroke(new BasicStroke(3f));  
        graph.draw(shape);  
        graph.dispose();  
        
        logo.flush();  
        image.flush(); 
        
        return image;
    }

    public static void generate(String path, String logoFile, String text, int height, int width, String filename, String format) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            BitMatrix bitMatrix = new MultiFormatWriter ().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage image =  toBufferedImage(bitMatrix);
            image = addLogo(image, "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/logo/logo.png");
            
            File outputFile = new File(path + File.separator + filename + "." + format);
            OutputStream os = new FileOutputStream(outputFile);
            ImageIO.write(image, "png", os);
 //           MatrixToImageWriter.writeToStream(bitMatrix, format, os);
            IOUtils.closeQuietly(os);
        } catch (WriterException | IOException e) {
        	logger.error("qrcode generate fail", e);
        }
    }
    
    public static void main(String... aaa) {
        QRCodeUtil.generate("E:\\qrcode\\", "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/logo/logo.png", "http://www.yujiejie.com/static/app/seller_register.html?/id=1&type=2", 400, 400, "800000001", "png");
//        QrcodeUtils qrcodeUtils = new QrcodeUtils();  
//        qrcodeUtils.getFile(); 
    }
    public static HttpServletResponse getFile(HttpServletRequest request, HttpServletResponse response, String url, String fileName, int width, int high) {
    	String logoURL = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/logo/logo.png";
    	return getFile( request,  response,  url,  fileName,  width,  high,logoURL) ;  
    }
    
    public static HttpServletResponse getFile(HttpServletRequest request, HttpServletResponse response, String url, String fileName, int width, int high, String logoURL) {
    	try {            
    		 BitMatrix bitMatrix = new MultiFormatWriter ().encode(url, BarcodeFormat.QR_CODE, width, high, hints);
    		 BufferedImage image =  toBufferedImage(bitMatrix);
    		 if(StringUtils.isNotEmpty(logoURL)){
    			 image = addLogo(image, logoURL);
    		 }
    		 ByteArrayOutputStream out = new ByteArrayOutputStream();  
             ImageIO.write(image, "png", out);  
                      
             
             byte[] b = out.toByteArray();  

             InputStream fis = new ByteArrayInputStream(b); 
             byte[] buffer = new byte[fis.available()];           
             fis.read(buffer);           
             fis.close();            
             response.reset();            
             // 设置response的Header            
             fileName += ".png";
             response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"),"ISO-8859-1"));           
             response.addHeader("Content-Length", "" + b.length);           
             OutputStream toClient = new BufferedOutputStream(response.getOutputStream());           
             response.setContentType("application/octet-stream");            
             toClient.write(buffer);            
             toClient.flush();            
             toClient.close();        
    	} catch (IOException | WriterException ex) {
    		ex.printStackTrace();        
		}
        return response;
    }
    
    public void createCode() throws IOException, WriterException {
    	 BitMatrix bitMatrix = new MultiFormatWriter ().encode("wwwww", BarcodeFormat.QR_CODE, 1181, 1181, hints);
		 BufferedImage image =  MatrixToImageWriter.toBufferedImage(bitMatrix);
		 
		 ByteArrayOutputStream out = new ByteArrayOutputStream();  
         ImageIO.write(image, "png", out);  
         byte[] b = out.toByteArray();  
    	
    	BufferedImage buffImg = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();  
        g.drawBytes(b, 0, b.length, 0, 0);
    }      
}
