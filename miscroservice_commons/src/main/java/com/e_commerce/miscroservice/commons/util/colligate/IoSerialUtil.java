package com.e_commerce.miscroservice.commons.util.colligate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 
 * 功能描述:序列化和反序列化
 * 模块:
 * 项目:
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:2017年7月7日 下午4:04:18
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class IoSerialUtil {
	
    /**
     * 对象序列化
     * @param serializedObject
     * @return
     */
    public static byte[] serialize( Serializable serializedObject ){
        byte[] results = null;       
       
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try{
            baos = new ByteArrayOutputStream( );
            oos = new ObjectOutputStream( baos );
            oos.writeObject( serializedObject );           
            results = baos.toByteArray( );
            baos.flush();
            oos.flush();
        }catch( IOException e ){
            e.printStackTrace();
        }finally{
            closeStream( baos );
            closeStream( oos );
        }
       
        return results;
    }
   
    /**
     * 反序列化
     * @param bytes
     * @return
     */
    public static Serializable deserialize( byte[] bytes ){
        Serializable serializedObject = null;
       
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;       
        try{
            bais = new ByteArrayInputStream( bytes );
            ois = new ObjectInputStream( bais );
            serializedObject = (Serializable)ois.readObject( );
        }catch( IOException e ){
            e.printStackTrace();
        }catch( ClassNotFoundException e ){
            e.printStackTrace();
        }finally{
            closeStream( ois );
            closeStream( bais );
        }
       
        return serializedObject;
    }
   
    /**
     * 关闭流
     * @param os
     */
    public static void closeStream( Closeable os ){
        try{
            if( os != null ) os.close();
        }catch( IOException e ){
            e.printStackTrace();
        }
    }
    

}
