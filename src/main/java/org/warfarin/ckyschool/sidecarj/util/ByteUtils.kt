package org.warfarin.ckyschool.sidecarj.util

fun ByteArray.intFromLittleEndianBytes(start: Int, endExclusive: Int): Int {
    return this.sliceArray(IntRange(start, endExclusive - 1)).let {
        it[0].toInt() + (it[1].toInt() shl 8) + (it[2].toInt() shl 16) + (it[3].toInt() shl 24)
    }
}

fun ByteArray.intFromLittleEndianBytes(range: IntRange): Int {
    return this.sliceArray(range).let {
        it[0].toInt() + (it[1].toInt() shl 8) + (it[2].toInt() shl 16) + (it[3].toInt() shl 24)
    }
}

fun ByteArray.intFromBigEndianBytes(start: Int, endExclusive: Int): Int {
    return this.sliceArray(IntRange(start, endExclusive - 1)).let {
        it[3].toInt() + (it[2].toInt() shl 8) + (it[1].toInt() shl 16) + (it[0].toInt() shl 24)
    }
}

fun ByteArray.intFromBigEndianBytes(range: IntRange): Int {
    return this.sliceArray(range).let {
        it[3].toInt() + (it[2].toInt() shl 8) + (it[1].toInt() shl 16) + (it[0].toInt() shl 24)
    }
}

fun ByteArray.fillWithLittleEndianBytesFromInt(from: Int, offset: Int) {
    this[offset] = (from and 0xFF).toByte()
    this[offset + 1] = ((from ushr 8) and 0xFF).toByte()
    this[offset + 2] = ((from ushr 16) and 0xFF).toByte()
    this[offset + 3] = ((from ushr 24) and 0xFF).toByte()
}