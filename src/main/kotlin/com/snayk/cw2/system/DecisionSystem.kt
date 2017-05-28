package com.snayk.cw2.system

class DecisionSystem(fileText: String) {
    val decisionObjects: MutableList<DecisionObject> = mutableListOf()
    val uniqueDecisions: List<String> by lazy {
        decisionObjects.map { it.decision }.toSet().toList()
    }
    val maxRules: Int by lazy {
        decisionObjects.size
    }
    val minRules: Int by lazy {
        uniqueDecisions.size
    }

    init {
        val rows: List<String> = fileText.trim().split("\n")
        for (i in 0 until rows.size) {
            val row: List<String> = rows[i].trim().split(" ")
            val descriptorList: MutableList<Descriptor> = mutableListOf()
            (0 until row.size - 1).mapTo(descriptorList) { Descriptor(it, row[it]) }
            decisionObjects.add(DecisionObject(i, descriptorList, row[row.size - 1]))
        }
    }

    fun areObjectsLeft(): Boolean {
        return this.decisionObjects.any { it.coverable }
    }

    fun resetCoverable() {
        this.decisionObjects.forEach { it.coverable = true }
    }

    override fun toString(): String {
        var system: String = "      "
        for(i in 1..decisionObjects[0].descriptors.size)
            system += "a$i "
        system += " d\n"
        for((i, dOb) in decisionObjects.withIndex()) {
            system += "o${i+1}  "
            for(desc in dOb.descriptors)
                system += "${desc.value}   "
            system += "${dOb.decision}\n"
        }
        return system
    }
}