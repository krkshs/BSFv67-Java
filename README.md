![Screenshot](Screenshot.jpg)

# BSFv67-Java

This is a Brawl Stars Core server (version 67.264.1) rewritten in **Java**. It is a fork of the original repository: https://github.com/FMZNkdv/BSFv67.

## Features
- Fully translated to Java with a minimalist, clean, lowercase code style.
- Multi-threaded asynchronous socket handling using standard TCP socket loops.
- Core packet structure, custom byte stream parsing, and packed booleans.
- Automatic splitting of large method serialization streams to bypass JVM 64KB method limits.

## Requirements
- Java Development Kit (JDK 17 or newer)
- Windows / Linux / macOS

## Building

To compile the codebase, run:

```bash
javac -d bin Core/cfg.java server.java Core/Byte/bytestrm.java Core/Byte/wtr.java Core/piranha.java Core/logger.java Gate/msg.java Message/Receive/Login/hello.java Message/Receive/Login/auth.java Message/Transmit/Login/hello.java Message/Transmit/Login/authok.java Message/Transmit/Home/owndata.java
```

## Running

To run the compiled server, execute:

```bash
java -cp bin server
```

The server will start listening on `0.0.0.0:9339` by default (configurable via `Core/cfg.java`).
