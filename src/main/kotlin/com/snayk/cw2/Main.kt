import com.snayk.cw2.system.DecisionSystem
import java.io.File

fun main(args: Array<String>) {

    val filePathCovExh = "SystemDecyzyjnyCovExh.txt"
    val filePathLem = "SystemDecyzyjnyLem.txt"
    val fileTextCovExh = File(filePathCovExh).absoluteFile.readText()
    val fileTextLen = File(filePathLem).absoluteFile.readText()
    val covExhSystem = DecisionSystem(fileTextCovExh)
    val lemSystem = DecisionSystem(fileTextLen)
    print(covExhSystem)
    println()
    print(lemSystem)
    var option: String = ""

    while (option != "0") {
        println("Select technique:\n1.Covering\n2.Exhaustive\n3.LEM2\n0.Exit\n")
        option = readLine()!!
        when (option) {
            "1" -> {
                covering(covExhSystem)
                covExhSystem.resetCoverable()
            }
            "2" -> exhaustive(covExhSystem)
            "3" -> {
                lem(lemSystem)
                lemSystem.resetCoverable()
            }
            "0" -> println("Exiting...")
        }
    }

}




