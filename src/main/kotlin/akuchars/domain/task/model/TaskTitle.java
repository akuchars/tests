package akuchars.domain.task.model;

import kotlin.jvm.internal.Intrinsics;

import javax.persistence.Embeddable;

@Embeddable
public class TaskTitle {
	private String value;

	public TaskTitle(String value) {
		Intrinsics.checkParameterIsNotNull(value, "value");
		this.value = value;
	}

	TaskTitle() {
	}

	public String getValue() {
		return value;
	}
}
