package com.e_commerce.miscroservice.commons.helper.util.application.conver;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/20 0:51
 * @Copyright 玖远网络
 */
public class DownFilesUtils {

    public static HttpServletResponse download(String path, HttpServletResponse response) {
        try {
//            InputStream fis=Object.class.getResourceAsStream("/客户管理模板.xlsx");

            // path是指欲下载的文件的路径。
            File file = new File(path);

            System.out.println("AbsolutePath="+ file.getAbsolutePath());
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));

            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }
}
