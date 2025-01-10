package com.example.finpro.Domain;

import java.util.List;

public class GeminiRequest {
    private List<Content> contents;

    public GeminiRequest(String message) {
        this.contents = List.of(new Content(new Part(message)));
    }

    static class Content {
        private List<Part> parts;

        Content(Part part) {
            this.parts = List.of(part);
        }
    }

    static class Part {
        private String text;

        Part(String text) {
            this.text = text;
        }
    }
}
