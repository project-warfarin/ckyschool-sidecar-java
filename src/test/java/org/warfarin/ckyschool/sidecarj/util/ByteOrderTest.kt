package org.warfarin.ckyschool.sidecarj.util

import org.junit.Test

class ByteOrderTest {
    @Test
    fun should_convert_int32_from_little_endian() {
        val from = byteArrayOf(0x12, 0x34, 0x56, 0x78)
        val target = 0x78563412

        val to1 = from.intFromLittleEndianBytes(0, 4)
        val to2 = from.intFromLittleEndianBytes(IntRange(0, 3))

        assert(target == to1)
        assert(target == to2)
    }

    @Test
    fun should_convert_int32_from_big_endian() {
        val from = byteArrayOf(0x78, 0x56, 0x34, 0x12)
        val target = 0x78563412

        val to1 = from.intFromBigEndianBytes(0, 4)
        val to2 = from.intFromBigEndianBytes(IntRange(0, 3))

        assert(target == to1)
        assert(target == to2)
    }

    @Test
    fun should_fill_array_with_little_endian_bytes_from_local_int32() {
        val from = 0x78563412
        val buffer = ByteArray(4)
        val target = byteArrayOf(0x12, 0x34, 0x56, 0x78)

        buffer.fillWithLittleEndianBytesFromInt(from, 0)

        target.forEachIndexed { index, element -> assert(buffer[index] == element) }
    }
}