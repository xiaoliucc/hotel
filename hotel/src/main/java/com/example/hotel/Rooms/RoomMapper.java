package com.example.hotel.Rooms;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoomMapper {

//    /* ① 定义映射规则，id = "roomMap" 复用 */
//    @Results(id = "roomMap",value = {
//            @Result(column = "room_number",property = "roomNumber"),
//            @Result(column = "create_time",property = "createTime")
//    })
//    /*全表查询*/
//    @ResultMap("roomMap")
    @Select("select * from rooms order by id desc")
    List<Room> findAll();

    /*查询房间：id*/
//    @ResultMap("roomMap")
    @Select("select * from rooms where id=#{id}")
    Room findById(Long id);

    /*新增房间*/
    @Insert("insert into rooms (room_number,type,price,status)"+
            " values(#{roomNumber},#{type},#{price},#{status})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insert(Room room);


    /*更新房间信息*/
    @Update("update rooms set room_number=#{roomNumber},type=#{type},"+"" +
            "price=#{price},status=#{status} where id=#{id}")
    void update(Room room);

    /*删除房间*/
    @Delete("delete from rooms where id=#{id}")
    void deleteById(Long id);
}
