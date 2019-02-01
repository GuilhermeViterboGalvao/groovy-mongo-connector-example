@Grapes([
    @GrabConfig(systemClassLoader = true),
    @Grab('log4j:log4j:1.2.17')
])

import org.apache.log4j.*

def config = new ConfigSlurper().parse(new File("log4j.groovy").toURL())
PropertyConfigurator.configure(config.toProperties())

def logger = Logger.getLogger("")

if (this.args.length < 4) {
    logger.info( """    
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

try {
    def collectionUser = MongoConnector.getDb().getCollection("user")
    for (def i = 0; i < 100; i++) {
        collectionUser.insertOne(
            UserUtils.getUser(MongoConnector.getNextSequence())
        )
    }
    collectionUser.find().each() { document ->
        println document
    }
} catch (e) {
    e.printStackTrace()
} finally {
    try {
        MongoConnector.getMongoClient().close()
    } catch (e) {
        e.printStackTrace()
    }
}