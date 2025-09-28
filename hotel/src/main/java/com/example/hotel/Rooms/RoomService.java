package com.example.hotel.Rooms;

import com.example.hotel.Orders.Order;
import com.example.hotel.Orders.OrderMapper;
import com.example.hotel.Page.Page;
import com.example.hotel.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private OrderMapper orderMapper;


    public void addRoom(Room room){
        if(room.getStatus()==null || room.getStatus().isBlank()){
            room.setStatus("AVAILABLE");
        }
        roomMapper.insert(room);
    }

    public List<Room> listAll(){
        return roomMapper.findAll();
    }

    public Room getById(Long id){
        Room room=roomMapper.findById(id);
        if(room==null){
            throw new RuntimeException("房间不存在");
        }
        return room;
    }

    public void deleteRoom(Long id){
        roomMapper.deleteById(id);
    }

    public void updateRoom(Room room){
        roomMapper.update(room);
    }



    public List<Room> findAvailableRoom(LocalDate checkIn,LocalDate checkOut,String roomType){
        List<Long> bookedRoomIds=orderMapper.findOrdersBetweenDate(checkIn,checkOut)
                .stream()
                .mapToLong(Order::getRoomId)
                .distinct()
                .boxed()
                .collect(Collectors.toList());

        //查询所有房间
        List<Room> allRooms=roomMapper.findAll().stream()
                //过滤出未被预订切状态为AVAILABLE的房间
                .filter(room -> !bookedRoomIds.contains(room.getId())&&
                        (roomType==null || room.getType().equals(roomType))&&
                        "AVAILABLE".equals(room.getStatus()))
                .collect(Collectors.toList());
        return allRooms;

    }


}
