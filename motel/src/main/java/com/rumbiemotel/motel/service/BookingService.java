package com.rumbiemotel.motel.service;

import com.rumbiemotel.motel.entity.Booking;
import com.rumbiemotel.motel.entity.Room;
import com.rumbiemotel.motel.entity.User;
import com.rumbiemotel.motel.repository.BookingRepository;
import com.rumbiemotel.motel.repository.RoomRepository;
import com.rumbiemotel.motel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    public Booking bookRoom(Booking booking, String userEmail) {

        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new UsernameNotFoundException("User not found"));

        Room room = roomRepository.findById(booking.getRoom().getId()).orElse(null);
        if (room != null && room.isAvailable()) {
            room.setAvailable(false);
            roomRepository.save(room);

            booking.setUser(user);
            return bookingRepository.save(booking);
        }
        return null;
    }
}
