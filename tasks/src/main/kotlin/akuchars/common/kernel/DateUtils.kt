package akuchars.common.kernel

import java.time.LocalDateTime

fun String?.toLocalTime(): LocalDateTime = LocalDateTime.parse(this)