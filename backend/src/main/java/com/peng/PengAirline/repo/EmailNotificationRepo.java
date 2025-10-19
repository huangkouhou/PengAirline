package com.peng.PengAirline.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peng.PengAirline.entities.EmailNotification;

public interface EmailNotificationRepo extends JpaRepository<EmailNotification, Long>{

}
