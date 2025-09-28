package com.example.hotel.Orders;

import com.example.hotel.Rooms.Room;
import com.example.hotel.Rooms.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RoomMapper roomMapper;

    public Order createOrder(Long userId,Order order){
        Room room=roomMapper.findById(order.getRoomId());
        if(room==null){
            throw new RuntimeException("房间不存在");
        }

        /* 2. 日期合法性 */
        if(order.getCheckOut().isBefore(order.getCheckIn()) ||
                order.getCheckOut().equals(order.getCheckIn())){
            throw new RuntimeException("退房日期必须晚于入住日期");
        }

        /*冲突检查*/
        System.out.println("检查冲突：roomId="+order.getRoomId()+
                ",checkIn="+order.getCheckIn()+
                ",checkOut="+order.getCheckOut());

        int booked=orderMapper.countBooked( order.getRoomId(),
                                            order.getCheckIn(),
                                            order.getCheckOut() );
        if (booked>0){
            throw new RuntimeException("该房间在选定日期被预订");
        }

        order.setUserId(userId);
        orderMapper.insert(order);
        return order;
    }

    public List<Order> myOrders(Long userId){
        return orderMapper.findByUser(userId);
    }
    public List<Order> getAllOrders(){
        return orderMapper.getAllOrders();
    }
    public void cancel(Long orderId,Long userId){
        /*用户*/
        Order order=orderMapper.findById(orderId);
        if(order==null){
            throw new RuntimeException("订单不存在");
        }
        if(!order.getUserId().equals(userId)){
            throw new RuntimeException("只能取消自己的订单");
        }
        if(!"BOOKED".equals(order.getstatus())){
            throw new RuntimeException("订单状态不允许取消");
        }
        orderMapper.updateStatus(orderId,"CANCELLED");
    }

    public void cancel(Long orderId){
        Order order=orderMapper.findById(orderId);
        if(order==null){
            throw new RuntimeException("订单不存在");
        }
        if(!"BOOKED".equals(order.getstatus())){
            throw new RuntimeException("订单状态不允许取消");
        }
        orderMapper.updateStatus(orderId,"CANCELLED");
    }

    public List<Order> getBookedOrdersByRoomId(Long roomId){
        return orderMapper.findByRoomIdAndStatus(roomId,"BOOKED");
    }
}
