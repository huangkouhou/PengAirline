package com.peng.PengAirline.services;

import java.util.List;

import com.peng.PengAirline.dtos.BookingDTO;
import com.peng.PengAirline.dtos.CreateBookingRequest;
import com.peng.PengAirline.dtos.Response;
import com.peng.PengAirline.enums.BookingStatus;

public interface BookingService {

    Response<?> createBooking(CreateBookingRequest createBookingRequest);
    Response<BookingDTO> getBookingById(Long id);
    Response<List<BookingDTO>> getAllBookings();
    Response<List<BookingDTO>> getMyBookings();
    Response<?> updateBookingStatus(Long id, BookingStatus status);
}
