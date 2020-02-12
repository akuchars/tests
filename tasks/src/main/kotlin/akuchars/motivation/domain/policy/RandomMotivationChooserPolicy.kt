package akuchars.motivation.domain.policy

import akuchars.motivation.domain.model.MotivationQuotation
import akuchars.motivation.domain.model.SourceMotivationQuotation
import org.springframework.stereotype.Component

@Component
class RandomMotivationChooserPolicy : MotivationChooserPolicy {

	override fun chooseMotivationQuote(motivations: List<SourceMotivationQuotation>): MotivationQuotation =
			motivations.filter { it.active }.shuffled().first().motivation
}