package com.snayk.cw2.system

data class DecisionObject(val index: Int,
                          val descriptors: List<Descriptor>,
                          val decision: String,
                          var coverable: Boolean = true)

fun List<DecisionObject>.findMostCommonDescriptor(usedDescriptors: List<Descriptor> = emptyList()): Descriptor? {
    val descriptors = this.flatMap { it -> it.descriptors }.toMutableList()
    descriptors.removeAll(usedDescriptors)
    val descriptorsMap: MutableMap<Descriptor, Int> = mutableMapOf()
    for(desc in descriptors) {
        if(!descriptorsMap.containsKey(desc))
            descriptorsMap.put(desc, 1)
        else
            descriptorsMap[desc] = descriptorsMap.getValue(desc) + 1
    }
    val maxOccurences = descriptorsMap.maxBy { it.value }?.value
    return descriptorsMap.filter { it.value == maxOccurences }
            .minBy { it.key.index }?.key

}