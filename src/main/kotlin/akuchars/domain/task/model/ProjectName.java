package akuchars.domain.task.model;

import kotlin.jvm.internal.Intrinsics;

import javax.persistence.Embeddable;

@Embeddable
public class ProjectName {
	private String value;

	public ProjectName(String value) {
		Intrinsics.checkParameterIsNotNull(value, "value");
		this.value = value;
	}

	ProjectName() {
	}

	public String getValue() {
		return value;
	}
}
