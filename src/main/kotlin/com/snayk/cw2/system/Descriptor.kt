package com.snayk.cw2.system

data class Descriptor(val index: Int, val value: String) {
    override fun toString(): String = "(a${index + 1} = $value)"
    override operator fun equals(other: Any?): Boolean {
        return when (other) {
            is Descriptor -> ((this.index == other.index) && (this.value == other.value))
            else -> throw Exception("Wrong types")
        }
    }

    override fun hashCode(): Int {
        var result = 17
        result = 31 * result + index
        result = 31 * result + value.hashCode()
        return result
    }
}