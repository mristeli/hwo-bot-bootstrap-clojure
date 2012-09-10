# Pingpong Clojure Client

This project contains a clojure client for the [Reaktor Hello World Open event](http://helloworldopen.fi/).

NOTICE: The test server enforces a threshold of 10 messages per client in a second. At the moment the bot answers each message from the server with up direction message. This exceeds the threshold defined by the server and kicks the bot out of the game.

## Requires

[leiningen](http://leiningen.org/)

## Usage

to build:
`./build.sh`

to run:
`./start.sh <bot-name> <host> <port>`

to stop
`./stop.sh`

## Credits

Copyright (C) 2012 Tuomas Hakkarainen, Antti Holvikari, Aku Kotkavuo, Oskar Ojala

Distributed under the Apache-2.0 license http://www.apache.org/licenses/LICENSE-2.0.html
