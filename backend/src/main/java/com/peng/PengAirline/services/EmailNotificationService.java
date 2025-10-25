package com.peng.PengAirline.services;

import com.peng.PengAirline.entities.Booking;
import com.peng.PengAirline.entities.User;

public interface EmailNotificationService {

    void sendBookingTickerEmail(Booking booking);
    void sendWelcomeEmail(User user);

}
