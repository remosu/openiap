package dev.hyo.openiap

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AmazonFreeTrialOfferTest {

    @Test
    fun `word based trial periods map to a free-trial offer`() {
        val offer = buildAmazonFreeTrialOffer("dev.hyo.martie.premium", "Weekly")

        assertEquals(PaymentMode.FreeTrial, offer?.paymentMode)
        assertEquals(DiscountOfferType.Introductory, offer?.type)
        assertEquals("dev.hyo.martie.premium", offer?.basePlanIdAndroid)
        assertEquals(0.0, offer?.price)
        assertEquals(
            "P1W",
            offer?.pricingPhasesAndroid?.pricingPhaseList?.firstOrNull()?.billingPeriod
        )
    }

    @Test
    fun `day count trial periods normalize to ISO days`() {
        val offer = buildAmazonFreeTrialOffer("dev.hyo.martie.premium", "7 Days")

        assertEquals(
            "P7D",
            offer?.pricingPhasesAndroid?.pricingPhaseList?.firstOrNull()?.billingPeriod
        )
    }

    @Test
    fun `missing trial periods produce no offer`() {
        assertNull(buildAmazonFreeTrialOffer("dev.hyo.martie.premium", null))
        assertNull(buildAmazonFreeTrialOffer("dev.hyo.martie.premium", "   "))
    }

    @Test
    fun `period normalization keeps existing word and ISO mappings`() {
        assertEquals("P7D", amazonBillingPeriodToIso("7 Days"))
        assertEquals("P14D", amazonBillingPeriodToIso("14 days"))
        assertEquals("P1M", amazonBillingPeriodToIso("Monthly"))
        assertEquals("P1Y", amazonBillingPeriodToIso("Annually"))
        assertEquals("P1M", amazonBillingPeriodToIso("P1M"))
        assertEquals("", amazonBillingPeriodToIso(null))
    }
}
