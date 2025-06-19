package com.rumbiemotel.motel.repository;

import com.rumbiemotel.motel.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
