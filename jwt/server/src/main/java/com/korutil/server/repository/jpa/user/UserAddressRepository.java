package com.korutil.server.repository.jpa.user;

import com.korutil.server.domain.user.UserAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddressEntity, Long> {

    List<UserAddressEntity> findAllByUserIdAndActivatedTrueOrderBySeq( Long userId );
}
