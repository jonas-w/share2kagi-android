package com.kagi.summarizer

sealed class KagiType {
    object TRANSLATE : KagiType()

    sealed class SUMMARY : KagiType() {
        object DISCUSS : SUMMARY()
        object TAKEAWAY : SUMMARY()
        object DEFAULT : SUMMARY()
    }
}
//
//enum class KagiSumType {
//    SUMMARY, TAKEAWAY, DISCUSS,
//}