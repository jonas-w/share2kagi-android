package com.kagi.share

sealed class KagiType {
    data object TRANSLATE : KagiType()
    data object IMAGE : KagiType()

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