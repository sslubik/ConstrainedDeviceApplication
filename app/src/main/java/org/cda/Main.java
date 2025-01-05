package org.cda;

import org.cda.app.App;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Main.log.info("Initializing Constrained Device Application...");

        App cda = new App();
        cda.run();
    }
}
