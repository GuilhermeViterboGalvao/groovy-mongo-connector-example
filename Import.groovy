@Grapes([
    @GrabConfig(systemClassLoader = true),
    @Grab('log4j:log4j:1.2.17')
])

import org.apache.log4j.*

def config = new ConfigSlurper().parse(new File("log4j.groovy").toURL())
PropertyConfigurator.configure(config.toProperties())
def log = Logger.getLogger("")

if (this.args.length < 4) {
    log.info( """    
    *****************************
    **                         **
    ** Uninformed parameters   **
    **                         **
    ** example:                **
    **                         **
    ** groovy Import.groovy \\ **
    **  \$mongoDbName \\       **
    **  \$mongoUserName \\     **
    **  \$mongoPassword \\     **
    **  \$mongoHostname \\     **
    **  \$mongoOptions         **
    **                         **
    *****************************
    """)
    return System.exit(0)
}

def scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent
def MongoConnector = new GroovyScriptEngine(scriptDir).loadScriptByName("MongoConnector.groovy")
def UserUtils = new GroovyScriptEngine(scriptDir).loadScriptByName("UserUtils.groovy")

MongoConnector.init([
    dbName   : this.args[0],
    userName : this.args[1],
    password : this.args[2],
    hostName : this.args[3],
    options  : this.args[4]
])

UserUtils.init(
    log,
    MongoConnector.getDb().getCollection("user")
)

try {
    UserUtils.read().each() { document ->
        log.info("Deleting user = ${document}")
        UserUtils.delete(document)
    }
    for (def id = 1; id <= 50; id++) {
        UserUtils.create(id)
    }
    UserUtils.read().each() { document ->
        log.info(document)
    }
    log.info("UserUtils.getById(10) = ${UserUtils.getById(10)}")
    log.info("UserUtils.getByNameAndEmail(\"user-20\", \"user20@gmail.com\") = ${UserUtils.getByNameAndEmail("user-20", "user20@gmail.com").first()}")
    def userToUpdate = UserUtils.getById(15)
    userToUpdate.name = "new-name-of-user-15"
    UserUtils.update(userToUpdate)
} catch (e) {
    e.printStackTrace()
} finally {
    try {
        MongoConnector.getMongoClient().close()
    } catch (e) {
        e.printStackTrace()
    }
}