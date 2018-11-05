package com.nmys.story.utils;

import com.google.gson.Gson;
import com.nmys.story.constant.QiniuFileServerConstants;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadUtil {

    private static final Logger log = LoggerFactory.getLogger(UploadUtil.class);

    /**
     * 将pdf文件上传到bucket
     */
    public static String upload(String localFilePath) {
        //1.构建一个带指定区域对象的配置类
        Configuration cfg = new Configuration(QiniuFileServerConstants.ZONE_AREA);
        UploadManager manage = new UploadManager(cfg);
        //生成上传凭证,然后准备上传
        //默认不指定key的情况下,以文件内容的hash值作为文件名
        String key = null;
        //1.进行身份认证
        Auth upAuth = Auth.create(QiniuFileServerConstants.ACCESSKEY, QiniuFileServerConstants.SECRETKEY);
        String upToken = upAuth.uploadToken(QiniuFileServerConstants.BUCKET);

        try {
            Response response = manage.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException e) {
            Response r = e.response;
            log.info("七牛上传文件失败!" + r.toString());
        }
        return "";
    }

    public static String getURL(String key) {
        //1.构建公开空间访问链接
        try {
            String url = "";
            url = "http://" + QiniuFileServerConstants.DOMIAN_NAME + "/" + key;
            //2.进行私有授权签名
            Auth auth = Auth.create(QiniuFileServerConstants.ACCESSKEY, QiniuFileServerConstants.SECRETKEY);
            //自定义链接过期时间(单位s)
            long expireInSeconds = 3600;//1小时
            //生成下载链接
            String finalUrl = auth.privateDownloadUrl(url, expireInSeconds);
            return finalUrl;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

}
