package com.cec.launcher

import java.util.Calendar

object Affirmations {

    private val list = listOf(
        "I am aligned with the infinite wisdom of the universe.",
        "My consciousness expands with every breath I take.",
        "I radiate love, light, and healing energy.",
        "I am a vessel of divine creativity and purpose.",
        "Every cell in my body vibrates with health and vitality.",
        "I am connected to the sacred web of all living beings.",
        "My intentions manifest with grace and perfect timing.",
        "I trust the journey of my soul's evolution.",
        "I am at peace with all that is, was, and will be.",
        "Abundance flows to me from all directions.",
        "I open my heart chakra to give and receive love freely.",
        "My third eye sees the truth in all situations.",
        "I speak my highest truth with clarity and compassion.",
        "I am grounded in the present moment, rooted like a tree.",
        "Sacred geometry surrounds and protects me.",
        "I align with the 528 Hz frequency of love and healing.",
        "My aura shines with golden light and protective energy.",
        "I release all that no longer serves my highest good.",
        "Divine synchronicities guide my path every day.",
        "I am the architect of my own reality.",
        "My meditation deepens my connection to source energy.",
        "I embody the seven chakras in perfect harmony.",
        "The universe conspires in my favor in every moment.",
        "I am a conscious co-creator of this beautiful existence.",
        "My spirit is ancient, wise, and infinitely powerful.",
        "I breathe in peace and breathe out all tension.",
        "I am worthy of all the beauty life has to offer.",
        "My consciousness is a star shining in the cosmic tapestry.",
        "I honor the sacred within myself and all beings.",
        "Love is the highest frequency, and I embody it fully.",
        "I walk the path of awakening with joy and wonder.",
        "Each sunrise brings me closer to my highest self.",
        "I am the observer, the witness, the eternal awareness."
    )

    fun getTodayAffirmation(): String {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        return list[dayOfYear % list.size]
    }
}
