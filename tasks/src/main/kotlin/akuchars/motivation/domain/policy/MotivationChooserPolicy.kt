package akuchars.motivation.domain.policy

import akuchars.motivation.domain.model.MotivationQuotation
import akuchars.motivation.domain.model.SourceMotivationQuotation

interface MotivationChooserPolicy {

	fun chooseMotivationQuote(motivations: List<SourceMotivationQuotation>): MotivationQuotation
}