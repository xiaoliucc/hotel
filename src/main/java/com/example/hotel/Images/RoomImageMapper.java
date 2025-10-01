package com.example.hotel.Images;

import com.example.hotel.Rooms.Room;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoomImageMapper {
    //插入
    @Insert("insert into room_images(room_id,image_url,is_primary,sort_order)"+"" +
            "values (#{roomId},#{imageUrl},#{isPrimary},#{sortOrder})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insert(RoomImage roomImage);

    //更具房间id查询
    @Select("select * from room_images where room_id=#{roomId} order by is_primary desc,sort_order asc")
    List<RoomImage> findByRoomId(Long roomId);

    //查询房间的主图
    @Select("select * from room_images where room_id=#{roomId} and is_primary=1 limit 1")
    RoomImage findPrimaryImage(Long roomId);

    //删除图片
    @Delete("delete from room_images where id=#{id}")
    void deleteById(Long id);

    //删除房间所有图片
    @Delete("delete from room_images where room_id= #{roomId}")
    void deleteByRoomId(Long roomId);

    //设置主图
    @Update("update room_images set is_primary=0 where room_id=#{roomId}")
    void clearPrimaryFlag(Long roomId);

    @Update("update room_images set is_primary=1 where id= #{id}")
    void setPrimary(Long id);

    @Select("select * from room_images where id= #{id}")
    RoomImage findById(Long imageId);
}

