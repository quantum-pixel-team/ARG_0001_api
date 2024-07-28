package com.quantum_pixel.arg.hotel.repository;

import com.quantum_pixel.arg.hotel.model.Room;
import com.quantum_pixel.arg.hotel.model.RoomView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(nativeQuery = true,
            value = """
                    SELECT r.id,
                           r.name,
                           r.description,
                           r.price               AS total_price,
                           r.capacity,
                           r.images_url,
                           1                     AS avalible_rooms,
                           COALESCE(JSONB_AGG(JSONB_BUILD_OBJECT(
                                   'id', f.id,
                                   'name', f.name,
                                   'usage-count', f.usage_count,
                                   'icon-name', f.icon_name
                                              ) ORDER BY f.usage_count DESC ) FILTER (WHERE f.id IS NOT NULL),
                                    '[]'::jsonb) AS facilities
                    FROM room r
                             LEFT JOIN room_facility rf ON rf.room_id = r.id
                             LEFT JOIN facility f ON f.id = rf.facility_id
                    WHERE (:roomTypes IS NULL OR r.type IN (:roomTypes))
                      AND (:minPrice IS NULL OR r.price >= :minPrice)
                      AND (:maxPrice IS NULL OR r.price <= :maxPrice)
                      AND (:roomFacilities IS NULL OR lower(f.name) IN (:roomFacilities))
                    GROUP BY r.id,
                             r.name,
                             r.description,
                             r.price,
                             r.capacity,
                             r.images_url""")
    Page<RoomView> getRoomAggregated(@Param("roomTypes") Set<String> roomTypes,
                                     @Param("minPrice") Float minPrice,
                                     @Param("maxPrice") Float maxPrice,
                                     @Param("roomFacilities") Set<String> roomFacilities, Pageable pageable);
}
