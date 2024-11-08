package com.kagi.summarizer

sealed class KagiType {
    data object TRANSLATE : KagiType()

    sealed class SUMMARY : KagiType() {
        data object DISCUSS : SUMMARY()
        data object TAKEAWAY : SUMMARY()
        data object DEFAULT : SUMMARY()
    }
}
//
//enum class KagiSumType {
//    SUMMARY, TAKEAWAY, DISCUSS,
//}