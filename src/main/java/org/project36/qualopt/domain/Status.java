package org.project36.qualopt.domain;
// This enum represents the stages in a studies lifecycle.
public enum Status {
    Inactive, // When no emails have been sent out.
    Active, // When emails have been sent out.
    Completed; // When the study has finished and no more changes are required.
}
