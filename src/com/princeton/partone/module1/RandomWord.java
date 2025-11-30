package com.princeton.partone.module1;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = "";
        int i = 0;

        while (!StdIn.isEmpty()) {
            String word =  StdIn.readString();
            i++;
            if(StdRandom.bernoulli(1.0 / i)){
                champion = word;
            }
        }

        if(champion != null) {
            System.out.println(champion);
        }
    }
}
