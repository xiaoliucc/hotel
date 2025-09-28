package com.example.hotel.Orders;

import com.example.hotel.AdminOnly;
import com.example.hotel.R;
import com.example.hotel.Rooms.Room;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /*下单*/
    @PostMapping
    public R<Order> create(@RequestBody Order order, HttpServletRequest request){
//        Order od=orderService.createOrder(1L,order);
//        return R.ok("下单成功",od);
        Long userId=(Long) request.getAttribute("userId");
        Order od=orderService.createOrder(userId,order);
        return R.ok("下单成功",od);
    }

    /*我的订单*/
    @GetMapping("/my")
    public R<List<Order>> myOrders(HttpServletRequest request){
//        List<Order> list=orderService.myOrders(1L);
        Long userId=(Long) request.getAttribute("userId");
        return R.ok(orderService.myOrders(userId));
    }

    /*用户取消订单*/
    @PutMapping("/{id}/cancel")
    public R<Void> cancel(@PathVariable Long id,HttpServletRequest request){
        Long userId=(Long) request.getAttribute("userId");
        orderService.cancel(id,userId);
        return R.ok("取消成功");
    }

    /*获取某个房间被预订的时间*/
    @GetMapping("/room/{roomId}/booked-times")
    public R<List<Order>> getRoomBookedTimes(@PathVariable Long roomId){
        List<Order> bookedOrders=orderService.getBookedOrdersByRoomId(roomId);
        return R.ok(bookedOrders);
    }
    /*管理员*/
    /*全部订单*/
    @GetMapping("/all")
    @AdminOnly
    public R<List<Order>> allOrders(HttpServletRequest request){
        return R.ok(orderService.getAllOrders());
    }

    /*管理员取消任意订单*/
    @PutMapping("/{id}/forceCancel")
    @AdminOnly
    public R<Void> forceCancel(@PathVariable Long id){
        orderService.cancel(id,null);
        return R.ok("强制取消成功");
    }
}
