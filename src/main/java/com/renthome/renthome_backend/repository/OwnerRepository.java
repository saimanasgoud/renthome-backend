package com.renthome.renthome_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.renthome.renthome_backend.entity.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Owner findByMobile(String mobile);

    Owner findTopByEmail(String email);

@Query("SELECT o FROM Owner o WHERE o.email = :email")
Owner getOwnerByEmail(@Param("email") String email);

}