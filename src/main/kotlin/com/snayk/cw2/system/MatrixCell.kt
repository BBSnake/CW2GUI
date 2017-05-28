package com.snayk.cw2.system

class MatrixCell(val descriptors: MutableList<Descriptor>) {
    fun isCombinationInCell(combination: List<Descriptor>): Boolean {
        return combination.none { !this.descriptors.contains(it) }
    }

    override fun toString(): String {
        return descriptors.toString()
    }
}

fun createMatrixCell(ob1: DecisionObject, ob2: DecisionObject): MatrixCell {
    val cell: MutableList<Descriptor> = mutableListOf()
    if(ob1.decision == ob2.decision)
        return MatrixCell(cell)

    (0 until ob1.descriptors.size)
            .filter { ob1.descriptors[it] == ob2.descriptors[it] }
            .mapTo(cell) { ob1.descriptors[it] }
    return MatrixCell(cell)
}