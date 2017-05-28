package com.snayk.cw2.system

class Rule(val fromObject: Int = -1,
           val descriptors: List<Descriptor>,
           val decisions: List<String>,
           var support: Int = 0) {
    override fun toString(): String {
        var rule: String = ""
        for (i in 0 until descriptors.size - 1)
            rule += "${descriptors[i]}^"
        rule += "${descriptors[descriptors.size - 1]} => "
        for (i in 0 until decisions.size - 1) {
            rule += "(d = ${decisions[i]}) v "
        }
        rule += "(d = ${decisions[decisions.size - 1]})"
        if (support > 1)
            rule += " [$support]"
        return rule
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Rule -> (this.descriptors == other.descriptors)
            else -> throw Exception("Wrong types")
        }
    }

    override fun hashCode(): Int {
        var result = 17
        for (desc in descriptors) {
            result = 31 * result + desc.hashCode()
        }
        return result
    }

    fun doesObjectCoverRule(dObj: DecisionObject): Boolean {
        return this.descriptors.none { dObj.descriptors[it.index].value != it.value }
    }

    fun isRuleTrue(dObjs: List<DecisionObject>): Boolean {
        return dObjs.none { doesObjectCoverRule(it) && !this.decisions.contains(it.decision) }
    }

    fun objectsCoveringRule(dObjs: List<DecisionObject>): List<DecisionObject> {
        return dObjs.filter { doesObjectCoverRule(it) }
    }

    fun supportingObjects(dObjs: List<DecisionObject>): List<Int> {
        return dObjs.filter { doesObjectCoverRule(it) && this.decisions.contains(it.decision) }
                .map { it.index }
    }

    fun containsRule(newRule: Rule): Boolean {
        for ((index, value) in this.descriptors) {
            val indexMap = newRule.descriptors.map { (index1) -> index1 }
            val decision = newRule.descriptors.find { it.index == index }
            if (!indexMap.contains(index) || decision?.value != value) {
                return false
            }
        }
        return true
    }
}

fun List<Rule>.containsRule(newRule: Rule): Boolean {
    return this.any { it.containsRule(newRule) }
}

fun List<Rule>.exhaustiveString(tier: Int): String {
    var text = ""
    if (this.isEmpty())
        return text
    text += "Tier $tier:\n"
    this.forEach { text += "$it\n" }
    return text
}