package com.venky.openai.model;

import java.util.List;

public record CountryDetails(String country, List<String> cities, int population, String area) {}
