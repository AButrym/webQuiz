package engine.security.jwt

import engine.common.logger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class TokenCleanupService(
    private val invalidatedTokenRepo: InvalidatedTokenRepo
) {
    private val log = logger()

    @Scheduled(cron = "0 0 2 * * ?") // daily at 02:00
    @Transactional
    fun purgeExpiredTokens() {
        log.info("Running scheduled job to purge expired tokens.")
        invalidatedTokenRepo.deleteByExpirationTimeBefore(Date().time)
        log.info("Expired token purge complete.")
    }
}
