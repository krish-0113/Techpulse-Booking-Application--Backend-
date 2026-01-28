package com.booking.application.repository;

import com.booking.application.entity.Slot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface  SlotRepository extends JpaRepository<Slot,Long> {
    //duplicate slot check
   boolean existsByStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime);

   //for overlapping slot check
    @Query("""
            SELECT COUNT(s) > 0
            FROM Slot s
            WHERE 
               :startTime < s.endTime
               AND :endTime > s.startTime
            """)
    boolean existsOverlappingSlot(
           @Param("startTime") LocalDateTime startTime,
           @Param("endTime") LocalDateTime endTime);

    //Lock slot row to prevent race condition
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Slot s WHERE s.id = :slotId")
    Optional<Slot> findByIdForUpdate( @Param("slotId") Long slotId);
}
