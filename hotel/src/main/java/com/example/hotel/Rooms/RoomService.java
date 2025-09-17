package com.example.hotel.Rooms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomMapper roomMapper;

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
}
