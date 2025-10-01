package com.example.hotel.Images;

import com.example.hotel.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class RoomImageService {
    @Autowired
    private RoomImageMapper roomImageMapper;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    public RoomImage addRoomImage(Long roomId, MultipartFile file,Boolean isPrimary) throws IOException{
        if(!fileUploadUtil.isImageFile(file)){
            throw new RuntimeException("上传的文件不是图片");
        }
        if(!fileUploadUtil.isFileSizeValid(file,5)){
            throw new RuntimeException("上传的文件大小超过限制");
        }
        String imageUrl=fileUploadUtil.saveFile(file);

        if(isPrimary){
            roomImageMapper.clearPrimaryFlag(roomId);
        }
        RoomImage roomImage=new RoomImage(roomId,imageUrl,isPrimary);
        roomImage.setSortOrder(0);
        roomImageMapper.insert(roomImage);
        return roomImage;
    }

    public List<RoomImage>getRoomImages(Long roomId){
        return roomImageMapper.findByRoomId(roomId);
    }

    public RoomImage getPrimaryImage(Long roomId){
        return roomImageMapper.findPrimaryImage(roomId);
    }

    public void deleteImage(Long imageId){
       RoomImage image=roomImageMapper.findById(imageId);
        if (image != null) {
            fileUploadUtil.deleteFile(image.getImageUrl());
            roomImageMapper.deleteById(imageId);
        }
    }

    public void setPrimaryImage(Long imageId){
        RoomImage image=roomImageMapper.findById(imageId);
        if (image != null) {
            roomImageMapper.clearPrimaryFlag(image.getRoomId());
            roomImageMapper.setPrimary(imageId);
        }
    }
}
