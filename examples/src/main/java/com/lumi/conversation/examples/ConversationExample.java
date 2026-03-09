package com.lumi.conversation.examples;

import com.lumi.conversation.brain.Brain;

/**
 * Example workflow demonstrating basic conversation processing.
 */
public class ConversationExample {

    public static void main(String[] args) {
        Brain brain = new Brain();
        String response = brain.process("Hello, Lumi!");
        System.out.println("Response: " + response);
    }
}
