import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
object DateUtility {
    fun timePassed(date: Date): String {
        val now = Date()

        val timePassed = DateUtils.getRelativeTimeSpanString(date.time, now.time, DateUtils.MINUTE_IN_MILLIS)

        return when {
            now.after(date) -> "$timePassed ago"
            now.before(date) -> "In the future"
            else -> "Just now"
        }
    }
}
fun getCurrentDateTimeString(): String {
    val currentDateTime = OffsetDateTime.now(ZoneId.of("Europe/Istanbul"))
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
    return currentDateTime.format(formatter)
}