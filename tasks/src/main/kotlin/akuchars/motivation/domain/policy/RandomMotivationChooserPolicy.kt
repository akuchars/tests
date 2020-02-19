package akuchars.motivation.domain.policy

import akuchars.motivation.domain.model.Quotation
import akuchars.motivation.domain.model.SourceQuotation
import org.springframework.stereotype.Component

@Component
class RandomMotivationChooserPolicy : MotivationChooserPolicy {

	override fun chooseMotivationQuote(motivations: List<SourceQuotation>): Quotation =
			motivations.filter { it.active }.shuffled().first().motivation
}