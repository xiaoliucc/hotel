// FileUploadUtil.java
package com.example.hotel.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUploadUtil {

    @Value("${file.upload-dir:./uploads/}")
    private String uploadDir;

    @Value("${image.base-url:http://localhost:8080/images/}")
    private String imageBaseUrl;

    /**
     * 保存上传的文件
     * @param file 上传的文件
     * @return 文件的访问URL
     * @throws IOException 文件操作异常
     */
    public String saveFile(MultipartFile file) throws IOException {
        // 验证文件是否为空
        if (file.isEmpty()) {
            throw new IOException("上传的文件为空");
        }

        // 确保上传目录存在
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new IOException("无法创建上传目录: " + uploadDir);
            }
        }

        // 获取原始文件名和扩展名
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // 生成唯一文件名，防止重名
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        // 构建完整文件路径
        Path filePath = Paths.get(uploadDir + File.separator + newFileName);

        try {
            // 保存文件到磁盘
            Files.copy(file.getInputStream(), filePath);

            // 返回访问URL（不包含本地路径分隔符）
            return imageBaseUrl + newFileName;
        } catch (IOException e) {
            throw new IOException("文件保存失败: " + e.getMessage(), e);
        }
    }

    /**
     * 删除文件
     * @param imageUrl 文件的URL
     * @return 是否删除成功
     */
    public boolean deleteFile(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return false;
        }

        try {
            // 从URL中提取文件名
            String fileName;
            if (imageUrl.startsWith(imageBaseUrl)) {
                fileName = imageUrl.substring(imageBaseUrl.length());
            } else {
                // 如果传的是相对路径，直接使用
                fileName = imageUrl;
            }

            // 构建文件路径
            Path filePath = Paths.get(uploadDir + File.separator + fileName);

            // 删除文件
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("删除文件失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 验证文件类型是否为图片
     * @param file 上传的文件
     * @return 是否是图片文件
     */
    public boolean isImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

//        String contentType = file.getContentType();
//        return contentType != null && contentType.startsWith("image/");
        return isSupportedFormat(file);
    }

    /**
     * 验证文件大小
     * @param file 上传的文件
     * @param maxSizeMB 最大大小（MB）
     * @return 是否在限制范围内
     */
    public boolean isFileSizeValid(MultipartFile file, long maxSizeMB) {
        if (file == null) {
            return false;
        }

        long maxSizeBytes = maxSizeMB * 1024 * 1024;
        return file.getSize() <= maxSizeBytes;
    }

    /**
     * 获取支持的图片格式
     * @return 支持的格式数组
     */
    public String[] getSupportedImageFormats() {
        return new String[]{"image/jpeg", "image/png", "image/gif", "image/webp"};
    }

    /**
     * 检查文件格式是否支持
     * @param file 上传的文件
     * @return 是否支持该格式
     */
//    public boolean isSupportedFormat(MultipartFile file) {
//        if (file == null) {
//            return false;
//        }
//
//        String contentType = file.getContentType();
//        if (contentType == null) {
//            return false;
//        }
//
//        for (String format : getSupportedImageFormats()) {
//            if (format.equals(contentType)) {
//                return true;
//            }
//        }
//        return false;
//    }
    public boolean isSupportedFormat(MultipartFile file) {
        if (file == null) {
            return false;
        }

        // 方式1：校验Content-Type（保留原有逻辑，增加兼容类型）
        String contentType = file.getContentType();
        String[] supportedContentTypes = getSupportedImageFormats();
        if (contentType != null) {
            for (String format : supportedContentTypes) {
                if (format.equals(contentType) || contentType.equals("image/pjpeg")) { // 兼容IE的jpeg类型
                    return true;
                }
            }
        }

        // 方式2：校验文件后缀名（新增逻辑，双重保障）
        String originalFileName = file.getOriginalFilename();
        if (originalFileName != null && originalFileName.contains(".")) {
            String suffix = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
            String[] supportedSuffixes = {".jpg", ".jpeg", ".png", ".gif", ".webp"}; // 支持的后缀
            for (String s : supportedSuffixes) {
                if (s.equals(suffix)) {
                    return true;
                }
            }
        }

        return false;
    }
}