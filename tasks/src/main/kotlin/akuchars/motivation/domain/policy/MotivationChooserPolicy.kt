package akuchars.motivation.domain.policy

import akuchars.motivation.domain.model.Quotation
import akuchars.motivation.domain.model.SourceQuotation

interface MotivationChooserPolicy {

	fun chooseMotivationQuote(motivations: List<SourceQuotation>): Quotation
}