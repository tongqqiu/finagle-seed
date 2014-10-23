# Finagle Seed Project

This is maven based finagle project skeleton


## Features

* Scala Basic
    * Mixed Java and Scala Code
    * Incremental Compiling with Zinc (need to download Zinc and run `zinc -start`)
    * Scala style checking
    * Comprehensive `.gitignore`
    * Scalatest integration
    * Version management for scala, java and maven
    * Markdown based documentation site generation (in `doc` folder)
* Finagle
    * Twitter Finagle RPC examples 
    * Auto generated Finagle skeleton base on Thrift contract
    * Send the trace to local Zipkin



## Usage

Package

```
mvn clean package
```

Test only

```
mvn test
```



