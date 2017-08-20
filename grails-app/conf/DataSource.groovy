dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    //hibernate.show_sql = true
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        logSql = true
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop','update'
//            url = "jdbc:hsqldb:mem:devDB"     

            driverClassName = "org.postgresql.Driver"
            dialect = org.hibernate.dialect.PostgreSQLDialect
            username = "root"
            password = "root"
//            dbCreate = "update"
            url = "jdbc:postgresql://localhost/gestimmo"    
//            //url = "jdbc:postgresql://localhost/gestimmo20130724"    
        }
        /*dataSource {
            driverClassName = "org.postgresql.Driver"
            dialect = org.hibernate.dialect.PostgreSQLDialect
            username = "root"
            password = "root"
            dbCreate = "update"
            url = "jdbc:postgresql://localhost/gestimmodev"  
            //            url = "jdbc:postgresql://localhost/gestimmo20121024"          
        }*/
    }
    test {    
        dataSource {

            driverClassName = "org.postgresql.Driver"
            dialect = org.hibernate.dialect.PostgreSQLDialect
            username = "root"
            password = "root"
            dbCreate = "update"
            url = "jdbc:postgresql://localhost/gestimmo20121001"          
        }
    }
    production {
        dataSource {
            driverClassName = "org.postgresql.Driver"
            dialect = org.hibernate.dialect.PostgreSQLDialect
            username = "gestimmo"
            password = "gestimmo"
            dbCreate = "validate"
            url = "jdbc:postgresql://localhost/gestimmo" 
        }
    }
}
