import groovy.json.JsonOutput

def saveDataToFile(content, tgtFile) {
    new File(tgtFile).withWriter { writer ->
        writer << content
    }
}

def sh(command) {
    println("Command [$command]")
    def process = command.execute()

    def exitCode = process.waitFor()
    def error = process.err.text
    println("Error message [$error]")

    def response = process.text

    return response
}

def today = new Date()
def todayDate = today.format("dd-MMMM-yyyy")

def tomorrowDate
def jiraTomorrowDate

today.withCalendar { calendar ->
    def day = calendar[Calendar.DAY_OF_WEEK]

    if (day == Calendar.FRIDAY) {
        tomorrowDate = today.next().next().next().next().format("dd-MMMM-yyyy")
    } else {
        tomorrowDate = today.next().format("dd-MMMM-yyyy")
    }

    jiraTomorrowDate = tomorrowDate.format("yyyy-MM-dd'T'HH:mm:ss.sZD")
}

saveDataToFile(todayDate, "/bp/workspace/prDate")
saveDataToFile(tomorrowDate, "/bp/workspace/deploymentDate")

println "Deployment date is ${tomorrowDate}"
println "Pull request is raised on date ${todayDate}"
