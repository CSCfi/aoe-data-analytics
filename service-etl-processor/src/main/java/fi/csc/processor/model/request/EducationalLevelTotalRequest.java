package fi.csc.processor.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EducationalLevelTotalRequest extends BaseTotalRequest {
    private String[] educationalLevels;
}
