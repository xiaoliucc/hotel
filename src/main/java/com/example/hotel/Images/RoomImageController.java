package com.example.hotel.Images;

import com.example.hotel.utils.AdminOnly;
import com.example.hotel.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/room-images")
public class RoomImageController {
    @Autowired
    private RoomImageService roomImageService;

    @PostMapping("/upload")
    @AdminOnly
    public R<RoomImage> uploadImage(@RequestParam Long roomId,
                                    @RequestParam MultipartFile image,
                                    @RequestParam(defaultValue = "false")Boolean isPrimary){
        try{
            RoomImage roomImage=roomImageService.addRoomImage(roomId,image,isPrimary);
            return R.ok("图片上传成功",roomImage);
        }catch (Exception e){
            return R.fail("图片上传失败"+e.getMessage());
        }
    }

    @GetMapping("/room/{roomId}")
    public R<List<RoomImage>> getRoomImages(@PathVariable Long roomId){
        List<RoomImage> roomImages=roomImageService.getRoomImages(roomId);
        return R.ok("获取图片列表成功",roomImages);
    }

    @DeleteMapping("/{imageId}")
    @AdminOnly
    public R<Void> deleteImage(@PathVariable Long imageId){
        roomImageService.deleteImage(imageId);
        return R.ok("图片删除成功");
    }

    @PutMapping("/{imageId}/set-primary")
    @AdminOnly
    public R<Void> setPrimaryImage(@PathVariable Long imageId){
        roomImageService.setPrimaryImage(imageId);
        return R.ok("设置图片为主图成功");
    }
}
