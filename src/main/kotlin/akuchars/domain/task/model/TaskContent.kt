package akuchars.domain.task.model;

import kotlin.jvm.internal.Intrinsics;

import javax.persistence.Embeddable;

@Embeddable
public class TaskContent {
	private String value;

	public TaskContent(String value) {
		Intrinsics.checkParameterIsNotNull(value, "value");
		this.value = value;
	}

	TaskContent() {
	}
}
