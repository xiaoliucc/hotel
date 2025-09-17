package com.example.hotel.Orders;

import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;


@Mapper
public interface OrderMapper {
    /* 查某用户全部订单 */

    @Select("select * from orders where user_id=#{userId} order by id desc")
    List<Order> findByUser(Long userId);

    @Select("select * from orders where id=#{id}")
    Order findById(Long id);

    /*查看全部订单*/
    @Select("select * from orders")
    List<Order> getAllOrders();

    /* 查某房间在指定日期范围内已确认的订单数 */
    @Select("select count(*) from orders " +
            "where room_id=#{roomId} and status='BOOKED' " +
            "and (check_in < #{end} and check_out>#{start})")
    int countBooked(@Param("roomId") Long roomId,
                    @Param("start")LocalDate start,
                    @Param("end") LocalDate end);

    @Insert("insert into orders(user_id,room_id,check_in,check_out,status) " +  // 增加空格
            "values(#{userId},#{roomId},#{checkIn},#{checkOut},'BOOKED')")  // 修复拼写错误chekIn->checkIn，增加右括号
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insert(Order order);

    @Update("update orders set status=#{status} where id=#{id}")
    void updateStatus(@Param("id") Long id,
                      @Param("status") String status);

    @Delete("delete from orders where id=#{id}")
    void delete(@Param("id") Long id);
}