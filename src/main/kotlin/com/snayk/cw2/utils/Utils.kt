import com.snayk.cw2.system.*

fun MutableList<MatrixCell>.isCombinationInRow(combination: List<Descriptor>): Boolean {
    return this.any { it.isCombinationInCell(combination) }
}

fun unmatchingMatrix(decisionSystem: DecisionSystem): MutableList<MutableList<MatrixCell>> {
    val size = decisionSystem.decisionObjects.size
    val matrix: MutableList<MutableList<MatrixCell>> =
            MutableList(size, { _ -> MutableList(size, { _ -> MatrixCell(emptyList<Descriptor>().toMutableList()) }) })
    for (i in 0 until decisionSystem.decisionObjects.size) {
        for (j in 0 until decisionSystem.decisionObjects.size) {
            matrix[i][j] = createMatrixCell(decisionSystem.decisionObjects[i], decisionSystem.decisionObjects[j])
        }
    }
    return matrix
}

fun findRule(decisionSystem: DecisionSystem,
             supportingDObs: List<DecisionObject>,
             descriptors: MutableList<Descriptor>,
             decision: String): Rule {
    val ruleCandidate = Rule(descriptors = descriptors, decisions = listOf(decision))

    if(!ruleCandidate.isRuleTrue(decisionSystem.decisionObjects)) {
        if(descriptors.size == supportingDObs[0].descriptors.size) {
            val objectsCoveredByRule = ruleCandidate.objectsCoveringRule(decisionSystem.decisionObjects)
            val objectsDecisions = mutableListOf<String>()
            objectsCoveredByRule.mapTo(objectsDecisions) { it.decision }
            return Rule(descriptors = descriptors, decisions = objectsDecisions)
        }
        val mostCommon = supportingDObs.findMostCommonDescriptor(descriptors)
        val supportingObjects = supportingDObs.filter { it.descriptors.contains(mostCommon) }
        descriptors.add(mostCommon!!)
        return findRule(decisionSystem,
                supportingObjects,
                descriptors,
                decision)
    }
    return ruleCandidate
}

private fun <T> List<T>.flatMapTails(f: (List<T>) -> (List<List<T>>)): List<List<T>> =
        if (isEmpty()) emptyList()
        else f(this) + this.drop(1).flatMapTails(f)

fun <T> List<T>.combinations(length: Int): List<List<T>> =
        if (length == 0) listOf(emptyList())
        else this.flatMapTails { subList -> subList.drop(1).combinations(length - 1).map { (it + subList.first()) } }
