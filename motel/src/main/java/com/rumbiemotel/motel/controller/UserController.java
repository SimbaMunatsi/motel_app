package com.rumbiemotel.motel.controller;


import com.rumbiemotel.motel.entity.Booking;
import com.rumbiemotel.motel.entity.Room;
import com.rumbiemotel.motel.service.BookingService;
import com.rumbiemotel.motel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/rooms/available")
    public List<Room> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }

    @PostMapping("/bookings")
    public Booking bookRoom(@RequestBody Booking booking, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return bookingService.bookRoom(booking, userDetails.getUsername());
    }
}
