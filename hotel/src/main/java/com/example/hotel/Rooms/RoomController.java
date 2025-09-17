package com.example.hotel.Rooms;

import com.example.hotel.AdminOnly;
import com.example.hotel.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired RoomService roomService;

    /*新增*/
    @PostMapping
    @AdminOnly
    public R<Void> add(@RequestBody Room room){
        roomService.addRoom(room);
        return R.ok("新增房间成功");
    }

    /*列表*/
    @GetMapping
    public R<List<Room>> list(){
        List<Room>roomList=roomService.listAll();
        return R.ok(roomList);
    }

    /*单条*/
    @GetMapping("/{id}")
    public R<Room> get(@PathVariable Long id){
        Room room=roomService.getById(id);
        return R.ok(room);
    }

    /*更新*/
    @PutMapping
    @AdminOnly
    public R<Void> update(@RequestBody Room room){
        roomService.updateRoom(room);
        return R.ok("更新成功");
    }

    /*删除*/
    @DeleteMapping("/{id}")
    @AdminOnly
    public R<Void> delete(@PathVariable Long id){
        roomService.deleteRoom(id);
        return R.ok("删除成功");
    }
}
