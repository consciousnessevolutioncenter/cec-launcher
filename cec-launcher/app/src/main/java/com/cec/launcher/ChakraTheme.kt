package com.cec.launcher

object ChakraTheme {

    data class Chakra(
        val name: String,
        val sanskrit: String,
        val accentColor: Int,
        val hz: String
    )

    val chakras = listOf(
        Chakra("Root", "Muladhara", 0xFFB71C1C.toInt(), "396 Hz"),
        Chakra("Sacral", "Svadhisthana", 0xFFE65100.toInt(), "417 Hz"),
        Chakra("Solar Plexus", "Manipura", 0xFFF9A825.toInt(), "528 Hz"),
        Chakra("Heart", "Anahata", 0xFF1B5E20.toInt(), "639 Hz"),
        Chakra("Throat", "Vishuddha", 0xFF0277BD.toInt(), "741 Hz"),
        Chakra("Third Eye", "Ajna", 0xFF283593.toInt(), "852 Hz"),
        Chakra("Crown", "Sahasrara", 0xFF4A148C.toInt(), "963 Hz")
    )

    fun getAccentColor(index: Int): Int {
        return chakras.getOrNull(index)?.accentColor ?: 0xFFB388FF.toInt()
    }

    fun getChakra(index: Int): Chakra {
        return chakras.getOrNull(index) ?: chakras[6]
    }
}
