package akuchars.domain.task.model;

import kotlin.jvm.internal.Intrinsics;

import java.time.LocalDateTime;

class ChangeEntityTime {
	private LocalDateTime createdDate;
	private LocalDateTime updateDate;

	private ChangeEntityTime(LocalDateTime createdDate, LocalDateTime updateDate) {
		Intrinsics.checkParameterIsNotNull(createdDate, "createdDate");
		this.createdDate = createdDate;
		this.updateDate = updateDate;
	}

	ChangeEntityTime() {
	}

	static ChangeEntityTime now() {
		return new ChangeEntityTime(LocalDateTime.now(), null);
	}

	void updateTime() {
		this.updateDate = LocalDateTime.now();
	}
}
