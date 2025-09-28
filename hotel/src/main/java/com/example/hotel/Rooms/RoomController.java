package com.example.hotel.Rooms;

import com.example.hotel.AdminOnly;
import com.example.hotel.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    RoomService roomService;

    /* 新增 */
    @PostMapping
    @AdminOnly
    public R<Void> add(@RequestBody Room room) {
        roomService.addRoom(room);
        return R.ok("新增房间成功");
    }

    /* 列表 */
    @GetMapping("/list")
    public R<List<Room>> list() {
        List<Room> roomList = roomService.listAll();
        return R.ok(roomList);
    }

    /* 单条 */
    @GetMapping("/detail/{id}")
    public R<Room> get(@PathVariable Long id) {
        Room room = roomService.getById(id);
        return R.ok(room);
    }

    /* 更新 */
    @PutMapping("/{id}")
    @AdminOnly
    public R<Void> update(@RequestBody Room room) {
        roomService.updateRoom(room);
        return R.ok("更新成功");
    }

    /* 删除 */
    @DeleteMapping("/{id}")
    @AdminOnly
    public R<Void> delete(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return R.ok("删除成功");
    }

    @GetMapping("/search")
    public R<List<Room>> searchRooms(@RequestParam LocalDate checkIn,
                                     @RequestParam LocalDate checkOut,
                                     @RequestParam(required = false) String roomType){
        List<Room> availableRooms=roomService.findAvailableRoom(checkIn,checkOut,roomType);
        return R.ok(availableRooms);
    }
    @PostMapping("/search")
    public R<List<Room>> searchRooms(@RequestBody Map<String,String>params){
        String roomType=params.get("roomType");
        String checkIn=params.get("checkIn");
        String checkOut=params.get("checkOut");
        List<Room> availableRooms=roomService.findAvailableRoom(LocalDate.parse(checkIn),LocalDate.parse(checkOut),roomType);
        return R.ok(availableRooms);
    }


}
