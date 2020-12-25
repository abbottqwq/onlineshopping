package com.edu.neu.project.functionalinterface;

@FunctionalInterface
public interface UserImgFileNameGenerator {
    String getImgFileName(Integer productID, String uploadFileName, String suffix);
}
