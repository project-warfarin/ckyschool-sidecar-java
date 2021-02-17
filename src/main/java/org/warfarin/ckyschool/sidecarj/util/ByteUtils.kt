package org.warfarin.ckyschool.sidecarj.util

fun ByteArray.intFromLittleEndianBytes(start: Int, endExclusive: Int): Int {
    return this.sliceArray(IntRange(start, endExclusive)).let {
        it[0].toInt() + (it[1].toInt() shl 8) + (it[2].toInt() shl 16) + (it[3].toInt() shl 24)
    }
}

