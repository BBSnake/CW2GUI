import com.snayk.cw2.system.*

fun covering(decisionSystem: DecisionSystem): String {
    val rules: MutableList<Rule> = mutableListOf()
    var tier = 1
    while (true) {
        if (rules.size >= decisionSystem.maxRules || !decisionSystem.areObjectsLeft())
            break
        for ((index, descriptors, decision, coverable) in decisionSystem.decisionObjects) {
            if (coverable) {
                val combinations: List<List<Descriptor>> =
                        descriptors.combinations(tier)
                for (comb in combinations) {
                    val rule: Rule = Rule(index, comb.reversed(), listOf(decision))
                    if (rule.isRuleTrue(decisionSystem.decisionObjects)) {
                        val supportingObj = rule.supportingObjects(decisionSystem.decisionObjects)
                        supportingObj.forEach { decisionSystem.decisionObjects[it].coverable = false }
                        rule.support = supportingObj.size
                        rules.add(rule)
                        break
                    }
                    if(rule.descriptors.size == descriptors.size)
                        decisionSystem.decisionObjects[index].coverable = false
                }
            }
        }
        tier++
    }
    var rulesText: String = ""
    rules.forEach { rulesText += "From object o${it.fromObject + 1}: $it\n" }
    return rulesText
}

fun exhaustive(decisionSystem: DecisionSystem): String {
    val rulesList: MutableList<Rule> = mutableListOf()
    val unmatchingMatrix: MutableList<MutableList<MatrixCell>> = unmatchingMatrix(decisionSystem)
    val maxTier: Int = decisionSystem.decisionObjects.first().descriptors.size
    var rulesText: String = ""
    for (i in 1..maxTier) {
        val ruleCandidates: MutableList<Rule> = mutableListOf()
        for (j in 0 until decisionSystem.decisionObjects.size) {
            val combinations: List<List<Descriptor>> =
                    decisionSystem.decisionObjects[j].descriptors.combinations(i)
            combinations
                    .asSequence()
                    .filterNot { unmatchingMatrix[j].isCombinationInRow(it.reversed()) }
                    .map {
                        Rule(descriptors = it.reversed(),
                                decisions = listOf(decisionSystem.decisionObjects[j].decision),
                                support = 1)
                    }
                    .forEach {
                        if (ruleCandidates.isEmpty()) {
                            ruleCandidates.add(it)
                        } else {
                            var ruleFlag = false
                            for(rule in ruleCandidates) {
                                if(rule == it) {
                                    rule.support++
                                    ruleFlag = true
                                }
                            }
                            if (!ruleFlag)
                                ruleCandidates.add(it)
                        }
                    }
        }
        if (rulesList.isEmpty()) {
            rulesList.addAll(ruleCandidates)
            rulesText += rulesList.exhaustiveString(i)
        } else {
            val filteredRules = ruleCandidates
                    .filterNot { rulesList.containsRule(it) }
            rulesList.addAll(filteredRules)
            rulesText += filteredRules.exhaustiveString(i)
        }
    }
    return rulesText
}

fun lem(decisionSystem: DecisionSystem): String {
    val concepts: MutableList<Concept> = mutableListOf()
    val rules: MutableList<Rule> = mutableListOf()
    var rulesText = ""

    for(decision in decisionSystem.uniqueDecisions) {
        val dObjs = decisionSystem.decisionObjects
                .filter { it.decision == decision }
        concepts.add(Concept(dObjs, decision))
    }

    for((decisionObjects, decision) in concepts) {
        while (decisionObjects.any { it.coverable }) {
            val coverableObjects = decisionObjects.filter { it.coverable }
            val mostCommonDescriptor = coverableObjects.findMostCommonDescriptor()
            val supportingDObs = coverableObjects.filter { it.descriptors.contains(mostCommonDescriptor) }
            val rule = findRule(decisionSystem,
                    supportingDObs,
                    mutableListOf(mostCommonDescriptor!!),
                    decision)
            val ruleSupportingObjects = rule.supportingObjects(decisionSystem.decisionObjects)
            ruleSupportingObjects.forEach { decisionSystem.decisionObjects[it].coverable = false }
            rule.support = ruleSupportingObjects.size
            rules.add(rule)
        }
    }
    rules.forEachIndexed { index, rule -> rulesText += "rule${index+1} $rule\n" }
    return rulesText
}