package com.herokuapp.lzqwebsoft.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.herokuapp.lzqwebsoft.pojo.Image;
import com.herokuapp.lzqwebsoft.service.ImageService;
import com.herokuapp.lzqwebsoft.util.QiniuUtil;

/**
 * 工具控制器，用于提供一些工具操作
 * 
 * @author ZQLUO
 * 
 */
@Controller
@RequestMapping("/tools")
public class ToolsController {
    @Autowired
    private ImageService imageService;

    // 七牛云图片文件批量上传备份工具
    @ResponseBody
    @RequestMapping(value = "/images_upload")
    public List<Map> imageUpload() {
        List<Map> list = new ArrayList<Map>();

        List<Image> images = imageService.getAllImages();
        if (images != null && images.size() > 0) {
            for (Image image : images) {
                Map<String, String> map = new HashMap<String, String>();
                String status = "不需要上传";
                if (image.getQiniuKey() == null || image.getQiniuKey().trim().length() == 0) {
                    image = QiniuUtil.upload(image);  // 七牛云上传图片
                    if (image.getQiniuKey() != null && image.getQiniuKey().trim().length() > 0) {
                        imageService.update(image);
                        status = "上传成功";
                    } else {
                        status = "上传失败";
                    }
                }
                map.put("id", image.getId());
                map.put("filename", image.getFileName());
                map.put("qiniuKey", image.getQiniuKey());
                map.put("status", status);
                list.add(map);
            }
        }
        return list;
    }
}