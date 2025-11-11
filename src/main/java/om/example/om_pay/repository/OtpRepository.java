package om.example.om_pay.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import om.example.om_pay.entity.Otp;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findByPhoneNumberAndCode(String phoneNumber, String code);

    Optional<Otp> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("DELETE FROM Otp o WHERE o.expirationDateTime < :now")
    void deleteExpiredOtps(@Param("now") LocalDateTime now);

    @Query("SELECT COUNT(o) > 0 FROM Otp o WHERE o.phoneNumber = :phoneNumber AND o.expirationDateTime > :now")
    boolean existsValidOtpByPhoneNumber(@Param("phoneNumber") String phoneNumber, @Param("now") LocalDateTime now);
}