package com.quantum_pixel.arg.hotel.repository;

import com.quantum_pixel.arg.hotel.model.Room;
import com.quantum_pixel.arg.hotel.model.RoomView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Transactional
    @Query(nativeQuery = true,
            value = """
                    WITH agg_room_res AS (SELECT r.id,
                                                 r.name AS source_name,
                                                 rt.name,
                                                 rt.short_description,
                                                 rt.description,
                                                 r.capacity,
                                                 r.rate_applies_to,
                                                 r.images_url,
                                                 rr.date           AS reservation_date,
                                                 rr.current_price  AS reservation_current_price,
                                                 rr.available      AS reservation_available,
                                                 rr.minimum_nights AS reservation_minimum_nights,
                                                 CASE
                                                     WHEN :numberOfGuests <= r.capacity * :numberOfRooms THEN
                                                         CASE
                                                             WHEN :numberOfGuests = r.rate_applies_to + 1 THEN rr.current_price + 15
                                                             WHEN :numberOfGuests = r.rate_applies_to + 2 THEN rr.current_price + 25
                                                             ELSE rr.current_price
                                                             END * :numberOfRooms
                                                     ELSE 0
                                                     END           AS reservation_price_per_night
                                          FROM room r
                                              LEFT JOIN room_translation rt ON r.id = rt.room_id AND rt.language = :language
                                                   LEFT JOIN room_reservation rr
                                                             ON r.id = rr.room_id
                                                                 AND rr.date >= :checkInDate
                                                                 AND rr.date < :checkOutDate),
                         room_res AS (SELECT id,
                                             name,
                                             source_name,
                                             description,
                                             short_description,
                                             capacity * :numberOfRooms        AS total_capacity,
                                             images_url,
                                             SUM(reservation_price_per_night) AS total_price,
                                             MIN(reservation_available)       AS available_rooms,
                                             MAX(reservation_minimum_nights)  AS minimum_nights
                                      FROM agg_room_res
                                      GROUP BY id, name,source_name, description, short_description, capacity, rate_applies_to, images_url),
                         res AS (SELECT r.id,
                                        r.name,
                                        r.short_description,
                                        r.description,
                                        r.total_price,
                                        r.total_capacity,
                                        r.images_url,
                                        r.available_rooms,
                                        r.minimum_nights,
                                        COALESCE(JSONB_AGG(
                                                 JSONB_BUILD_OBJECT(
                                                         'id', f.id,
                                                         'name', f.name,
                                                         'usageCount', f.usage_count,
                                                         'iconName', f.icon_name) ORDER BY f.usage_count DESC)
                                                 FILTER (WHERE f.id IS NOT NULL), '[]'::jsonb) AS facilities
                                 FROM room_res r
                                          LEFT JOIN room_facility rf ON rf.room_id = r.id
                                          LEFT JOIN facility f ON f.id = rf.facility_id
                                 WHERE ((:roomTypes) IS NULL
                                     OR r.source_name ILIKE ANY (:roomTypes) OR  ARRAY_LENGTH(:roomTypes, 1) IS NULL)
                                   AND
                                     (:minPrice IS NULL OR r.total_price >= :minPrice)
                                   AND (:maxPrice IS NULL OR r.total_price <= :maxPrice)
                                   AND (:available IS NULL OR (:available = true AND available_rooms >= 1 * :numberOfRooms) OR :available = false)
                                 GROUP BY r.id, r.name, r.description, short_description, r.total_capacity, r.images_url, r.total_price,
                                          r.available_rooms,
                                          r.minimum_nights
                                 HAVING (:roomFacilities) IS NULL
                                     OR ARRAY_AGG(f.name) @> :roomFacilities)
                    SELECT *
                    FROM res
                    """, countQuery = "SELECT COUNT(*) FROM room")
    Page<RoomView> getRoomAggregated(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("language") String language,
            @Param("numberOfGuests") Integer numberOfGuests,
            @Param("numberOfRooms") Integer numberOfRooms,
            @Param("roomTypes") String[] roomTypes,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("roomFacilities") String[] roomFacilities,
            @Param("available") Boolean available,
            Pageable pageable
    );
}
