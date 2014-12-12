# Finagle Seed Project

[![Build Status](https://travis-ci.org/tongqqiu/finagle-seed.svg)](https://travis-ci.org/tongqqiu/finagle-seed)

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
* Proto-Buffer
    * Maven plug-in to auto generated proto-buffer classes based on proto-buffer IDL
    * Examples to read and write messages using Proto-buffer `AddPerson` and `ListPerson`

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





[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/tongqqiu/finagle-seed/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

