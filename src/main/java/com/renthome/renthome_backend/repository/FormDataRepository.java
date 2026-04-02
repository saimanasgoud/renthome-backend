package com.renthome.renthome_backend.repository;

import com.renthome.renthome_backend.entity.FormData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FormDataRepository extends JpaRepository<FormData, Long> {

    FormData findTopByOrderByCreatedAtDesc();
    List<FormData> findByOwnerId(Long ownerId);

    
}
